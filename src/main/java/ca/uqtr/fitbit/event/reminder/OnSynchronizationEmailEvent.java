package ca.uqtr.fitbit.event.reminder;

import ca.uqtr.fitbit.dto.DeviceDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnSynchronizationEmailEvent extends ApplicationEvent {
    private DeviceDto device;

    public OnSynchronizationEmailEvent(DeviceDto device) {
        super(device);
        this.device = device;
    }

}
