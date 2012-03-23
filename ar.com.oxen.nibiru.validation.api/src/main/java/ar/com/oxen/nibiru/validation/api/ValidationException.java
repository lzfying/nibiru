package ar.com.oxen.nibiru.validation.api;

/**
 * Exception that holds the error codes. Such error codes can be prefixed (for
 * example, at UI level) in order to get an internationalized description.
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 4252348877291968679L;
	private String[] errorCodes;

	public ValidationException(String... errorCodes) {
		super();
		this.errorCodes = errorCodes;
	}

	/**
	 * @return The error codes.
	 */
	public String[] getErrorCodes() {
		return errorCodes;
	}
}
