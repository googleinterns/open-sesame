package com.google.opensesame.github;
import java.util.HashMap;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.concurrent.TimeUnit;
import com.google.appengine.api.memcache.jsr107cache.*;


public class OpenSesameCache  {
  private static long HOURS_TO_CACHE_FOR =  1L;

  private static Cache instantiateCache() {
    Cache cache;
    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      Map<Object, Object> properties = new HashMap<>();
      properties.put(GCacheFactory.EXPIRATION_DELTA, 
          TimeUnit.HOURS.toSeconds(HOURS_TO_CACHE_FOR));
      cache = cacheFactory.createCache(properties);
    } catch (CacheException e) {
      cache = null;
    }
    return cache;
  }
}