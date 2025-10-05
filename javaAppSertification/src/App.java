import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// Класс Product
class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть отрицательной");
        }
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}

// Класс Person
class Person {
    private String name;
    private int money;
    private List<Product> products;

    public Person(String name, int money) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов");
        }
        if (money < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательными");
        }
        this.name = name;
        this.money = money;
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void buyProduct(Product product) {
        if (this.money >= product.getPrice()) {
            this.money -= product.getPrice();
            this.products.add(product);
            System.out.println(name + " купил " + product.getName());
        } else {
            System.out.println(name + " не может позволить себе " + product.getName());
        }
    }

    @Override
    public String toString() {
        if (products.isEmpty()) {
            return name + " - Ничего не куплено";
        }
        StringBuilder sb = new StringBuilder(name + " - ");
        for (Product product : products) {
            sb.append(product.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()); // Убираем последнюю запятую
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return money == person.money && Objects.equals(name, person.name) && Objects.equals(products, person.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, money, products);
    }
}

// Основной класс с методом main
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Создание пользователей
        Person p1 = new Person("Павел Андреевич", 10000);
        Person p2 = new Person("Анна Петровна", 2000);
        Person p3 = new Person("Борис", 10);

        // Создание продуктов
        Product bread = new Product("Хлеб", 40);
        Product milk = new Product("Молоко", 60);
        Product cake = new Product("Торт", 1000);
        Product coffee = new Product("Кофе растворимый", 879);
        Product butter = new Product("Масло", 150);

        // Список покупателей и продуктов
        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);

        List<Product> products = new ArrayList<>();
        products.add(bread);
        products.add(milk);
        products.add(cake);
        products.add(coffee);
        products.add(butter);

        // Цикл покупок
        while (true) {
            System.out.println("Введите покупку: имя покупателя и продукт (или END для завершения)");
            String line = scanner.nextLine();
            if (line.equals("END")) {
                break;
            }

            String[] input = line.split(" - ");
            if (input.length != 2) {
                System.out.println("Некорректный ввод");
                continue;
            }

            String buyerName = input[0].trim();
            String productName = input[1].trim();

            // Находим покупателя по имени
            Person buyer = null;
            for (Person person : persons) {
                if (person.getName().equals(buyerName)) {
                    buyer = person;
                    break;
                }
            }

            if (buyer == null) {
                System.out.println("Покупатель не найден");
                continue;
            }

            // Находим продукт по названию
            Product selectedProduct = null;
            for (Product product : products) {
                if (product.getName().equals(productName)) {
                    selectedProduct = product;
                    break;
                }
            }

            if (selectedProduct == null) {
                System.out.println("Продукт не найден");
                continue;
            }

            // Покупка продукта
            buyer.buyProduct(selectedProduct);
        }

        // Печать итогов
        for (Person person : persons) {
            System.out.println(person);
        }

        scanner.close();
    }
}
