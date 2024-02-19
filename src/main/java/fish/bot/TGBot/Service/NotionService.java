package fish.bot.TGBot.Service;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Service
public class NotionService {

    @Value("${notion.api.token}")
    private String notionApiToken;

    @Value("${notion.api.databaseId}")
    private String notionDatabaseId;

    private final RestTemplate restTemplate;

    public NotionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createNotionRecord(
            String nameFam,
            String telegram,
            String country,
            String language,
            String aboutProj,
            String stageProj,
            String linkProj,
            String roleProj,
            String helpProj,
            String expertise,
            String canHelp,
            String inspire,
            String values,
            String photoLink,
            String date
    ) {


        String jsonBody = "{\n" +
                "  \"parent\": {\n" +
                "    \"database_id\": \"" + notionDatabaseId + "\"\n" +
                "  },\n" +
                "  \"properties\": {\n" +
                "    \"Имя/Фамилия\": {\n" +
                "      \"title\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + nameFam + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Telegram\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + telegram + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Страна проживания\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + country + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Языки\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + language + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"О проекте\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + aboutProj + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Стадия проекта\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + stageProj + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Ссылка на проект\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + linkProj + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Должность\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + roleProj + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Нужна помощь\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + helpProj + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Экспертиза\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + expertise + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Могу помочь\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + canHelp + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Что вдохновляет?\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + inspire + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Чем бы занимался, если бы не работать и платить по счетам\": {\n" +
                "      \"rich_text\": [\n" +
                "        {\n" +
                "          \"text\": {\n" +
                "            \"content\": \"" + values + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Photo\": {\n" +
                "      \"url\": \"" + photoLink + "\"\n" +
                "    },\n" +
                "    \"Digital Nomad?\": {\n" +
                "      \"select\": {\n" +
                "        \"name\": \"Нет\",\n" +
                "        \"color\": \"red\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"Дата рождения\": {\n" +
                "      \"date\": {\n" +
                "        \"start\": \"" + date + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(notionApiToken);
        headers.set("Notion-Version", "2022-06-28");

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);


        // Замените URL на ваш URL Notion API
        String apiUrl = "https://api.notion.com/v1/pages";
        // Логирование перед отправкой запроса
        System.out.println("Sending request to: " + apiUrl);
        System.out.println("Request body: " + jsonBody);
        //    restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        // Отправка запроса и получение ответа
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Получение тела ответа
        String responseBody = responseEntity.getBody();
        String[] splitBody = responseBody.split("\"");
        String page = splitBody[7];
        System.out.println(responseBody);
        System.out.println(page);


        String jsonBodyPage = "{\n" +
                "    \"cover\": {\n" +
                "        \"type\": \"external\",\n" +
                "        \"external\": {\n" +
                "            \"url\": \"" + photoLink + "\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
 /*       HttpEntity<String> requestEntityPage = new HttpEntity<>(jsonBodyPage, headers);
        headers.set("X-HTTP-Method-Override", "PATCH");

        String apiUrlPage = "https://api.notion.com/v1/pages/"+page;
        ResponseEntity<String> responseEntityPage = restTemplate.exchange(apiUrlPage, HttpMethod.PATCH, requestEntityPage, String.class);
        //restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        System.out.println("Response body: " + responseBody);

*/
        // restTemplate некорретно выполняет PATCH в notion, поэтому пришлось создать заплатку в виде Apache



            HttpClient httpClient = HttpClients.createDefault();

// Создание HttpPatch с URL-адресом вашего запроса
            String apiUrlPage = "https://api.notion.com/v1/pages/" + page;
            HttpPatch httpPatch = new HttpPatch(apiUrlPage);


            // Установка заголовков
            httpPatch.addHeader("Content-Type", "application/json");
            httpPatch.addHeader("Authorization", "Bearer " + notionApiToken);
            httpPatch.addHeader("Notion-Version", "2022-06-28");
        try {
            // Установка тела запроса
            StringEntity stringEntity = new StringEntity(jsonBodyPage);
            httpPatch.setEntity(stringEntity);

            // Отправка запроса и получение ответа
            HttpResponse response = httpClient.execute(httpPatch);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}