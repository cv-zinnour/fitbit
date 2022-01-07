package ca.uqtr.fitbit.repository;

import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PatientDeviceRepository extends CrudRepository<PatientDevice, UUID> {
    @Query("select pd from PatientDevice pd where pd.device.id = :deviceId and pd.returnedAt is null")
    PatientDevice getByDeviceIdAndReturnedAtIsNull(UUID deviceId);

    List<PatientDevice> getByMedicalFileId(String medicalFileId);

    @Query("select min( pd.initDate ) from PatientDevice pd where pd.medicalFileId = :medicalFileId")
    Timestamp getByInitDate(String medicalFileId);



/*
    @Query("select sum(steps.value) from PatientDevice pd left join pd.activitiesSteps steps where pd.id = steps.patientDevice.id and pd.medicalFileId = :medicalFileId and steps.dateTime between :date1 and :date2 group by steps.dateTime")
    int getStepsBetweenTwoVisits(String medicalFileId, Date date1, Date date2);
*/

}
