package store;

import java.util.*;

public class ProductManager {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public void updateProduct(int id, double newPrice, String newDescription) {
        for (Product p : products) {
            if (p.getId() == id) {
                p.setPrice(newPrice);
                p.setDescription(newDescription);
                System.out.println("Cập nhật thành công!");
                return;
            }
        }
        System.out.println("Không tìm thấy sản phẩm!");
    }

    public void showByCategory(String category) {
        products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .forEach(System.out::println);
    }

    public void showByPrice() {
        products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .forEach(System.out::println);
    }

    public void totalByCategory(String category) {
        double total = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Product::getTotalValue)
                .sum();
        System.out.println("Tổng giá trị tồn kho danh mục " + category + ": " + total);
    }

    public void applyDiscount(int id, double percent) {
        for (Product p : products) {
            if (p.getId() == id) {
                double newPrice = p.getPrice() * (1 - percent / 100);
                p.setPrice(newPrice);
                System.out.println("Áp dụng giảm giá thành công!");
                return;
            }
        }
        System.out.println("Không tìm thấy sản phẩm!");
    }

    public List<Product> getProducts() {
        return products;
    }

    public void showAll() {
        products.forEach(System.out::println);
    }
}
