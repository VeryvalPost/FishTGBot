package fish.bot.TGBot.Model;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "Начать общение"),
            new BotCommand("/help", "Показать помощь")
          //  new BotCommand("/order", "Создать новую заявку")
         //   new BotCommand("/show", "Показать существующие заявки")
    );



    String HELP_TEXT = "Добрый день! Вас приветствует EdVentures HUB \n" +
            "Вы можете указать свои контактные данные и оставить заявку на обучение.\n" +
            "Наш сотрудник обязательно обработает запрос, свяжется с Вами и обязательно ответит на все интересующие вопросы.\n" +
            "Следующие команды помогут:\n" +
            "/start - начать наше общение заново\n" +
            "/order - сформировать новую заявку";
}