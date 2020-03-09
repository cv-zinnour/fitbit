package ca.uqtr.fitbit.entity.fitbit;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "activities_calories", schema = "public")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class ActivitiesCalories extends Activities{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "date")
    private Timestamp dateTime;
    @Column(name = "calories")
    private double calories;
    @Type(type = "jsonb")
    @Column(name = "dataset", columnDefinition = "jsonb")
    private String dataset;
    @Column(name = "dataset_interval")
    private int datasetInterval;



    public ActivitiesCalories(Timestamp dateTime, double calories, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.calories = calories;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
    public ActivitiesCalories() {
    }
}
