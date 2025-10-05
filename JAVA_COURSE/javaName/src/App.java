import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите имя: ");   
        String name = input.nextLine();
        System.out.println("Привет, " + name);
    }
}
