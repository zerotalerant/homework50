package kg.itacademy.xakaton.service.impl;

import kg.itacademy.xakaton.entity.UserEntity;
import kg.itacademy.xakaton.exceptions.IdIsNullException;
import kg.itacademy.xakaton.exceptions.IdNotFoundException;
import kg.itacademy.xakaton.exceptions.UserNotFoundException;
import kg.itacademy.xakaton.exceptions.UserSignInException;
import kg.itacademy.xakaton.model.UserAuthModel;
import kg.itacademy.xakaton.model.UserModel;
import kg.itacademy.xakaton.repository.UserRepository;
import kg.itacademy.xakaton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserModel createUser ( UserModel userModel )
    {
        if ( userModel == null )
        {
            throw new RuntimeException ( "userModel is null" );
        } else if ( userModel.getEmail () == null || userModel.getLogin () == null || userModel.getPassword () == null )
        {
            throw new RuntimeException ( "Pass/Log/Email can't be null" );
        }
        UserEntity user = new UserEntity ();
        user.setLogin ( userModel.getLogin () );
        user.setPassword ( userModel.getPassword () );
        user.setEmail ( userModel.getEmail () );

        user = userRepository.save ( user );

        userModel.setId ( user.getId () );

        return userModel;
    }

    @Override
    public Boolean deleteUserById ( Long userId )
    {
        if ( !userRepository.existsById ( userId ) )
        {
            throw new UserNotFoundException ( "User not found by id: " + userId );
        }

        userRepository.deleteById ( userId );

        return true;
    }

    @Override
    public UserModel getUserById ( Long userId )
    {
        if ( userId == null )
        {
            throw new IdIsNullException ( "Id is null" );
        }

        UserEntity existEntity = userRepository.getById ( userId );

        if ( existEntity == null )
        {
            throw new IdNotFoundException ( " User not found by id: " + userId );
        }

        UserModel existModel = new UserModel ();
        existModel.setId ( existEntity.getId () );
        existModel.setLogin ( existEntity.getLogin () );
        existModel.setPassword ( existEntity.getPassword () );
        existModel.setEmail ( existEntity.getEmail () );

        return existModel;
    }


    @Override
    public String getToken ( UserAuthModel userAuthDto )
    {
        UserEntity user = userRepository
                .getUserByLogin ( userAuthDto.getLogin () );
        if ( user == null )
        {
            throw new UsernameNotFoundException ( "Username not found" );
        }
        boolean isMatches = userAuthDto.getPassword ().equals ( user.getPassword () );
        if ( isMatches )
        {
            return "Basic " + new String ( Base64.getEncoder ()
                    .encode ( ( user.getLogin () + ":" + userAuthDto.getPassword () ).getBytes () ) );
        } else
        {
            throw new UserSignInException ( "Wrong password or login!", HttpStatus.UNAUTHORIZED );
        }
    }

    @Override
    public List<UserModel> getAllUsers ()
    {
        List<UserEntity> userEntityList = userRepository.findAll ();
        List<UserModel> userModelList = new ArrayList<> ();
        for (UserEntity user : userEntityList)
        {
            UserModel userModel = new UserModel ();
            userModel.setId ( user.getId () );
            userModel.setLogin ( user.getLogin () );
            userModel.setPassword ( user.getPassword () );
            userModel.setEmail ( user.getEmail () );
            userModelList.add ( userModel );
        }
        return userModelList;
    }


    @Override
    public Boolean updateUser ( UserModel userModel )
    {
        if ( userModel == null )
        {
            throw new RuntimeException ( "User model is  null" );
        } else if ( userModel.getId () == null || userModel.getId ().equals ( "" ) )
        {
            throw new RuntimeException ( "Id can't be null" );
        } else if ( userModel.getLogin () == null || userModel.getLogin ().equals ( "" ) )
        {
            throw new RuntimeException ( "Login can't be null" );
        } else if ( userModel.getPassword () == null || userModel.getPassword ().equals ( "" ) )
        {
            throw new RuntimeException ( "Password can't be null" );
        } else if ( userModel.getEmail () == null || userModel.getEmail ().equals ( "" ) )
        {
            throw new RuntimeException ( "Email can't be null" );
        }

        UserEntity existUser = userRepository.getById ( userModel.getId () );
        if ( existUser == null )
        {
            throw new RuntimeException ( "User by id: " + userModel.getId () + " doesn't exist" );
        }

        existUser.setLogin ( userModel.getLogin () );
        existUser.setPassword ( userModel.getPassword () );
        existUser.setEmail ( userModel.getEmail () );

        existUser = userRepository.save ( existUser );

        return true;
    }
}

