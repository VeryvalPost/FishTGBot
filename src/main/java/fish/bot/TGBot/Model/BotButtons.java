package fish.bot.TGBot.Model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class BotButtons {

    private static final InlineKeyboardButton YC = new InlineKeyboardButton("YC");
    private static final InlineKeyboardButton AI = new InlineKeyboardButton("AI");
    private static final InlineKeyboardButton CLUB = new InlineKeyboardButton("Начать");
    private static final InlineKeyboardButton PAY = new InlineKeyboardButton("Оплатить");
    private static final InlineKeyboardButton HAVEQUESTION = new InlineKeyboardButton("Узнать подробнее");
    private static final InlineKeyboardButton GFORMS = new InlineKeyboardButton("Перейти к заполнению анкеты");
    private static final InlineKeyboardButton CONFIRMGFORMS = new InlineKeyboardButton("Анкета заполнена");

    private static final InlineKeyboardButton CONFIRM = new InlineKeyboardButton("Готово");

    public static InlineKeyboardMarkup chooseCourseMarkup() {

        YC.setCallbackData("YC");
        AI.setCallbackData("AI");
        CLUB.setCallbackData("CLUB");

        //TODO в дальнейшем реализовать функционал записи на курс
        //List<InlineKeyboardButton> row1 = List.of(YC, AI);
        List<InlineKeyboardButton> row2 = List.of(CLUB);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row2);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }


    public static InlineKeyboardMarkup paymentOrNot(String course) {


        HAVEQUESTION.setCallbackData("HAVEQUESTION");
        if (course.equals("YC")){
            HAVEQUESTION.setUrl("https://edventureshub.com/ycstartupschool");
            PAY.setCallbackData("PAYYC");
        }
        if (course.equals("AI")){
            HAVEQUESTION.setUrl("https://edventureshub.com/AI1");
            PAY.setCallbackData("PAYAI");
        }

        List<InlineKeyboardButton> row1 = List.of(PAY, HAVEQUESTION);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }


    public ReplyKeyboard googleFormsLink() {
        GFORMS.setCallbackData("GFORMS");
        GFORMS.setUrl("https://docs.google.com/forms/d/e/1FAIpQLSepL6z9nnz9Wu8PbMAvRbJkFXySsk_QVdFeptkAW1ZA1sl30g/viewform");

        CONFIRMGFORMS.setCallbackData("CONFIRMGFORMS");

        List<InlineKeyboardButton> row1 = List.of(GFORMS);
        List<InlineKeyboardButton> row2 = List.of(CONFIRMGFORMS);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1,row2);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }


    public static InlineKeyboardMarkup chooseLanguageMarkup() {

        CONFIRM.setCallbackData("DONE");

        List<InlineKeyboardButton> row1 = List.of(CONFIRM);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public ReplyKeyboard chooseYesOrNo() {
         InlineKeyboardButton yesButton = new InlineKeyboardButton("Да");
        InlineKeyboardButton noButton = new InlineKeyboardButton("Нет");
        yesButton.setCallbackData("YES");
        noButton.setCallbackData("NO");

        List<InlineKeyboardButton> row1 = List.of(yesButton, noButton);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public ReplyKeyboard chooseYesOrNoCode() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton("Согласен");
        InlineKeyboardButton noButton = new InlineKeyboardButton("Не согласен");
        yesButton.setCallbackData("YESCODE");
        noButton.setCallbackData("NOCODE");

        List<InlineKeyboardButton> row1 = List.of(yesButton, noButton);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}