package fish.bot.TGBot.Repository;

import fish.bot.TGBot.Entity.Orders;
import org.springframework.data.repository.CrudRepository;
@org.springframework.stereotype.Repository
public interface OrdersRepository extends CrudRepository<Orders, Integer> {
}
