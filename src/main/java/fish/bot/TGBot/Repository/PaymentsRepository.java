package fish.bot.TGBot.Repository;

import fish.bot.TGBot.Entity.Orders;
import fish.bot.TGBot.Entity.Payment;
import org.springframework.data.repository.CrudRepository;

@org.springframework.stereotype.Repository
public interface PaymentsRepository extends CrudRepository<Payment, Integer> {

}
