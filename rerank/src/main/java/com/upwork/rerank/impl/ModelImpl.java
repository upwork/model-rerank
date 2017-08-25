/*-
 * #%L
 * Rerank - A library to rerank based on weka models
 * %%
 * Copyright (C) 2017 Upwork Inc.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package com.upwork.rerank.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.upwork.rerank.DistributionResult;
import com.upwork.rerank.FeatureDataType;
import com.upwork.rerank.FeatureDebugInfo;
import com.upwork.rerank.FeatureMeta;
import com.upwork.rerank.Model;
import com.upwork.rerank.ModelConfig;
import com.upwork.rerank.ModelExecutionException;
import com.upwork.rerank.thrift.TFeatureValue;
import com.upwork.rerank.thrift.TInstance;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 * Implements {@link Model}. This class encapsulates a weka {@link Classifier} and converts the application data
 * instances to weka Instances which are accepted by the classifier.
 *
 * @author agupta
 */
public class ModelImpl implements Model, Serializable {

  private static Logger logger = LoggerFactory.getLogger(Model.class);

  private Classifier classifier;
  private ModelConfig<? extends FeatureMeta> modelConfig;

  @Inject
  public ModelImpl(@Assisted ModelConfig<? extends FeatureMeta> modelConfig,
      @Assisted Classifier iClassifier) {
    this.modelConfig = modelConfig;
    this.classifier = iClassifier;
  }

  @Override
  public DistributionResult getDistribution(List<TInstance> tInstances, String classLabelForDist) throws
      ModelExecutionException {
    checkNotNull(tInstances, "impression set is null");
    checkArgument(tInstances.size() >= 0, "Number of impressions in the tInstances is empty");

    int numImpressions = tInstances.size();

    ArrayList<Attribute> attributes = createAttributes();

    Instances dataSet = new Instances(modelConfig.getName(), attributes, numImpressions);

    int classIdx = modelConfig.getNumberOfModelFeatures() - 1;
    //Important : class in the model features list should be just after the model attributes otherwise this assumption fails.
    dataSet.setClassIndex(classIdx);

    FeatureDebugInfo debugInfo = new FeatureDebugInfo(modelConfig);

    List<Instance> instanceList = createInstanceList(tInstances, dataSet, debugInfo);

    logger.info("Starting getDistribution for " + numImpressions + " feature sets");

    double[] scores = new double[numImpressions];

    for (int k = 0; k < numImpressions; k++) {
      Instance instance = instanceList.get(k);
      dataSet.add(instance);

      double[] fDistribution = null;
      try {
        //dataSet.lastInstance() is used instead of `instance` var because classification has to be run on the clone
        // of the `instance` which is maintained by the dataset.

        //the distribution returned is in the order of the labels passed for the class attribute e.g. {1:0}, so
        // first value will be for class "1" and second for class "0"
        fDistribution = this.classifier.distributionForInstance(dataSet.lastInstance());
      } catch (Exception e) {
        logger.error("Error while trying to get the distribution for the instance : " + instance);
        throw new ModelExecutionException("Error while trying to get the distribution for the instance : " + instance,
            e);
      }
      if (modelConfig.getFeatures().get(classIdx).getDataType()
          == FeatureDataType.NUMERIC) {
        logger.info("Numeric Distribution : {} ", fDistribution[0]);
        scores[k] = fDistribution[0];
      } else {
        int posClassIdx = attributes.get(classIdx).indexOfValue(classLabelForDist);
        logger.info("Class: {}, Score: {}", new Object[]{classLabelForDist, fDistribution[posClassIdx]});
        scores[k] = fDistribution[posClassIdx];
      }
    }
    return new DistributionResult(scores, debugInfo);
  }

  /**
   * Method to create list of weka instance objects from application instance objects
   *
   * @param tInstances - list of instances
   * @param dataSet - Weka structure object to get attribute information
   * @param debugInfo - debug info to populate with required and missing features
   * @return - list of weka instance objects
   */
  private List<Instance> createInstanceList(List<TInstance> tInstances,
      Instances dataSet,
      FeatureDebugInfo debugInfo) {
    List<Instance> listOfInstances = new ArrayList();

    for (TInstance tInstance : tInstances) {
      Instance instance = getInputInstance(tInstance, dataSet, debugInfo);
      listOfInstances.add(instance);
    }
    return listOfInstances;
  }

  /**
   * Converts a single application instance object to a weka instance object
   *
   * @param tInstance an application instance
   * @param dataset weka Instances object
   * @param debugInfo object to store required/missing/found information for every feature
   * @return a weka instance object
   */
  private Instance getInputInstance(TInstance tInstance, Instances dataset, FeatureDebugInfo debugInfo) {
    double[] attWekaVals = new double[this.modelConfig.getNumberOfModelFeatures()];

    for (int i = 0; i < this.modelConfig.getNumberOfModelFeatures(); i++) {
      Attribute attribute = dataset.attribute(i);
      TFeatureValue featureVal = tInstance.getFeatures() != null ? tInstance.getFeatures().get(attribute.name()) : null;

      boolean found = true;
      if (featureVal != null) {
        switch (attribute.type()) {
          case Attribute.NUMERIC:
            attWekaVals[i] = featureVal.getDoubleVal();
            break;
          case Attribute.NOMINAL:
            attWekaVals[i] = attribute.indexOfValue(featureVal.getStringVal());
            break;
          case Attribute.STRING:
            attWekaVals[i] = attribute.addStringValue(featureVal.getStringVal());
            break;
          case Attribute.DATE:
            try {
              attWekaVals[i] = attribute.parseDate(featureVal.getStringVal());
            } catch (ParseException e) {
              logger.error("Attribute {} is in a dateformat different from what configured for attributes",
                  attribute.name());
              attWekaVals[i] = Utils.missingValue();
              found = false;
            }
            break;
          case Attribute.RELATIONAL:
            logger.error("Relational attribute {} is not supported", attribute.name());
            attWekaVals[i] = Utils.missingValue();
            found = false;
            break;
          default:
            String errorMessage = String
                .format("Attribute {} type could not be recognized, check model configuration", attribute.name());
            logger.error(errorMessage);
            found = false;
            throw new RuntimeException(errorMessage);
        }
      } else {
        found = false;
        //Fixme : Ideally the median of the particular feature should be used for missing values
        // we do still count in the missing to indicate we used zeros for how many
        if (attribute.type() == Attribute.NUMERIC) {
          attWekaVals[i] = 0.0;
        } else {
          attWekaVals[i] = Utils.missingValue();
        }
      }
      if (found) {
        debugInfo.noteFoundFeature(attribute.name());
      } else {
        debugInfo.noteMissingFeature(attribute.name());
      }
    }
    return new DenseInstance(1.0, attWekaVals);
  }

  /**
   * Method to create a list of {@link Attribute}
   *
   * @return a list of weka {@link Attribute} objects
   */
  private ArrayList<Attribute> createAttributes() {
    ArrayList<Attribute> attributes = new ArrayList<>();
    //add all the non-class attribute
    for (FeatureMeta attMeta : this.modelConfig.getModelFeatures()) {
      Attribute attribute = createAttribute(attMeta);
      attributes.add(attribute);
    }
    return attributes;
  }

  /**
   * @param attMeta feature meta data
   * @return returns a weka {@link Attribute}
   */
  private Attribute createAttribute(FeatureMeta attMeta) {
    FeatureDataType dataType = attMeta.getDataType();

    String attName = attMeta.getName();

    switch (dataType) {
      case NUMERIC:
        return new Attribute(attName);
      case NOMINAL:
        //passing non-null for second argument ensures this is treated as a nominal attribute
        return new Attribute(attName, attMeta.getNominalValues());
      case STRING:
        //passing null ensure this is treated as a String attribute
        return new Attribute(attName, (List) null);
      case DATE:
        // TODO : Implement this properly when we have a date attributes
        // return new Attribute(attName, "yyyyMMdd");
        throw new UnsupportedOperationException("Date attributes are not supported");
      case RELATIONAL:
        // TODO : Implement this properly when we have a relational attribute
        // return new Attribute(attName, (Instances) null);
        throw new UnsupportedOperationException("Relational attributes/Multi instance classification is supported");
      case UNKNOWN:
        throw new RuntimeException("Unknown attribute type");
    }
    return null;
  }
}
