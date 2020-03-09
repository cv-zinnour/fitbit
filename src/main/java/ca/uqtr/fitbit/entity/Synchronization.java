package ca.uqtr.fitbit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "synchronization", schema = "public")
public class Synchronization  implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "last_sync_time", nullable = false)
    private Timestamp lastSyncTime;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Device device;

}
