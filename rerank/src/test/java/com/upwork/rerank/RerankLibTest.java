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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.upwork.rerank.thrift.TInstance;
import java.io.IOException;
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
public class RerankLibTest {

  private final static Logger logger = LoggerFactory.getLogger(RerankLibTest.class);

  private static Map<String, ModelConfig<FeatureMeta>> modelConfigMap;

  @BeforeClass
  public static void setup() throws IOException {
    List<String> supportedModelsList = Arrays.asList(RERANK_MODEL);

    ModelReader modelReader = new ModelReader();
    modelConfigMap = modelReader.readModelConfigs(supportedModelsList);
  }

  @Test
  @UseDataProvider("modelDataProvider")
  public void testModelExecution(String testCaseId,
      String modelName,
      int numImp) throws ModelExecutionException, ExecutionException {
    logger.info("TestCase: " + testCaseId + ", modelName: " + modelName);
    RerankLibFactory rerankLibFactory = RerankLibFactory.getInstance(10, 20);

    RerankLib modelLib = rerankLibFactory.getRerankLib(modelName);

    Assert.assertNotNull(modelLib);

    List<TInstance> instances = TestDataUtil.createTInstances(numImp, modelConfigMap.get(modelName));

    long start = System.currentTimeMillis();
    RerankResultSet resultSet = modelLib.rerank(instances, "1");
    logger.info("Time to run model {} on {} is {}",
        new Object[]{modelName, numImp, (System.currentTimeMillis() - start)});

    Assert.assertNotNull(resultSet);
    Assert.assertNotNull(resultSet.getIds());
    Assert.assertEquals(resultSet.getIds().size(), numImp);
  }

  @DataProvider
  public static Object[][] modelDataProvider() {
    Object[] testCase1 = new Object[]{"TC1", RERANK_MODEL, 10};

    return new Object[][]{testCase1};
  }
}