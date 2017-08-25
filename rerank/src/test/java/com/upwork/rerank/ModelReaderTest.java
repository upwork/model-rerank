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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.upwork.rerank.TestDataUtil.RERANK_MODEL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author agupta
 */

@RunWith(DataProviderRunner.class)
public class ModelReaderTest {

  private static Map<String, ModelConfig<FeatureMeta>> modelConfigMap;

  public static final String TOTAL_ATT = "total_att";
  public static final String MODEL_ATT = "model_att";

  @BeforeClass
  public static void beforeClass() throws IOException {
    modelConfigMap = new ModelReader()
        .readModelConfigs(Arrays.asList(RERANK_MODEL));
  }

  int count(Map<String, Integer> counts, String att) {
    if (counts.get(att) == null) {
      return 0;
    }
    return counts.get(att).intValue();
  }

  @Test
  @UseDataProvider("modelMetaProvider")
  public void testModelMeta(String modelName,
      String modelShortName,
      Map<String, Integer> counts) {
    ModelConfig modelConfig = modelConfigMap.get(modelName);

    assertNotNull(modelConfig);
    assertNotNull(modelConfig.getName(), modelName);
    assertNotNull(modelConfig.getShortName(), modelShortName);

    assertNotNull(modelConfig.getFeatures());
    assertEquals(count(counts, TOTAL_ATT), modelConfig.getFeatures().size());

    assertNotNull(modelConfig.getModelFeatures());
    assertEquals(count(counts, MODEL_ATT), modelConfig.getNumberOfModelFeatures());
    assertEquals(count(counts, MODEL_ATT), modelConfig.getModelFeatures().size());
  }

  @DataProvider
  public static Object[][] modelMetaProvider() {
    Map<String, Integer> countsMap1 = new HashMap<>();
    countsMap1.put(TOTAL_ATT, 6);
    countsMap1.put(MODEL_ATT, 6);

    Object[] testCase1 = new Object[]{RERANK_MODEL, RERANK_MODEL, countsMap1};

    return new Object[][]{testCase1};
  }
}
