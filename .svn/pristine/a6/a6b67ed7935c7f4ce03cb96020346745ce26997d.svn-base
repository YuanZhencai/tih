package com.wcs.tih.filenet.helper.pe;


import filenet.vw.api.VWException;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterElement;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;
import filenet.vw.api.VWWorkObjectNumber;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;

public class RosterHelper {
    private static final Logger logger = Logger.getLogger(RosterHelper.class);
    public static String DEFAULT_ROSTER_NAME = "DefaultRoster";

    public static String vwString(VWSession vwSession) throws VWException {
        return vwString(getDefaultRoster(vwSession));
    }

    public static VWRoster getDefaultRoster(VWSession vwSession) throws VWException {
        return vwSession.getRoster(DEFAULT_ROSTER_NAME);
    }

    public static String vwString(VWRoster vwRoster) throws VWException {
        int queryFlags = 0;
        VWRosterQuery query = vwRoster.createQuery(null, null, null, queryFlags, null, null, 4);
        ToStringBuilder b = ToStringUtil.createBuilder(vwRoster);
        while (query.hasNext()) {
            VWRosterElement rosterElement = (VWRosterElement) query.next();
            VWWorkObject wo = rosterElement.fetchWorkObject(false, false);
            b.append(WorkObjectHelper.vwString(wo));
        }
        return b.toString();
    }

    public static VWStepElement getStepElementByWorkflowNumber(VWSession vwSession, String workflowNumber) throws VWException {
        long currentUserSecId = vwSession.getCurrentUserSecId();
        List<VWStepElement> stepElements = getStepElements(vwSession, DEFAULT_ROSTER_NAME, workflowNumber);
        for (VWStepElement st : stepElements) {
            VWParticipant participant = st.getParticipantNamePx();
            if (participant != null) {
                if (currentUserSecId == participant.getUserId()) { return st; }
            } else {
                if (stepElements.size() == 1) { return stepElements.get(0); }
            }
        }
        throw new NullPointerException("User [" + currentUserSecId + "] does not have work item with the work object number ["
                + workflowNumber + "]");
    }

    private static List<VWStepElement> getStepElements(VWSession vwSession, String RosterName, String workflowNumber) {
        List<VWStepElement> stepElements = new ArrayList<VWStepElement>();
        try {
            VWRoster roster = vwSession.getRoster(RosterName);
            int queryFlags = 0;
            String filter = "F_WorkflowNumber = :WorkflowNumber";
            VWWorkObjectNumber vwWorkflowNumber = new VWWorkObjectNumber(workflowNumber);
            Object[] substitutionVars = { vwWorkflowNumber };
            roster.setBufferSize(50);
            VWRosterQuery rQuery = roster.createQuery(null, null, null, queryFlags, filter, substitutionVars, 4);
            logger.debug("fetch Count:" + rQuery.fetchCount());
            while (rQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) rQuery.next();
                stepElements.add(re.fetchStepElement(false, false));
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return stepElements;
    }

    public static VWStepElement getStepElementByWorkObjectNumber(VWSession vwSession, String wobNum) throws VWException {
        return getStepElement(vwSession, DEFAULT_ROSTER_NAME, wobNum);
    }

    private static VWStepElement getStepElement(VWSession vwSession, String RosterName, String wobNum) {
        try {
            VWRoster roster = vwSession.getRoster(RosterName);
            int queryFlags = 0;
            String filter = "F_WobNum = :WobNum";
            VWWorkObjectNumber vwWobNum = new VWWorkObjectNumber(wobNum);
            Object[] substitutionVars = { vwWobNum };
            roster.setBufferSize(1);
            VWRosterQuery rQuery = roster.createQuery(null, null, null, queryFlags, filter, substitutionVars, 4);
            if (rQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) rQuery.next();
                if (rQuery.hasNext()) { throw new P8BpmException("More that 1 work item has the work object number ["
                        + vwWobNum.getWorkObjectNumber() + ". It is impossible."); }
                return re.fetchStepElement(false, false);
            }
            throw new NullPointerException("No work item with the work object number [" + vwWobNum.getWorkObjectNumber()
                    + "] exists");
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
    }

    public static List<VWRosterElement> getRosterElements(VWSession vwSession, String condition) {
        List<VWRosterElement> rosterElements = new ArrayList<VWRosterElement>();
        try {
            VWRoster roster = vwSession.getRoster(DEFAULT_ROSTER_NAME);
            int queryFlags = 0;
            roster.setBufferSize(50);
            VWRosterQuery rQuery = roster.createQuery(null, null, null, queryFlags, condition, null, 4);
            if (logger.isDebugEnabled()) {
                logger.debug("condition: " + condition);
                logger.debug("Query result count: " + rQuery.fetchCount());
            }
            while (rQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) rQuery.next();
                rosterElements.add(re);
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return rosterElements;
    }

    public static List<VWStepElement> getStepElements(VWSession vwSession, String condition) {
        List<VWStepElement> steps = new ArrayList<VWStepElement>();
        try {
            VWRoster roster = vwSession.getRoster(DEFAULT_ROSTER_NAME);
            int queryFlags = 0;
            roster.setBufferSize(50);
            VWRosterQuery rQuery = roster.createQuery(null, null, null, queryFlags, condition, null, 4);
            while (rQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) rQuery.next();
                steps.add(re.fetchStepElement(false, false));
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return steps;
    }

    public static List<VWWorkObject> getWorkObjects(VWSession vwSession, String condition) {
        List<VWWorkObject> workObjects = new ArrayList<VWWorkObject>();
        try {
            VWRoster roster = vwSession.getRoster(DEFAULT_ROSTER_NAME);
            int queryFlags = 0;
            roster.setBufferSize(50);
            VWRosterQuery rQuery = roster.createQuery(null, null, null, queryFlags, condition, null, 1);
            while (rQuery.hasNext()) {
                VWWorkObject workObject = (VWWorkObject) rQuery.next();
                workObjects.add(workObject);
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return workObjects;
    }
}