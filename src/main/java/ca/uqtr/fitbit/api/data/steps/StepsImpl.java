package ca.uqtr.fitbit.api.data.steps;

import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import lombok.SneakyThrows;
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
import java.text.SimpleDateFormat;

@Component
public class StepsImpl implements Steps {
    private OkHttpClient okHttpClient;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    //private String json = Json.getContent();



    @Autowired
    public StepsImpl(OkHttpClient okHttpClient) throws IOException {
        this.okHttpClient = okHttpClient;
    }

    @SneakyThrows
    @Override
    public ActivitiesSteps getStepsOfDayPerMinuteFromApi(String date, String accessToken)  {
        String json;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/15min.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/steps/date/"+date+"/1d/1min.json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        System.out.println("+++++++++++++"+okHttpClient.newCall(request).execute());
        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println(response.body().string());
            json = response.body().string();
        }
        //json = this.json;

        //System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray activities_steps = jsonObject.getJSONArray("activities-steps");
        Date dateTime = Date.valueOf(activities_steps.getJSONObject(0).getString("dateTime"));;
        int stepsValue = activities_steps.getJSONObject(0).getInt("value");
        JSONObject activities_steps_intraday = jsonObject.getJSONObject("activities-steps-intraday");
        int datasetInterval = activities_steps_intraday.getInt("datasetInterval");

        JSONArray dataset = activities_steps_intraday.getJSONArray("dataset");
        JSONArray jsonArray = new JSONArray();
        for(int n = 0; n < dataset.length(); n++)
        {
            JSONObject object = dataset.getJSONObject(n);
            if (object.getInt("value") > 0){
                jsonArray.put(object);
            }
        }

        return new ActivitiesSteps(dateTime, stepsValue, jsonArray.toString(), datasetInterval);

    }

    @SneakyThrows
    @Override
    public ActivitiesSteps getStepsOfDayBetweenTwoTimePerMinuteFromApi(String date, String startTime, String endTime, String accessToken)  {
        String json;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/1min/time/08%3A00/12%3A00.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/steps/date/"+date+"/1d/1min/time/"+java.net.URLEncoder.encode(startTime, StandardCharsets.UTF_8)+"/"+java.net.URLEncoder.encode(endTime, StandardCharsets.UTF_8)+".json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        System.out.println("+++++++++++++"+okHttpClient.newCall(request).execute());
        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println(response.body().string());
            json = response.body().string();
        }
        //json = this.json;

        //System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray activities_steps = jsonObject.getJSONArray("activities-steps");
        Date dateTime = Date.valueOf(activities_steps.getJSONObject(0).getString("dateTime"));;
        int stepsValue = activities_steps.getJSONObject(0).getInt("value");
        JSONObject activities_steps_intraday = jsonObject.getJSONObject("activities-steps-intraday");
        int datasetInterval = activities_steps_intraday.getInt("datasetInterval");

        JSONArray dataset = activities_steps_intraday.getJSONArray("dataset");
        JSONArray jsonArray = new JSONArray();
        for(int n = 0; n < dataset.length(); n++)
        {
            JSONObject object = dataset.getJSONObject(n);
            if (object.getInt("value") > 0){
                jsonArray.put(object);
            }
        }

        return new ActivitiesSteps(dateTime, stepsValue, jsonArray.toString(), datasetInterval);

    }
}
