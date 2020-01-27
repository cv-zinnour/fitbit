package ca.uqtr.fitbit.api.data.activities;

import ca.uqtr.fitbit.entity.fitbit.Activity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Activities {


    String getData(String uri, String dateStart, String dateEnd, String accessToken) throws IOException;

    List<Activity> getActivities(String dateStart, String dateEnd, String accessToken) throws IOException, ParseException;


}
