import java.util.Random;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        String[] choices = {"камень", "ножницы", "бумага"};

        System.out.println("Давайте сыграем в Камень, Ножницы, Бумага!");

        while (true) {
            System.out.println("Введите ваш выбор (камень, бумага или ножницы):");
            String playerMove = scanner.nextLine().toLowerCase();
            System.out.println("Ввели: " + playerMove);
            if (playerMove.equals("quit")) {
                break;
            }

            if (!isValidMove(playerMove, choices)) {
                System.out.println("Неверный ввод. Пожалуйста, введите камень, бумагу или ножницы.");
                continue;
            }

            int randomIndex = random.nextInt(choices.length);
            String computerMove = choices[randomIndex];

            System.out.println("Компьютер выбрал: " + computerMove);

            String result = determineWinner(playerMove, computerMove);
            System.out.println(result);
        }

        System.out.println("Спасибо за игру!");
        scanner.close();
    }

    static boolean isValidMove(String move, String[] choices) {
        for (String choice : choices) {
            if (choice.equals(move)) {
                return true;
            }
        }
        return false;
    }

    static String determineWinner(String playerMove, String computerMove) {
        if (playerMove.equals(computerMove)) {
            return "Ничья!";
        } else if ((playerMove.equals("камень") && computerMove.equals("ножницы")) ||
                   (playerMove.equals("ножницы") && computerMove.equals("бумага")) ||
                   (playerMove.equals("бумага") && computerMove.equals("камень"))) {
            return "Вы выиграли!";
        } else {
            return "Компьютер выиграл!";
        }
    }
}