package ca.uqtr.fitbit.service;

import ca.uqtr.fitbit.api.IApi;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceServiceImpl implements DeviceService {

    private DeviceRepository deviceRepository;
    IApi api;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, IApi api) {
        this.deviceRepository = deviceRepository;
        this.api = api;
    }


    @Override
    public Device addDevice(Device device) {
        return null;
    }


}
