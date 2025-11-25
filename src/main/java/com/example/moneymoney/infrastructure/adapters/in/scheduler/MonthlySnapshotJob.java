package com.example.moneymoney.infrastructure.adapters.in.scheduler;

import com.example.moneymoney.application.port.in.ProcessMonthlySnapshotUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlySnapshotJob {

    private static final Logger logger = LoggerFactory.getLogger(MonthlySnapshotJob.class);
    private final ProcessMonthlySnapshotUseCase processMonthlySnapshotUseCase;

    public MonthlySnapshotJob(ProcessMonthlySnapshotUseCase processMonthlySnapshotUseCase) {
        this.processMonthlySnapshotUseCase = processMonthlySnapshotUseCase;
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Run at midnight on the 1st of every month
    public void processMonthlySnapshot() {
        logger.info("Executing monthly snapshot job");
        processMonthlySnapshotUseCase.processMonthlySnapshot();
        logger.info("Monthly snapshot job finished");
    }
}
