package account.Repository;

import account.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String username);
    User findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);



}
