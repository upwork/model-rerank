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

import java.util.HashMap;
import java.util.Map;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * This class holds the stats about each of the features of the model.
 *
 * @author agupta
 */
@AutoProperty
public class FeatureDebugInfo {

  private static final int COUNT_PADDING = 10;
  private static final int FEATURE_PADDING = 60;

  private ModelConfig<? extends FeatureMeta> modelConfig;
  private Map<String, FeatureStats> featureStatsMap;

  /**
   * Creates a FeatureDebugInfo object for the given {@link ModelConfig}
   *
   * @param modelConfig modelConfig to manage the feature debug info for a model and its features
   */
  public FeatureDebugInfo(ModelConfig<? extends FeatureMeta> modelConfig) {
    this.modelConfig = modelConfig;
    this.featureStatsMap = new HashMap<>();
  }

  /**
   * @return the model config for which this FeatureDebugInfo is collected
   */
  public ModelConfig<? extends FeatureMeta> getModelConfig() {
    return modelConfig;
  }

  /**
   * @return a mapping from feature to {@link FeatureStats}
   */
  public Map<String, FeatureStats> getFeatureStatsMap() {
    return featureStatsMap;
  }

  /**
   * This method increments the requirement and missing counts for the given feature by 1
   *
   * @param featureName name of the feature whose requirement and missing count is to be updated
   */
  public void noteMissingFeature(String featureName) {
    addFeatureStats(featureName, new FeatureStats(1, 1));
  }

  /**
   * This method increments the required count for the given feature by 1
   *
   * @param featureName name of the feature whose required count is to be updated
   */
  public void noteFoundFeature(String featureName) {
    addFeatureStats(featureName, new FeatureStats(1, 0));
  }

  /**
   * Merges the input FeatureDebugInfo with this one.
   *
   * @param debugInfo feature debug info to be merged in
   */
  public void mergeIn(FeatureDebugInfo debugInfo) {
    if (debugInfo == null || debugInfo.featureStatsMap == null || debugInfo.featureStatsMap.size() == 0) {
      return;
    }

    for (String featureName : debugInfo.featureStatsMap.keySet()) {
      addFeatureStats(featureName, debugInfo.featureStatsMap.get(featureName));
    }
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
    StringBuilder sb = new StringBuilder();

    sb.append("\n");
    sb.append(padRight("Feature,", FEATURE_PADDING));
    sb.append(padRight("Required,", COUNT_PADDING));
    sb.append(padRight("Missing", COUNT_PADDING));

    for (FeatureMeta attMeta : modelConfig.getFeatures()) {
      FeatureStats featureStats = featureStatsMap.get(attMeta.getName());
      if (featureStats == null) {
        continue;
      }
      sb.append("\n");
      sb.append(padRight(attMeta.getName() + ",", FEATURE_PADDING));
      sb.append(padRight(featureStats.getFeatureNeededTimes() + ",", COUNT_PADDING));
      sb.append(padRight(featureStats.getFeatureMissingTimes() + ",", COUNT_PADDING));
    }
    return sb.toString();
  }

  /**
   * Add a feature stat to this FeatureDebugInfo object
   *
   * @param featureName name of the feature
   * @param incrementalStat feature stats to be added
   */
  private void addFeatureStats(String featureName,
      FeatureStats incrementalStat) {
    FeatureStats featureStats = featureStatsMap.get(featureName);
    if (featureStats == null) {
      featureStats = new FeatureStats();
      featureStatsMap.put(featureName, featureStats);
    }
    featureStats
        .incrementFeatureStats(incrementalStat.getFeatureNeededTimes(), incrementalStat.getFeatureMissingTimes());
  }

  /**
   * Right pads a {@link String} by x spaces
   *
   * @param s input string
   * @param x length of padding
   * @return right padded string
   */
  private String padRight(String s,
      int x) {
    return String.format("%1$-" + x + "s", s);
  }
}
