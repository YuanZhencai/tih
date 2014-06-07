/**
 * Application.java
 * Created: 2011-11-29 上午2:12:00
 */
package com.wcs.tihm;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

import com.wcs.tihm.service.DummyResource;
import com.wcs.tihm.service.TihmNewsResource;
import com.wcs.tihm.service.TihmQuestionResource;
import com.wcs.tihm.service.TihmTaskResource;

/**
 * <p>Project: mds</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2011 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:gaoyuxiang@wcs-global.com">Gao Yuxiang</a>
 */
@ApplicationPath("rs")
public class Application extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
       Set<Class<?>> s = new HashSet<Class<?>>();
       s.add(DummyResource.class);
       s.add(TihmNewsResource.class);
       s.add(TihmQuestionResource.class);
       s.add(TihmTaskResource.class);
       return s;
    }

}
