package ca.uqtr.fitbit.repository;


import ca.uqtr.fitbit.entity.view.Minutes;
import ca.uqtr.fitbit.entity.view.Steps;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;


@Transactional( readOnly = true )
@Repository
public interface MinutesRepository extends CrudRepository<Minutes, Integer> {

    @Query("select minutes from Minutes minutes where minutes.medicalFileId = :medicalFileId and minutes.date between :date1 and :date2")
    List<Minutes> getByMedicalFileIdAndTwoDates(String medicalFileId, Date date1, Date date2);
    @Query("select minutes from Minutes minutes where minutes.medicalFileId = :medicalFileId and minutes.date = :date")
    Minutes getByMedicalFileIdAndOneDate(String medicalFileId, Date date);
    List<Minutes> getMinutesByMedicalFileId(String medicalFileId);

}
