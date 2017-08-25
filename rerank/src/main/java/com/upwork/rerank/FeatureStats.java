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

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * Class encapsulating two counts for every feature; the number of times a feature was expected and the number of
 * times it was missing
 *
 * @author agupta
 */
@AutoProperty
public class FeatureStats {

  private long featureNeededTimes = 0;
  private long featureMissingTimes = 0;

  /**
   * Creates an empty feature stat object with both the counts set to zero
   */
  public FeatureStats() {
  }

  /**
   * Creates a feature stat object with the supplied counts
   *
   * @param featureNeededTimes number of times a feature was expected
   * @param featureMissingTimes number of times a feature was missing
   */
  public FeatureStats(long featureNeededTimes, long featureMissingTimes) {
    this.featureNeededTimes = featureNeededTimes;
    this.featureMissingTimes = featureMissingTimes;
  }

  /**
   * @return number of times a feature was expected
   */
  public long getFeatureNeededTimes() {
    return featureNeededTimes;
  }

  /**
   * @return number of times a feature was missing
   */
  public long getFeatureMissingTimes() {
    return featureMissingTimes;
  }

  /**
   * Update the feature stats with the supplied counts
   *
   * @param required incremental required count
   * @param missing incremental missing count
   */
  public void incrementFeatureStats(long required, long missing) {
    this.featureNeededTimes += required;
    this.featureMissingTimes += missing;
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
