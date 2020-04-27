package ca.uqtr.fitbit.api;

import ca.uqtr.fitbit.api.data.ActivitiesTypeData;
import ca.uqtr.fitbit.api.data.activities.Activities;
import ca.uqtr.fitbit.api.data.calories.Calories;
import ca.uqtr.fitbit.api.data.distance.Distance;
import ca.uqtr.fitbit.api.data.steps.Steps;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import javassist.bytecode.stackmap.TypeData;
import okhttp3.*;
import okio.Buffer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FitbitApiImpl implements FitbitApi {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    private static final String clientId = "22DBSJ";
    private static final String secretId = "f5c5a80085a01ef93a7711d199a2cfbc";
    private static final String TOKENS_URL = "https://api.fitbit.com/oauth2/token";
    private OkHttpClient okHttpClient;
    private Activities activities;
    private Steps steps;
    private Calories calories;
    private Distance distance;
    private ActivitiesTypeData activitiesTypeData;
    private MessageSource messageSource;

    @Autowired
    public FitbitApiImpl(OkHttpClient okHttpClient, Activities activities, Steps steps, Distance distance, ActivitiesTypeData activitiesTypeData, Calories calories, MessageSource messageSource) {
        this.okHttpClient = okHttpClient;
        this.activities = activities;
        this.steps = steps;
        this.distance = distance;
        this.activitiesTypeData = activitiesTypeData;
        this.calories = calories;
        this.messageSource = messageSource;
    }

    @Override
    public String getAccessTokenRefreshToken(String authorizationCode) throws IOException {

        String code = clientId+":"+secretId;
        String AuthorizationCodeBase64 = Base64.getEncoder().encodeToString(code.getBytes());

        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded; charset=utf-8"),
                "client_id=22DBSJ&grant_type=authorization_code&redirect_uri=https%3A%2F%2Fipodsante-92c27.firebaseapp.com%2Fdevice&code="+authorizationCode);
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
        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded"),
                "token="+accessToken);

        RequestBody requestBody = new FormBody.Builder()
                .add("token", accessToken)
                .build();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
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
    public ca.uqtr.fitbit.dto.Response updateProfile(String accessToken, String gender, String birthday, String height) {
        RequestBody requestBody  = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("gender", gender)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("height", height)
                .build();

        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/profile.json")
                .post(requestBody)
                .header("Authorization", "Bearer "+accessToken)
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            int statusCode = response.code();
            return new ca.uqtr.fitbit.dto.Response(statusCode, null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new ca.uqtr.fitbit.dto.Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.profile.update.id", null, Locale.US)),
                            messageSource.getMessage("error.profile.update.message", null, Locale.US)));

        }
    }

    @Override
    public ca.uqtr.fitbit.dto.Response updateWeight(String accessToken, String weight, String date, String time) throws UnsupportedEncodingException {
        RequestBody body = RequestBody.create(MediaType.get("application/x-www-form-urlencoded; charset=utf-8"),
                "weight="+weight+"&date="+date+"&time="+URLEncoder.encode(time, StandardCharsets.UTF_8.toString()));
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/profile.json")
                .post(body)
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            int statusCode = response.code();
            return new ca.uqtr.fitbit.dto.Response(statusCode, null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new ca.uqtr.fitbit.dto.Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.weight.update.id", null, Locale.US)),
                            messageSource.getMessage("error.weight.update.message", null, Locale.US)));

        }
    }

    @Override
    public ca.uqtr.fitbit.dto.Response addSubscription(FitbitSubscription fitbitSubscription, String accessToken, String collectionPath) throws IOException {
        FitbitSubscription fitbitSubscriptionObj = new FitbitSubscription();
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), "");
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/"+collectionPath+"/apiSubscriptions/"+fitbitSubscription.getSubscriptionId()+".json")
                .post(body)
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            System.out.println("---------------  "+response.body().string());
            ResponseBody responseBody = response.body();
            if (responseBody == null)
                return new ca.uqtr.fitbit.dto.Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            final JSONObject jsonObject = new JSONObject(responseBody.string());
            fitbitSubscriptionObj.setCollectionType(jsonObject.getString("collectionType"));
            fitbitSubscriptionObj.setOwnerId(jsonObject.getString("ownerId"));
            fitbitSubscriptionObj.setOwnerType(jsonObject.getString("ownerType"));
            fitbitSubscriptionObj.setSubscriberId(jsonObject.getString("subscriberId"));
            fitbitSubscriptionObj.setSubscriptionId(jsonObject.getString("subscriptionId"));
            fitbitSubscriptionObj.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));

            return new ca.uqtr.fitbit.dto.Response(fitbitSubscriptionObj, null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new ca.uqtr.fitbit.dto.Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.subscription.add.id", null, Locale.US)),
                            messageSource.getMessage("error.subscription.add.message", null, Locale.US)));

        }
    }

    @Override
    public ca.uqtr.fitbit.dto.Response removeSubscription(FitbitSubscription fitbitSubscription, String accessToken, String collectionPath) throws IOException {
        if (fitbitSubscription == null)
            return new ca.uqtr.fitbit.dto.Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));

        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/"+collectionPath+"/apiSubscriptions/"+fitbitSubscription.getSubscriptionId()+".json")
                .delete()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            int statusCode = response.code();
            return new ca.uqtr.fitbit.dto.Response(statusCode, null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new ca.uqtr.fitbit.dto.Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.subscription.remove.id", null, Locale.US)),
                            messageSource.getMessage("error.subscription.remove.message", null, Locale.US)));

        }
    }

    @Override
    public ca.uqtr.fitbit.dto.Response allSubscriptions(String accessToken, String collectionPath) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/"+collectionPath+"/apiSubscriptions.json")
                .delete()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return new ca.uqtr.fitbit.dto.Response(response.body().string(), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new ca.uqtr.fitbit.dto.Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.subscription.remove.id", null, Locale.US)),
                            messageSource.getMessage("error.subscription.remove.message", null, Locale.US)));

        }
    }


    @Override
    public Activities getActivities() {
        return activities;
    }

    @Override
    public Steps getSteps() {
        return steps;
    }

    @Override
    public Calories getCalories() {
        return calories;
    }

    @Override
    public Distance geDistance() {
        return distance;
    }

    @Override
    public ActivitiesTypeData getActivitiesTypeData() {
        return activitiesTypeData;
    }

    @Override
    public String getFitbitProfileId(String accessToken) {
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/profile.json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONObject user = jsonObject.getJSONObject("user");
            String encodedId = user.getString("encodedId");

            return encodedId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
