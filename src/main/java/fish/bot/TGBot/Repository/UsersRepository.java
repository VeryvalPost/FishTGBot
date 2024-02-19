package fish.bot.TGBot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import fish.bot.TGBot.Entity.Users;

@org.springframework.stereotype.Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
}

