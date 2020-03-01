package ca.uqtr.fitbit.entity;

import ca.uqtr.fitbit.entity.fitbit.Auth;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "device", schema = "public")
public class Device extends BaseEntity {


    @Column(name = "device_code", nullable = false)
    private String deviceCode;
    @Column(name = "device_version", nullable = false)
    private String deviceVersion;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "last_sync_date", nullable = false)
    private Date lastSyncDate;
    @Column(name = "authorized", nullable = false)
    private boolean authorized;
    @Column(name = "admin_id", nullable = false)
    private UUID adminId;
    @Column(name = "available", nullable = false)
    private boolean available;
    @Column(name = "institution_code", nullable = false)
    private UUID institutionCode;
    @JsonManagedReference
    @OneToOne(mappedBy="device", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Auth auth;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "device_id")
    private List<PatientDevice> patientDevices;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "device_id")
    private List<FitbitSubscription> fitbitSubscriptions;

}
