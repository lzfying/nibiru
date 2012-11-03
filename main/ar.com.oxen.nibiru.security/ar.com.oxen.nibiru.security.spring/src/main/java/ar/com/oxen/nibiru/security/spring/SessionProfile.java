package ar.com.oxen.nibiru.security.spring;

import ar.com.oxen.nibiru.security.api.Profile;
import ar.com.oxen.nibiru.session.api.Session;

public class SessionProfile implements Profile {
	private final static String PROFILE_KEY = "ar.com.oxen.nibiru.security.Profile";
	private Session session;

	@Override
	public boolean isActive() {
		return this.getHolder() != null;
	}

	@Override
	public String getUsername() {
		return this.getHolder().getUsername();
	}

	@Override
	public String getFirstName() {
		return this.getHolder().getFirstName();
	}

	@Override
	public String getLastName() {
		return this.getHolder().getLastName();
	}

	public void activate(String username, String firstName, String lastName) {
		ProfileHolder holder = new ProfileHolder(username, firstName, lastName);
		this.session.put(PROFILE_KEY, holder);
	}

	public void deactivate() {
		this.session.remove(PROFILE_KEY);
	}

	private ProfileHolder getHolder() {
		return session.get(PROFILE_KEY);
	}

	private class ProfileHolder {
		private String username;
		private String firstName;
		private String lastName;

		public ProfileHolder(String username, String firstName, String lastName) {
			super();
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getUsername() {
			return username;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
