package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.view.Steps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import java.sql.Date;
import java.sql.Timestamp;
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StepsDto {

    private String medicalFileId;
    private Date date;
    private Double steps;

    public Steps dtoToObj(ModelMapper modelMapper) {
        return modelMapper.map(this, Steps.class);
    }

}
