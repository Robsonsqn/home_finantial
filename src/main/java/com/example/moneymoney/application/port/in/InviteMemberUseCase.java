package com.example.moneymoney.application.port.in;

import com.example.moneymoney.domain.model.User;

public interface InviteMemberUseCase {
    void inviteMember(Long houseId, String emailInvited, User requester);
}
