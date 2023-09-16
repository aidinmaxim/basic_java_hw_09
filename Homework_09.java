package homeworks.homework_09;

import java.util.HashMap;
import java.util.Scanner;

/**
 * 16.09.2023
 * basic_programming
 *
 * @author Maksym Aidin
 */
public class Homework_09 {

    private static final int[] DENOMINATIONS = {5, 10, 20, 50, 100, 200};
    //    private static int[] counts = {0, 0, 0, 0, 0, 0};
    private static int[] counts = {1, 0, 30, 23, 0, 1}; // Sum: 1955
    private static int deposit = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Банкомат");

        while (true) {

            printMenu();
            int command = getUserInput(scanner);

            switch (command) {
                case 1:
                    withdrawMoney(scanner);
                    break;

                case 2:
                    putMoney(scanner);
                    break;

                case 3:
                    checkBalance();
                    break;

                case 4:
                    putMoneyOnDeposit();
                    break;

                case 0:
                    exitATM();
                    break;

                default:
                    System.err.println("Команда не распознана");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Меню:");
        System.out.println("1. Получить деньги");
        System.out.println("2. Внести деньги");
        System.out.println("3. Проверить баланс");
        System.out.println("0. Завершить работу");
    }

    private static int getUserInput(Scanner scanner) {

        while (true) {
            String userInput = scanner.next();

            if (!userInput.matches("\\d+") || Integer.parseInt(userInput) < 0) {
                System.err.println("Недопустимый ввод. Повторите попытку.");
            } else {
                return Integer.parseInt(userInput);
            }
        }
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.println("Получение денег");
        int requestedAmount = getUserInput(scanner);
        int sum = calculateBalance();

        if (requestedAmount > sum) {
            System.err.println("Недостаточно денег на счете");
            return;
        }

        if (requestedAmount % 5 != 0) {
            System.err.println("Запрошенная сумма не кратна 5");
            return;
        }

        int dispensedSum = 0;

        StringBuilder withdrawMoneyMessage = new StringBuilder();
        int[] tmpCounts = counts.clone();
        for (int i = DENOMINATIONS.length - 1; i >= 0; i--) {
            int denomination = DENOMINATIONS[i];
            int count = counts[i];

            int countNotesToDispense = Math.min(requestedAmount / denomination, count);
            requestedAmount -= countNotesToDispense * denomination;
            tmpCounts[i] -= countNotesToDispense;

            if (countNotesToDispense > 0) {
                withdrawMoneyMessage.append("Выдаем ").append(denomination).append("-ками ").append(countNotesToDispense).append(" осталось выдать - ").append(requestedAmount).append("\n");
                dispensedSum += countNotesToDispense * denomination;
            }

            if (requestedAmount == 0) {
                System.out.println(withdrawMoneyMessage);
                break;
            }
        }

        if (requestedAmount > 0) {
            System.out.println("Не удается выдать запрошенную сумму. Желаете снять: " + dispensedSum + " EUR?");

            System.out.println("Продолжить? (1 - Да, 0 - Отменить)");
            int confirmTransaction = getUserInput(scanner);
            if (confirmTransaction != 1) {
                System.out.println("Транзакция отменена.");

                return;
            }

            counts = tmpCounts;
            System.out.println(withdrawMoneyMessage);
        }

        System.out.println("--> Остаток на счету: " + calculateBalance() + " EUR");
    }

    private static void putMoney(Scanner scanner) {
        System.out.println("Внесение денег");

        for (int i = 0; i < DENOMINATIONS.length; i++) {
            System.out.println("Введите количество купюр, номинала " + DENOMINATIONS[i]);
            int newBanknotes = getUserInput(scanner);
            counts[i] += newBanknotes;
            System.out.println("--> Вы положили на счет " + newBanknotes + " купюр номиналом " + DENOMINATIONS[i] + " EUR");
        }
    }

    private static void checkBalance() {
        System.out.println("Проверка баланса");
        System.out.println("--> Ваш баланс равен: " + calculateBalance() + " EUR");
    }

    private static void exitATM() {
        System.out.println("Завершение работы");
        System.exit(0);
    }

    private static int calculateBalance() {
        int balance = 0;
        for (int i = 0; i < DENOMINATIONS.length; i++) {
            balance += DENOMINATIONS[i] * counts[i];
        }

        return balance;
    }

    private static void putMoneyOnDeposit() {

    }
}
