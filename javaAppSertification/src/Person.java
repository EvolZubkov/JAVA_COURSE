// Source code is decompiled from a .class file using FernFlower decompiler.
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

class Person {
   private String name;
   private int money;
   private List<Product> products;

   public Person(String var1, int var2) {
      if (var1 != null && !var1.trim().isEmpty()) {
         if (var1.length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов");
         } else if (var2 < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательными");
         } else {
            this.name = var1;
            this.money = var2;
            this.products = new ArrayList();
         }
      } else {
         throw new IllegalArgumentException("Имя не может быть пустым");
      }
   }

   public String getName() {
      return this.name;
   }

   public int getMoney() {
      return this.money;
   }

   public List<Product> getProducts() {
      return this.products;
   }

   public void buyProduct(Product var1) {
      String var10001;
      if (this.money >= var1.getPrice()) {
         this.money -= var1.getPrice();
         this.products.add(var1);
         var10001 = this.name;
         System.out.println(var10001 + " купил " + var1.getName());
      } else {
         var10001 = this.name;
         System.out.println(var10001 + " не может позволить себе " + var1.getName());
      }

   }

   public String toString() {
      if (this.products.isEmpty()) {
         return this.name + " - Ничего не куплено";
      } else {
         StringBuilder var1 = new StringBuilder(this.name + " - ");
         Iterator var2 = this.products.iterator();

         while(var2.hasNext()) {
            Product var3 = (Product)var2.next();
            var1.append(var3.getName()).append(", ");
         }

         var1.delete(var1.length() - 2, var1.length());
         return var1.toString();
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Person var2 = (Person)var1;
         return this.money == var2.money && Objects.equals(this.name, var2.name) && Objects.equals(this.products, var2.products);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.money, this.products});
   }
}
