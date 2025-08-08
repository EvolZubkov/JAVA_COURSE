// Source code is decompiled from a .class file using FernFlower decompiler.
import java.util.Objects;

class Product {
   private String name;
   private int price;

   public Product(String var1, int var2) {
      if (var1 != null && !var1.trim().isEmpty()) {
         if (var2 < 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть отрицательной");
         } else {
            this.name = var1;
            this.price = var2;
         }
      } else {
         throw new IllegalArgumentException("Название продукта не может быть пустым");
      }
   }

   public String getName() {
      return this.name;
   }

   public int getPrice() {
      return this.price;
   }

   public String toString() {
      return this.name;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Product var2 = (Product)var1;
         return this.price == var2.price && Objects.equals(this.name, var2.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.price});
   }
}
