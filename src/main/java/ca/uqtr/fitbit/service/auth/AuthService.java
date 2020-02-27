package ca.uqtr.fitbit.service.auth;


import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.fitbit.Auth;

import java.io.IOException;
import java.util.UUID;

public interface AuthService {

    Auth findByAccessToken(String accessToken);

    void updateAuthorizationCode(String authorizationCode);

    String getAccessToken(Device device) throws IOException;

    String getAuthorizationCode(Device device);

    int deleteByAccessToken(String accessToken);

    void deleteById(UUID id);

    long count();

    Auth authorizationCode2AccessAndRefreshToken(String authorizationCode, Device device);

}
