package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWLog;
import filenet.vw.api.VWLogElement;
import filenet.vw.api.VWLogQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWWorkObjectNumber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;

public class EventLogHelper {
    private static final Logger logger = Logger.getLogger(EventLogHelper.class);
    private static final String DEFAULT_EVENT_LOG = "DefaultEventLog";

    public static String vwString(VWSession vwSession) throws VWException, BPMException {
        return vwString(vwSession.fetchEventLog(DEFAULT_EVENT_LOG));
    }

    public static String vwString(VWLog vwLog) throws VWException, BPMException {
        VWLogQuery logQuery = vwLog.startQuery(null, null, null, 0, null, null);

        ToStringBuilder b = ToStringUtil.createBuilder(vwLog);
        b.append("fetch count", logQuery.fetchCount());

        while (logQuery.hasNext()) {
            b.append(LogElementHelper.vwString(logQuery.next()));
        }
        return b.toString();
    }

    public static List<VWLogElement> queryEventLogs(VWSession vwSession, String filter) throws VWException {
        List<VWLogElement> results = new ArrayList<VWLogElement>();

        VWLog eventLog = vwSession.fetchEventLog(DEFAULT_EVENT_LOG);
        VWLogQuery rQuery = eventLog.startQuery(null, null, null, 0, filter, null);
        if (logger.isDebugEnabled()) {
            logger.debug("filter: " + filter);
            logger.debug("Query result count: " + rQuery.fetchCount());
        }
        VWLogElement logElement = null;

        while (rQuery.hasNext()) {
            logElement = rQuery.next();
            results.add(logElement);
        }
        return results;
    }

    public static List<VWLogElement> queryEventLogsByWobNum(VWSession vwSession, String wobNum) throws VWException {
        List<VWLogElement> results = new ArrayList<VWLogElement>();

        VWLog eventLog = vwSession.fetchEventLog(DEFAULT_EVENT_LOG);
        String filter = "F_WobNum = :WobNum AND F_EventType = :EventTypeA";
        VWWorkObjectNumber vwWobNum = new VWWorkObjectNumber(wobNum);
        Object[] substitutionVars = { vwWobNum, "360" };
        VWLogQuery rQuery = eventLog.startQuery(null, null, null, 0, filter, substitutionVars);
        if (logger.isDebugEnabled()) {
            logger.debug("filter: " + filter);
            logger.debug("Query result count: " + rQuery.fetchCount());
        }
        VWLogElement logElement = null;

        while (rQuery.hasNext()) {
            logElement = rQuery.next();
            results.add(logElement);
        }
        return results;
    }

    public static List<String> queryWorkflows(VWSession vwSession, String filter) throws VWException {
        List<String> results = new ArrayList<String>();
        Set<String> resultsNotSame = new HashSet<String>();

        VWLog eventLog = vwSession.fetchEventLog(DEFAULT_EVENT_LOG);
        VWLogQuery rQuery = eventLog.startQuery(null, null, null, 0, filter, null);
        if (logger.isDebugEnabled()) {
            logger.debug("filter: " + filter);
            logger.debug("Query result count: " + rQuery.fetchCount());
        }
        VWLogElement logElement = null;

        while (rQuery.hasNext()) {
            logElement = rQuery.next();
            resultsNotSame.add((String) logElement.getFieldValue("F_WorkFlowNumber"));
        }
        results.addAll(resultsNotSame);
        return results;
    }
}