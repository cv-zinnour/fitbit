package ca.uqtr.fitbit.repository;


import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.view.Steps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional( readOnly = true )
@Repository
public interface StepsRepository extends CrudRepository<Steps, Integer> {

}
