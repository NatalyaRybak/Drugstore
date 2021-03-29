package com.kmadrugstore.service;

import com.kmadrugstore.dto.ChangePasswordDTO;
import com.kmadrugstore.dto.RegistrationDTO;
import com.kmadrugstore.dto.UserDTO;
import com.kmadrugstore.entity.User;

public interface IUserService {

    User findByEmail(final String email);

    User findById(final int id);

    User registerAsUser(final RegistrationDTO user);

    User getGuest(final String name, final String surname, final String email);

    UserDTO increaseBonusesBy(final User user, final int n);

    UserDTO decreaseBonusesBy(final User user, final int n);

    void resetPassword(final String email, final String contextPath);

    boolean passwordResetTokenIsValid(final String token);

    void changePassword(final ChangePasswordDTO newPasswordInfo);

    UserDTO userToUserDTO(final User user);

    void cleanupExpiredPasswordResetTokens();
}
