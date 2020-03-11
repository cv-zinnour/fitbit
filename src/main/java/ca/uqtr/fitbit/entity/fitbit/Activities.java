package ca.uqtr.fitbit.entity.fitbit;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Activities {

    private Date dateTime;
    private double value;
    private String dataset;
    private int datasetInterval;

    public Activities() {
    }

    public Activities(Date dateTime, double value, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.value = value;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
}
