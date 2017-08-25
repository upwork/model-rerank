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

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.upwork.rerank.impl.RerankLibImpl;
import com.upwork.rerank.thrift.TInstance;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.upwork.rerank.TestDataUtil.RERANK_MODEL;

/**
 * @author agupta
 */
@RunWith(DataProviderRunner.class)
public class RerankLibSortingTest {

  private final static Logger logger = LoggerFactory.getLogger(RerankLibSortingTest.class);

  private static Map<String, ModelConfig<FeatureMeta>> modelConfigMap;

  @BeforeClass
  public static void setup() throws IOException {
    List<String> supportedModelsList = Arrays.asList(RERANK_MODEL);

    ModelReader modelReader = new ModelReader();
    modelConfigMap = modelReader.readModelConfigs(supportedModelsList);
  }

  @Test
  public void testModelExecution()
      throws ModelExecutionException, ExecutionException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    logger.info("TestCase: testModelExecution");
    RerankLibFactory rerankLibFactory = RerankLibFactory.getInstance(10, 20);

    double[] dist = new double[]{1.0, 0.5, 0.8, 0.3};
    int records = dist.length;

    RerankLib modelLib = rerankLibFactory.getRerankLib(RERANK_MODEL);

    Assert.assertNotNull(modelLib);

    RerankLibImpl rerankLib = (RerankLibImpl) modelLib;

    List<TInstance> instances = TestDataUtil.createTInstances(records, modelConfigMap.get(RERANK_MODEL));

    long start = System.currentTimeMillis();
    Method method = rerankLib.getClass().getDeclaredMethod("sort", new double[]{}.getClass(), List.class);

    method.setAccessible(true);

    List<Long> sortedIds = (List<Long>) method.invoke(rerankLib, dist, instances);

    Assert.assertEquals(sortedIds.size(), instances.size());

    Assert.assertEquals(sortedIds.get(0).longValue(), 0);
    Assert.assertEquals(sortedIds.get(1).longValue(), 2);
    Assert.assertEquals(sortedIds.get(2).longValue(), 1);
    Assert.assertEquals(sortedIds.get(3).longValue(), 3);
  }
}