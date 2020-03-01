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

    List<Device> findAllByAdminId(UUID adminId);
    List<Device> findAllByAdminIdAndAvailable(UUID adminId, boolean available);
    List<Device> findAllByInstitutionCodeAndAvailable(UUID institutionCode, boolean available);
    @Query("select d from Device d left join fetch d.patientDevices pd where d.id = :id ORDER BY pd.id DESC ")
    Device  getDeviceWith_LastPatientDevice_FetchTypeEAGER(UUID id);

    @Query("select d from Device d left join fetch d.fitbitSubscriptions fs where d.id = :id ORDER BY fs.id DESC ")
    Device  getDeviceWith_LastFitbitSubscription_FetchTypeEAGER(UUID id);

}
