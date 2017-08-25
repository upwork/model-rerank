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
 * This class encapsulates distribution for a set of instances and the feature stats debug info for the features.
 *
 * @author agupta
 */
@AutoProperty
public class DistributionResult {

  private double[] distribution;
  private FeatureDebugInfo modelExecDebugInfo;

  /**
   * Creates an instance with distribution and the feature debug info
   *
   * @param distribution distribution array
   * @param modelExecDebugInfo debug info containing feature stats during model execution
   */
  public DistributionResult(double[] distribution,
      FeatureDebugInfo modelExecDebugInfo) {
    this.distribution = distribution;
    this.modelExecDebugInfo = modelExecDebugInfo;
  }

  public FeatureDebugInfo getModelExecDebugInfo() {
    return modelExecDebugInfo;
  }

  public double[] getDistribution() {
    return distribution;
  }

  public void setDistribution(double[] distribution) {
    this.distribution = distribution;
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
