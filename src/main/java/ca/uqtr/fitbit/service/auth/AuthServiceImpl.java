package ca.uqtr.fitbit.service.auth;

import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.repository.AuthRepository;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthServiceImpl  implements AuthService {

    private AuthRepository authRepository;
    private FitbitApi fitbitApi;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, FitbitApi fitbitApi) {
        this.authRepository = authRepository;
        this.fitbitApi = fitbitApi;
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

    //TODO: authRepository.findTopByOrderByIdDesc() == null
    @Override
    public String getAccessToken() throws IOException {
        Auth auth = authRepository.findTopByOrderByIdDesc();
        boolean isExpired = fitbitApi.isTokenExpired(auth.getAccessToken());
        System.out.println("is token expired : "+isExpired);
        if (isExpired){
            Auth authWithNewRefreshToken = fitbitApi.refreshToken(auth.getRefreshToken(), auth);
            System.out.println("TokenExpired");
            System.out.println("new Access token : "+authWithNewRefreshToken.getAccessToken());
            return this.authRepository.save(authWithNewRefreshToken).getAccessToken();
        }else{
            System.out.println("db Access token : "+auth.getAccessToken());
            return auth.getAccessToken();
        }

    }

    @Override
    public int deleteByAccessToken(String accessToken) {
        return authRepository.deleteByAccessToken(accessToken);
    }

    @Override
    public long count() {
        return authRepository.count();
    }

    @SneakyThrows
    @Override
    public Auth authorizationCode2AccessAndRefreshToken(String authorizationCode) {
        JSONObject jsonObj = new JSONObject(this.fitbitApi.getAccessTokenRefreshToken(authorizationCode));
        System.out.println(jsonObj.toString());
        return authRepository.save(new Auth(authorizationCode,
                jsonObj.getString("access_token"),
                jsonObj.getString("refresh_token"),
                false,
                jsonObj.getInt("expires_in"),
                jsonObj.getString("scope"),
                jsonObj.getString("token_type")));
    }


}
