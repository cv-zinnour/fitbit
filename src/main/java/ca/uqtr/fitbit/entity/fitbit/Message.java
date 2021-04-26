package ca.uqtr.fitbit.entity.fitbit;

import ca.uqtr.fitbit.entity.Device;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Message {
    @JsonProperty("time")
    private Timestamp time;
    @JsonProperty("subscriptionId")
    private String subscriptionId;


}
