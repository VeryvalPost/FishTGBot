package fish.bot.TGBot.Entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @OneToOne
    @JoinColumn(name = "chatId")
    private Users user;

    @Column
    private String course;

    @Column
    private Timestamp datetime;

    @Column
    private boolean orderStatus;
}