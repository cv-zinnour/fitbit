package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private String id;
    private String deviceCode;
    private String deviceVersion;
    private String type;
    private Date lastSyncDate;
    private boolean authorized;
    private String adminId;
    private boolean available;
    private String institutionCode;
    private Auth auth;
    private List<PatientDeviceDto> patientDevices = new ArrayList<>();

    public DeviceDto(String id) {
        this.id = id;
    }

    public Device dtoToObj(ModelMapper modelMapper) {
        return modelMapper.map(this, Device.class);
    }

    public UUID getId() {
        if (this.id != null)
            return UUID.fromString(this.id);
        else
            return null;
    }

    public UUID getInstitutionCode() {
        if (this.institutionCode != null)
            return UUID.fromString(this.institutionCode);
        else
            return null;
    }

    public UUID getAdminId() {
        if (this.adminId != null)
            return UUID.fromString(this.adminId);
        else
            return null;
    }

}
