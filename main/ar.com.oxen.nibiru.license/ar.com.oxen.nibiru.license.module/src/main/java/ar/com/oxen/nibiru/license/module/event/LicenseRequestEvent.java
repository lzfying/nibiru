package ar.com.oxen.nibiru.license.module.event;

public class LicenseRequestEvent {
	private boolean showInvalidLicenseMessage;
	private Object callbackEvent;

	/**
	 * @param showInvalidLicenseMessage
	 *            True if an invalid license message must be shown
	 * @param callbackEvent
	 *            The event to be fired once the license is loaded
	 */
	public LicenseRequestEvent(boolean showInvalidLicenseMessage,
			Object callbackEvent) {
		super();
		this.showInvalidLicenseMessage = showInvalidLicenseMessage;
		this.callbackEvent = callbackEvent;
	}

	public boolean getShowInvalidLicenseMessage() {
		return showInvalidLicenseMessage;
	}

	public Object getCallbackEvent() {
		return callbackEvent;
	}
}
