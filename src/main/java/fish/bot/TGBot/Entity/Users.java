package fish.bot.TGBot.Entity;


import fish.bot.TGBot.Model.Languages;
import lombok.Data;



import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;


@Entity
@Data
@Table(name = "users")
public class Users {
    @Id
    private Long chatId;

    @Column
    private String nickname;

    @Column
    private String name;

    @Column
    private Date birthDate;

    @Column
    private String country;

    @Column
    private String languages;

    @Column
    private boolean dNormad;

    @Column
    private String aboutProj;
    @Column
    private String linkProj;
    @Column
    private String stageProj;
    @Column
    private String roleProj;

    @Column
    private String projHelp;

    @Column
    private String aboutValues;

    @Column
    private String aboutInspires;

    @Column
    private String expertise;
    @Column
    private String canHelp;

    @Column
    private String email;

    @Column
    private boolean agreeCode;

    @Column
    private boolean administrator;

    @Column
    private Timestamp registrationTime;

    @Column
    private String photoLink;
}