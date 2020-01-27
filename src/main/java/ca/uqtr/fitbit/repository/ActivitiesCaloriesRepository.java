package ca.uqtr.fitbit.repository;


import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivitiesCaloriesRepository extends CrudRepository<ActivitiesCalories, UUID> {
}
