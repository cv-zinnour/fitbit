package ca.uqtr.fitbit.api;


import ca.uqtr.fitbit.entity.Activity;
import ca.uqtr.fitbit.entity.Auth;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

public interface IApi {

    String getAccessTokenRefreshToken(String url, String authorizationCode) throws IOException;

    boolean isTokenExpired(String accessToken) throws IOException;

    void refreshToken(String refreshToken, Auth auth) throws IOException ;

    String getData(String uri, Date dateStart, Date dateEnd, String accessToken) throws IOException;

    List<Activity> getActivities(Date dateStart, Date dateEnd, String accessToken) throws IOException, ParseException;

    List<String[]> json2Array(String json, String dataGenre);

}
