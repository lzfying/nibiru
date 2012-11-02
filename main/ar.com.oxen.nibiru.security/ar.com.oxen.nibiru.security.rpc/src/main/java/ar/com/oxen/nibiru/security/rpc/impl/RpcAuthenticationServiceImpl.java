package ar.com.oxen.nibiru.security.rpc.impl;

import ar.com.oxen.nibiru.security.rpc.LoginDto;
import ar.com.oxen.nibiru.security.rpc.RpcAuthenticationService;
import ar.com.oxen.nibiru.security.rpc.UserDto;
import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.security.api.BadCredentialsException;

public class RpcAuthenticationServiceImpl implements RpcAuthenticationService {
	private AuthenticationService authenticationService;

	@Override
	public UserDto login(LoginDto loginDto) {
		try {
			this.authenticationService.login(loginDto.getUsername(),
					loginDto.getPassword());
			return new UserDto("Pepe", "Sanchez");
		} catch (BadCredentialsException e) {
			return null;
		}
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
}
