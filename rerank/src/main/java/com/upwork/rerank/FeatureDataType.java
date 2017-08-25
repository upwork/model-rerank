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

import java.io.Serializable;

/**
 * Defines the data type of the features for weka. Every weka data type maps to a java type
 *
 * @author agupta
 */
public enum FeatureDataType implements Serializable {
  NUMERIC(FeatureJavaType.DOUBLE),
  NOMINAL(FeatureJavaType.STRING),
  STRING(FeatureJavaType.STRING),
  DATE(FeatureJavaType.STRING),
  RELATIONAL(FeatureJavaType.UNKNOWN),
  UNKNOWN(FeatureJavaType.UNKNOWN);

  private FeatureJavaType javaType = FeatureJavaType.UNKNOWN;

  FeatureDataType(FeatureJavaType javaType) {
    this.javaType = javaType;
  }

  /**
   * Gets {@link FeatureDataType} for a given string representation
   *
   * @param name name representation of the potential enum
   * @return {@link FeatureDataType} for a given string representation
   */
  public static FeatureDataType getEnum(String name) {
    name = name.toUpperCase();
    return valueOf(name);
  }

  /**
   * @return the {@link FeatureJavaType} for this FeatureDataType
   */
  public FeatureJavaType getJavaType() {
    return this.javaType;
  }
}
