package dk.thesocialnetwork.repository;

import dk.thesocialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //@Query()
    User findByUsername(String username);
}
