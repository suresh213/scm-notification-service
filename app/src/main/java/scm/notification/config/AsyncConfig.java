package scm.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Production-grade async and scheduling configuration.
 * Configures thread pool for notification processing with proper sizing and
 * naming.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {

    /**
     * Configures the thread pool executor for async notification processing.
     * 
     * Pool sizing strategy:
     * - Core pool: 5 threads (handles typical load)
     * - Max pool: 20 threads (handles burst traffic)
     * - Queue capacity: 100 (buffers requests during peaks)
     * - Keep alive: 60 seconds (releases idle threads)
     */
    @Bean(name = "notificationExecutor")
    public Executor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size - minimum threads to keep alive
        executor.setCorePoolSize(5);

        // Maximum pool size - upper limit for thread creation
        executor.setMaxPoolSize(20);

        // Queue capacity before rejecting new tasks
        executor.setQueueCapacity(100);

        // Thread name prefix for easy identification in logs/monitoring
        executor.setThreadNamePrefix("notification-async-");

        // Keep alive time for idle threads (in seconds)
        executor.setKeepAliveSeconds(60);

        // Allow core threads to time out
        executor.setAllowCoreThreadTimeOut(true);

        // Wait for tasks to complete on shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // Timeout for waiting on shutdown
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();

        return executor;
    }
}
