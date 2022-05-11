package kg.itacademy.xakaton.repository;

import kg.itacademy.xakaton.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<UserEntity, Long> {


    @Query(value = "select l.* from users u where u.user_login = :login", nativeQuery = true)
    UserEntity getUserByLogin ( String login );
}
