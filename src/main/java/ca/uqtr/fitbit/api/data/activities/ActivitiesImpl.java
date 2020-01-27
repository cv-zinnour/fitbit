package ca.uqtr.fitbit.api.data.activities;

import ca.uqtr.fitbit.entity.fitbit.Activity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActivitiesImpl implements Activities {
    private OkHttpClient okHttpClient;



    @Autowired
    public ActivitiesImpl(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    @Override
    public String getData(String uri, String dateStart, String dateEnd, String accessToken) throws IOException {

        Request request = new Request.Builder()
                .url("https://api.fitbit.com/1/user/-/activities/tracker/"+uri+"/date/"+dateStart+"/"+dateEnd+".json")
                .get()
                .header("Authorization", "Bearer "+accessToken)
                .build();
        System.out.println("+++++++++++++"+okHttpClient.newCall(request).execute());
        try (Response response = okHttpClient.newCall(request).execute()) {
            //System.out.println(response.body().string());
            return response.body().string();
        }
    }

    @Override
    public List<Activity> getActivities(String dateStart, String dateEnd, String accessToken) throws IOException, ParseException {
        System.out.println("getActivities ="+ dateStart.toString()+", "+dateEnd.toString()+", "+accessToken);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<Activity> activities = new ArrayList<>();
        List<String[]> calories = json2Array(getData("calories", dateStart, dateEnd, accessToken), "calories");
        List<String[]> steps = json2Array(getData("steps", dateStart, dateEnd, accessToken), "steps");
        List<String[]> distance = json2Array(getData("distance", dateStart, dateEnd, accessToken), "distance");
        List<String[]> minutesSedentary = json2Array(getData("minutesSedentary", dateStart, dateEnd, accessToken), "minutesSedentary");
        List<String[]> minutesLightlyActive = json2Array(getData("minutesLightlyActive", dateStart, dateEnd, accessToken), "minutesLightlyActive");
        List<String[]> minutesFairlyActive = json2Array(getData("minutesFairlyActive", dateStart, dateEnd, accessToken), "minutesFairlyActive");
        List<String[]> minutesVeryActive = json2Array(getData("minutesVeryActive", dateStart, dateEnd, accessToken), "minutesVeryActive");
        List<String[]> activityCalories = json2Array(getData("activityCalories", dateStart, dateEnd, accessToken), "activityCalories");

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
