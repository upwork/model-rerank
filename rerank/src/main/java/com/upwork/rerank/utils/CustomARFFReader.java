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
package com.upwork.rerank.utils;

import com.upwork.rerank.FeatureDataType;
import com.upwork.rerank.thrift.TFeatureValue;
import com.upwork.rerank.thrift.TInstance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/**
 * This class is an utility and can be used for the purpose of converting the model specification coming in the ARFF
 * format into JSON format and also for loading the data coming in the ARFF file to test drive the model which use
 * the generated json config.
 *
 * @author agupta
 */
public class CustomARFFReader {

  public static final String NAME = "name";
  public static final String SHORT_NAME = "shortName";
  public static final String DATA_TYPE = "dataType";
  public static final String VALUES = "values";
  public static final String CLASS = "class";
  public static final String FEATURES = "features";
  private final ArffReader arffReader;

  /**
   * @param storeDir directory where the arff file is stored and where the json file is going to be output
   * @param arffFileName arff file name with extension ".arff"
   * @throws IOException for any issues with reading the files
   */
  public CustomARFFReader(String storeDir, String arffFileName) throws IOException {
    this.arffReader = getArffReader(storeDir, arffFileName);
  }

  /**
   * This method loaders data from ARFF file and converts into a list of {@link TInstance} objects. This method
   * should be used for testing out the model, in production the application using this library would supply the list
   * of {@link TInstance}
   *
   * @return a list of {@link TInstance}
   */
  public List<TInstance> getData() {
    List<TInstance> data = new ArrayList<>();

    Instances instances = arffReader.getData();

    Enumeration<Instance> instanceEnumeration = instances.enumerateInstances();

    int i = 0;
    while (instanceEnumeration.hasMoreElements()) {
      Instance instance = instanceEnumeration.nextElement();
      Enumeration<Attribute> attributeEnumeration = instance.enumerateAttributes();

      TInstance tInstance = new TInstance(i++, new HashMap<>());

      while (attributeEnumeration.hasMoreElements()) {
        Attribute attribute = attributeEnumeration.nextElement();
        String attName = attribute.name();
        tInstance.putToFeatures(attName, getTFeatureValue(attribute, instance));
      }
      data.add(tInstance);
    }
    return data;
  }

  /**
   * Reads an ARFF and converts it to the json config as required by the library
   *
   * @return an object map having the fields required in the json config for a model.
   */
  public Map<String, Object> convertToObjectMap() {
    Instances instances = arffReader.getStructure();

    Enumeration<Attribute> atts = instances.enumerateAttributes();

    Map<String, Object> jsonMap = new HashMap<>();

    jsonMap.put(NAME, instances.relationName());
    jsonMap.put(SHORT_NAME, instances.relationName());

    List features = new ArrayList();

    int i = 0;
    LinkedHashMap<String, Object> feature = null;
    while (atts.hasMoreElements()) {
      Attribute att = atts.nextElement();
      feature = new LinkedHashMap<>();

      String name = att.name();
      feature.put(NAME, name);

      FeatureDataType ft = getFeatureDataType(att);
      List<String> nominalVals = null;
      feature.put(DATA_TYPE, ft.toString().toLowerCase());
      if (ft == FeatureDataType.NOMINAL) {
        nominalVals = getValues(att.enumerateValues());
        feature.put(VALUES, nominalVals);
      }
      features.add(feature);
      i++;
    }
    //last feature is the class/target
    feature.put(CLASS, true);
    jsonMap.put(FEATURES, features);
    return jsonMap;
  }

  private static ArffReader getArffReader(String storeDir, String arffFileName) throws IOException {
    String fullFileName = RerankUtil.relativeToAbsPath(storeDir) + File.separatorChar + arffFileName;
    return new ArffReader(new BufferedReader(new FileReader(fullFileName)));
  }

  private static TFeatureValue getTFeatureValue(Attribute attribute, Instance instance) {
    TFeatureValue val = new TFeatureValue();
    if (attribute.isString() || attribute.isNominal()) {
      val.setStringVal(instance.stringValue(attribute.index()));
    } else if (attribute.isNumeric()) {
      val.setDoubleVal(instance.value(attribute.index()));
    } else {
      throw new UnsupportedOperationException("Feature type not supported " + attribute.type());
    }
    return val;
  }

  private static List<String> getValues(Enumeration<Object> objectEnumeration) {
    List<String> nominalVals = new ArrayList<>();

    while (objectEnumeration.hasMoreElements()) {
      Object val = objectEnumeration.nextElement();
      nominalVals.add((String) val);
    }
    return nominalVals;
  }

  private static FeatureDataType getFeatureDataType(Attribute att) {
    if (att.isNominal()) {
      return FeatureDataType.NOMINAL;
    } else if (att.isNumeric()) {
      return FeatureDataType.NUMERIC;
    } else if (att.isRelationValued()) {
      return FeatureDataType.RELATIONAL;
    } else if (att.isDate()) {
      return FeatureDataType.DATE;
    } else if (att.isString()) {
      return FeatureDataType.STRING;
    } else {
      return FeatureDataType.UNKNOWN;
    }
  }
}
