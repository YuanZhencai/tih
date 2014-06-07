package com.wcs.tih.filenet.pe.service;

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

import javax.ejb.Stateless;

import com.wcs.tih.filenet.pe.exception.P8BpmException;

/**
 * <p>Project: tih</p>
 * <p>Description: RosterService</p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class RosterService {
    public static String DEFAULT_ROSTER_NAME = "DefaultRoster";

    public VWRoster getDefaultRoster(VWSession vwSession) throws VWException {
        return vwSession.getRoster(DEFAULT_ROSTER_NAME);
    }

    public VWStepElement getStepElementByWorkObjectNumber(VWSession vwSession, String wobNum) throws VWException {
        return getStepElement(vwSession, DEFAULT_ROSTER_NAME, wobNum);
    }
    
    public VWStepElement getStepElementByWorkflowNumber(VWSession vwSession, String workflowNumber) throws VWException {
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

    private VWStepElement getStepElement(VWSession vwSession, String RosterName, String wobNum) {
        try {
            VWRoster roster = vwSession.getRoster(RosterName);
            int queryFlags = 0;
            String filter = "F_WobNum = :WobNum";
            VWWorkObjectNumber vwWobNum = new VWWorkObjectNumber(wobNum);
            Object[] substitutionVars = { vwWobNum };
            roster.setBufferSize(1);
            VWRosterQuery qQuery = roster.createQuery(null, null, null, queryFlags, filter, substitutionVars, 4);
            if (qQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) qQuery.next();
                if (qQuery.hasNext()) { throw new P8BpmException("More that 1 work item has the work object number ["
                        + vwWobNum.getWorkObjectNumber() + ". It is impossible."); }
                return re.fetchStepElement(false, false);
            }
            throw new NullPointerException("No work item with the work object number [" + vwWobNum.getWorkObjectNumber()
                    + "] exists");
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
    }
    
    private List<VWStepElement> getStepElements(VWSession vwSession, String RosterName, String workflowNumber) {
        List<VWStepElement> stepElements = new ArrayList<VWStepElement>();
        try {
            VWRoster roster = vwSession.getRoster(RosterName);
            int queryFlags = 0;
            String filter = "F_WorkflowNumber = :WorkflowNumber";
            VWWorkObjectNumber vwWorkflowNumber = new VWWorkObjectNumber(workflowNumber);
            Object[] substitutionVars = { vwWorkflowNumber };
            roster.setBufferSize(50);
            VWRosterQuery qQuery = roster.createQuery(null, null, null, queryFlags, filter, substitutionVars, 4);
            System.out.println("fetch Count:" + qQuery.fetchCount());
            while (qQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) qQuery.next();
                stepElements.add(re.fetchStepElement(false, false));
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return stepElements;
    }

    public List<VWRosterElement> getRosterElements(VWSession vwSession, String condition) {
        List<VWRosterElement> rosterElements = new ArrayList<VWRosterElement>();
        try {
            VWRoster roster = vwSession.getRoster(DEFAULT_ROSTER_NAME);
            int queryFlags = 0;
            roster.setBufferSize(50);
            VWRosterQuery qQuery = roster.createQuery(null, null, null, queryFlags, condition, null, 4);
            while (qQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) qQuery.next();
                rosterElements.add(re);
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return rosterElements;
    }

    public List<VWStepElement> getStepElements(VWSession vwSession, String condition) {
        List<VWStepElement> steps = new ArrayList<VWStepElement>();
        try {
            VWRoster roster = vwSession.getRoster(DEFAULT_ROSTER_NAME);
            int queryFlags = 0;
            roster.setBufferSize(50);
            VWRosterQuery qQuery = roster.createQuery(null, null, null, queryFlags, condition, null, 4);
            while (qQuery.hasNext()) {
                VWRosterElement re = (VWRosterElement) qQuery.next();
                steps.add(re.fetchStepElement(false, false));
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return steps;
    }

    public List<VWWorkObject> getWorkObjects(VWSession vwSession, String condition) {
        List<VWWorkObject> workObjects = new ArrayList<VWWorkObject>();
        try {
            VWRoster roster = vwSession.getRoster(DEFAULT_ROSTER_NAME);
            int queryFlags = 0;
            roster.setBufferSize(50);
            VWRosterQuery qQuery = roster.createQuery(null, null, null, queryFlags, condition, null, 1);
            while (qQuery.hasNext()) {
                VWWorkObject workObject = (VWWorkObject) qQuery.next();
                workObjects.add(workObject);
            }
        } catch (VWException e) {
            throw new P8BpmException(e);
        }
        return workObjects;
    }
}