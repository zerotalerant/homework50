package kg.itacademy.xakaton.controller;

import kg.itacademy.xakaton.exceptions.UserNotFoundException;
import kg.itacademy.xakaton.model.UserAuthModel;
import kg.itacademy.xakaton.model.UserModel;
import kg.itacademy.xakaton.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserController {


    final UserService userService;

    //Регистрация
    @PostMapping(path = "/create")
    public ResponseEntity<UserModel> createUser ( @RequestBody UserModel userModel )
    {
        UserModel createdUser = userService.createUser ( userModel );
        if ( createdUser.getId () != null )
        {
            return ResponseEntity.status ( HttpStatus.CREATED ).body ( createdUser );
        } else
        {
            return ResponseEntity
                    .status ( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body ( null );
        }
    }

    //Вытащить пользователя по id
    @GetMapping(path = "/get/{userId}")
    public ResponseEntity<UserModel> getUserById ( @PathVariable("userId") Long userId )
    {
        try
        {
            return ResponseEntity.ok ( userService.getUserById ( userId ) );
        } catch (UserNotFoundException ex)
        {
            log.error ( ex.getMessage (), ex );
            return ResponseEntity
                    .status ( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body ( null );
        }
    }

    //Удалить пользователя по id
    @GetMapping(path = "/delete/{userId}")
    public ResponseEntity<Boolean> deleteUserById ( @PathVariable("userId") Long userId )
    {
        try
        {
            return ResponseEntity.ok ( userService.deleteUserById ( userId ) );
        } catch (UserNotFoundException ex)
        {
            log.error ( ex.getMessage (), ex );
            return ResponseEntity
                    .status ( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body ( null );
        }
    }

    //Обновление пользователя
    @PutMapping(path = "/update")
    public ResponseEntity<Boolean> updateUser ( @RequestBody UserModel userModel )
    {
        try
        {
            userService.updateUser ( userModel );
            return ResponseEntity.ok ( true );
        } catch (UserNotFoundException ex)
        {

            log.error ( ex.getMessage (), ex );
            return ResponseEntity
                    .status ( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body ( null );
        }
    }

    //Вытащить всех пользователей
    @GetMapping(path = "/get/all-users")
    public List<UserModel> getAllUsers ()
    {
        return userService.getAllUsers ();
    }

    //Авторизация
    @PostMapping(path = "/sign-in")
    public String getAuthToken ( @Valid @RequestBody UserAuthModel userAuthDto )
    {
        return userService.getToken ( userAuthDto );
    }
}