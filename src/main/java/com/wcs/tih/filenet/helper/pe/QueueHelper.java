package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWQueueQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;
import filenet.vw.api.VWWorkObjectNumber;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

public class QueueHelper {
    private static final Logger logger = Logger.getLogger(QueueHelper.class);

    public static String[] getQueueList(VWSession vwSession, int fetchFlag) throws VWException {
        return vwSession.fetchQueueNames(fetchFlag);
    }

    public static VWStepElement lockStepElementByWorkObjectNumber(VWSession vwSession, String queueName, String wobNum)
            throws VWException {
        VWQueue vwQueue = SessionHelper.getVWQueue(vwSession, queueName);
        return lockStepElementByWorkObjectNumber(vwQueue, new VWWorkObjectNumber(wobNum));
    }

    public static VWWorkObject lockWorkObjectByWorkObjectNumber(VWSession vwSession, String queueName, String wobNum)
            throws BPMException {
        try {
            VWQueue vwQueue = SessionHelper.getVWQueue(vwSession, queueName);
            return lockStepElementByWorkObjectNumber(vwQueue, new VWWorkObjectNumber(wobNum)).fetchWorkObject(false, false);
        } catch (VWException e) {
            throw new BPMException(e);
        }
    }

    public static VWWorkObject getWorkObjectByWorkflowNumber(VWSession vwSession, String queueName, String workflowNumber)
            throws VWException {
        VWQueue vwQueue = SessionHelper.getVWQueue(vwSession, queueName);
        return getStepElementByWorkflowNumber(vwQueue, workflowNumber, 1).fetchWorkObject(false, false);
    }

    public static VWWorkObject getWorkObjectByWorkObjectNumber(VWSession vwSession, String queueName, String wobNum)
            throws VWException {
        VWQueue vwQueue = SessionHelper.getVWQueue(vwSession, queueName);
        return getStepElementByWorkObjectNumber(vwQueue, new VWWorkObjectNumber(wobNum), 1).fetchWorkObject(false, false);
    }

    public static List<VWQueueElement> getQueueElementsByfilter(VWSession session, String queueName, String condition)
            throws VWException {
        return getQueueElementsByfilter(session, queueName, condition, null, 1);
    }

    public static List<VWQueueElement> getQueueElementsByfilter(VWSession session, String queueName, String filter,
            Object[] substitutionVars) throws VWException {
        return getQueueElementsByfilter(session, queueName, filter, substitutionVars, 1);
    }

    private static List<VWQueueElement> getQueueElementsByfilter(VWSession session, String queueName, String filter,
            Object[] substitutionVars, int queryFlags) throws VWException {
        VWQueue vwQueue = session.getQueue(queueName);
        List<VWQueueElement> queueElements = new ArrayList<VWQueueElement>();
        vwQueue.setBufferSize(25);
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, queryFlags, filter, substitutionVars, 3);
        logger.debug("Fetch Count:" + qQuery.fetchCount());
        while (qQuery.hasNext()) {
            queueElements.add((VWQueueElement) qQuery.next());
        }
        return queueElements;
    }

    public static void lockStepElement(VWSession vwSession, VWStepElement stepElement) throws VWException {
        VWParticipant lockedUserPx = stepElement.getLockedUserPx();
        String lockedUser;
        if ((lockedUserPx != null) && (lockedUserPx.getUserId() != vwSession.getCurrentUserSecId())) {
            lockedUser = lockedUserPx.getDisplayName();
            logger.debug("user:" + lockedUser + "already lock");
        } else {
            stepElement.doLock(true);
        }
    }

    public static List<VWStepElement> getStepElements(VWSession session, String queueName) throws VWException {
        return getStepElementsByfilter(session, queueName, null, null);
    }

    public static List<VWStepElement> getStepElementsByStepName(VWSession session, String queueName, String stepName)
            throws VWException {
        String filter = "F_StepName = :stepName";
        Object[] substitutionVars = { stepName };
        return getStepElementsByfilter(session, queueName, filter, substitutionVars);
    }

    public static List<VWStepElement> getStepElementsByfilter(VWSession session, String queueName, String filter,
            Object[] substitutionVars) throws VWException {
        return getStepElementsByfilter(session, queueName, filter, substitutionVars, 1);
    }

    public static List<VWStepElement> getStepElementsByfilter(VWSession session, String queueName, String filter,
            Object[] substitutionVars, int queryFlags) throws VWException {
        VWQueue vwQueue = session.getQueue(queueName);
        List<VWStepElement> stepElements = new ArrayList<VWStepElement>();
        vwQueue.setBufferSize(25);
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, queryFlags, filter, substitutionVars, 5);
        logger.debug("Fetch Count:" + qQuery.fetchCount());
        while (qQuery.hasNext()) {
            stepElements.add((VWStepElement) qQuery.next());
        }
        return stepElements;
    }

    public static VWStepElement lockStepElementByWorkObjectNumber(VWQueue vwQueue, VWWorkObjectNumber vwWobNum)
            throws VWException {
        return getStepElementByWorkObjectNumber(vwQueue, vwWobNum, 16);
    }

    public static VWStepElement getStepElementByWorkflowNumber(VWQueue vwQueue, String workflowNumber, int queryFlags)
            throws VWException {
        String filter = "F_WorkFlowNumber = '" + workflowNumber + "'";
        vwQueue.setBufferSize(1);
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, queryFlags, filter, null, 5);
        if (qQuery.hasNext()) {
            VWStepElement st = (VWStepElement) qQuery.next();
            if (qQuery.hasNext()) { throw new IllegalStateException("More that 1 work step element has the workflowNumber ["
                    + workflowNumber + ". It is impossible."); }
            return st;
        }
        throw new NullPointerException("No work step element with the workflowNumber [" + workflowNumber + "] exists");
    }

    public static VWStepElement getStepElementByWorkObjectNumber(VWQueue vwQueue, VWWorkObjectNumber vwWobNum, int queryFlags)
            throws VWException {
        String filter = "F_WobNum = :WobNum";
        Object[] substitutionVars = { vwWobNum };
        vwQueue.setBufferSize(1);
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, queryFlags, filter, substitutionVars, 5);
        if (qQuery.hasNext()) {
            VWStepElement st = (VWStepElement) qQuery.next();
            if (qQuery.hasNext()) { throw new IllegalStateException(
                    "More that 1 work step element has the work object number [" + vwWobNum.getWorkObjectNumber()
                            + ". It is impossible."); }
            return st;
        }
        throw new NullPointerException("No work step element with the work object number [" + vwWobNum.getWorkObjectNumber()
                + "] exists");
    }

    public static String vwString(VWQueue vwQueue) throws VWException, BPMException {
        ToStringBuilder b = initToStringBuilder(vwQueue);
        appendQueueElements(vwQueue, b);
        appendStepElements(vwQueue, b);
        appendWorkObjects(vwQueue, b);
        return b.toString();
    }

    public static String vwQueueElementString(VWQueue vwQueue) throws VWException, BPMException {
        ToStringBuilder b = initToStringBuilder(vwQueue);
        appendQueueElements(vwQueue, b);
        return b.toString();
    }

    public static String vwStepElementString(VWQueue vwQueue) throws VWException, BPMException {
        ToStringBuilder b = initToStringBuilder(vwQueue);
        appendStepElements(vwQueue, b);
        return b.toString();
    }

    public static String vwWorkObjectString(VWQueue vwQueue) throws VWException {
        ToStringBuilder b = initToStringBuilder(vwQueue);
        appendWorkObjects(vwQueue, b);
        return b.toString();
    }

    private static ToStringBuilder initToStringBuilder(VWQueue vwQueue) throws VWException {
        ToStringBuilder b = new ToStringBuilder(vwQueue, ToStringStyle.MULTI_LINE_STYLE);
        b.append("authored name:  " + vwQueue.getAuthoredName());
        b.append("depth:  " + vwQueue.fetchCount());
        return b;
    }

    private static void appendQueueElements(VWQueue vwQueue, ToStringBuilder b) throws VWException, BPMException {
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, 0, null, null, 3);
        b.append("=== Queue Elements ===");
        while (qQuery.hasNext()) {
            VWQueueElement vwQueueElement = (VWQueueElement) qQuery.next();
            b.append(QueueElementHelper.vwString(vwQueueElement));
        }
    }

    private static void appendStepElements(VWQueue vwQueue, ToStringBuilder b) throws VWException, BPMException {
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, 0, null, null, 5);
        b.append("=== Step Elements ===");
        while (qQuery.hasNext()) {
            VWStepElement vwStepElement = (VWStepElement) qQuery.next();
            b.append(StepElementHelper.vwString(vwStepElement));
        }
    }

    private static void appendWorkObjects(VWQueue vwQueue, ToStringBuilder b) throws VWException {
        VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, 0, null, null, 1);
        b.append("=== Work Objects ===");
        while (qQuery.hasNext()) {
            VWWorkObject vwWorkObject = (VWWorkObject) qQuery.next();
            b.append(WorkObjectHelper.vwString(vwWorkObject));
        }
    }
}