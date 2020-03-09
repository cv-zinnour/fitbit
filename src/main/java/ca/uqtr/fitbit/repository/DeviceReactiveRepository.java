package ca.uqtr.fitbit.repository;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

/*
public interface DeviceReactiveRepository extends R2dbcRepository<Device, UUID> {

    @Query("select d.id, d.patientDevices, d.synchronizations from Device d left join fetch d.patientDevices pd where pd.returnedAt IS NULL ORDER BY pd.id DESC")
    Flux<Device> devicesNotReturned();
}
*/
