package ca.uqtr.fitbit.schedule;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.sql.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class main {


    public static void main(String[] args) {

        Calendar cal = Calendar.getInstance();
        long d1 = java.sql.Timestamp.valueOf("2020-03-08 17:56:50.327000").getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(cal.getTime().getTime() - d1);
        int j = (int) (minutes/1440);
        System.out.println(j);
        long d2 = d1 + TimeUnit.MINUTES.toMillis(1439);
        for (int i = 0; i < j; i++) {
            System.out.println(i);
            System.out.println("d1 =   "+new Date(d1).toLocalDate().toString() +"   d2   "+ new Date(d2).toLocalDate());
            System.out.println("d1 =   "+ new Time(d1).toString().substring(0,5)+"   d2   "+ new Time(d2).toString().substring(0,5));
            minutes -= 1440;
            if (minutes >= 1440){
                d1 = d2 + TimeUnit.MINUTES.toMillis(1);
                d2 = d1 + TimeUnit.MINUTES.toMillis(1439);
            }
        }
        if (minutes > 0){
            System.out.println(minutes);
            d1 = d2 + TimeUnit.MINUTES.toMillis(1);
            d2 = d1 + TimeUnit.MINUTES.toMillis(minutes);
            System.out.println("d1 =   "+new Timestamp(d1)+"   d2   "+new Timestamp(d2));
        }
        System.out.println(new Timestamp(d2 + TimeUnit.MINUTES.toMillis(1)));

    }
}
