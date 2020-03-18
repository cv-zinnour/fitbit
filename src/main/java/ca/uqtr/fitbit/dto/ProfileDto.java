package ca.uqtr.fitbit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private String gender;
    private String birthday;
    private String height;
    private String weight;
}
