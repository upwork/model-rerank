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
 * RerankLib interface. This defines the api for re-ranking a list of instances by the distribution for a given class
 */
public interface RerankLib {

  /**
   * This methods takes as input a list of instances and a class label to rerank the instances on.
   *
   * @param instances list of data instances, each of which is composed of an id and a set of features
   * @param classLabelToRerankOn class label whose distribution the results will be re-ranked on, required only for
   * classification model and null can be passed for a regression model.
   * @throws ModelExecutionException This exception is thrown if there are any issues with model execution
   */
  RerankResultSet rerank(List<TInstance> instances, String classLabelToRerankOn) throws ModelExecutionException;
}
