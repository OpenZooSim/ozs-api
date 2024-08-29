package com.openzoosim.services.users;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.models.interfaces.IUserService;
import com.openzoosim.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService implements IUserService {

    @Inject
    UserRepository _userRepository;

    public UserEntity getUserByID(Long id) {
        return _userRepository.findById(id);
    }

    public UserEntity getUserByEmail(String email) {
        return _userRepository.findByEmail(email.toLowerCase());
    }

    public UserEntity createNewUser(String email, String password, String name) {
        UserEntity user = new UserEntity();
        user.Email = email.toLowerCase();
        user.PasswordHash = password;
        user.Username = name;
        _userRepository.persist(user);
        return user;
    }

    public void updateUser(UserEntity user) {
        _userRepository.persist(user);
    }

}
