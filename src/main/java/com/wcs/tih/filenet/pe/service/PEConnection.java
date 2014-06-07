package com.wcs.tih.filenet.pe.service;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.wcs.tih.filenet.helper.pe.SessionHelper;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;

public class PEConnection implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(PEConnection.class);


    private static class SingletonHolder {
        static final PEConnection INSTNACE = new PEConnection();
    }

    private PEConnection() {
        logger.debug("PEConnection init");
    }

    public static PEConnection getInstance() {
        return SingletonHolder.INSTNACE;
    }

    public VWSession getSession(String username, String password) throws VWException {
        VWSession vwSession = SessionHelper.logon(username, password);
        return vwSession;
    }
    
    public VWSession getAdminSession() throws VWException {
    	VWSession vwSession = SessionHelper.getPEAdminSession();
    	return vwSession;
    }

    public static void main(String[] args) {
        try {
            logger.info("session1:" + new Date());
            VWSession session2 = PEConnection.getInstance().getSession("fnadmin", "fnadmin123");
            logger.info("session2:" + new Date());
            session2.logoff();
            VWSession session3 = PEConnection.getInstance().getSession("liqing1", "tihfilenet");
            logger.info("session3:" + new Date());

            session3.logoff();

        } catch (VWException e) {
            logger.error("", e);
        }
        
    }
}
