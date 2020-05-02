package ca.uqtr.fitbit.entity.fitbit;

import ca.uqtr.fitbit.entity.PatientDevice;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "activities_distance", schema = "public")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class ActivitiesDistance extends Activities{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "date")
    private Date dateTime;
    @Column(name = "value")
    private double value;
    @Type(type = "jsonb")
    @Column(name = "dataset", columnDefinition = "jsonb")
    private String dataset;
    @Column(name = "dataset_interval")
    private int datasetInterval;
    @Column(name = "time_start")
    private Timestamp timeStart;
    @Column(name = "time_end")
    private Timestamp timeEnd;
    @Column(name = "sync_time")
    private Timestamp SyncTime;
    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private PatientDevice patientDevice;

    public ActivitiesDistance(Date dateTime, double value, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.value = value;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }

    public ActivitiesDistance() {
    }


}
