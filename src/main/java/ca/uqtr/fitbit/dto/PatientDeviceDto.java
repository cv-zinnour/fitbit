package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDeviceDto {
    private int id;
    private Date initDate;
    private Date returnDate;
    private String professionalId;
    private String medicalFileId;
    private String patientEmail;
    private Date returnedAt;
    //private List<DeviceDto> devices = new ArrayList<>();

    public PatientDevice dtoToObj(ModelMapper modelMapper) {
        return modelMapper.map(this, PatientDevice.class);
    }

    public UUID getProfessionalId() {
        if (this.professionalId != null)
            return UUID.fromString(this.professionalId);
        else
            return null;
    }

}
