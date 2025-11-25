package com.example.moneymoney.application.service;

import com.example.moneymoney.application.port.in.ProcessMonthlySnapshotUseCase;
import com.example.moneymoney.application.port.out.HouseRepositoryPort;
import com.example.moneymoney.domain.model.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SnapshotApplicationService implements ProcessMonthlySnapshotUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotApplicationService.class);
    private final HouseRepositoryPort houseRepository;
    private final ContributionService contributionService;

    public SnapshotApplicationService(HouseRepositoryPort houseRepository, ContributionService contributionService) {
        this.houseRepository = houseRepository;
        this.contributionService = contributionService;
    }

    @Override
    @Transactional
    public void processMonthlySnapshot() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        logger.info("Starting monthly snapshot for {}/{}", month, year);

        List<House> houses = houseRepository.findAll();
        for (House house : houses) {
            try {
                contributionService.calculateForHouse(house, month, year);
            } catch (Exception e) {
                logger.error("Error calculating contribution for house ID: {}", house.getId(), e);
            }
        }

        logger.info("Monthly snapshot completed");
    }
}
