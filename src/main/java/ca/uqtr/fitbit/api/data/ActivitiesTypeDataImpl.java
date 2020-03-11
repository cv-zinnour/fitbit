package ca.uqtr.fitbit.api.data;

import ca.uqtr.fitbit.entity.fitbit.Activities;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class ActivitiesTypeDataImpl implements ActivitiesTypeData<Activities> {

    private OkHttpClient okHttpClient;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    public ActivitiesTypeDataImpl(OkHttpClient okHttpClient) throws IOException {
        this.okHttpClient = okHttpClient;
    }

    @Override
    @Retryable(
            value = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 60000))
    public Activities getDataOfDayPerMinute(String activityType, String date, String accessToken) throws ParseException {
        Response response = null;
        Serialization data = null;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/15min.json
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/"+activityType+"/date/"+date+"/1d/1min.json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try {
            response = okHttpClient.newCall(request).execute();
            //System.out.println(response.body().string());
            data = this.deserialization(response.body().string(), activityType);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return new Activities(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(data.getDateTime()).getTime()), data.getValue(), data.getDataset(), data.getDatasetInterval());
    }

    @Override
    @Retryable(
            value = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 60000))
    public Activities getDataOfDayBetweenTwoTimePerMinute(String activityType, String date, String endDate, String startTime, String endTime, String accessToken) throws UnsupportedEncodingException, ParseException {
        Response response = null;
        Serialization data = null;
        //https://api.fitbit.com/1/user/-/activities/steps/date/2020-01-20/1d/1min/time/08%3A00/12%3A00.json
        String url =URLEncoder.encode(startTime, StandardCharsets.UTF_8.toString());
        System.out.println(url);
        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/"+activityType+"/date/"+date+"/"+endDate+"/1min/time/"+URLEncoder.encode(startTime, StandardCharsets.UTF_8.toString())+"/"+URLEncoder.encode(endTime, StandardCharsets.UTF_8.toString())+".json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        try {
            response = okHttpClient.newCall(request).execute();
            //System.out.println(response.body().string());
            data = this.deserialization(response.body().string(), activityType);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        //System.out.println(data.toString());
        return new Activities(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(data.getDateTime()).getTime()), data.getValue(), data.getDataset(), data.getDatasetInterval());
    }

    @Override
    public Serialization deserialization(String json, String activityType) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray activities_type = jsonObject.getJSONArray("activities-"+activityType);
        String dateTime = activities_type.getJSONObject(0).getString("dateTime");
        double value;
        if (activityType.equals("steps"))
            value = activities_type.getJSONObject(0).getInt("value");
        else
            value = activities_type.getJSONObject(0).getDouble("value");
        JSONObject activities_type_intraday = jsonObject.getJSONObject("activities-"+activityType+"-intraday");
        JSONArray dataset = activities_type_intraday.getJSONArray("dataset");
        JSONArray jsonArray = new JSONArray();
        for(int n = 0; n < dataset.length(); n++)
        {
            JSONObject object = dataset.getJSONObject(n);
            if (object.getInt("value") > 0){
                jsonArray.put(object);
            }
        };
        int datasetInterval = activities_type_intraday.getInt("datasetInterval");
        return new Serialization(dateTime, value, dataset.toString(), datasetInterval);
    }

}
