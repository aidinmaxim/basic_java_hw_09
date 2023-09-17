package homeworks.homework_09;

import java.util.Scanner;

/**
 * 16.09.2023
 * basic_programming
 *
 * @author Maksym Aidin
 */
public class Homework_09 {

    private static final int[] DENOMINATIONS = {5, 10, 20, 50, 100, 200};
    private static int[] counts = {1, 0, 30, 23, 0, 1};
    private static double deposit = 0;
    private static double balance = 1945;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Банкомат");

        while (true) {

            printMenu();
            int command = getIntFromUserInput(scanner);

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
                    putMoneyOnDeposit(scanner);
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
        System.out.println("4. Открыть депозит");
        System.out.println("0. Завершить работу");
    }

    private static double getDoubleFromUserInput(Scanner scanner) {
        double result;
        while (true) {
            String input = scanner.nextLine();

            try {
                result = Double.parseDouble(input);
                return result;
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: Введите допустимое число.");
            }
        }
    }

    private static int getIntFromUserInput(Scanner scanner) {
        int result;
        while (true) {
            String input = scanner.nextLine();

            try {
                result = Integer.parseInt(input);
                return result;
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: Введите допустимое число.");
            }
        }
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.println("Получение денег");
        int requestedAmount = getIntFromUserInput(scanner);

        if (requestedAmount > balance) {
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
            int confirmTransaction = getIntFromUserInput(scanner);
            if (confirmTransaction != 1) {
                System.out.println("Транзакция отменена.");

                return;
            }

            counts = tmpCounts;
            balance -= dispensedSum;
            System.out.println(withdrawMoneyMessage);
        }

        System.out.println("--> Остаток на счету: " + balance + " EUR");
    }

    private static void putMoney(Scanner scanner) {
        System.out.println("Внесение денег");

        int money = 0;
        for (int i = 0; i < DENOMINATIONS.length; i++) {
            System.out.println("Введите количество купюр, номинала " + DENOMINATIONS[i]);
            int newBanknotes = getIntFromUserInput(scanner);
            counts[i] += newBanknotes;
            money += newBanknotes * DENOMINATIONS[i];
            System.out.println("--> Вы положили на счет " + newBanknotes + " купюр номиналом " + DENOMINATIONS[i] + " EUR");
        }
        balance += money;
    }

    private static void checkBalance() {
        System.out.println("Проверка баланса");
        System.out.println("--> Ваш баланс равен: " + balance + " EUR");
        System.out.println("--> Ваш баланс дипозитного счета: " + deposit + " EUR");
    }

    private static void exitATM() {
        System.out.println("Завершение работы");
        System.exit(0);
    }

    private static void putMoneyOnDeposit(Scanner scanner) {
        System.out.println("Введите сумму которую хотите положить на депозит");
        double moneyForDeposit = getDoubleFromUserInput(scanner);

        if (moneyForDeposit > balance) {
            System.err.println("Недостаточно денег на счете");

            return;
        }

        balance -= moneyForDeposit;
        deposit += moneyForDeposit;

        System.out.println("Введите процент на депозите (годовой):");
        double annualInterestRate = getDoubleFromUserInput(scanner);

        calculateDepositEarnings(balance, deposit, annualInterestRate);
    }

    private static void calculateDepositEarnings(double balance, double depositAmount, double annualInterestRate) {
        // проценты по сложной процентной ставке
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        System.out.printf("Месячная ставка: %.4f%%%n", monthlyInterestRate * 100);

        for (int month = 1; month <= 5; month++) {
            double interestEarned = depositAmount * monthlyInterestRate;
            depositAmount += interestEarned;
            balance += interestEarned;

            System.out.printf("Месяц %d: Начисления - %.2f Баланс - %.2f%n", month, interestEarned, balance);
        }
    }
}
