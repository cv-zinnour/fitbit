package ca.uqtr.fitbit.api.data.calories;

import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class CaloriesImpl implements Calories {

    private OkHttpClient okHttpClient;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");



    @Autowired
    public CaloriesImpl(OkHttpClient okHttpClient) throws IOException {
        this.okHttpClient = okHttpClient;
    }


    @Override
    public ActivitiesCalories getCaloriesOfDayPerMinute(String date, String accessToken) throws IOException, ParseException {
        System.out.println(accessToken);
        String json;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/15min.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/calories/date/"+date+"/1d/1min.json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println(response.body().string());
            json = response.body().string();
        }
        //json = this.json;

        //System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray activities_calories = jsonObject.getJSONArray("activities-calories");
        String dateTime = activities_calories.getJSONObject(0).getString("dateTime");
        double caloriesValue = activities_calories.getJSONObject(0).getDouble("value");
        JSONObject activities_calories_intraday = jsonObject.getJSONObject("activities-calories-intraday");
        int datasetInterval = activities_calories_intraday.getInt("datasetInterval");

        JSONArray dataset = activities_calories_intraday.getJSONArray("dataset");
        JSONArray jsonArray = new JSONArray();
        for(int n = 0; n < dataset.length(); n++)
        {
            JSONObject object = dataset.getJSONObject(n);
            if (object.getInt("value") > 0){
                jsonArray.put(object);
            }
        }

        return new ActivitiesCalories(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateTime).getTime()), caloriesValue, jsonArray.toString(), datasetInterval);
    }

    @Override
    public ActivitiesCalories getCaloriesOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime, String accessToken) throws IOException, ParseException {
        String json;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/1min/time/08%3A00/12%3A00.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/calories/date/"+date+"/1d/1min/time/"+startTime+"/"+endTime+".json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println(response.body().string());
            json = response.body().string();
        }
        //json = this.json;

        //System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray activities_calories = jsonObject.getJSONArray("activities-calories");
        String dateTime = activities_calories.getJSONObject(0).getString("dateTime");
        double caloriesValue = activities_calories.getJSONObject(0).getDouble("value");
        JSONObject activities_calories_intraday = jsonObject.getJSONObject("activities-calories-intraday");
        int datasetInterval = activities_calories_intraday.getInt("datasetInterval");

        JSONArray dataset = activities_calories_intraday.getJSONArray("dataset");
        JSONArray jsonArray = new JSONArray();
        for(int n = 0; n < dataset.length(); n++)
        {
            JSONObject object = dataset.getJSONObject(n);
            if (object.getInt("value") > 0){
                jsonArray.put(object);
            }
        }

        return new ActivitiesCalories(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateTime).getTime()), caloriesValue, jsonArray.toString(), datasetInterval);

    }
}
