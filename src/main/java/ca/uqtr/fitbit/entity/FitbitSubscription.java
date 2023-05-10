package ca.uqtr.fitbit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "fitbit_subscription", schema = "public")
public class FitbitSubscription implements Serializable {

    static final long serialVersionUID = 1L;
    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "collection_type")
    private String collectionType;
    @Column(name = "owner_id")
    private String ownerId;
    @Column(name = "owner_type")
    private String ownerType;
    @Column(name = "subscriber_id")
    private String subscriberId;
    @Column(name = "subscription_id")
    private String subscriptionId;
    @Column(name = "date")
    private Date date;
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public FitbitSubscription(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
