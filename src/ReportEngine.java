import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ReportEngine {
    String[] lines;
    MonthlyReport monthReport = new MonthlyReport();
    YearlyReport yearlyReport = new YearlyReport();

    void LoadMonth(String yearNumber) {
        for (int i = 1; i <= 12; i++) {
            ArrayList<StrokaMonth> nameArrayMonth = new ArrayList();
            String monthNumber;
            try {
                if (i <= 9) {
                    monthNumber = "0" + i;
                    String put = "resources/m." + yearNumber + monthNumber + ".csv";
                    lines = (Files.readString(Path.of(put)).split("\r?\n|\r"));
                } else {
                    monthNumber = Integer.toString(i);

                    String put = "resources/m." + yearNumber + monthNumber + "csv";
                    lines = (Files.readString(Path.of(put)).split("\r?\n|\r"));

                }
            } catch (IOException e) {


                System.out.println("\nЗагружены отчеты за  " + (i - 1) + " месяц(ев)\n");
                return;
            }

            for (int k = 1; k < lines.length; k++) {
                String[] lineContents = lines[k].split(",");
                StrokaMonth strokaMonth = new StrokaMonth(lineContents[0], Boolean.parseBoolean(lineContents[1]), Integer.parseInt(lineContents[2]), Integer.parseInt(lineContents[3]));
                nameArrayMonth.add(strokaMonth);
            }
            Month month = new Month(nameArrayMonth);
            monthReport.MonthlyReport.put(i, month);
        }
        System.out.println("\nЗагружены отчеты за 12 месяц(ев)\n");
    }

    public void printMonthReport(String yearNumber) {
        System.out.println("Помесячный отчет за: " + yearNumber + " год");
        for (int i : monthReport.MonthlyReport.keySet()) {
            int maxDohod = 0;
            String maxDohodName = "";
            int maxRashod = 0;
            String maxRashodName = "";

            for (StrokaMonth j : monthReport.MonthlyReport.get(i).nameArrayMonth) {
                if (j.is_expense) {
                    if (j.value > maxDohod) {
                        maxDohod = j.value;
                        maxDohodName = j.item_name;
                    }
                } else {
                    if (j.value > maxRashod) {
                        maxRashod = j.value;
                        maxRashodName = j.item_name;
                    }
                }
            }
            System.out.println(monthToString(i));
            if (maxDohod != 0) {
                System.out.println("         Максимальный доход:" + "\n" + "    " + maxDohodName + " - " + maxDohod + "р.");
            }
            if (maxRashod != 0) {
                System.out.println("         Максимальный расход:" + "\n" + "    " + maxRashodName + " - " + maxRashod + "р.");
            }
            System.out.println("");
        }
    }


    void Loadyear(String yearNumber) {
        ArrayList<StrokaYear> strokaYearArray = new ArrayList<>();
        try {
            String put = "resources/y." + yearNumber + ".csv";
            lines = (Files.readString(Path.of(put)).split("\r?\n|\r"));
        } catch (IOException e) {
            System.out.println("Ошибка. Отчет не загружен. Возможно, файл находится не в нужной директории");
            return;
        }
        for (int k = 1; k < lines.length; k++) {
            String[] lineContents = lines[k].split(",");
            StrokaYear strokaYear = new StrokaYear(lineContents[0], Integer.parseInt(lineContents[1]), Boolean.parseBoolean(lineContents[2]));
            strokaYearArray.add(strokaYear);
        }
        Year year = new Year(strokaYearArray);
        yearlyReport.yearReport.put(Integer.parseInt(yearNumber), year);
        System.out.println("\nЗагружен отчет за: " + yearNumber + " год\n");
        System.out.println("lol");
    }


    void printYearReport(String yearNumber) {

        System.out.println("Годовой отчет за: " + yearNumber + " год");
        int month = 1;
        ArrayList<Integer> dohodi = new ArrayList<>();
        ArrayList<Integer> rashodi = new ArrayList<>();
        int allDohod = 0;
        int allRashod = 0;
        int middleDohod = 0;
        int middleRashod = 0;
        for (Year year : yearlyReport.yearReport.values()) {
            int dohod = 0;
            int rashod = 0;
            int schet = 0;
            int cash = 0;
            for (StrokaYear stroka : year.nameArrayYear) {

                if (stroka.is_expense) {
                    rashod = stroka.amount;
                    schet++;
                } else {
                    dohod = stroka.amount;
                    schet++;
                }
                if (schet % 2 == 0) {
                    dohodi.add(dohod);
                    rashodi.add(rashod);
                    cash = dohod - rashod;
                    System.out.println(monthToString(Integer.parseInt(stroka.month)) + ":");
                    System.out.println("Доход - " + dohod);
                    System.out.println("Расход - " + rashod);
                    System.out.println("Доход - " + cash + "\n");
                    month = Integer.parseInt(stroka.month) + 1;
                    schet = 0;
                }

            }
        }
        for (int i = 0; i < dohodi.size(); i++) {
            allDohod = allDohod + dohodi.get(i);
        }
        middleDohod = allDohod / dohodi.size();
        System.out.println("Средний доход за " + dohodi.size() + " месяца(ев): " + middleDohod);
        for (int i = 0; i < rashodi.size(); i++) {
            allRashod = allRashod + rashodi.get(i);
        }
        middleRashod = allRashod / rashodi.size();
        System.out.println("Средний доход за " + rashodi.size() + " месяца(ев): " + middleRashod + "\n");



/*
            if (yearReport.get(i).get(0).equals(month)) {
                if (yearReport.get(i).get(2).equals("false")) {
                    monthUpMoney = Integer.parseInt(yearReport.get(i).get(1));

                } else {
                    monthDownMoney = Integer.parseInt(yearReport.get(i).get(1));
                }

            } else {
                month = yearReport.get(i).get(0);
                if (yearReport.get(i).get(2).equals("false")) {
                    monthUpMoney = Integer.parseInt(yearReport.get(i).get(1));

                } else {
                    monthDownMoney = Integer.parseInt(yearReport.get(i).get(1));
                }

            }

            if (i % 2 == 0) {
                System.out.println("\nПрибыль за " +   monthToString(Integer.parseInt(month)) + ": " + (monthUpMoney - monthDownMoney));
                upMoney = upMoney + monthUpMoney;
                downMoney = downMoney + monthDownMoney;
                monthHowMuch++;
            }

        }
        System.out.println("\nСредний расход за " + monthHowMuch + " месяц(ев): " + (downMoney / monthHowMuch));
        System.out.println("\nСредний доход за " + monthHowMuch + " месяц(ев): " + (upMoney / monthHowMuch)+ "\n");*/
    }

    public String monthToString(Integer month) {
        switch (month) {
            case 1:
                return "Январь";
            case 2:
                return "Февраль";
            case 3:
                return "Март";
            case 4:
                return "Апрель";
            case 5:
                return "Май";
            case 6:
                return "Июнь";
            case 7:
                return "Июль";
            case 8:
                return "Август";
            case 9:
                return "Сентябрь";
            case 10:
                return "Октябрь";
            case 11:
                return "Ноябрь";
            case 12:
                return "Декабрь";
        }
        return "error";
    }


}
