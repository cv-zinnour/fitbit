package ca.uqtr.fitbit.entity;

import ca.uqtr.fitbit.entity.vo.Battery;
import ca.uqtr.fitbit.entity.vo.Type;
import ca.uqtr.fitbit.entity.vo.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "device", schema = "public")
public class Device extends BaseEntity {

    @Column(name = "device_id", nullable = false)
    private String deviceId;
    @Column(name = "device_version", nullable = false)
    @Enumerated(EnumType.STRING)
    private Version deviceVersion;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "battery", nullable = false)
    @Enumerated(EnumType.STRING)
    private Battery battery;
    @Column(name = "last_sync_time", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date lastSyncTime;


}
