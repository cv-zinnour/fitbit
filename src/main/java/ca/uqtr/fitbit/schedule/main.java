package ca.uqtr.fitbit.schedule;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class main {


    public static void main(String[] args) throws ParseException {
        Calendar cal = Calendar.getInstance();

        long a = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-05-02 20:58:17.667000").getTime()).getTime();
        long b = TimeUnit.DAYS.toSeconds(5);
        long c = cal.getTime().getTime();
        long d = c - a + b;
        System.out.println(d);
        System.out.println(TimeUnit.DAYS.toMillis(5));
    }


}
