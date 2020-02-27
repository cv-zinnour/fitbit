package ca.uqtr.fitbit.service.device;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Response;

import java.util.List;

public interface DeviceService {

    Response createDevice(DeviceDto device);
    Response readDevice(DeviceDto device);
    void deleteDevice(DeviceDto device);
    Response updateDevice(DeviceDto device);
    Response readDevices(DeviceDto device);
    Response authorizeDevice(DeviceDto device, String code);

    Response unauthorizeDevice(DeviceDto device);

    Response readAvailableDevices(DeviceDto device);
    Response readAvailableDevicesByInstitutionCode(DeviceDto device);
    Response assignDevice(DeviceDto device);
    Response getBackDevice(DeviceDto device);
}
