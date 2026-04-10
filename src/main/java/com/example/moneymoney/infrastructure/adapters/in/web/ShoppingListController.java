package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.application.dto.shopping.CreateShoppingItemDTO;
import com.example.moneymoney.application.dto.shopping.ShoppingItemResponseDTO;
import com.example.moneymoney.application.service.ShoppingListService;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.security.AuthenticationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final AuthenticationHelper authHelper;

    public ShoppingListController(ShoppingListService shoppingListService, AuthenticationHelper authHelper) {
        this.shoppingListService = shoppingListService;
        this.authHelper = authHelper;
    }

    @PostMapping("/houses/{houseId}/shopping-items")
    public ResponseEntity<ShoppingItemResponseDTO> addItem(
            @PathVariable Long houseId,
            @RequestBody CreateShoppingItemDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authHelper.getCurrentUser(userDetails);
        ShoppingItemResponseDTO response = shoppingListService.addItem(houseId, dto.name(), currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/houses/{houseId}/shopping-items")
    public ResponseEntity<List<ShoppingItemResponseDTO>> getList(
            @PathVariable Long houseId,
            @RequestParam(required = false, defaultValue = "false") boolean includePurchased,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authHelper.getCurrentUser(userDetails);
        List<ShoppingItemResponseDTO> list = shoppingListService.getList(houseId, currentUser.getId(),
                includePurchased);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/shopping-items/{itemId}/vote")
    public ResponseEntity<Void> toggleVote(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authHelper.getCurrentUser(userDetails);
        shoppingListService.toggleVote(itemId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/shopping-items/{itemId}/purchased")
    public ResponseEntity<Void> markAsPurchased(
            @PathVariable Long itemId) {
        shoppingListService.markAsPurchased(itemId);
        return ResponseEntity.noContent().build();
    }
}
