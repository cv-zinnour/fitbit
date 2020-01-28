package ca.uqtr.fitbit.api.data;

import ca.uqtr.fitbit.entity.fitbit.ActivitesT;
import ca.uqtr.fitbit.entity.fitbit.Activities;
import lombok.Getter;
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
public class ActivitiesTypeDataImpl implements ActivitiesTypeData<ActivitesT> {

    private OkHttpClient okHttpClient;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String json = Json.getContent();



    @Autowired
    public ActivitiesTypeDataImpl(OkHttpClient okHttpClient) throws IOException {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public ActivitesT getDataOfDayPerMinute(String activityType, String date, String accessToken) throws IOException {
        String json;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/15min.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/"+activityType+"/date/"+date+"/1d/1min.json")
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
        Serialization data = this.deserialization(json, activityType);

        return new Activities(data.dateTime, data.value, data.dataset, data.datasetInterval);
    }

    @Override
    public ActivitesT getDataOfDayBetweenTwoTimePerMinute(String activityType, String date, String startTime, String endTime, String accessToken) throws IOException {
        String json;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/1min/time/08%3A00/12%3A00.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/calories/date/"+date+"/1d/1min/time/"+java.net.URLEncoder.encode(startTime, StandardCharsets.UTF_8)+"/"+java.net.URLEncoder.encode(endTime, StandardCharsets.UTF_8)+".json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println(response.body().string());
            json = response.body().string();
        }
        //json = this.json;

        Serialization data = this.deserialization(json, activityType);

        return new Activities(data.dateTime, data.value, data.dataset, data.datasetInterval);
    }

    @Override
    public Serialization deserialization(String json, String activityType) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray activities_type = jsonObject.getJSONArray("activities-"+activityType);
        Date dateTime = Date.valueOf(activities_type.getJSONObject(0).getString("dateTime"));;
        int value = activities_type.getJSONObject(0).getInt("value");
        JSONObject activities_type_intraday = jsonObject.getJSONObject("activities-"+activityType+"-intraday");
        int datasetInterval = activities_type_intraday.getInt("datasetInterval");

        JSONArray dataset = activities_type_intraday.getJSONArray("dataset");
        JSONArray jsonArray = new JSONArray();
        for(int n = 0; n < dataset.length(); n++)
        {
            JSONObject object = dataset.getJSONObject(n);
            if (object.getInt("value") > 0){
                jsonArray.put(object);
            }
        };
        return new Serialization(dateTime, value, dataset.toString(), datasetInterval);
    }

    @Getter
    class Serialization{
        private Date dateTime;
        private int value;
        private String dataset;
        private int datasetInterval;

        public Serialization() {
        }

        public Serialization(Date dateTime, int value, String dataset, int datasetInterval) {
            this.dateTime = dateTime;
            this.value = value;
            this.dataset = dataset;
            this.datasetInterval = datasetInterval;
        }
    }
}
