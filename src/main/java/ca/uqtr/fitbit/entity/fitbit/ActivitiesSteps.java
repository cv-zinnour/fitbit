package ca.uqtr.fitbit.entity.fitbit;

import lombok.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "activities_steps", schema = "public")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class ActivitiesSteps {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "date")
    private Date dateTime;
    @Column(name = "steps")
    private int steps;
    @Type(type = "jsonb")
    @Column(name = "dataset", columnDefinition = "jsonb")
    private String dataset;
    @Column(name = "dataset_interval")
    private int datasetInterval;

    public ActivitiesSteps() {
    }

    public ActivitiesSteps(Date dateTime, int steps, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.steps = steps;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
}
