/** * FileCacheService.java 
 * Created on 2014年5月5日 下午4:15:03 
 */

package com.wcs.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;


/** 
* <p>Project: tih</p> 
* <p>Title: FileCacheService.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@Stateless
public class FileCacheService implements CacheInterface {
	private static Logger logger = LoggerFactory.getLogger(FileCacheService.class);

	private static final String CACHE_FOLDER = "cache";

	private static String cacheFolderPath = null;
	
	@PostConstruct
	private void init() {
		cacheFolderPath = getCacheFolderPath();
	}

	private String getCacheFolderPath(){
		String folderPath = JSFUtils.getRootPath() + File.separator + CACHE_FOLDER + File.separator;
		File file = new File(folderPath);
		if(file != null && !file.exists()){
			file.mkdir();
		}
		logger.info("[Cache Folder Path]" + folderPath);
		return folderPath;
	}
	
	@Override
	public void putCache(String key, Cache obj) {
		ObjectOutputStream os = null;
		try {
			FileOutputStream cacheFileOs = new FileOutputStream(cacheFolderPath+ key);
			os = new ObjectOutputStream(cacheFileOs);
			os.writeObject(obj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public Cache getCache(String key) {
		Cache cache;
		ObjectInputStream ois = null;
		String cacheFileName = cacheFolderPath + key;
		File cacheFile = new File(cacheFileName);
		try {
			if (cacheFile != null && cacheFile.exists()) {
				FileInputStream cacheFileIs = new FileInputStream(cacheFileName);
				ois = new ObjectInputStream(cacheFileIs);
				cache = (Cache) ois.readObject();
				return cache;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	@Override
	public void removeCache(String key) {
		File file = new File(cacheFolderPath + key);
		if (file != null && file.isFile()) {
			file.delete();
		}
	}

	@Override
	public void clear() {
		File cacheFolder = new File(cacheFolderPath);
		if (cacheFolder.isDirectory()) {
			File[] files = cacheFolder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}

	}

	@Override
	public void bulkRemove(String prefix) {
		File cacheFolder = new File(cacheFolderPath);
		if (cacheFolder.isDirectory()) {
			File[] files = cacheFolder.listFiles();
			for (File file : files) {
				if (file.isFile() && file.getName().startsWith(prefix)) {
					file.delete();
				}
			}
		}
	}
	
}
