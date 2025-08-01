import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Random rand = new Random();
        Scanner input = new Scanner(System.in);
        System.out.print("Введите бренд телевизора или нажмите Enter для случайного: ");
        String userBrand = input.nextLine();

        String[] brands = {"Samsung", "LG", "Sony", "Philips"};
        for (int i = 0; i < 3; i++) {
            String brand;
            if (!userBrand.isEmpty()) {
                brand = userBrand;
            } else {
                brand = brands[rand.nextInt(brands.length)];
            }
            int channel = rand.nextInt(100) + 1;
            int size = rand.nextInt(32) + 32;

            Television tv = new Television(brand, channel, size);
            tv.turnOn();
            tv.setSize(rand.nextInt(32) + 32);
            tv.changeChannel(rand.nextInt(100) + 1);
            tv.turnOff();
        }
        input.close();
    }
}

