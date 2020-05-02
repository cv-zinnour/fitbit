package ca.uqtr.fitbit.repository;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PatientDeviceRepository extends CrudRepository<PatientDevice, UUID> {
    @Query("select pd from PatientDevice pd where pd.device.id = :deviceId and pd.returnedAt is null")
    PatientDevice getByDeviceIdAndReturnedAtIsNull(UUID deviceId);

/*
    @Query("select sum(steps.value) from PatientDevice pd left join fetch pd.activitiesSteps steps where pd.id = steps.patientDevice.id and pd.medicalFileId = :medicalFileId and steps.dateTime between :date1 and :date2 group by steps.dateTime")
    List<Integer> getStepsBetweenTwoVisits(String medicalFileId, Date date1, Date date2);
*/

}
