import java.util.Random;

public class App {
    public static void main(String[] args) {
        Random rand = new Random();

        String[] brands = {"Samsung", "LG", "Sony", "Philips"};
        for (int i = 0; i < 3; i++) {
            String brand = brands[rand.nextInt(brands.length)];
            int channel = rand.nextInt(100) + 1;
            int size = rand.nextInt(32) + 1;

            Television tv = new Television(brand, channel, size);
            tv.turnOn();
            tv.baySize(rand.nextInt(32) + 1);
            tv.changeChannel(rand.nextInt(100) + 1);
            tv.turnOff();
        }
    }
}
