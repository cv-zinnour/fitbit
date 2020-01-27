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
    private String json = "{\n" +
            "  \"activities-steps\": [\n" +
            "    {\n" +
            "      \"dateTime\": \"2020-01-20\",\n" +
            "      \"value\": \"2258\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"activities-steps-intraday\": {\n" +
            "    \"dataset\": [\n" +
            "      {\n" +
            "        \"time\": \"00:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"00:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"00:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"00:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"01:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"01:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"01:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"01:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"02:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"02:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"02:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"02:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"03:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"03:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"03:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"03:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"04:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"04:15:00\",\n" +
            "        \"value\": 390\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"04:30:00\",\n" +
            "        \"value\": 309\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"04:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"05:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"05:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"05:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"05:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"06:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"06:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"06:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"06:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"07:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"07:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"07:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"07:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"08:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"08:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"08:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"08:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"09:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"09:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"09:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"09:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"10:00:00\",\n" +
            "        \"value\": 863\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"10:15:00\",\n" +
            "        \"value\": 696\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"10:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"10:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"11:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"11:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"11:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"11:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"12:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"12:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"12:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"12:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"13:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"13:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"13:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"13:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"14:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"14:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"14:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"14:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"15:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"15:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"15:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"15:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"16:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"16:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"16:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"16:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"17:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"17:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"17:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"17:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"18:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"18:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"18:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"18:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"19:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"19:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"19:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"19:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"20:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"20:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"20:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"20:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"21:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"21:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"21:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"21:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"22:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"22:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"22:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"22:45:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"23:00:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"23:15:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"23:30:00\",\n" +
            "        \"value\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"time\": \"23:45:00\",\n" +
            "        \"value\": 0\n" +
            "      }\n" +
            "    ],\n" +
            "    \"datasetInterval\": 15,\n" +
            "    \"datasetType\": \"minute\"\n" +
            "  }\n" +
            "}";



    @Autowired
    public StepsImpl(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @SneakyThrows
    @Override
    public ActivitiesSteps getStepsOfDayPerMinute(String date, String accessToken) throws IOException {
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
        json = this.json;

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
    public ActivitiesSteps getStepsOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime, String accessToken) throws IOException {
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
