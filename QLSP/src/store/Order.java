package store;

import java.util.Scanner;

public class Order {
    private ProductManager manager;

    public Order(ProductManager manager) {
        this.manager = manager;
    }

    public void createOrder(Scanner sc) {
        manager.showAll();
        System.out.print("Nhập ID sản phẩm muốn đặt: ");
        int id = sc.nextInt();
        System.out.print("Nhập số lượng: ");
        int qty = sc.nextInt();

        for (Product p : manager.getProducts()) {
            if (p.getId() == id) {
                if (qty <= p.getQuantity()) {
                    double total = qty * p.getPrice();
                    System.out.println("Đặt hàng thành công!");
                    System.out.println("Tổng số tiền: " + total);
                    p.setQuantity(p.getQuantity() - qty);
                } else {
                    System.out.println("Số lượng không đủ!");
                }
                return;
            }
        }
        System.out.println("Không tìm thấy sản phẩm!");
    }
}
