package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.entity.PatientDevice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.persistence.Version;
import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitbitSubscriptionDto {

    private int id;
    private String collectionType;
    private String ownerId;
    private String ownerType;
    private String subscriberId;
    private String subscriptionId;
    private Date date;

    public FitbitSubscription dtoToObj(ModelMapper modelMapper) {
        return modelMapper.map(this, FitbitSubscription.class);
    }

}
