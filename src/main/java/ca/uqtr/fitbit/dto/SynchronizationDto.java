package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.Synchronization;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SynchronizationDto {


    private int id;
    private int version;
    private Timestamp lastSyncTime;
    private DeviceDto device;

    public Synchronization dtoToObj(ModelMapper modelMapper) {
        return modelMapper.map(this, Synchronization.class);
    }

}
