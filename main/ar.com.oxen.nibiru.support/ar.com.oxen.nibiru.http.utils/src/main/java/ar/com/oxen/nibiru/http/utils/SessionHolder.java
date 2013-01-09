package ar.com.oxen.nibiru.http.utils;

import javax.servlet.http.HttpSession;

/**
 * Utility class for accessing the current HTTP session.
 */
public class SessionHolder {
	private static ThreadLocal<HttpSession> threadLocal = new ThreadLocal<HttpSession>();

	public static HttpSession getSession() {
		return threadLocal.get();
	}

	static void setSession(HttpSession session) {
		threadLocal.set(session);
	}
}
