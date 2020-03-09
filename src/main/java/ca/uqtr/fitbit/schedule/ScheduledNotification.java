package ca.uqtr.fitbit.schedule;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.event.reminder.OnSynchronizationEmailEvent;
import ca.uqtr.fitbit.repository.DeviceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledNotification {

    private DeviceRepository deviceRepository;
    private ApplicationEventPublisher eventPublisher;
    private ModelMapper modelMapper;

    public ScheduledNotification(DeviceRepository deviceRepository, ApplicationEventPublisher eventPublisher, ModelMapper modelMapper) {
        this.deviceRepository = deviceRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
    }


    /*@Bean
    public int isTherePatientDoesntSync()
    {
        return cronRepo.findOne("cron").getCronValue();
    }, cron="#{@getCronValue}"*/

    @Scheduled(fixedDelay = 43200)
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        Calendar cal = Calendar.getInstance();
        List<Device> devices = deviceRepository.devicesNotReturned();
        if (devices != null && !devices.isEmpty()){
            for (Device device: devices) {
                long time = cal.getTime().getTime() - (device.getSynchronizations().get(0).getLastSyncTime().getTime() + TimeUnit.DAYS.toMillis(5));
                if (time <= 0){
                    eventPublisher.publishEvent(new OnSynchronizationEmailEvent(modelMapper.map(device, DeviceDto.class)));
                }
            }
        }
    }
}
