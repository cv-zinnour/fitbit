package ca.uqtr.fitbit.entity.fitbit;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
public class Activities {

    private Timestamp dateTime;
    private double value;
    private String dataset;
    private int datasetInterval;
    private Timestamp timeStart;
    private Timestamp timeEnd;

    public Activities() {
    }

    public Activities(Timestamp dateTime, double value, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.value = value;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
}
