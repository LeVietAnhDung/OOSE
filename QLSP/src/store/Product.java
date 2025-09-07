package store;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String description;

    public Product(int id, String name, String category, double price, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    // Getter và Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getDescription() { return description; }

    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format("ID:%d | %s | Danh mục: %s | Giá: %.2f | SL: %d | %s",
                id, name, category, price, quantity, description);
    }
}
