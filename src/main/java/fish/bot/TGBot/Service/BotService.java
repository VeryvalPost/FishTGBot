package fish.bot.TGBot.Service;


import fish.bot.TGBot.Entity.Orders;

import fish.bot.TGBot.Entity.Payment;
import fish.bot.TGBot.Model.*;
import fish.bot.TGBot.Model.Clubstatus;
import fish.bot.TGBot.Repository.OrdersRepository;
import fish.bot.TGBot.Repository.PaymentsRepository;
import lombok.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import fish.bot.TGBot.Config.TelegramBotConfig;

import fish.bot.TGBot.Entity.Users;

import fish.bot.TGBot.Repository.UsersRepository;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


@Component
public class BotService extends TelegramLongPollingBot implements BotCommands {
    private static final Logger log = Logger.getLogger(BotService.class);
    private static final String START = "/start";
    private static final String ORDER = "/order";
    private static final String HELP = "/help";
    private static final String SHOW = "/show";
    private Emoji emoji = new Emoji();

    @Value("${spreadsheetId}")
    String spreadsheetId;

    @Value("${range}")
    String rangeSheet;


    private int messageID;


    TelegramBotConfig config;

    SheetsToSQL sheetsToSQL;

    private HashMap<Long, String> clubstatuses = new HashMap<>();


    public BotService(TelegramBotConfig config, NotionService notionService) {
        this.config = config;
        this.notionService = notionService;
    }

    @Setter
    @Getter
    String usersNickName;
    @Setter
    @Getter
    String token;
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PaymentsRepository paymentsRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    EmailService emailService;
    @Autowired
    private final NotionService notionService;


    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        Long chatId;
        Long userId;
        String inputText;


        //если получено сообщение текстом
        if (update.hasMessage()) {

            if (usersRepository.findById(update.getMessage().getChatId()).isEmpty())
                chatId = 0L;
            chatId = update.getMessage().getChatId();
            usersNickName = update.getMessage().getFrom().getUserName();


            if (usersRepository.findById(chatId).isEmpty()) {
                Users newUsers = new Users();
                newUsers.setChatId(chatId);
                newUsers.setNickname(usersNickName);
                usersRepository.saveAndFlush(newUsers);
                clubstatuses.put(chatId,"NONE");
            } else {
                Users newUsers = usersRepository.findById(chatId).get();

            }

            if (update.getMessage().hasText()) {
                inputText = update.getMessage().getText();
                messageID = update.getMessage().getMessageId();
                String clubstatus = clubstatuses.get(chatId);

                try {

                    if (inputText.startsWith("/")) {
                        botAnswerUtils(inputText, chatId, usersNickName);
                    } else if (clubstatus.equals("NAME")) {

                        setName(inputText, chatId);

                        sendMsg(chatId, "Вопрос 2 из 14. \n" +
                                "Двигаемся далее:\n" +
                                "Введи дату твоего рождения в формате \"DD/MM/YYYY\"");
                        clubstatuses.replace(chatId,"CALENDAR");

                    } else if (clubstatus.equals("CALENDAR")) {

                        String birthDateFormatted = Calendar.formatDate(inputText);
                        if (birthDateFormatted.equals("ОШИБКА")){
                            sendMsg(chatId, "Где-то ошибка, посмотри пожалуйста еще раз. Дата должна быть в формате \"DD/MM/YYYY\" " + emoji.calendar);
                        } else{
                            setBirthDate(birthDateFormatted, chatId);
                            sendMsg(chatId, "Вопрос 3 из 14. \n" +
                                    "В какой стране ты проживаешь? " + emoji.newspaper);
                            clubstatuses.replace(chatId,"COUNTRY");
                        }




                    } else if (clubstatus.equals("COUNTRY")) {

                        setCountry(inputText, chatId);
                        sendMsg(chatId, "Вопрос 4 из 14. \n" +
                                "Какими языками ты владеешь? \n"+emoji.worldmap);
                        clubstatuses.replace(chatId,"LANGUAGE");

                    } else if (clubstatus.equals("LANGUAGE")) {

                        setLanguages(inputText, chatId);

                        BotButtons digButton = new BotButtons();
                        SendMessage digital = new SendMessage();
                        digital.setChatId(chatId);
                        digital.setReplyMarkup(digButton.chooseYesOrNo());
                        digital.setText("Вопрос 5 из 14. \n" +
                                "Считаешь ли себя Digital Normad? "+ emoji.earth);
                        execute(digital);
                        clubstatuses.replace(chatId,"NORMAD");


                    } else if (clubstatus.equals("ABOUTPROJ")) {
                        setAboutProj(inputText, chatId);

                        sendMsg(chatId, "Вопрос 7 из 14. \n" +
                                "Добавь ссылку на страницу/питчдек " + emoji.newspaper);
                        clubstatuses.replace(chatId,"LINKPROG");


                    } else if (clubstatus.equals("LINKPROG")) {
                        setLinkProj(inputText, chatId);

                        sendMsg(chatId, "Вопрос 8 из 14. \n" +
                                "На какой стадии проект сейчас? " + emoji.motorway);
                        clubstatuses.replace(chatId,"STAGEPROG");


                    } else if (clubstatus.equals("STAGEPROG")) {
                        setStageProj(inputText, chatId);

                        sendMsg(chatId, "Вопрос 9 из 14. \n" +
                                "Какая твоя роль/должность в проекте? " + emoji.necktie);
                        clubstatuses.replace(chatId,"ROLEPROJ");

                    } else if (clubstatus.equals("ROLEPROJ")) {
                        setRoleProj(inputText, chatId);


                        sendMsg(chatId, "Вопрос 10 из 14. \n" +
                                "Какой сейчас у тебя запрос? Какая помощь необходима в твоем проекте? "+emoji.raisingHand+"\n" +
                                "(Чем конкретнее ты составишь запросы, тем легче нам будет найти возможности для помощи).");
                        clubstatuses.replace(chatId,"PROJHELP");

                    } else if (clubstatus.equals("PROJHELP")) {

                        setProjHelp(inputText, chatId);

                        sendMsg(chatId, "Вопрос 11 из 14. \n" +
                                "Нам очень важно объединять людей со схожими ценностями, давай чуть больше узнаем о твоих. " + emoji.heartHand+ "\n"+
                                "Чем бы ты занимался, если бы не надо было работать и платить по счетам?");
                        clubstatuses.replace(chatId,"VALUES");

                    } else if (clubstatus.equals("VALUES")) {

                        setAboutValues(inputText, chatId);

                        sendMsg(chatId, "Вопрос 12 из 14. \n" +
                                "Что или кто является твоим источником вдохновения? "+emoji.sparkles);
                        clubstatuses.replace(chatId,"INSPIRES");
                    } else if (clubstatus.equals("INSPIRES")) {

                        setAboutInspires(inputText, chatId);

                        sendMsg(chatId, "Вопрос 13 из 14.\n" +
                                "\n" +
                                "Нам важно, чтобы участники помогали друг другу. \n" +
                                "\n" +
                                "Какова твоя экспертиза? В какой сфере у тебя есть опыт и как давно? \n");


                        clubstatuses.replace(chatId,"EXPERTISE");


                    } else if (clubstatus.equals("EXPERTISE")) {

                        setExpertise(inputText, chatId);

                        sendMsg(chatId, "Вопрос 14 из 14. Мы близки к развязке! "+emoji.wink+"\n" +
                                "В чем ты разбираешься хорошо и можешь с этим помочь другим?"+emoji.muscle);

                        clubstatuses.replace(chatId,"CANHELP");

                    } else if (clubstatus.equals("CANHELP")) {

                        setCanHelp(inputText, chatId);
                        sendMsg(chatId, "Мы почти закончили! "+emoji.fire+"\n" +
                                "Пожалуйста, прикрепи сюда фото, которое хочешь видеть в своем профиле клуба Edventures" + emoji.camera);



                    }


                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            //если нажата одна из кнопок бота
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            usersNickName = update.getCallbackQuery().getFrom().getFirstName();
            messageID = update.getCallbackQuery().getMessage().getMessageId();
            inputText = update.getCallbackQuery().getData();



            try {
                botButtonUtils(inputText, chatId, usersNickName, messageID);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Обрабатываем сообщение с фотографией
            Message message = update.getMessage();
            long chatID = message.getChatId();
            List<PhotoSize> photos = message.getPhoto();

            // Выбираем самую большую фотографию (по размеру)
            PhotoSize photo = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null);

            if (photo != null) {
                // Получаем идентификатор файла фотографии
                String fileId = photo.getFileId();
                System.out.println(fileId);


                // Получаем объект файла по его fileId
                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(fileId);
                try {
                    org.telegram.telegrambots.meta.api.objects.File file = execute(getFileMethod);

                    // Получаем ссылку на файл
                    String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
                    setPhoto(fileUrl,chatID);


                    createRecordInNotion(chatID);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                clubstatuses.replace(chatID,"PHOTO");

                BotButtons codeButton = new BotButtons();
                SendMessage code = new SendMessage();
                code.setChatId(chatID);
                code.setReplyMarkup(codeButton.chooseYesOrNoCode());
                code.setText("И последнее. Самое главное. \n" +
                        emoji.exclamation+" Согласие с культурным кодом и условиями участия- обязательная часть для рассмотрения заявки. \n" +
                        "Пожалуйста, прочитай их "+emoji.arrowDown+"\n" +
                        "<a href=\"https://docs.google.com/document/d/12UgmpZaJlvu3lp9hbfmQFRFNtsSCXlQ46u3a46jIpT0\">Условия участия и культурный код</a>");
                code.setParseMode("HTML");


                try {
                    execute(code);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }


        }


    }

    @Transactional
    private void setName(String name, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setChatId(chatID);
        curUser.setName(name);
        usersRepository.saveAndFlush(curUser);

    }

    @Transactional
    private void setBirthDate(String birthdate, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        Date date = Date.valueOf(birthdate);
        curUser.setBirthDate(date);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setCountry(String country, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setCountry(country);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setLanguages(String language, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setLanguages(language);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setDigitalNorm(boolean digNorm, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setDNormad(digNorm);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setAboutProj(String aboutProj, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setAboutProj(aboutProj);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setProjHelp(String aboutHelp, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setProjHelp(aboutHelp);
        usersRepository.saveAndFlush(curUser);
    }


    @Transactional
    private void setAboutValues(String values, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setAboutValues(values);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setAboutInspires(String inspires, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setAboutInspires(inspires);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setExpertise(String expertise, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setExpertise(expertise);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setCanHelp(String canHelp, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setCanHelp(canHelp);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setAgreeCode(boolean agreeCode, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setAgreeCode(agreeCode);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setLinkProj(String link, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setLinkProj(link);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setStageProj(String stage, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setStageProj(stage);
        usersRepository.saveAndFlush(curUser);
    }

    @Transactional
    private void setRoleProj(String role, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setRoleProj(role);
        usersRepository.saveAndFlush(curUser);
    }


    @Transactional
    private void setPhoto(String photoLink, long chatID) {
        Users curUser = usersRepository.findById(chatID).get();
        curUser.setPhotoLink(photoLink);
        usersRepository.saveAndFlush(curUser);
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) throws TelegramApiException {
        switch (receivedMessage) {
            case (START):
                startCommand(chatId, userName);
                break;
            case (ORDER):
                break;
            case (HELP):
                helpCommand(chatId);
                break;
            case (SHOW):
                showCommand(chatId);
                break;
            default:
                unknownCommand(chatId);
                break;


        }
    }

    private void applyForms(long chatId) {
        SheetsToSQL sheets = new SheetsToSQL(spreadsheetId, rangeSheet);
        try {
            List<List<Object>> formsData = sheets.sheetsToSQL();

            Users user = new Users();

            List<Object> lastrow = formsData.get(formsData.size() - 1);

            Timestamp time = (Timestamp) lastrow.get(0);
            String email = (String) lastrow.get(1);
            String name = (String) lastrow.get(2);
            String foto = (String) lastrow.get(3);
            Date birthdate = (Date) lastrow.get(4);
            String tg = (String) (lastrow.get(19));


            user.setRegistrationTime(time);
            user.setEmail(email);
            user.setName(name);
            user.setPhotoLink(foto);
            user.setBirthDate(birthdate);

            usersRepository.save(user);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }


    private void createRecordInNotion(long chatID) {
        Users userDB = usersRepository.findById(chatID).get();

        Date dateSQL = userDB.getBirthDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(dateSQL);

        String nameFam = userDB.getName();
        String telegram =userDB.getNickname();
        String country = userDB.getCountry();
        String language =userDB.getLanguages();
        String aboutProj =userDB.getAboutProj();
        String stageProj =userDB.getStageProj();
        String linkProj =userDB.getLinkProj();
        String roleProj =userDB.getRoleProj();
        String helpProj =userDB.getProjHelp();
        String expertise =userDB.getExpertise();
        String canHelp =userDB.getCanHelp();
        String inspire =userDB.getAboutInspires();
        String values =userDB.getAboutValues();
        String photoLink =userDB.getPhotoLink();
        String date =formattedDate;


        notionService.createNotionRecord(nameFam,telegram, country,language, aboutProj, stageProj, linkProj, roleProj, helpProj, expertise, canHelp,  inspire, values ,photoLink ,date);
    }


    private void showCommand(long chatId) {
        sendMsg(chatId, "TO DO показать все сообщения");
    }


    @Override
    public String getBotUsername() {
        return config.getUserName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    private void startCommand(Long chatId, String userName) throws TelegramApiException {
        sendMsg(chatId, "Привет, " + userName + "!");
        chooseYourCourse(chatId);


    }

    private void chooseYourCourse(Long chatId) {

        BotButtons buttons = new BotButtons();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String text = String.format("Приглашаем тебя оформить заявку на вступление в наш клуб! " + emoji.huggingFace+ " \n " +
                "Мы собираем в нашем клубе окружение, в котором интересно развиваться вместе и безопасно ошибаться и исследовать. \n" +
                "\n" +
                "Поэтому мы с экспертами клуба внимательно изучаем каждую анкету. " +
                "Цель отбора - сохранить в нашем пространстве атмосферу доверия и ощущение \"своих\" людей, " +
                "разделяющих общие ценности и цели " +emoji.dizzy+ ". \n" +
                " \n" +
                "Чтобы нашим ребятам  было проще  узнать про тебя и твой проект, найди  5-10 мин на 14 коротких вопросов.\n" +
                " \n" +
                "Все твои ответы будут отражаться в профилях на странице клуба, открытых для всех участников.");
        message.setText(text);
        message.setReplyMarkup(buttons.chooseCourseMarkup());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMsg(chatId, text);
    }

    public void sendMsg(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        executeMsg(sendMessage);
    }


    private void botButtonUtils(String receivedMessage, long chatId, String userName, int messageID) throws ParseException, TelegramApiException {
/*
        Calendar sendCal = new Calendar();

        if (receivedMessage.startsWith("!?")) {
            int year = Integer.parseInt(receivedMessage.substring(2));
            sendCal.setYear(year);
            SendMessage yearMsg = new SendMessage();
            yearMsg.setChatId(chatId);
            yearMsg.setText("Теперь выбери месяц рождения");
            yearMsg.setReplyMarkup(sendCal.chooseMonth());
            try {
                execute(yearMsg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }

        if (receivedMessage.startsWith("?!")) {
            int month = Integer.parseInt(receivedMessage.substring(2));
            sendCal.setMonth(month);
            SendMessage monthMsg = new SendMessage();
            monthMsg.setChatId(chatId);
            monthMsg.setText("Теперь выбери день рождения");


            monthMsg.setReplyMarkup(sendCal.chooseDay());
            try {
                execute(monthMsg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }

        if (receivedMessage.startsWith("%!")) {
            String day = receivedMessage.substring(2);
            String birthday = sendCal.getYear() + "-" + sendCal.getMonth() + "-" + day;
            setBirthDate(birthday, chatId);

            sendMsg(chatId, "В какой стране ты проживаешь?");
            clubStatus = "COUNTRY";
        }

*/

        switch (receivedMessage) {

            case ("YC"):


                BotButtons buttonsYC = new BotButtons();
                SendMessage messageYC = new SendMessage();
                messageYC.setChatId(String.valueOf(chatId));
                messageYC.setText("Отлчиный выбор, можно сразу перейти к оплате, либо ознакомится с курсом подробнее:");
                messageYC.setReplyMarkup(buttonsYC.paymentOrNot("YC"));

                try {
                    execute(messageYC);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;


            case ("AI"):

                BotButtons buttonsAI = new BotButtons();
                SendMessage messageAI = new SendMessage();
                messageAI.setChatId(String.valueOf(chatId));
                messageAI.setText("Отлчиный выбор, можно сразу перейти к оплате, либо ознакомится с курсом подробнее:");
                messageAI.setReplyMarkup(buttonsAI.paymentOrNot("AI"));
                try {
                    execute(messageAI);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;


            case ("CLUB"):

                //BotButtons buttonsCLUB = new BotButtons();
                SendMessage messageCLUB = new SendMessage();
                messageCLUB.setChatId(String.valueOf(chatId));
                messageCLUB.setText("Вопрос 1 из 14. \n" +
                        " Для начала, как твои имя и фамилия?" + emoji.memo);
                clubstatuses.replace(chatId,"NAME");


                try {
                    execute(messageCLUB);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;


            case ("HAVEQUESTION"):

                SendMessage msgQuest = new SendMessage();
                msgQuest.setChatId(String.valueOf(chatId));
                msgQuest.setText("Переходим по ссылке на основной сайт");


                try {
                    execute(msgQuest);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;

            case ("PAYYC"):
                SendMessage msgPayYC = new SendMessage();
                msgPayYC.setChatId(String.valueOf(chatId));
                msgPayYC.setText("Тут должна быть Ваша оплата. Пока этот раздел в разработке: \n" +
                        "Будет создана тестовая транзакция. \n" +
                        "В базу данных будут добавлены имя и рандомная сумма от 1000 руб до 10000 руб");


                createTestTransaction(chatId, userName, Math.random() * 10000, "YC");


                try {
                    execute(msgPayYC);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;


            case ("PAYAI"):
                SendMessage msgPayAI = new SendMessage();
                msgPayAI.setChatId(String.valueOf(chatId));
                msgPayAI.setText("Тут должна быть Ваша оплата. Пока этот раздел в разработке: \n" +
                        "Будет создана тестовая транзакция. \n" +
                        "В базу данных будут добавлены имя и рандомная сумма от 1000 руб до 10000 руб");


                createTestTransaction(chatId, userName, Math.random() * 10000, "AI");


                try {
                    execute(msgPayAI);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;


            case ("CONFIRMGFORMS"):
                applyForms(chatId);
                break;

            case ("YES"):
                setDigitalNorm(true, chatId);

                sendMsg(chatId, "Вопрос 6 из 14. " +"\n" +
                        "А теперь нам очень интересно узнать про твой проект!"+emoji.fire+"\n" +
                        "\n" +
                        "Опиши его одной фразой "+emoji.dart);
                clubstatuses.replace(chatId,"ABOUTPROJ");

                break;

            case ("NO"):
                setDigitalNorm(false, chatId);

                sendMsg(chatId, "Вопрос 6 из 14. " +"\n" +
                        "А теперь нам очень интересно узнать про твой проект! "+emoji.fire+"\n" +
                        "\n" +
                        "Опиши его одной фразой\n"+emoji.dart);
                clubstatuses.replace(chatId,"ABOUTPROJ");
                break;
            case ("YESCODE"):
                setAgreeCode(true, chatId);
                SendMessage sendMessage = new SendMessage();
                sendMsg(chatId, "Ну вот и все!"+emoji.partiyngFace+"\n " +
                        "Спасибо за ответы! \n" +
                        "Мы ознакомимся с анкетой в течение 3 рабочих дней и вернемся с обратной связью "+emoji.okHand);
                clubstatuses.replace(chatId,"NONE");
                break;
            case ("NOCODE"):
                setAgreeCode(false, chatId);
                sendMsg(chatId, "В таком случае, мы не можем далее продолжить знакомство. Спасибо за ответы.");
                clubstatuses.replace(chatId,"NONE");
                break;
        }


    }

    private void createTestTransaction(long chatId, String userName, double money, String course) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Внесены данные: \n" +
                "Логин = " + userName + "\n" +
                "Сумма = " + money);
        Orders order = new Orders();
        order.setUser(usersRepository.findById(chatId).get());
        order.setCourse(course);
        ordersRepository.save(order);
        ;
        Payment payment = new Payment();
        payment.setOrders(order);
        payment.setPayQty(money);
        payment.setDatetime(Timestamp.valueOf(getCurrentDate()));
        payment.setPaymentStatus(true);
        paymentsRepository.save(payment);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        createGoogleForms(chatId);

    }

    private void createGoogleForms(long chatId) {

        BotButtons buttonsGForms = new BotButtons();
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Далее по ссылке требуется заполнить анкету. После заполнения необходимо отметить соответствующую кнопку.");
        message.setReplyMarkup(buttonsGForms.googleFormsLink());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void helpCommand(Long chatId) {
        var text = HELP_TEXT;
        sendMsg(chatId, text);
    }


    private LocalDateTime getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        return now;
    }

    private void executeMsg(SendMessage message) {
        try {
            execute(message);

        } catch (TelegramApiException e) {
            log.error("Error:" + e);
        }
    }

}


