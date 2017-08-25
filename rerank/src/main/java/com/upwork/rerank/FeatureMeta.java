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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * Class containing metadata for feature required by the models
 *
 * @author agupta
 */
@AutoProperty
public class FeatureMeta implements Serializable {

  private String name;

  @JsonProperty("dataType")
  private String dataTypeStr;

  @JsonProperty("modelFeature")
  private Boolean isModelFeature = true;

  @JsonProperty("values")
  private List<String> nominalValues;

  private Map<String, Object> customConfig;

  @JsonProperty("class")
  private Boolean isClass = false;

  @JsonProperty("scoreOn")
  private String scoreOn;

  @JsonIgnore
  private FeatureDataType dataType;

  /**
   * This constructor is required for the ObjectMapper to serialize json
   */
  public FeatureMeta() {

  }

  /**
   * This is a copy constructor. This is useful when an instance of subclass of this class is to be created. In such
   * cases, all the parent attributes in the instances can be copied using the super() call.
   *
   * @param featureMeta the FeatureMeta instance to copy from
   */
  public FeatureMeta(FeatureMeta featureMeta) {
    this.name = featureMeta.getName();
    this.setDataTypeStr(featureMeta.getDataTypeStr());
    this.isModelFeature = featureMeta.isModelFeature();
    this.nominalValues = featureMeta.getNominalValues();
    this.isClass = featureMeta.isClass();
    this.customConfig = featureMeta.getCustomConfig();
    this.scoreOn = featureMeta.scoreOn;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FeatureDataType getDataType() {
    return dataType;
  }

  private String getDataTypeStr() {
    return dataTypeStr;
  }

  /**
   * This method is used by the json deserializer to create an instance of this class
   *
   * @param dataTypeStr a string indicating the dataType, generally comes from the json config for the model
   */
  public void setDataTypeStr(String dataTypeStr) {
    this.dataTypeStr = dataTypeStr;
    this.dataType = FeatureDataType.getEnum(this.dataTypeStr);
  }

  /**
   * @return a boolean indicating if this is a model feature
   */
  public boolean isModelFeature() {
    return isModelFeature;
  }

  /**
   * This methods is used by the Json deserializer
   *
   * @param isModelFeature boolean to indicate if this is a model feature. The default value is true
   */
  public void setIsModelFeature(Boolean isModelFeature) {
    this.isModelFeature = isModelFeature == null ? true : isModelFeature.booleanValue();
  }

  /**
   * Returns the optional custom config as a map. This can be used to extend the functionality of the json config e.g
   * adding custom configuration for data sources of the features if they come from heterogenous sources.
   *
   * @return a map containing the custom config
   */
  public Map<String, Object> getCustomConfig() {
    return customConfig;
  }

  /**
   * This method is called by the Json deserializer to set the custom config
   *
   * @param customConfig custom config map
   */
  public void setCustomConfig(Map<String, Object> customConfig) {
    this.customConfig = customConfig;
  }

  /**
   * @return the list of nominal values for a feature whose {@link FeatureDataType} is NOMINAL
   */
  public List<String> getNominalValues() {
    return nominalValues;
  }

  /**
   * Method to set nominal values of a feature if it is a nominal feature
   *
   * @param nominalValues list of nominal values
   */
  public void setNominalValues(List<String> nominalValues) {
    this.nominalValues = nominalValues;
  }

  /**
   * This method is called by the Json deserializer to set if this feature represents the class
   *
   * @param isClass if it is a class feature
   */
  public void setIsClass(Boolean isClass) {
    this.isClass = isClass;
  }

  /**
   * @return a boolean indicating if this is a feature indicating the class
   */
  public Boolean isClass() {
    return isClass;
  }

  /**
   * Gets the value of the class attribute on which to score descending
   * @return
   */
  public String getScoreOn() {
    return scoreOn;
  }

  /**
   * Sets the value of the class attribute on which to score descending
   * @return
   */
  public void setScoreOn(String scoreOn) {
    this.scoreOn = scoreOn;
  }

  @Override
  public boolean equals(Object o) {
    return Pojomatic.equals(this, o);
  }

  @Override
  public int hashCode() {
    return Pojomatic.hashCode(this);
  }

  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
