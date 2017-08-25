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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.upwork.rerank.impl.RerankLibImpl;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A factory class for RerankLib. This class takes care of caching the models and returning RerankLib instances with
 * cached models
 *
 * @author agupta
 */
public class RerankLibFactory {

  private static volatile RerankLibFactory instance = null;
  private LoadingCache<String, Model> cache;

  /**
   * Private constructor for creating RerankLibFactory
   *
   * @param maxEntries - number of max entries in the cache
   * @param expiresAfterAccessMin - expiration in Mins for a cache entry
   */
  private RerankLibFactory(int maxEntries, int expiresAfterAccessMin) {
    cache = CacheBuilder.newBuilder()
        .maximumSize(maxEntries)
        .expireAfterAccess(expiresAfterAccessMin, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Model>() {

          @Override
          public Model load(String modelName) throws Exception {
            //make the expensive call
            return ModelReader.loadModel(modelName);
          }
        });
  }

  /**
   * Factory method to create a RerankLibFactory instance
   *
   * @param maxEntries - number of max entries in the cache
   * @param expiresAfterAccessMin - expiration in Mins for a cache entry
   * @return - an instance of {@link RerankLibFactory}
   */
  public static RerankLibFactory getInstance(int maxEntries, int expiresAfterAccessMin) {
    if (instance == null) {
      synchronized (RerankLibFactory.class) {
        if (instance == null) {
          instance = new RerankLibFactory(maxEntries, expiresAfterAccessMin);
        }
      }
    }
    return instance;
  }

  /**
   * Creates an instance of RerankLib given a model name
   *
   * @param modelName name of the model
   * @return an instance of RerankLib
   * @throws ExecutionException exception related with cache get operations
   */
  public RerankLib getRerankLib(String modelName) throws ExecutionException {
    return new RerankLibImpl(cache.get(modelName));
  }
}
