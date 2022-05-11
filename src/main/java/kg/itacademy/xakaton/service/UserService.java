package kg.itacademy.xakaton.service;

import kg.itacademy.xakaton.model.UserAuthModel;
import kg.itacademy.xakaton.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserModel createUser ( UserModel userModel );

    Boolean deleteUserById ( Long userId );

    UserModel getUserById ( Long userId );

    String getToken ( UserAuthModel userAuthDto );

    List<UserModel> getAllUsers ();

    Boolean updateUser ( UserModel userModel );

}
