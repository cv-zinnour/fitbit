package ca.uqtr.fitbit.schedule;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.event.reminder.OnSynchronizationEmailEvent;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class main {


    public static void main(String[] args) throws ParseException {
        Calendar cal = Calendar.getInstance();

        long a = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-01-05 20:58:17.667000").getTime()).getTime();
        long b = TimeUnit.DAYS.toMillis(5);
        long c = cal.getTime().getTime();
        long time = c - (a + b);


        System.out.println(a);
        System.out.println(b);
        System.out.println(TimeUnit.DAYS.toMillis(7));
        System.out.println(c);
        System.out.println(time);

        if (time >= 0 && time < TimeUnit.DAYS.toMillis(2 ) ){
            System.out.println(1111111111);
        }else System.out.println(00000000000);
    }


}
