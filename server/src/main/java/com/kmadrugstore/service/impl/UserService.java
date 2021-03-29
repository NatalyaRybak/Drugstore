package com.kmadrugstore.service.impl;

import com.kmadrugstore.dto.ChangePasswordDTO;
import com.kmadrugstore.dto.RegistrationDTO;
import com.kmadrugstore.dto.UserDTO;
import com.kmadrugstore.entity.PasswordResetToken;
import com.kmadrugstore.entity.User;
import com.kmadrugstore.exception.EmailIsUsedException;
import com.kmadrugstore.exception.PasswordResetTokenInvalidException;
import com.kmadrugstore.exception.UserNotFoundException;
import com.kmadrugstore.repository.IPasswordResetTokenRepository;
import com.kmadrugstore.repository.IUserRepository;
import com.kmadrugstore.service.IEmailService;
import com.kmadrugstore.service.IUserService;
import com.kmadrugstore.utils.Constants;
import com.kmadrugstore.utils.RoleFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final IEmailService emailService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User registerAsUser(final RegistrationDTO user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailIsUsedException();
        }
        return userRepository.save(
                User.builder()
                        .email(user.getEmail())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .password(encodePassword(user.getPassword()))
                        .role(RoleFactory.user())
                        .build());
    }

    @Override
    public User getGuest(final String name, final String surname,
                         final String email) {
        Optional<User> user = userRepository.findByNameAndSurnameAndEmail(name,
                surname, email);
        return user.orElseGet(() ->
                userRepository.save(
                        new User(name, surname, email, RoleFactory.guest())));
    }

    @Override
    public UserDTO increaseBonusesBy(final User user, final int n) {
        int bonuses = user.getBonuses();
        user.setBonuses((bonuses + n));
        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getSurname(),
                user.getEmail(), user.getBonuses(), user.getRole());
    }

    @Override
    public UserDTO decreaseBonusesBy(final User user, final int n) {
        int bonuses = user.getBonuses();
        user.setBonuses((bonuses - n));
        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getSurname(),
                user.getEmail(), user.getBonuses(), user.getRole());
    }

    @Override
    public void resetPassword(final String email, final String contextPath) {
        User user = findByEmail(email);
        String tokenString = UUID.randomUUID().toString();

        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenString)
                .user(user)
                .expirationTime(LocalDateTime.now().plusMinutes(
                        Constants.PASSWORD_RESET_TOKEN_VALIDITY_IN_MINUTES))
                .build();
        passwordResetTokenRepository.save(token);

        String url = contextPath + Constants.CHANGE_PASSWORD_URL + tokenString;
        String mes = String.format(Constants.RESET_PASSWORD_EMAIL, url);
        emailService.sendMessageToEmail(email,
                Constants.RESET_PASSWORD_EMAIL_SUBJECT, mes);
    }

    @Override
    public boolean passwordResetTokenIsValid(final String token) {
        return getPasswordResetTokenInfoIfValid(token) != null;
    }

    @Override
    public void changePassword(final ChangePasswordDTO newPasswordInfo) {
        PasswordResetToken tokenInfo =
                getPasswordResetTokenInfoIfValid(newPasswordInfo.getToken());
        if (tokenInfo == null) {
            throw new PasswordResetTokenInvalidException();
        }
        User user = tokenInfo.getUser();
        user.setPassword(encodePassword(newPasswordInfo.getPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(tokenInfo);
    }

    @Override
    public void cleanupExpiredPasswordResetTokens() {
        List<PasswordResetToken> tokens = passwordResetTokenRepository.findAll();
        for (PasswordResetToken token : tokens) {
            if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
                passwordResetTokenRepository.delete(token);
            }
        }
    }




    @Override
    public UserDetails loadUserByUsername(final String email) {
        User user = findByEmail(email);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);

    }

    private PasswordResetToken getPasswordResetTokenInfoIfValid(final String token) {
        Optional<PasswordResetToken> tokenInfo =
                passwordResetTokenRepository.findByToken(token);
        if (!tokenInfo.isPresent()) {
            return null;
        }
        LocalDateTime expirationTime = tokenInfo.get().getExpirationTime();
        if (expirationTime.isAfter(LocalDateTime.now())) {
            return tokenInfo.get();
        } else {
            return null;
        }
    }

    private String encodePassword(final String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }


    /************** MAPPERS ***************/

    @Override
    public UserDTO userToUserDTO(final User user)
    {
        return UserDTO.builder().id(user.getId()).
                bonuses(user.getBonuses()).email(user.getEmail())
                .name(user.getName()).role(user.getRole()).
                        surname(user.getSurname()).build();
    }



}
