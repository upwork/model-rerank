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

/**
 * File containing various properties required by this library
 *
 * Created by @agupta
 */
public class RerankProperties {

  /**
   * Property pointing to the file system location where the ".model" files and their corresponding ".json" files are
   * stored
   */
  public static final String MODELS_REPO_PROP = "rerank.models.repo";

  /**
   * Property for listing the supported models
   */
  public static final String RERANK_MODELS_SUPPORTED_PROP = "rerank.models.supported";
}
