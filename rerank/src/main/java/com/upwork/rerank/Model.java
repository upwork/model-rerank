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

import com.upwork.rerank.thrift.TInstance;
import java.util.List;

/**
 * This abstracts out an executable Model.
 */
public interface Model {

  /**
   * This method returns the distribution of the instances in the order the instances are passed.
   *
   * @param tInstances the instances
   * @param classLabelForDist this is the class label for which distribution would be returned.
   * @return a {@link DistributionResult} object which has the distribution and the debug info.
   * @throws ModelExecutionException for any issues with model exception
   */
  DistributionResult getDistribution(List<TInstance> tInstances, String classLabelForDist) throws
      ModelExecutionException;
}
