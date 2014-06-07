/** * MemoryCacheService.java 
* Created on 2014年5月6日 上午9:47:55 
*/

package com.wcs.cache;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* <p>Project: tih</p> 
* <p>Title: MemoryCacheService.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@Singleton
@Startup
@Lock(LockType.READ)
public class MemoryCacheService implements CacheInterface {
	private static Logger logger = LoggerFactory.getLogger(MemoryCacheService.class);
	
	
	private ConcurrentHashMap<String, Cache> cache = new ConcurrentHashMap<String, Cache>();
	
	@PostConstruct
	private void init() {
		logger.debug("MemoryCacheService.init()");
	}
	
	@Override
	@AccessTimeout(1000)
	@Lock(LockType.WRITE)
	public void putCache(String key, Cache obj) {
		cache.put(key, obj);
	}

	@Override
	public Cache getCache(String key) {
		return cache.get(key);
	}

	@Override
	public void removeCache(String key) {
		cache.remove(key);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	@AccessTimeout(0)
    @Lock(LockType.WRITE)
	public void bulkRemove(String prefix) {
		for (String key : cache.keySet()) {
            if (key.startsWith(prefix)) {
                cache.remove(key);
            }
        }
	}
	
	@PreDestroy
	private void cleanup(){
		logger.debug("MemoryCacheService.cleanup()");
	}

}
