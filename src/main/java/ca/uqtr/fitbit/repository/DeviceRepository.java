package ca.uqtr.fitbit.repository;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends CrudRepository<Device, UUID> {

    Device getDeviceById(UUID id);

    List<Device> findAllByAdminId(UUID adminId);
    List<Device> findAllByAdminIdAndAvailable(UUID adminId, boolean available);
    List<Device> findAllByInstitutionCodeAndAvailableAndAuthorized(UUID institutionCode, boolean available, boolean authorized);
    @Query("select d from Device d left join fetch d.patientDevices pd where d.id = pd.device.id and pd.returnedAt is null and d.id = :id ")
    Device  getDeviceWith_LastPatientDevice_FetchTypeEAGER(UUID id);

    @Query("select d from Device d left join fetch d.fitbitSubscriptions fs where d.id = :id ORDER BY fs.id DESC ")
    Device  getDeviceWith_LastFitbitSubscription_FetchTypeEAGER(UUID id);

    @Query("select d from Device d left join fetch d.patientDevices pd where pd.medicalFileId = :medicalFileId AND pd.returnedAt IS NULL ORDER BY pd.id DESC")
    Device  isPatientHasDevice(String medicalFileId);

    @Query("select d from Device d left join fetch d.patientDevices pd where d.id = pd.device.id and pd.returnedAt IS NULL")
    List<Device>  devicesNotReturned();


}
