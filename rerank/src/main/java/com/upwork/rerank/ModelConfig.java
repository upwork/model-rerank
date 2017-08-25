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
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.pojomatic.Pojomatic;

/**
 * Defines the object which holds the configuration of a model. This class can be further extended to support
 * features with extended metadata e.g. data sources.
 *
 * This class can be serialized from the json config for a model.
 *
 * @author agupta
 */

public class ModelConfig<T extends FeatureMeta> {

  protected String name;
  protected String shortName;
  protected List<T> features;

  // This field is added to support additional features which are not in the input to the model.
  // These features are called non-model features or auxiliary features and can be potentially used to extend the json
  // config for having additional functionality e.g. to support auxiliary features to compute model features.
  // This will be described more in extension possibilities section of the README.md.
  @JsonIgnore
  protected int numberOfModelFeatures = -1;

  @JsonIgnore
  protected ImmutableMap<String, T> featureNameMetaMap;

  /**
   * This constructor is required for the jackson's ObjectMapper to deserialize the json config.
   */
  public ModelConfig() {
  }

  /**
   * This constructor may be required to copy a model config into a new one albeit with a different object type for
   * features e.g. say ExtendedAttMeta which extends from FeatureMeta. This may be useful in cases when the json
   * supports additional information in the customConfig e.g. the data source definitions to import each of the
   * features
   *
   * @param name - name of the model.
   * @param shortName - short name of the model.
   * @param features - list of feature metadata objects for the model
   */
  public ModelConfig(String name, String shortName, List<T> features) {
    this.name = name;
    this.shortName = shortName;
    this.setFeatures(features);
  }

  /**
   * @return the name of the model.
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return returns the shortName of the model. The shortname may be useful in certain situations e.g. prefixing for
   * storage etc.
   */
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   * @return a list of metadata of all the features configured
   */
  public List<T> getFeatures() {
    return features;
  }

  public void setFeatures(List<T> features) {
    this.features = features;
    this.numberOfModelFeatures = computeNumberOfModelFeatures(features);
    this.featureNameMetaMap = getNameMetadataMap(features);
  }

  /**
   * @return the number of model features
   */
  public int getNumberOfModelFeatures() {
    return numberOfModelFeatures;
  }

  /**
   * @param featureName name of the feature for which to get the feature metadata
   * @return returns feature metadata for a given feature name
   */
  public T getFeatures(String featureName) {
    return this.featureNameMetaMap.get(featureName);
  }

  /**
   * Returns only the model features
   *
   * @return a list of model feature metadata
   */
  public List<T> getModelFeatures() {
    //Note : The top  numberOfModelFeatures in this list are model features as per current implementation.
    return this.features.subList(0, this.numberOfModelFeatures);
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

  /**
   * Computes the number of model features.
   *
   * @param features list of parameterized feature meta objects
   * @param <X> generic type of the items
   * @return the number of model features
   */
  private static <X extends FeatureMeta> int computeNumberOfModelFeatures(List<X> features) {
    int count = 0;
    for (int i = 0; i < features.size(); i++) {
      if (features.get(i).isModelFeature()) {
        count++;
      }
    }
    return count;
  }

  /**
   * @param features list of feature metadata objects
   * @param <X> the generic type for feature metadata objects
   * @return returns a mapping of feature name to feature given a list of features
   */
  private static <X extends FeatureMeta> ImmutableMap<String, X> getNameMetadataMap(List<X> features) {
    return ImmutableMap.copyOf(features.stream().collect(Collectors.toMap(X::getName, Function
        .identity())));
  }
}
