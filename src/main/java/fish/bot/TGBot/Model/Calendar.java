package fish.bot.TGBot.Model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class Calendar {

    private static int year;

    private static int month;

static int curYear = LocalDateTime.now().getYear();

    private static final InlineKeyboardButton[] yearButton = new InlineKeyboardButton[50];
    private static final InlineKeyboardButton[] monthButton = new InlineKeyboardButton[12];
    private static final InlineKeyboardButton[] dayButton = new InlineKeyboardButton[31];

    public static String formatDate(String inputText) {
        String formattedDate;
        String[] splitDate = inputText.split("/");
        try {
            if (splitDate[0].length() > 2 || splitDate[1].length() > 2 || splitDate[2].length() != 4) {
                formattedDate = "ОШИБКА";
                return formattedDate;
            } else if ((Integer.parseInt(splitDate[2]) > curYear) || (Integer.parseInt(splitDate[1]) > 12)) {
                formattedDate = "ОШИБКА";
                return formattedDate;
            }
            formattedDate = splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
            return formattedDate;
        }catch (NumberFormatException e){
            formattedDate = "ОШИБКА";
            return formattedDate;
        }

    }
/*
    public InlineKeyboardMarkup chooseYear() {
        for (int i=0; i<50; i++){
            String text = String.valueOf(curYear-17-i);
            String callback = "!?"+text;
            yearButton[i] = new InlineKeyboardButton(text);
            yearButton[i].setCallbackData(callback);
        }
        List<InlineKeyboardButton> row1 = List.of(yearButton[0], yearButton[1], yearButton[2],yearButton[3], yearButton[4]);
        List<InlineKeyboardButton> row2 = List.of(yearButton[5], yearButton[6], yearButton[7],yearButton[8], yearButton[9]);
        List<InlineKeyboardButton> row3 = List.of(yearButton[10], yearButton[11], yearButton[12],yearButton[13], yearButton[14]);
        List<InlineKeyboardButton> row4 = List.of(yearButton[15], yearButton[16], yearButton[17],yearButton[18], yearButton[19]);
        List<InlineKeyboardButton> row5 = List.of(yearButton[20], yearButton[21], yearButton[22],yearButton[23], yearButton[24]);
        List<InlineKeyboardButton> row6 = List.of(yearButton[25], yearButton[26], yearButton[27],yearButton[28], yearButton[29]);
        List<InlineKeyboardButton> row7 = List.of(yearButton[30], yearButton[31], yearButton[32],yearButton[33], yearButton[34]);
        List<InlineKeyboardButton> row8 = List.of(yearButton[35], yearButton[36], yearButton[37],yearButton[38], yearButton[39]);
        List<InlineKeyboardButton> row9 = List.of(yearButton[40], yearButton[41], yearButton[42],yearButton[43], yearButton[44]);
        List<InlineKeyboardButton> row10 = List.of(yearButton[45], yearButton[46], yearButton[47],yearButton[48], yearButton[49]);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4,row5, row6,row7,row8,row9,row10);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;


    }

    public  InlineKeyboardMarkup chooseMonth() {
        monthButton[0] = new InlineKeyboardButton("Январь");
        monthButton[1] = new InlineKeyboardButton("Февраль");
        monthButton[2] = new InlineKeyboardButton("Март");
        monthButton[3] = new InlineKeyboardButton("Апрель");
        monthButton[4] = new InlineKeyboardButton("Май");
        monthButton[5] = new InlineKeyboardButton("Июнь");
        monthButton[6] = new InlineKeyboardButton("Июль");
        monthButton[7] = new InlineKeyboardButton("Август");
        monthButton[8] = new InlineKeyboardButton("Сентябрь");
        monthButton[9] = new InlineKeyboardButton("Октябрь");
        monthButton[10] = new InlineKeyboardButton("Ноябрь");
        monthButton[11] = new InlineKeyboardButton("Декабрь");

        monthButton[0].setCallbackData("?!01");
        monthButton[1].setCallbackData("?!02");
        monthButton[2].setCallbackData("?!03");
        monthButton[3].setCallbackData("?!04");
        monthButton[4].setCallbackData("?!05");
        monthButton[5].setCallbackData("?!06");
        monthButton[6].setCallbackData("?!07");
        monthButton[7].setCallbackData("?!08");
        monthButton[8].setCallbackData("?!09");
        monthButton[9].setCallbackData("?!10");
        monthButton[10].setCallbackData("?!11");
        monthButton[11].setCallbackData("?!12");


        List<InlineKeyboardButton> row1 = List.of(monthButton[0], monthButton[1], monthButton[2]);
        List<InlineKeyboardButton> row2 = List.of(monthButton[3], monthButton[4], monthButton[5]);
        List<InlineKeyboardButton> row3 = List.of(monthButton[6], monthButton[7], monthButton[8]);
        List<InlineKeyboardButton> row4 = List.of(monthButton[9], monthButton[10], monthButton[11]);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public InlineKeyboardMarkup chooseDay() {
      for (int i = 1; i<=31; i++){
          dayButton[i-1] = new InlineKeyboardButton(String.valueOf(i));
          dayButton[i-1].setCallbackData("%!" + i);
      }


        if ((month==1)||(month==3)||(month==5)||(month==7)||(month==8)||(month==10)||(month==12)) {
            List<InlineKeyboardButton> row1 = List.of(dayButton[0], dayButton[1], dayButton[2], dayButton[3], dayButton[4], dayButton[5],dayButton[6],dayButton[7]);
            List<InlineKeyboardButton> row2 = List.of(dayButton[8], dayButton[9], dayButton[10], dayButton[11], dayButton[12],dayButton[13], dayButton[14], dayButton[15]);
            List<InlineKeyboardButton> row3 = List.of(dayButton[16], dayButton[17], dayButton[18], dayButton[19],dayButton[20],dayButton[21], dayButton[22], dayButton[23]);
            List<InlineKeyboardButton> row4 = List.of( dayButton[24], dayButton[25], dayButton[26],dayButton[27],dayButton[28], dayButton[29], dayButton[30]);

            List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard(rowsInLine);
            return markupInline;
        } else if ((month==4)||(month==6)||(month==9)||(month==11)){
            List<InlineKeyboardButton> row1 = List.of(dayButton[0], dayButton[1], dayButton[2], dayButton[3], dayButton[4], dayButton[5],dayButton[6],dayButton[7]);
            List<InlineKeyboardButton> row2 = List.of(dayButton[8], dayButton[9], dayButton[10], dayButton[11], dayButton[12],dayButton[13], dayButton[14], dayButton[15]);
            List<InlineKeyboardButton> row3 = List.of(dayButton[16], dayButton[17], dayButton[18], dayButton[19],dayButton[20],dayButton[21], dayButton[22], dayButton[23]);
            List<InlineKeyboardButton> row4 = List.of( dayButton[24], dayButton[25], dayButton[26],dayButton[27],dayButton[28], dayButton[29]);

            List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard(rowsInLine);
            return markupInline;
        } else if (!(isLeapYear(year))&&(month==2)) {
            List<InlineKeyboardButton> row1 = List.of(dayButton[0], dayButton[1], dayButton[2], dayButton[3], dayButton[4], dayButton[5],dayButton[6],dayButton[7]);
            List<InlineKeyboardButton> row2 = List.of(dayButton[8], dayButton[9], dayButton[10], dayButton[11], dayButton[12],dayButton[13], dayButton[14], dayButton[15]);
            List<InlineKeyboardButton> row3 = List.of(dayButton[16], dayButton[17], dayButton[18], dayButton[19],dayButton[20],dayButton[21], dayButton[22], dayButton[23]);
            List<InlineKeyboardButton> row4 = List.of( dayButton[24], dayButton[25], dayButton[26],dayButton[27]);

            List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard(rowsInLine);
            return markupInline;
        } else {
            List<InlineKeyboardButton> row1 = List.of(dayButton[0], dayButton[1], dayButton[2], dayButton[3], dayButton[4], dayButton[5],dayButton[6],dayButton[7]);
            List<InlineKeyboardButton> row2 = List.of(dayButton[8], dayButton[9], dayButton[10], dayButton[11], dayButton[12],dayButton[13], dayButton[14], dayButton[15]);
            List<InlineKeyboardButton> row3 = List.of(dayButton[16], dayButton[17], dayButton[18], dayButton[19],dayButton[20],dayButton[21], dayButton[22], dayButton[23]);
            List<InlineKeyboardButton> row4 = List.of( dayButton[24], dayButton[25], dayButton[26],dayButton[27], dayButton[28]);

            List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4);
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard(rowsInLine);
            return markupInline;
        }




    }


    public static boolean isLeapYear(int year) {
        // Год является високосным, если он делится на 4 без остатка
        // Но не является високосным, если он также делится на 100 без остатка,
        // за исключением тех случаев, когда год делится на 400 без остатка
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
    }

    public static int getYear() {
        return year;
    }

    public static void setYear(int year) {
        Calendar.year = year;
    }

    public static int getMonth() {
        return month;
    }

    public static void setMonth(int month) {
        Calendar.month = month;
    }

*/


}