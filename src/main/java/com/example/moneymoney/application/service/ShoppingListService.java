package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.shopping.ShoppingItemResponseDTO;
import com.example.moneymoney.application.port.out.HouseRepositoryPort;
import com.example.moneymoney.application.port.out.ShoppingItemRepositoryPort;
import com.example.moneymoney.application.port.out.UserHouseRepositoryPort;
import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.exception.BusinessException;
import com.example.moneymoney.domain.exception.HouseNotFoundException;
import com.example.moneymoney.domain.exception.UserNotFoundException;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.ShoppingItem;
import com.example.moneymoney.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final ShoppingItemRepositoryPort shoppingItemRepository;
    private final HouseRepositoryPort houseRepository;
    private final UserRepositoryPort userRepository;
    private final UserHouseRepositoryPort userHouseRepository;

    public ShoppingListService(ShoppingItemRepositoryPort shoppingItemRepository,
            HouseRepositoryPort houseRepository,
            UserRepositoryPort userRepository,
            UserHouseRepositoryPort userHouseRepository) {
        this.shoppingItemRepository = shoppingItemRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
        this.userHouseRepository = userHouseRepository;
    }

    @Transactional
    public ShoppingItemResponseDTO addItem(Long houseId, String itemName, Long creatorId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!userHouseRepository.existsByUserIdAndHouseId(creatorId, houseId)) {
            throw new BusinessException("User is not a member of this house");
        }

        if (shoppingItemRepository.findByNameAndHouseIdAndIsPurchasedFalse(itemName, houseId).isPresent()) {
            throw new BusinessException("Item already exists in the list");
        }

        ShoppingItem newItem = new ShoppingItem();
        newItem.setName(itemName);
        newItem.setHouse(house);
        newItem.setCreatedBy(creator);
        newItem.setPurchased(false);

        ShoppingItem savedItem = shoppingItemRepository.save(newItem);

        return new ShoppingItemResponseDTO(
                savedItem.getId(),
                savedItem.getName(),
                0,
                false,
                savedItem.isPurchased());
    }

    @Transactional
    public void toggleVote(Long itemId, Long userId) {
        ShoppingItem item = shoppingItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException("Item not found"));

        if (item.isPurchased()) {
            throw new BusinessException("Item already purchased!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if user is already in votes.
        // Note: We need to rely on equals/hashCode of User or check by ID.
        // Since we are using Domain Models, we should check if the set contains a user
        // with the same ID.

        boolean alreadyVoted = item.getVotes().stream()
                .anyMatch(v -> v.getId().equals(userId));

        if (alreadyVoted) {
            item.getVotes().removeIf(v -> v.getId().equals(userId));
        } else {
            item.addVote(user);
        }

        shoppingItemRepository.save(item);
    }

    @Transactional
    public void markAsPurchased(Long itemId) {
        ShoppingItem item = shoppingItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException("Item not found"));

        item.setPurchased(true);
        shoppingItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ShoppingItemResponseDTO> getList(Long houseId, Long currentUserId, boolean includePurchased) {
        if (!houseRepository.existsById(houseId)) {
            throw new HouseNotFoundException("House not found");
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<ShoppingItem> items;
        if (includePurchased) {
            items = shoppingItemRepository.findAllByHouseIdSortedByVotes(houseId);
        } else {
            items = shoppingItemRepository.findAllActiveByHouseIdSortedByVotes(houseId);
        }

        return items.stream()
                .map(item -> new ShoppingItemResponseDTO(
                        item.getId(),
                        item.getName(),
                        item.getVotes().size(),
                        item.getVotes().stream().anyMatch(v -> v.getId().equals(currentUserId)),
                        item.isPurchased()))
                .collect(Collectors.toList());
    }
}
