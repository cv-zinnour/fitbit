package ca.uqtr.fitbit.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "init_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date initDate;
    @Column(name = "return_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date returnDate;
    @Column(name = "expert_id", nullable = false)
    private UUID expertId;
    @Column(name = "patient_id", nullable = false)
    private UUID patientId;
    @OneToMany
    @JoinColumn(name = "device_id")
    private List<Device> devices = new ArrayList<>();
}
