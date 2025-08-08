import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return price == product.price && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}

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
        if (this.money < product.getPrice()) {
            System.out.println(name + " не может позволить себе " + product.getName());
        } else {
            this.money -= product.getPrice();
            this.products.add(product);
            System.out.println(name + " купил " + product.getName());
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
}

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Списки покупателей и продуктов
        List<Person> persons = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        // Ввод данных о покупателях (одна строка)
        System.out.println("Введите данные о покупателях (например: 'Павел Андреевич = 10000; Анна Петровна = 2000; Борис = 10'): ");
        String personsData = scanner.nextLine();
        String[] personsArray = personsData.split(";");

        // Разбор строки и создание объектов Person
        for (String personData : personsArray) {
            personData = personData.trim();
            String[] parts = personData.split("=");

            if (parts.length == 2) {
                try {
                    String name = parts[0].trim();
                    int money = Integer.parseInt(parts[1].trim());
                    Person person = new Person(name, money);
                    persons.add(person);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка ввода для покупателя: " + personData);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // Ввод данных о продуктах (одна строка)
        System.out.println("Введите данные о продуктах (например: 'Хлеб = 40; Молоко = 60; Торт = 1000'): ");
        String productsData = scanner.nextLine();
        String[] productsArray = productsData.split(";");

        // Разбор строки и создание объектов Product
        for (String productData : productsArray) {
            productData = productData.trim();
            String[] parts = productData.split("=");

            if (parts.length == 2) {
                try {
                    String productName = parts[0].trim();
                    int productPrice = Integer.parseInt(parts[1].trim());
                    Product product = new Product(productName, productPrice);
                    products.add(product);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка ввода для продукта: " + productData);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // Цикл, в котором покупатели покупают продукты по очереди
        while (true) {
            System.out.println("\nВведите покупку в формате 'Имя покупателя - Продукт' или 'END' для завершения:");
            String purchaseData = scanner.nextLine().trim();

            if (purchaseData.equals("END")) {
                break;
            }

            // Разделение строки на имя покупателя и продукт
            String[] purchaseParts = purchaseData.split("-");
            if (purchaseParts.length != 2) {
                System.out.println("Ошибка ввода. Используйте формат 'Имя покупателя - Продукт'.");
                continue;
            }

            String personName = purchaseParts[0].trim();
            String productName = purchaseParts[1].trim();

            // Поиск покупателя
            Person person = null;
            for (Person p : persons) {
                if (p.getName().trim().equalsIgnoreCase(personName.trim())) { // Используем trim()
                    person = p;
                    break;
                }
            }

            if (person == null) {
                System.out.println("Покупатель с таким именем не найден!");
                continue;
            }

            // Поиск продукта
            Product selectedProduct = null;
            for (Product p : products) {
                if (p.getName().trim().equalsIgnoreCase(productName.trim())) { // Используем trim()
                    selectedProduct = p;
                    break;
                }
            }

            if (selectedProduct == null) {
                System.out.println("Продукт с таким названием не найден!");
                continue;
            }

            // Попытка купить продукт
            person.buyProduct(selectedProduct);
        }

        // Печать итогов покупок
        System.out.println("\nИтоги покупок:");
        for (Person person : persons) {
            System.out.println(person);
        }

        scanner.close();
    }
}
