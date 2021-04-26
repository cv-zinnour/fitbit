package ca.uqtr.fitbit.entity.fitbit;

import ca.uqtr.fitbit.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Message {

    private Timestamp time;
    private String subscriptionId;


}
