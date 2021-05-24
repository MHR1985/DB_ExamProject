package dk.thesocialnetwork.repository;

import dk.thesocialnetwork.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM ACCOUNT", nativeQuery = true)
    Collection<User> findUsers();

    @Query(value = "SELECT * FROM ACCOUNT WHERE username = :username", nativeQuery = true)
    User findUserWithUsername(@Param("username") String username);
}
