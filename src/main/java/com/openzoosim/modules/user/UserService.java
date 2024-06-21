package com.openzoosim.modules.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository _userRepository;

    public UserEntity GetUserByID(Long id) {
        return _userRepository.findById(id);
    }

    public UserEntity GetUserByEmail(String email) {
        return _userRepository.findByEmail(email.toLowerCase());
    }

    public UserEntity CreateNewUser(String email, String password, String name) {
        UserEntity user = new UserEntity();
        user.Email = email.toLowerCase();
        user.Password = password;
        user.Name = name;
        _userRepository.persist(user);
        return user;
    }

}
