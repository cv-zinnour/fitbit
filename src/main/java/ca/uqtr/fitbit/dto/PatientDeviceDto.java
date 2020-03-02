package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PatientDeviceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Date initDate;
    private Date returnDate;
    private UUID professionalId;
    private UUID medicalFileId;
    private List<DeviceDto> devices = new ArrayList<>();
}
