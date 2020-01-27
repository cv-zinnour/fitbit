package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.vo.Battery;
import ca.uqtr.fitbit.entity.vo.Type;
import ca.uqtr.fitbit.entity.vo.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID id;
    private String deviceId;
    private Version deviceVersion;
    private Type type;
    private Battery battery;
    private Date lastSyncTime;
}
