public class Television {
    // Поля (состояние телевизора)
    private String brand;   // марка телевизора
    private int channel;    // текущий канал
    private boolean isOn;   // включен ли телевизор
    private int size;       // размер телевизора

    // Конструктор
    public Television(String brand, int channel, int size) {
        this.brand = brand;
        this.channel = channel;
        this.size = size;
        this.isOn = false; // изначально телевизор выключен
    }

    // Методы (действия)
    public void turnOn() {
        isOn = true;
        System.out.println(brand + " включен.");
    }

    public void turnOff() {
        isOn = false;
        System.out.println(brand + " выключен.");
    }
    public void baySize(int newSize) {
        if (isOn) {
            size = newSize;
            System.out.println("Норм такой телик. Размер " + size + " дюймов.");
        } else {
            System.out.println("Выбери уже наконец себе телик.");
        }
    }
    public void changeChannel(int newChannel) {
        if (isOn) {
            channel = newChannel;
            System.out.println("Канал переключен на " + channel);
        } else {
            System.out.println("Телевизор выключен. Сначала включите его.");
        }
    }

    // Геттеры (свойства)
    public String getBrand() {
        return brand;
    }

    public int getChannel() {
        return channel;
    }

    public boolean isOn() {
        return isOn;
    }
    public int getSize() {
        return size;
    }
}
