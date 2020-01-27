package ca.uqtr.fitbit.api;

import ca.uqtr.fitbit.api.data.activities.Activities;
import ca.uqtr.fitbit.api.data.steps.Steps;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import okhttp3.*;
import okio.Buffer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class FitbitApiImpl implements FitbitApi {



    private static final String clientId = "22DBSJ";
    private static final String secretId = "f5c5a80085a01ef93a7711d199a2cfbc";
    private static final String TOKENS_URL = "https://api.fitbit.com/oauth2/token";
    private OkHttpClient okHttpClient;
    private Activities activities;
    private Steps steps;

    @Autowired
    public FitbitApiImpl(OkHttpClient okHttpClient, Activities activities, Steps steps) {
        this.okHttpClient = okHttpClient;
        this.activities = activities;
        this.steps = steps;
    }

    @Override
    public String getAccessTokenRefreshToken(String authorizationCode) throws IOException {

        String code = clientId+":"+secretId;
        String AuthorizationCodeBase64 = Base64.getEncoder().encodeToString(code.getBytes());

        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded; charset=utf-8"),
                "client_id=22DBSJ&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8765%2Fauthorization&code="+authorizationCode);
        Request request = new Request.Builder()
                .url(TOKENS_URL)
                .post(body)
                .header("Authorization", "Basic "+AuthorizationCodeBase64)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public boolean isTokenExpired(String accessToken) throws IOException {
        System.out.println(accessToken);
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        //RequestBody body = RequestBody.create(mediaType, "eeee");
        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded"),
                "token="+accessToken);

        RequestBody requestBody = new FormBody.Builder()
                .add("token", accessToken)
                .build();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        System.out.println("---------- bufer : "+buffer.readUtf8());

        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1.1/oauth2/introspect")
                .post(requestBody)
                .header("authorization", "Bearer "+accessToken)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println("refresh = ======= "+response.body().string());
            final JSONObject jsonObject = new JSONObject(response.body().string());
            if (jsonObject.has("active")){
                if (jsonObject.getBoolean("active")){
                    return false;
                }
                return true;
            }else{
                return true;
            }

        }
    }

    @Override
    public Auth refreshToken(String refreshToken, Auth auth) throws IOException {

        String code = clientId+":"+secretId;
        String AuthorizationCodeBase64 = Base64.getEncoder().encodeToString(code.getBytes());
        System.out.println(AuthorizationCodeBase64);

        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded; charset=utf-8"),
                "grant_type=refresh_token&refresh_token="+refreshToken);
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/oauth2/token")
                .post(body)
                .header("Authorization", "Basic "+AuthorizationCodeBase64)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
           //System.out.println("---------------  "+response.body().string());
            final JSONObject jsonObject = new JSONObject(response.body().string());
             auth.setAccessTokenRefreshToken(jsonObject.getString("access_token"), jsonObject.getString("refresh_token"));

        }
        return auth;
    }

    @Override
    public Activities getActivities() {
        return activities;
    }

    @Override
    public Steps getSteps() {
        return steps;
    }
}
