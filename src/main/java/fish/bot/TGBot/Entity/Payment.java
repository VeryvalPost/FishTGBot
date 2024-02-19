package fish.bot.TGBot.Entity;

import fish.bot.TGBot.Entity.Orders;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne
    @JoinColumn(name = "orderId")
    private Orders orders;

    @Column
    private double payQty;

    @Column
    private Timestamp datetime;

    @Column
    private boolean paymentStatus;
}