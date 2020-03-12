package ca.uqtr.fitbit.repository;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientDeviceRepository extends CrudRepository<PatientDevice, UUID> {
    @Query("select pd from PatientDevice pd where pd.device.id = :id and pd.returnedAt is null")
    PatientDevice get(UUID deviceId);
}
