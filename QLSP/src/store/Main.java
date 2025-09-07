package store;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductManager manager = new ProductManager();
        Order order = new Order(manager);
        Scanner sc = new Scanner(System.in);

        // Thêm sản phẩm mẫu
        manager.addProduct(new Product(1, "Laptop Dell", "Điện tử", 15000000, 5, "Laptop văn phòng"));
        manager.addProduct(new Product(2, "Iphone 14", "Điện tử", 20000000, 3, "Điện thoại Apple"));
        manager.addProduct(new Product(3, "Áo thun", "Thời trang", 200000, 20, "Áo cotton"));

        int choice;
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Hiển thị tất cả sản phẩm");
            System.out.println("2. Cập nhật sản phẩm");
            System.out.println("3. Hiển thị sản phẩm theo giá");
            System.out.println("4. Hiển thị sản phẩm theo danh mục");
            System.out.println("5. Tính tổng tồn kho theo danh mục");
            System.out.println("6. Giảm giá sản phẩm");
            System.out.println("7. Đặt hàng");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    manager.showAll();
                    break;
                case 2:
                    System.out.print("Nhập ID sản phẩm: ");
                    int id = sc.nextInt();
                    System.out.print("Nhập giá mới: ");
                    double newPrice = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Nhập mô tả mới: ");
                    String newDesc = sc.nextLine();
                    manager.updateProduct(id, newPrice, newDesc);
                    break;
                case 3:
                    manager.showByPrice();
                    break;
                case 4:
                    System.out.print("Nhập danh mục: ");
                    String cat = sc.nextLine();
                    manager.showByCategory(cat);
                    break;
                case 5:
                    System.out.print("Nhập danh mục: ");
                    String cat2 = sc.nextLine();
                    manager.totalByCategory(cat2);
                    break;
                case 6:
                    System.out.print("Nhập ID sản phẩm: ");
                    int id2 = sc.nextInt();
                    System.out.print("Nhập % giảm giá: ");
                    double percent = sc.nextDouble();
                    manager.applyDiscount(id2, percent);
                    break;
                case 7:
                    order.createOrder(sc);
                    break;
                case 0:
                    System.out.println("Thoát chương trình!");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);

        sc.close();
    }
}
