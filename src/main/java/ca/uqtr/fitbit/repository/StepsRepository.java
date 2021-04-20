package ca.uqtr.fitbit.repository;


import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.view.Steps;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;


@Transactional( readOnly = true )
@Repository
public interface StepsRepository extends CrudRepository<Steps, Integer> {

    @Query("select steps from Steps steps where steps.medicalFileId = :medicalFileId and steps.date between :date1 and :date2")
    List<Steps> getByMedicalFileIdAndTwoDates(String medicalFileId, Date date1, Date date2);

    @Query("select steps from Steps steps where steps.medicalFileId = :medicalFileId and steps.date = :date")
    Steps getByMedicalFileIdAndOneDate(String medicalFileId, Date date);

}
