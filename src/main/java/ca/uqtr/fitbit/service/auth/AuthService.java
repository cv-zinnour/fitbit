package ca.uqtr.fitbit.service.auth;


import ca.uqtr.fitbit.entity.Auth;

import java.io.IOException;

public interface AuthService {

    Auth findByAccessToken(String accessToken);

    void updateAuthorizationCode(String authorizationCode);

    String getAccessToken() throws IOException;

    String getAuthorizationCode();

    int deleteByAccessToken(String accessToken);

    Auth save(Auth auth);

    long count();

}
