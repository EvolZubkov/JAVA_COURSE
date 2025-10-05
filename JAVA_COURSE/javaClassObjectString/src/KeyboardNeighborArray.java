import java.util.Scanner;

public class KeyboardNeighborArray {
    public static void main(String[] args) {
        // Замкнутая "раскладка" в виде строки
        String layout = "qwertyuiopasdfghjklzxcvbnm";  

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите строчную английскую букву: ");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.matches("[a-z]")) {
            char ch = input.charAt(0);
            int index = layout.indexOf(ch);

            if (index != -1) {
                int leftIndex = (index - 1 + layout.length()) % layout.length();
                char leftChar = layout.charAt(leftIndex);
                System.out.println("Слева стоит буква: " + leftChar);
            } else {
                System.out.println("Буква не найдена в раскладке.");
            }
        } else {
            System.out.println("Ошибка: введите одну букву от a до z.");
        }

        scanner.close();
    }
}
