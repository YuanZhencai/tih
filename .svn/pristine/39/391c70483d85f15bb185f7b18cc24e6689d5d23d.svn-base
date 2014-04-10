package com.wcs.tih.filenet.helper.pe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;



import com.wcs.tih.filenet.helper.ce.util.CeConfigOptions;

import filenet.vw.api.VWException;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWSession;

public final class SessionHelper {
	private static final Logger logger = Logger.getLogger(SessionHelper.class);
	static final String INBOX_NAME = "Inbox(0)";

	static {
		logger.debug("Set JVM Params");
		PEConfigUtils.config();
	}

	private SessionHelper() {
		// TODO Auto-generated constructor stub
	}

	public static VWSession getPEAdminSession() {
		return logon(CeConfigOptions.getAdminName(), CeConfigOptions.getAdminPassword());
	}

	public static VWSession logon(String user, String password) {
		logger.debug(user + " try to logon to PE...");
		VWSession vwSession = null;

		try {
			vwSession = new VWSession(user, password, PEConfigOptions.getConnectionPointName());
			logger.debug("User \"" + user + "\" logon PE successfully!");
		} catch (VWException e) {
			logger.error("Get PE VWSession Failed!", e);
		}
		return vwSession;
	}

	public static void logoff(VWSession vWSession) {
		try {
			if (vWSession != null && vWSession.isLoggedOn()) {
				vWSession.logoff();
			}
		} catch (VWException e) {
			logger.error("logoff:", e);
		}
	}

	public static List<String> getAllQueueNames(VWSession vwSession) throws VWException {
		int nFlags = 23;

		String[] queueNames = vwSession.fetchQueueNames(nFlags);
		return Arrays.asList(queueNames);
	}

	public static VWQueue getVWQueue(VWSession session, String queueName) {
		try {
			return session.getQueue(queueName);
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
	}

	public static VWQueue getUserQueue(VWSession session) {
		return getVWQueue(session, "Inbox(0)");
	}

	public static VWParticipant getParticipant(VWSession session, long userId) {
		try {
			return session.convertIdToUserNamePx(userId);
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
	}

	public static int convertUserNameToId(VWSession session, String userName) throws BPMException {
		int userId = -1;
		try {
			userId = session.convertUserNameToId(userName);
		} catch (VWException vwe) {
			throw new BPMException(vwe);
		}
		return userId;
	}

	public static List<VWParticipant> getParticipants(VWSession session, List<String> userNames) throws BPMException {
		List<VWParticipant> participants = new ArrayList<VWParticipant>();
		int userId = -1;
		try {
			for (String userName : userNames) {
				userId = session.convertUserNameToId(userName);
				VWParticipant participant = session.convertIdToUserNamePx(userId);
				participants.add(participant);
			}
		} catch (VWException vwe) {
			throw new BPMException(vwe);
		}
		return participants;
	}
}