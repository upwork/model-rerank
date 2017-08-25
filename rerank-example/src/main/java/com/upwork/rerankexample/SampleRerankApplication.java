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
package com.upwork.rerankexample;

import com.upwork.rerank.ModelExecutionException;
import com.upwork.rerank.RerankLib;
import com.upwork.rerank.RerankLibFactory;
import com.upwork.rerank.RerankResultSet;
import com.upwork.rerank.apputils.JsonConfigGenerator;
import com.upwork.rerank.thrift.TInstance;
import com.upwork.rerank.utils.CustomARFFReader;
import com.upwork.rerank.utils.RerankUtil;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.fest.util.Strings;

/**
 * This is a sample reranking application which takes as input a sample dataset from an ARFF file and loads and executes
 * the models and displays the reranked results. To get this class working, the following needs to be done.
 *
 * <ol>
 *  <li>Place sample_models folder to some known location on disk, e.g. /upwork/sample_models.</li>
 *  <li>Update the properties "rerank.models.repo" and "rerank.models.supported" (see config.properties file) with
 *  the model store path and with the model name respectively. E.g. on Linux config.properties can be like this:
 *    <pre>
 *      rerank.models.repo=/upwork/sample_models
 *      rerank.models.supported=rerank_model
 *    </pre>
 *    or on Windows
 *    <pre>
 *      rerank.models.repo=/C:/upwork/sample_models
 *      rerank.models.supported=rerank_model
 *    </pre>
 *  </li>
 *  <li>Generate the json config from the ARFF file using {@link JsonConfigGenerator}</li>
 *  <li>Execute this class with the model name. The usage for executingthis file is as follows.
 *    <pre>
 *      java SampleRerankApplication rerank_model
 *    </pre>
 *  </li>
 * </ol>
 *
 * @author agupta
 */
public class SampleRerankApplication {

  public static void main(String[] args) throws ExecutionException, ModelExecutionException, IOException {
    if (args.length == 0 || Strings.isNullOrEmpty(args[0])) {
      throw new IllegalArgumentException("The usages is ::> java SampleRerankApplication <modelName>");
    }
    //Get the model name from the arguments
    String modelName = args[0].trim();

    // This application reads the model data from an arff file, however the data can come from any other source the
    // user depending on the end application
    CustomARFFReader reader = new CustomARFFReader(RerankUtil.getModelRepoPath(), modelName + ".arff");
    List<TInstance> instances = reader.getData();

    printListItems("Originally ranked items : ",
        instances.stream().map(x -> x.getInstanceId()).collect(Collectors.toList()));

    // Create the rerank lib with an internal LRU cache for model storage which can at max have 2 models and each of
    // those expire 10 mins after the last access
    RerankLib lib = RerankLibFactory.getInstance(2, 10).getRerankLib(modelName);

    //Rerank on class label "1"
    RerankResultSet rerankResultSet = lib.rerank(instances, "1");

    printListItems("Re-ranked items using model : " + modelName, rerankResultSet.getIds());
  }

  private static <T> void printListItems(String title, List<T> items) {
    System.out.println(title);
    for (int i = 0; i < items.size(); i++) {
      System.out.println("Item Rank : " + i + ", Item Id : " + items.get(i).toString());
    }
  }
}