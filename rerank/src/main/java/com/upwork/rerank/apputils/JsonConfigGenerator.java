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
package com.upwork.rerank.apputils;

import static com.upwork.rerank.utils.RerankUtil.MODEL_ARFF_FILE_SUFFIX;
import static com.upwork.rerank.utils.RerankUtil.MODEL_JSON_FILE_SUFFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.rerank.utils.CustomARFFReader;
import com.upwork.rerank.utils.RerankUtil;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.fest.util.Strings;

/**
 * This is an utility which can convert a given ARFF file into the custom json config being used by the rerank
 * library. The usage of this class is as follows.
 *
 * java JsonConfigGenerator modelName
 *
 * @author agupta
 */
public class JsonConfigGenerator {

  public static void main(String[] args) throws IOException {
    if (args.length == 0 || Strings.isNullOrEmpty(args[0])) {
      throw new IllegalArgumentException("The usages is --> java JsonConfigGenerator <modelName>");
    }
    String modelName = args[0].trim();

    CustomARFFReader reader = new CustomARFFReader(RerankUtil.getModelRepoPath(), modelName + MODEL_ARFF_FILE_SUFFIX);

    Map<String, Object> objectMap = reader.convertToObjectMap();
    String fullJsonFilePath = RerankUtil.getModelRepoPath() + File.separatorChar + modelName + MODEL_JSON_FILE_SUFFIX;

    System.out.println("Generating .json config file : " + fullJsonFilePath);

    new ObjectMapper().writeValue(new File(fullJsonFilePath), objectMap);
  }
}
