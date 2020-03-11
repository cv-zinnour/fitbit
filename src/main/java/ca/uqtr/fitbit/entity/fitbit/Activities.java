package ca.uqtr.fitbit.entity.fitbit;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Activities implements ActivitesT {

    private Date dateTime;
    private int steps;
    private String dataset;
    private int datasetInterval;

    public Activities() {
    }

    public Activities(Date dateTime, int steps, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.steps = steps;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
}
