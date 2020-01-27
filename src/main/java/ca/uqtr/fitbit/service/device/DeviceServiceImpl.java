package ca.uqtr.fitbit.service.device;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceServiceImpl implements DeviceService {

    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    @Override
    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }


}
