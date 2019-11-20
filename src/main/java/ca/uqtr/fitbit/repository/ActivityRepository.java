package ca.uqtr.fitbit.repository;


import ca.uqtr.fitbit.entity.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, UUID> {
}
