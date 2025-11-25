package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.BillRepositoryPort;
import com.example.moneymoney.domain.model.Bill;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.persistence.entity.BillJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.BillJpaRepository;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BillPersistenceAdapter implements BillRepositoryPort {

    private final BillJpaRepository billJpaRepository;

    public BillPersistenceAdapter(BillJpaRepository billJpaRepository) {
        this.billJpaRepository = billJpaRepository;
    }

    @Override
    public Bill save(Bill bill) {
        BillJpaEntity entity = toJpaEntity(bill);
        BillJpaEntity savedEntity = billJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return billJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Bill> findByHouseAndDueDateBetween(House house, LocalDate start, LocalDate end) {
        HouseJpaEntity houseEntity = new HouseJpaEntity();
        houseEntity.setId(house.getId()); // Assuming ID is enough for the query

        return billJpaRepository.findByHouseAndDueDateBetween(houseEntity, start, end)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Bill> findByUserAndHouseIsNullAndDueDateBetween(User user, LocalDate start, LocalDate end) {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(user.getId()); // Assuming ID is enough

        return billJpaRepository.findByUserAndHouseIsNullAndDueDateBetween(userEntity, start, end)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal sumByHouseAndDueDateBetween(House house, LocalDate start, LocalDate end) {
        HouseJpaEntity houseEntity = new HouseJpaEntity();
        houseEntity.setId(house.getId());
        return billJpaRepository.sumByHouseAndDueDateBetween(houseEntity, start, end);
    }

    @Override
    public BigDecimal sumByUserAndHouseIsNullAndDueDateBetween(User user, LocalDate start, LocalDate end) {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(user.getId());
        return billJpaRepository.sumByUserAndHouseIsNullAndDueDateBetween(userEntity, start, end);
    }

    private BillJpaEntity toJpaEntity(Bill bill) {
        BillJpaEntity entity = new BillJpaEntity();
        entity.setId(bill.getId());
        entity.setDescription(bill.getDescription());
        entity.setAmount(bill.getAmount());
        entity.setDueDate(bill.getDueDate());
        entity.setType(bill.getType());
        entity.setPaid(bill.isPaid());

        if (bill.getHouse() != null) {
            HouseJpaEntity houseEntity = new HouseJpaEntity();
            houseEntity.setId(bill.getHouse().getId());
            entity.setHouse(houseEntity);
        }

        if (bill.getUser() != null) {
            UserJpaEntity userEntity = new UserJpaEntity();
            userEntity.setId(bill.getUser().getId());
            entity.setUser(userEntity);
        }

        return entity;
    }

    private Bill toDomain(BillJpaEntity entity) {
        Bill bill = new Bill();
        bill.setId(entity.getId());
        bill.setDescription(entity.getDescription());
        bill.setAmount(entity.getAmount());
        bill.setDueDate(entity.getDueDate());
        bill.setType(entity.getType());
        bill.setPaid(entity.isPaid());

        if (entity.getHouse() != null) {
            House house = new House();
            house.setId(entity.getHouse().getId());
            // We might need more house details, but for now ID is critical.
            // Ideally we should fetch or map full object, but avoiding N+1 or extra queries
            // if not needed.
            bill.setHouse(house);
        }

        if (entity.getUser() != null) {
            User user = new User();
            user.setId(entity.getUser().getId());
            bill.setUser(user);
        }

        return bill;
    }
}
