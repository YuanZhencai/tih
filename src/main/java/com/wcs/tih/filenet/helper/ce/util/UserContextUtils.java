package com.wcs.tih.filenet.helper.ce.util;

import com.filenet.api.core.Connection;
import com.filenet.api.util.UserContext;
import javax.security.auth.Subject;

public class UserContextUtils {
    public static Subject popSubject() {
        UserContext uc = UserContext.get();
        return uc.popSubject();
    }

    public static Subject pushObject(Connection conn, String username, String password) {
        Subject subject = UserContext.createSubject(conn, username, password, CeConfigOptions.getJaasStanza());
        pushObject(conn, subject);
        return subject;
    }

    public static void pushObject(Connection conn, Subject subject) {
        UserContext uc = UserContext.get();
        uc.pushSubject(subject);
    }
}