package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.BillCreateDTO;
import com.example.moneymoney.application.dto.BillResponseDTO;
import com.example.moneymoney.application.port.out.BillRepositoryPort;
import com.example.moneymoney.application.port.out.HouseRepositoryPort;
import com.example.moneymoney.application.port.out.UserHouseRepositoryPort;
import com.example.moneymoney.domain.exception.HouseNotFoundException;
import com.example.moneymoney.domain.exception.InsufficientPermissionsException;
import com.example.moneymoney.domain.model.Bill;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private final BillRepositoryPort billRepository;
    private final HouseRepositoryPort houseRepository;
    private final UserHouseRepositoryPort userHouseRepository;

    public BillService(BillRepositoryPort billRepository, HouseRepositoryPort houseRepository,
            UserHouseRepositoryPort userHouseRepository) {
        this.billRepository = billRepository;
        this.houseRepository = houseRepository;
        this.userHouseRepository = userHouseRepository;
    }

    public BillResponseDTO createHouseBill(Long houseId, BillCreateDTO dto, User creator) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));

        if (!userHouseRepository.existsByHouseAndUser(house, creator)) {
            throw new InsufficientPermissionsException("User is not a member of this house");
        }

        Bill bill = new Bill();
        bill.setDescription(dto.getDescription());
        bill.setAmount(dto.getAmount());
        bill.setDueDate(dto.getDueDate());
        bill.setType(dto.getType());
        bill.setPaid(false);
        bill.setHouse(house);
        bill.setUser(creator);

        Bill savedBill = billRepository.save(bill);
        return toDTO(savedBill);
    }

    public BillResponseDTO createPersonalBill(BillCreateDTO dto, User creator) {
        Bill bill = new Bill();
        bill.setDescription(dto.getDescription());
        bill.setAmount(dto.getAmount());
        bill.setDueDate(dto.getDueDate());
        bill.setType(dto.getType());
        bill.setPaid(false);
        bill.setHouse(null);
        bill.setUser(creator);

        Bill savedBill = billRepository.save(bill);
        return toDTO(savedBill);
    }

    public List<BillResponseDTO> listHouseBills(Long houseId, int month, int year, User requestUser) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));

        if (!userHouseRepository.existsByHouseAndUser(house, requestUser)) {
            throw new InsufficientPermissionsException("User is not a member of this house");
        }

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Bill> bills = billRepository.findByHouseAndDueDateBetween(house, start, end);
        return bills.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<BillResponseDTO> listPersonalBills(int month, int year, User requestUser) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Bill> bills = billRepository.findByUserAndHouseIsNullAndDueDateBetween(requestUser, start, end);
        return bills.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public BillResponseDTO payBill(Long billId, User requestUser) {
        // Note: The prompt didn't explicitly ask for payBill logic validation, but I
        // should probably check ownership.
        // For now, I'll just implement the basic toggle as requested in step 5.
        // "PATCH /bills/{id}/pay: Marca a conta como paga (isPaid = true)."

        // I need to find the bill first.
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found")); // Should use a specific exception

        // Basic permission check: if house bill, user must be member. If personal, user
        // must be owner.
        if (bill.getHouse() != null) {
            if (!userHouseRepository.existsByHouseAndUser(bill.getHouse(), requestUser)) {
                throw new InsufficientPermissionsException("User is not a member of this house");
            }
        } else {
            if (!bill.getUser().getId().equals(requestUser.getId())) {
                throw new InsufficientPermissionsException("User does not own this bill");
            }
        }

        bill.setPaid(true);
        Bill savedBill = billRepository.save(bill);
        return toDTO(savedBill);
    }

    private BillResponseDTO toDTO(Bill bill) {
        return new BillResponseDTO(
                bill.getId(),
                bill.getDescription(),
                bill.getAmount(),
                bill.getDueDate(),
                bill.getType(),
                bill.isPaid(),
                bill.getHouse() != null ? bill.getHouse().getId() : null,
                bill.getUser() != null ? bill.getUser().getId() : null);
    }
}
