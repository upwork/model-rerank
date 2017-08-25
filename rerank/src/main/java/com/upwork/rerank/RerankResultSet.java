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

import java.util.List;
import java.util.Map;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class RerankResultSet {

  private List<Long> ids;
  private FeatureDebugInfo feature;
  private Map<Long, Double> distDebugInfo;

  public RerankResultSet(List<Long> ids,
      FeatureDebugInfo feature,
      Map<Long, Double> distDebugInfo) {
    this.ids = ids;
    this.feature = feature;
    this.distDebugInfo = distDebugInfo;
  }

  public List<Long> getIds() {
    return ids;
  }

  public FeatureDebugInfo getFeatureDebugInfo() {
    return feature;
  }

  public Map<Long, Double> getDistDebugInfo() {
    return distDebugInfo;
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
