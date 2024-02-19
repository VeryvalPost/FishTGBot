package fish.bot.TGBot.Config;

import fish.bot.TGBot.Model.BotCommands;
import fish.bot.TGBot.Service.BotService;
import org.apache.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotConnect {



    private static final Logger log = Logger.getLogger(BotService.class);


    BotService botService;
    private BotConnect(BotService botService){
        this.botService = botService;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        final int RECONNECT_PAUSE = 10000;
        try {

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(botService);
            log.info("TelegramAPI started. Look for messages");
            botService.execute(new SetMyCommands(BotCommands.LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));

        } catch (TelegramApiRequestException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
