package ca.uqtr.fitbit.service.auth;

import ca.uqtr.fitbit.api.IApi;
import ca.uqtr.fitbit.entity.Auth;
import ca.uqtr.fitbit.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthServiceImpl  implements AuthService {

    private AuthRepository authRepository;
    IApi api;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, IApi api) {
        this.authRepository = authRepository;
        this.api = api;
    }



    @Override
    public Auth findByAccessToken(String accessToken) {
        return authRepository.findByAccessToken(accessToken);
    }

    @Override
    public void updateAuthorizationCode(String authorizationCode) {
        authRepository.findTopByOrderByIdDesc().setAuthorizationCode(authorizationCode);
    }

    @Override
    public String getAuthorizationCode() {
        return authRepository.findTopByOrderByIdDesc().getAuthorizationCode();
    }

    @Override
    public String getAccessToken() throws IOException {
        String token = authRepository.findTopByOrderByIdDesc().getAccessToken();

        System.out.println("======= "+token);
        if (api.isTokenExpired(token)){
            api.refreshToken(authRepository.findTopByOrderByIdDesc().getRefreshToken(), authRepository.findTopByOrderByIdDesc());
        }
        return authRepository.findTopByOrderByIdDesc().getAccessToken();
    }

    @Override
    public int deleteByAccessToken(String accessToken) {
        return authRepository.deleteByAccessToken(accessToken);
    }

    @Override
    public Auth save(Auth auth) {
        return authRepository.save(auth);
    }

    @Override
    public long count() {
        return authRepository.count();
    }


}
