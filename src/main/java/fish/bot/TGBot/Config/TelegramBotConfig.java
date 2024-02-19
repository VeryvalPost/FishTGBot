package fish.bot.TGBot.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;


@Configuration
@Data
@PropertySource("bot.properties")

public class TelegramBotConfig {

    @Value("${telegrambot.userName}")
    String userName;
    @Value("${telegrambot.botToken}")
    String botToken;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}