package ca.uqtr.fitbit.entity;


import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesDistance;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "patient_device", schema = "public")
public class PatientDevice implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "init_date", nullable = false)
    private Timestamp initDate;

    @Column(name = "professional_id", nullable = false)
    private UUID professionalId;
    @Column(name = "medical_file_id", nullable = false)
    private String medicalFileId;
    @Column(name = "patient_email", nullable = false)
    private String patientEmail;
    @Column(name = "returned_at", nullable = false)
    private Timestamp returnedAt;
    @JsonManagedReference
    @OneToMany(mappedBy = "patientDevice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivitiesCalories> activitiesCalories = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "patientDevice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivitiesSteps> activitiesSteps = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "patientDevice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivitiesDistance> activitiesDistance = new ArrayList<>();
    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Device device;
}
