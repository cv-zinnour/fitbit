package ca.uqtr.fitbit.api;

import ca.uqtr.fitbit.entity.Activity;
import ca.uqtr.fitbit.entity.Auth;
import okhttp3.*;
import okio.Buffer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class Api implements IApi{

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .build();
    private static final String clientId = "22DBSJ";
    private static final String secretId = "f5c5a80085a01ef93a7711d199a2cfbc";
    private static final String authorization = "556ae8feceb85434abd057542b3a6bec999c72ea";


    @Override
    public String getAccessTokenRefreshToken(String url, String authorizationCode) throws IOException {

        String code = clientId+":"+secretId;
        String AuthorizationCodeBase64 = Base64.getEncoder().encodeToString(code.getBytes());
        System.out.println(AuthorizationCodeBase64);

        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded; charset=utf-8"),
                "client_id=22DBSJ&grant_type=authorization_code&redirect_uri=https%3A%2F%2Ffitbit-41668.firebaseapp.com%2Fauth&code="+authorizationCode);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Basic "+AuthorizationCodeBase64)
                .build();
        try (Response response = client.newCall(request).execute()) {
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


        try (Response response = client.newCall(request).execute()) {
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
    public void refreshToken(String refreshToken, Auth auth) throws IOException {

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
        try (Response response = client.newCall(request).execute()) {
           // System.out.println("---------------  "+response.body().string());
            final JSONObject jsonObject = new JSONObject(response.body().string());
            auth.setAccessTokenRefreshToken(jsonObject.getString("access_token"), jsonObject.getString("refresh_token"));

        }
    }

    @Override
    public String getData(String uri, Date dateStart, Date dateEnd, String accessToken) throws IOException {
        System.out.println("getData ="+ uri+", "+dateStart.toString()+", "+dateEnd.toString()+", "+accessToken);

        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/tracker/"+uri+"/date/"+dateStart+"/"+dateEnd+".json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        System.out.println("+++++++++++++"+client.newCall(request).execute());
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public List<Activity> getActivities(Date dateStart, Date dateEnd, String accessToken) throws IOException, ParseException {
        System.out.println("getActivities ="+ dateStart.toString()+", "+dateEnd.toString()+", "+accessToken);

        List<Activity> activities = new ArrayList<>();
        List<String[]> calories = json2Array(getData("calories", dateStart, dateEnd, accessToken), "calories");
        List<String[]> steps = json2Array(getData("steps", dateStart, dateEnd, accessToken), "steps");
        List<String[]> distance = json2Array(getData("distance", dateStart, dateEnd, accessToken), "distance");
        List<String[]> minutesSedentary = json2Array(getData("minutesSedentary", dateStart, dateEnd, accessToken), "minutesSedentary");
        List<String[]> minutesLightlyActive = json2Array(getData("minutesLightlyActive", dateStart, dateEnd, accessToken), "minutesLightlyActive");
        List<String[]> minutesFairlyActive = json2Array(getData("minutesFairlyActive", dateStart, dateEnd, accessToken), "minutesFairlyActive");
        List<String[]> minutesVeryActive = json2Array(getData("minutesVeryActive", dateStart, dateEnd, accessToken), "minutesVeryActive");
        List<String[]> activityCalories = json2Array(getData("activityCalories", dateStart, dateEnd, accessToken), "activityCalories");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < calories.size(); i++) {
            activities.add(new Activity(new Date(format.parse((calories.get(i)[0])).getTime()),
                    Integer.parseInt(calories.get(i)[1]) == 0 ? Integer.parseInt(calories.get(i)[1]) : Integer.parseInt(calories.get(i)[1]) - 2113,
                    Integer.parseInt(steps.get(i)[1]),
                    Double.parseDouble(distance.get(i)[1]),
                    Integer.parseInt(minutesSedentary.get(i)[1]),
                    Integer.parseInt(minutesLightlyActive.get(i)[1]),
                    Integer.parseInt(minutesFairlyActive.get(i)[1]),
                    Integer.parseInt(minutesVeryActive.get(i)[1]),
                    Integer.parseInt(activityCalories.get(i)[1])));
        }
        return activities;
    }

    @Override
    public List<String[]> json2Array(String json, String dataGenre){
        System.out.println("json2Array ="+ json+", "+dataGenre);

        List<String[]> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("activities-tracker-"+dataGenre);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject j = jsonArray.getJSONObject(i);
            list.add(new String[]{j.getString("dateTime"), j.getString("value")});
        }

        return list;
    }
}
