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
package com.upwork.rerank.impl;

import com.upwork.rerank.DistributionResult;
import com.upwork.rerank.IdDistribution;
import com.upwork.rerank.Model;
import com.upwork.rerank.ModelExecutionException;
import com.upwork.rerank.RerankLib;
import com.upwork.rerank.RerankResultSet;
import com.upwork.rerank.thrift.TInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link RerankLib}
 */
public class RerankLibImpl implements RerankLib {

  private static Logger logger = LoggerFactory.getLogger(RerankLibImpl.class);

  private final Model model;

  /**
   * Creates a RerankLibImpl as a wrapper around a {@link Model}
   *
   * @param iModel - the encapsulated model to execute to rerank
   */
  public RerankLibImpl(Model iModel) {
    this.model = iModel;
  }

  /**
   * This implementation passes the instances to the model and gets the distribution for them, sorts the
   * instances based on the distribution, maps each of the ids in an instance to its distribution. Finally it composes
   * the results and returns them.
   *
   * @param tInstances list of instances
   * @param classLabelToRerankOn class label for whose distribution the results will be re-ranked on
   * @return reranked result set
   * @throws ModelExecutionException is thrown if the execution fails for any reason
   */
  @Override
  public RerankResultSet rerank(List<TInstance> tInstances, String classLabelToRerankOn)
      throws ModelExecutionException {
    if (tInstances == null || tInstances.size() == 0){
      throw new IllegalArgumentException("Instances is null or empty");
    }
    DistributionResult distResult = model.getDistribution(tInstances, classLabelToRerankOn);

    List<Long> rerankedImpressions = sort(distResult.getDistribution(), tInstances);

    Map<Long, Double> distDebugInfo = getIdDistMap(tInstances, distResult.getDistribution());

    return new RerankResultSet(rerankedImpressions, distResult.getModelExecDebugInfo(), distDebugInfo);
  }

  /**
   * Maps ids to their distribution value
   *
   * @param tInstances list of instances
   * @param distribution an array of distribution for the instances
   * @return a map of id of the instance and its distribution
   */
  private Map<Long, Double> getIdDistMap(List<TInstance> tInstances,
      double[] distribution) {
    Map<Long, Double> idDistMap = new LinkedHashMap<>();

    int i = 0;
    for (TInstance tInstance : tInstances) {
      idDistMap.put(tInstance.getInstanceId(), distribution[i]);
      i++;
    }
    return idDistMap;
  }

  /**
   * Sorts a set of instances based on their distribution
   *
   * @param distribution distribution of the instances
   * @param tInstances instances
   * @return ids of the instances sorted by their distribution
   */
  private List<Long> sort(double[] distribution,
      List<TInstance> tInstances) {
    assert distribution.length == tInstances.size();

    List<IdDistribution> idDistributions = new ArrayList<>();

    int idx = 0;
    for (TInstance tInstance : tInstances) {
      idDistributions.add(new IdDistribution(tInstance.getInstanceId(), distribution[idx]));
      idx++;
    }

    final Comparator<IdDistribution> comparator = new Comparator<IdDistribution>() {
      @Override
      public final int compare(final IdDistribution impDist1,
          IdDistribution impDist2) {
        final double dist1 = impDist1.getDistribution();
        final double dist2 = impDist2.getDistribution();
        return Double.compare(dist1, dist2);
      }
    };

    Collections.sort(idDistributions, Collections.reverseOrder(comparator));

    List<Long> contractorIds = new ArrayList<>();
    for (IdDistribution impDis1 : idDistributions) {
      contractorIds.add(impDis1.getId());
    }

    return contractorIds;
  }
}
