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
package com.upwork.rerank;

import com.upwork.rerank.thrift.TFeatureValue;
import com.upwork.rerank.thrift.TInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author agupta
 */
public class TestDataUtil {

  public static final String RERANK_MODEL = "rerank_model";

  private final static Logger logger = LoggerFactory.getLogger(TestDataUtil.class);

  public static List<TInstance> createTInstances(int numOfImpressions,
      ModelConfig<FeatureMeta> modelConfig) {
    List<TInstance> instances = new ArrayList<>(numOfImpressions);

    for (int i = 0; i < numOfImpressions; i++) {
      TInstance instance = new TInstance(i, new HashMap<>());

      for (FeatureMeta featureMeta : modelConfig.getModelFeatures()) {
        TFeatureValue tFeatureValue = new TFeatureValue();
        if (featureMeta.getDataType() == FeatureDataType.NOMINAL
            || featureMeta.getDataType() == FeatureDataType.STRING) {
          if (featureMeta.isClass()) {
            tFeatureValue = null;
          } else {
            tFeatureValue.setStringVal("random_string_here");
          }
        } else {
          tFeatureValue.setDoubleVal(Math.random());
        }
        instance.getFeatures().put(featureMeta.getName(), tFeatureValue);
      }
      instances.add(instance);
    }
    return instances;
  }
}
