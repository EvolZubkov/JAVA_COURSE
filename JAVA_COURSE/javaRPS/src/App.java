
import java.util.Random;

public class App {

    public static void main(String[] args) {
        // Создание объекта Random с текущим временем в качестве начального значения
        Random random = new Random();
        
        String[] choices = {"Камень", "Ножницы", "Бумага"};

        int vasyaChoice = random.nextInt(3); // 0, 1 или 2
        int petyaChoice = random.nextInt(3);

        System.out.println("Вася выбрал: " + choices[vasyaChoice]);
        System.out.println("Петя выбрал: " + choices[petyaChoice]);
        System.out.println("Вася выбрал: " + vasyaChoice);
        System.out.println("Петя выбрал: " + petyaChoice);

        if (vasyaChoice == petyaChoice) {
            System.out.println("Ничья!");
        } else if ((vasyaChoice == 0 && petyaChoice == 1) ||
                   (vasyaChoice == 1 && petyaChoice == 2) ||
                   (vasyaChoice == 2 && petyaChoice == 0)) {
            System.out.println("Выиграл Петя!");
        } else {
            System.out.println("Выиграл Вася!");
        }
    }

}
