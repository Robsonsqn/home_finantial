package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.application.dto.BillCreateDTO;
import com.example.moneymoney.application.dto.BillResponseDTO;
import com.example.moneymoney.application.service.BillService;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.security.AuthenticationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillController {

    private final BillService billService;
    private final AuthenticationHelper authHelper;

    public BillController(BillService billService, AuthenticationHelper authHelper) {
        this.billService = billService;
        this.authHelper = authHelper;
    }

    @PostMapping("/houses/{houseId}/bills")
    @ResponseStatus(HttpStatus.CREATED)
    public BillResponseDTO createHouseBill(@PathVariable Long houseId, @RequestBody BillCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = authHelper.getCurrentUser(userDetails);
        return billService.createHouseBill(houseId, dto, user);
    }

    @GetMapping("/houses/{houseId}/bills")
    public List<BillResponseDTO> listHouseBills(@PathVariable Long houseId, @RequestParam int month,
            @RequestParam int year, @AuthenticationPrincipal UserDetails userDetails) {
        User user = authHelper.getCurrentUser(userDetails);
        return billService.listHouseBills(houseId, month, year, user);
    }

    @PostMapping("/bills/personal")
    @ResponseStatus(HttpStatus.CREATED)
    public BillResponseDTO createPersonalBill(@RequestBody BillCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = authHelper.getCurrentUser(userDetails);
        return billService.createPersonalBill(dto, user);
    }

    @GetMapping("/bills/personal")
    public List<BillResponseDTO> listPersonalBills(@RequestParam int month, @RequestParam int year,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = authHelper.getCurrentUser(userDetails);
        return billService.listPersonalBills(month, year, user);
    }

    @PatchMapping("/bills/{id}/pay")
    public BillResponseDTO payBill(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = authHelper.getCurrentUser(userDetails);
        return billService.payBill(id, user);
    }
}
