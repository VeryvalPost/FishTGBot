package fish.bot.TGBot.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Clubstatus {
    private long chatId;
    private String status;

    public Clubstatus(long chatId, String status) {
        this.chatId = chatId;
        this.status = status;
    }
}
