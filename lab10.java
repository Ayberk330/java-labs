import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


public class Product {
    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.name  = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName()  { return name; }
    public double getPrice() { return price; }
    public int getStock()    { return stock; }

    public void setName(String name)   { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock)    { this.stock = stock; }

    public void reduceStock(int quantity) {
        this.stock -= quantity;
    }

    public void restock(int quantity) {
        this.stock += quantity;
    }

    @Override
    public String toString() {
        return String.format("%s: price=%.2f, stock=%d", name, price, stock);
    }
}
// InventoryManager.java


 class InventoryManager {
    private HashMap<String, Product> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
    }

    public void addProduct(Product product) {
        inventory.put(product.getName(), product);
    }

    public Product getProduct(String name) {
        return inventory.get(name);
    }

    public void printStock() {
        System.out.println("=== Current Inventory ===");
        for (Map.Entry<String, Product> e : inventory.entrySet()) {
            System.out.println(e.getValue());
        }
    }
}

 class Sale<T extends Product> {
    private HashMap<T, Integer> items;

    public Sale() {
        items = new HashMap<>();
    }

    public void addItem(T product, int quantity) {
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException(
                    "Not enough stock for " + product.getName()
            );
        }
        product.reduceStock(quantity);

        items.merge(product, quantity, Integer::sum);
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (Map.Entry<T, Integer> e : items.entrySet()) {
            total += e.getKey().getPrice() * e.getValue();
        }
        return total;
    }
}


class SalesManager<T extends Product> {
    private List<Sale<T>> sales;

    public SalesManager() {
        sales = new ArrayList<>();
    }

    public void recordSale(Sale<T> sale) {
        sales.add(sale);
    }

    public double getDailyTotal() {
        return sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();
    }

    public void printSummary() {
        System.out.println("=== Daily Sales Summary ===");
        System.out.println("Number of sales: " + sales.size());
        System.out.printf("Total revenue: %.2f%n", getDailyTotal());
    }
}
class Test {
    public static void main(String[] args) {
        InventoryManager inventory = new InventoryManager();

        // add some products
        Product apple  = new Product("Apple",  0.50, 100);
        Product orange = new Product("Orange", 0.75,  80);
        Product milk   = new Product("Milk",   1.25,  50);

        inventory.addProduct(apple);
        inventory.addProduct(orange);
        inventory.addProduct(milk);

        inventory.printStock();

        // create a sale
        Sale<Product> sale1 = new Sale<>();
        sale1.addItem(apple,  10);
        sale1.addItem(milk,    2);

        // record it
        SalesManager<Product> sm = new SalesManager<>();
        sm.recordSale(sale1);

        // another sale
        Sale<Product> sale2 = new Sale<>();
        sale2.addItem(orange, 5);
        sm.recordSale(sale2);

        // print remaining stock and daily summary
        System.out.println();
        inventory.printStock();

        System.out.println();
        sm.printSummary();
    }
}
