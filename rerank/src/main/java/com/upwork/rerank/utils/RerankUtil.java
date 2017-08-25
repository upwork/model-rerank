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

import static com.upwork.rerank.FeatureJavaType.DOUBLE;
import static com.upwork.rerank.FeatureJavaType.STRING;
import static org.apache.xalan.xsltc.runtime.Operators.EQ;
import static org.apache.xalan.xsltc.runtime.Operators.GE;
import static org.apache.xalan.xsltc.runtime.Operators.GT;
import static org.apache.xalan.xsltc.runtime.Operators.LE;
import static org.apache.xalan.xsltc.runtime.Operators.LT;
import static org.apache.xalan.xsltc.runtime.Operators.NE;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.DynamicStringSetProperty;
import com.upwork.rerank.FeatureJavaType;
import com.upwork.rerank.FeatureMeta;
import com.upwork.rerank.RerankProperties;
import com.upwork.rerank.thrift.TFeatureValue;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.fest.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author agupta
 */
@Singleton
public class RerankUtil {

  private static final Logger logger = LoggerFactory.getLogger(RerankUtil.class);

  public final static String MODEL_FILE_SUFFIX = ".model";
  public final static String MODEL_JSON_FILE_SUFFIX = ".json";
  public final static String MODEL_ARFF_FILE_SUFFIX = ".arff";
  public static final String WEBAPP_MODELS = "../models/";
  public static final Double DOUBLE_ZERO = new Double(0.0);

  public String createFeatureCombinedId(Long primaryId,
      Long secondaryId) {
    Preconditions.checkArgument(primaryId != null, "primaryId is null");
    Preconditions.checkArgument(secondaryId != null, "secondaryId is null");

    return primaryId + "_" + secondaryId;
  }

  public boolean isValid(String val,
      FeatureMeta attMeta) {
    FeatureJavaType featureDataType = attMeta.getDataType().getJavaType();

    if (featureDataType == DOUBLE) {
      try {
        Double.parseDouble(val);
      } catch (NumberFormatException nfe) {
        logger.error("Parse error for feature {}, dataType {}, and val {}",
            new Object[]{attMeta.getName(), featureDataType, val});
        return false;
      }
    }
    return true;
  }

  public static boolean isNumericZero(String val,
      FeatureMeta attMeta) {
    FeatureJavaType featureDataType = attMeta.getDataType().getJavaType();

    if (featureDataType == DOUBLE) {
      //parsing should not cause any exception since we have already checked for validity.
      Double doubleVal = Double.parseDouble(val);
      return Double.compare(DOUBLE_ZERO, doubleVal) == 0;
    }
    return false;
  }

  /**
   * This is parameterized by the property name because both sync and search may run on same box and both may same
   * custom.properties. This is no needed if files are part of the artifact.
   *
   * @return a list of names of supported models.
   */
  public static List<String> getSupportedModels() {
    DynamicStringSetProperty modelsProp = new DynamicStringSetProperty(RerankProperties.RERANK_MODELS_SUPPORTED_PROP,
        new HashSet<>());

    Set<String> supportedModels = modelsProp.get();

    if (supportedModels.size() == 0) {
      logger.error("No models enabled for reranking");
      return new ArrayList<String>();
    }
    return supportedModels.stream().map(x -> x.toLowerCase()).collect(Collectors.toList());
  }

  public static TFeatureValue getTFeatureValueFromStr(FeatureJavaType javaType,
      String featureVal) {
    if (featureVal == null) {
      //whatever is missing, model logs it so not required to be logged while retrieving/parsing
      return null;
    }

    TFeatureValue featureValueUnion = null;

    if (javaType == STRING) {
      featureValueUnion = TFeatureValue.stringVal(featureVal);
    } else if (javaType == DOUBLE) {
      featureValueUnion = TFeatureValue.doubleVal(Double.valueOf(featureVal));
    } else {
      logger.error("Detected java type other than STRING/DOUBLE : {} ", javaType);
    }
    return featureValueUnion;
  }

  public TFeatureValue getTFeatureValueFromObj(FeatureJavaType att,
      Object featureVal) {
    if (featureVal == null) {
      return null;
    }
    return getTFeatureValueFromStr(att, featureVal.toString());
  }

  public boolean isNullOrEmpty(Map<String, TFeatureValue> tFeatures) {
    if (tFeatures == null || tFeatures.size() == 0) {
      return true;
    }
    return false;
  }

  public static boolean isNullOfStringEmpty(TFeatureValue strFeatureVal) {
    return strFeatureVal == null || strFeatureVal.isSetStringVal() == false || Strings
        .isNullOrEmpty(strFeatureVal.getStringVal());
  }

  public static boolean isNullOfDoubleEmpty(TFeatureValue doubleFeatureVal) {
    return doubleFeatureVal == null || doubleFeatureVal.isSetDoubleVal() == false;
  }

  public void ensureNotNull(Map<Long, Map<String, TFeatureValue>> featuresForAllPairs,
      List<Long> ids) {
    for (Long id : ids) {
      putIfAbsent(featuresForAllPairs, id);
    }
  }

  public Map<String, TFeatureValue> putIfAbsent(Map<Long, Map<String, TFeatureValue>> featuresMap,
      Long id) {
    if (featuresMap.get(id) == null) {
      featuresMap.put(id, new HashMap<String, TFeatureValue>());
    }
    return featuresMap.get(id);
  }

  public static boolean isRel(Double d1,
      Double d2,
      int op) throws RuntimeException {
    int res = Double.compare(d1, d2);

    switch (op) {
      case EQ:
        return res == 0;
      case NE:
        return res != 0;
      case GT:
        return res > 0;
      case GE:
        return res >= 0;
      case LT:
        return res < 0;
      case LE:
        return res <= 0;
      default:
        throw new RuntimeException("Erroneous Operator used :" + op);
    }
  }

  public static boolean isSupportedModels(String modelName) {
    for (String model : getSupportedModels()) {
      if (model.equalsIgnoreCase(modelName)) {
        return true;
      }
    }
    return false;
  }

  public static String getModelRepoPath() {
    DynamicStringProperty modelsRepo = new DynamicStringProperty(RerankProperties.MODELS_REPO_PROP,
        WEBAPP_MODELS);
    return relativeToAbsPath(modelsRepo.get());
  }

  public static String relativeToAbsPath(String path) {
    if (path.startsWith("/")) {
      return path;
    }
    return System.getProperty("user.dir") + File.separatorChar + path;
  }

  public static String getModelFeautresPath(String modelName) {
    return getModelRepoPath() + File.separatorChar + modelName + MODEL_JSON_FILE_SUFFIX;
  }

  public static String getModelBinPath(String modelName) {
    return getModelRepoPath() + File.separatorChar + modelName + MODEL_FILE_SUFFIX;
  }
}
