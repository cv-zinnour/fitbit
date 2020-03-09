package ca.uqtr.fitbit.entity.fitbit;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class Activities implements ActivitesT {

    private Timestamp dateTime;
    private int steps;
    private String dataset;
    private int datasetInterval;

    public Activities() {
    }

    public Activities(Timestamp dateTime, int steps, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.steps = steps;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
}
