package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.view.Steps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import java.sql.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MinutesDto {

    private String medicalFileId;
    private Date date;
    private int sedentary;
    private int lightly_active;
    private int fairly_active;
    private int very_active;

    public Steps dtoToObj(ModelMapper modelMapper) {
        return modelMapper.map(this, Steps.class);
    }

}
