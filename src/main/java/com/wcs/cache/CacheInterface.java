/** * CacheInterface.java 
 * Created on 2014年5月5日 下午3:53:49 
 */

package com.wcs.cache;

import javax.ejb.Local;


/** 
* <p>Project: tih</p> 
* <p>Title: CacheInterface.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@Local
public interface CacheInterface {

	public void putCache(String key, Cache obj);

	public Cache getCache(String key);

	public void removeCache(String key);
	
	public void clear();
	
	public void bulkRemove(String prefix);

}
