package ca.uqtr.fitbit.service.auth;

import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.repository.AuthRepository;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class AuthServiceImpl  implements AuthService {

    private AuthRepository authRepository;
    private FitbitApi fitbitApi;
    private ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, FitbitApi fitbitApi, ModelMapper modelMapper) {
        this.authRepository = authRepository;
        this.fitbitApi = fitbitApi;
        this.modelMapper = modelMapper;
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
    public String getAuthorizationCode(Device device) {
        return device.getAuth().getAuthorizationCode();
    }

    //TODO: authRepository.findTopByOrderByIdDesc() == null
    @Override
    public String getAccessToken(Device device) throws IOException {
        Auth auth = authRepository.getById(device.getId());
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
    public void deleteById(UUID id) {
        authRepository.deleteById(id);
    }


    @Override
    public long count() {
        return authRepository.count();
    }

    @SneakyThrows
    @Override
    public Auth authorizationCode2AccessAndRefreshToken(String authorizationCode, Device device) {
        JSONObject jsonObj = new JSONObject(this.fitbitApi.getAccessTokenRefreshToken(authorizationCode));
        System.out.println(jsonObj.toString());
        Auth auth = new Auth(authorizationCode,
                jsonObj.getString("access_token"),
                jsonObj.getString("refresh_token"),
                false,
                jsonObj.getInt("expires_in"),
                jsonObj.getString("scope"),
                jsonObj.getString("token_type"));
        auth.setDevice(device);
        return authRepository.save(auth);
    }

    @Override
    public String getFitbitProfileId(Device device) throws IOException {
        String profileId = fitbitApi.getFitbitProfileId(getAccessToken(device));

        return null;
    }

}
