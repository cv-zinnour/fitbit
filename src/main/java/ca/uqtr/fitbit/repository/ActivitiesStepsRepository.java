package ca.uqtr.fitbit.repository;


import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivitiesStepsRepository extends CrudRepository<ActivitiesSteps, UUID> {


}
