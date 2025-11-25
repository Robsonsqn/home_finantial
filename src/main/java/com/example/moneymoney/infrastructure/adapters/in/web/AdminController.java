package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.infrastructure.adapters.in.scheduler.MonthlySnapshotJob;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final MonthlySnapshotJob monthlySnapshotJob;

    public AdminController(MonthlySnapshotJob monthlySnapshotJob) {
        this.monthlySnapshotJob = monthlySnapshotJob;
    }

    @PostMapping("/force-calculation")
    public ResponseEntity<String> forceCalculation() {
        monthlySnapshotJob.processMonthlySnapshot();
        return ResponseEntity.ok("Calculation triggered successfully");
    }
}
