package ar.com.oxen.nibiru.security.rpc;

public interface RpcAuthenticationService {
	UserDto login(LoginDto loginDto);
}