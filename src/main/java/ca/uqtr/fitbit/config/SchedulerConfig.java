package ca.uqtr.fitbit.config;

import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    @Value("${scheduler.thread.pool.size}")
    private int POOL_SIZE;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
        taskRegistrar.addTriggerTask(
                () -> yourJob().performJob(),
                (TriggerContext triggerContext) -> yourService.getCron()
        );
    }

    @Bean(name = "taskScheduler")
    public TaskScheduler taskScheduler() {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(POOL_SIZE);
        scheduler.setThreadNamePrefix("My-Scheduler-");
        scheduler.setErrorHandler(t -> LOGGER.log( Level.ALL, "Unknown error occurred while executing task."+ t));
        scheduler.setRejectedExecutionHandler((r, e) -> LOGGER.log(Level.ALL, "Execution of task {} was rejected for unknown reasons."+ r));
        scheduler.initialize();

        return scheduler;
    }
}
*/
