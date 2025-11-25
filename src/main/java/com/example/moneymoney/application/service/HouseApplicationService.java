package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.HouseCreateDTO;
import com.example.moneymoney.application.dto.HouseResponseDTO;
import com.example.moneymoney.application.port.in.CreateHouseUseCase;
import com.example.moneymoney.application.port.in.GetMyHousesUseCase;
import com.example.moneymoney.application.port.in.InviteMemberUseCase;
import com.example.moneymoney.application.port.out.HouseRepositoryPort;
import com.example.moneymoney.application.port.out.UserHouseRepositoryPort;
import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.HouseRole;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.domain.model.UserHouse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseApplicationService implements CreateHouseUseCase, InviteMemberUseCase, GetMyHousesUseCase {

    private final HouseRepositoryPort houseRepository;
    private final UserHouseRepositoryPort userHouseRepository;
    private final UserRepositoryPort userRepository;
    private final ContributionService contributionService;

    public HouseApplicationService(HouseRepositoryPort houseRepository,
            UserHouseRepositoryPort userHouseRepository,
            UserRepositoryPort userRepository,
            ContributionService contributionService) {
        this.houseRepository = houseRepository;
        this.userHouseRepository = userHouseRepository;
        this.userRepository = userRepository;
        this.contributionService = contributionService;
    }

    @Override
    @Transactional
    public HouseResponseDTO createHouse(HouseCreateDTO dto, User creator) {
        House house = new House(dto.name());
        house = houseRepository.save(house);

        UserHouse userHouse = new UserHouse(creator, house, HouseRole.ADMIN);
        userHouseRepository.save(userHouse);

        LocalDate now = LocalDate.now();
        contributionService.calculateForHouse(house, now.getMonthValue(), now.getYear());

        return new HouseResponseDTO(house.getId(), house.getName(), HouseRole.ADMIN);
    }

    @Override
    public List<HouseResponseDTO> getMyHouses(User user) {
        return userHouseRepository.findByUser(user).stream()
                .map(uh -> new HouseResponseDTO(uh.getHouse().getId(), uh.getHouse().getName(), uh.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void inviteMember(Long houseId, String emailInvited, User requester) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found"));

        UserHouse requesterUserHouse = userHouseRepository.findByHouseAndUser(house, requester)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this house"));

        if (requesterUserHouse.getRole() != HouseRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can invite members");
        }

        User invitedUser = userRepository.findByEmail(emailInvited)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with email: " + emailInvited));

        if (userHouseRepository.existsByHouseAndUser(house, invitedUser)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member of this house");
        }

        UserHouse newUserHouse = new UserHouse(invitedUser, house, HouseRole.MEMBER);
        userHouseRepository.save(newUserHouse);

        LocalDate now = LocalDate.now();
        contributionService.calculateForHouse(house, now.getMonthValue(), now.getYear());
    }
}
