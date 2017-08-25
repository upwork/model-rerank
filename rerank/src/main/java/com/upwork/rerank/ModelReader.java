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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.upwork.rerank.impl.ModelImpl;
import com.upwork.rerank.utils.RerankUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.classifiers.Classifier;
import weka.core.SerializationHelper;

/**
 * This class has methods to read the ".json" config file for models, load the ".model" file which is the weka binary
 * for a model and creates a classifier
 *
 * @author agupta
 */
@Singleton
public class ModelReader {

  private static Logger logger = LoggerFactory.getLogger(ModelReader.class);

  /**
   * Utility method to read the config for all the models in one go.
   *
   * @param supportedModels - list of supported models
   * @return - a map of model names and model configs
   * @throws IOException - for any issues with reading the files
   */
  public static Map<String, ModelConfig<FeatureMeta>> readModelConfigs(List<String> supportedModels)
      throws IOException {
    Map<String, ModelConfig<FeatureMeta>> tModelConfigs = new HashMap<String, ModelConfig<FeatureMeta>>();

    for (String modelName : supportedModels) {
      logger.debug("Reading config for model " + modelName);
      tModelConfigs.put(modelName, readModelConfig(modelName));
    }
    return tModelConfigs;
  }

  /**
   * Reads the model config and model weka binary to create a model
   *
   * @param modelName - name of the model
   * @return - a {@link Model} object
   * @throws IOException - for any issues with reading either the config or the weka model binary file
   */
  public static Model loadModel(String modelName) throws IOException {
    logger.info("Loading and Creating model : {}", modelName);
    if (RerankUtil.isSupportedModels(modelName)) {
      ModelConfig modelConfig = ModelReader.readModelConfig(modelName);
      Classifier classifier = ModelReader.createClassifier(modelConfig);
      return new ModelImpl(modelConfig, classifier);
    } else {
      throw new RuntimeException("Unsupported model " + modelName);
    }
  }

  /**
   * Reads the model config
   *
   * @param modelName - name of the model
   * @return - an instance of {@link ModelConfig}
   * @throws IOException - for any issues with reading the model config file.
   */
  private static ModelConfig readModelConfig(String modelName) throws IOException {
    logger.debug("Reading config for model " + modelName);
    String modelFeaturesPath = RerankUtil.getModelFeautresPath(modelName);

    File featureFile = new File(modelFeaturesPath);
    String featureFileStr = FileUtils.readFileToString(featureFile);

    return new ObjectMapper().readValue(featureFileStr, ModelConfig.class);
  }

  /**
   * Creates and returns a weka {@link Classifier} instance for a model
   *
   * @param modelConfig model config
   * @return a classifier for the model for which the model config is supplied
   * @throws IOException for any issues with reading the weka model binary file
   */
  public static Classifier createClassifier(ModelConfig modelConfig) throws IOException {
    Classifier classifier = null;

    String modelBinPath = RerankUtil.getModelBinPath(modelConfig.getName());

    File modelBinFile = new File(modelBinPath);
    FileInputStream modelInputStream = FileUtils.openInputStream(modelBinFile);

    try {
      logger.debug(
          "Loading a model : " + modelConfig.getName() + " from location : " + modelBinPath);
      classifier = (Classifier) SerializationHelper.read(modelInputStream);
      if (classifier != null) {
        logger.debug("Model is successfully loaded from : " + modelBinPath);
      } else {
        logger.error("Model loaded from : " + modelBinPath + " is null");
        throw new RuntimeException("Model is null : " + modelBinPath);
      }
    } catch (Exception ex) {
      logger.error("Exception is thrown while trying to create a model from : " + modelBinPath, ex);
      throw new RuntimeException("Exception is thrown while trying to create a model from : " + modelBinPath, ex);
    }
    return classifier;
  }
}
