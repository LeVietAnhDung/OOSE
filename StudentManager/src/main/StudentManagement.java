package main;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class StudentManagement {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        StudentManagement app = new StudentManagement();
        app.seedData(); // thêm ~10 sv mẫu thuộc 3 lớp
        app.run();
    }

    private void run() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ SINH VIÊN =====");
            System.out.println("1. Thêm sinh viên");
            System.out.println("2. Xóa sinh viên (theo mã)");
            System.out.println("3. Sửa sinh viên (theo mã)");
            System.out.println("4. In danh sách theo lớp sinh hoạt");
            System.out.println("5. In toàn bộ sinh viên");
            System.out.println("6. In danh sách sinh viên của một ngành (CNTT/KTPM)");
            System.out.println("7. In danh sách sắp xếp theo điểm trung bình");
            System.out.println("8. In danh sách sinh viên sinh vào một tháng (nhập tháng số)");
            System.out.println("9. Thoát");
            System.out.print("Chọn (1-9): ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": addStudentInteractive(); break;
                case "2": deleteStudentInteractive(); break;
                case "3": editStudentInteractive(); break;
                case "4": printByClassInteractive(); break;
                case "5": printAll(); break;
                case "6": printByMajorInteractive(); break;
                case "7": printSortedByGpaInteractive(); break;
                case "8": printBornInMonthInteractive(); break;
                case "9":
                    System.out.println("Thoát chương trình. Tạm biệt!");
                    sc.close();
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Thử lại.");
            }
        }
    }

    private void addStudentInteractive() {
        System.out.println("\n-- Thêm sinh viên --");
        String id;
        while (true) {
            System.out.print("Mã sinh viên (10 chữ số, CNTT bắt đầu 455105..., KTPM bắt đầu 455109...): ");
            id = sc.nextLine().trim();
            if (!isValidIdFormat(id)) {
                System.out.println("Mã không hợp lệ (phải là 10 chữ số).");
                continue;
            }
            if (findById(id) != null) {
                System.out.println("Mã đã tồn tại. Nhập mã khác.");
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("Họ tên: ");
            name = sc.nextLine().trim();
            name = normalizeName(name);
            if (name.isEmpty()) {
                System.out.println("Họ tên không được rỗng.");
                continue;
            }
            break;
        }

        LocalDate dob;
        while (true) {
            System.out.print("Ngày sinh (dd/MM/yyyy): ");
            String s = sc.nextLine().trim();
            try {
                dob = LocalDate.parse(s, DTF);
                int age = calculateAge(dob);
                if (age < 15 || age > 110) {
                    System.out.println("Tuổi không hợp lệ: " + age + " (phải >=15 và <=110).");
                    continue;
                }
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ.");
            }
        }

        String major;
        while (true) {
            System.out.print("Ngành (CNTT hoặc KTPM): ");
            major = sc.nextLine().trim().toUpperCase();
            if (!major.equals("CNTT") && !major.equals("KTPM")) {
                System.out.println("Ngành chỉ có thể là CNTT hoặc KTPM.");
                continue;
            }
 // kiểm tra tiền tố mã phù hợp với ngành
            if (!idStartsWithForMajor(id, major)) {
                System.out.printf("Chú ý: mã %s không phù hợp với ngành %s.\n", id, major);
                System.out.println("Mã phải bắt đầu bằng 455105 (CNTT) hoặc 455109 (KTPM). Hãy sửa mã hoặc ngành.");
                return; // hoặc break để nhập lại tùy yêu cầu; ở đây dừng thao tác thêm để tránh mâu thuẫn
            }
            break;
        }

        double gpa;
        while (true) {
            System.out.print("Điểm trung bình (0.0 - 10.0): ");
            String s = sc.nextLine().trim();
            try {
                gpa = Double.parseDouble(s);
                if (gpa < 0.0 || gpa > 10.0) {
                    System.out.println("Điểm phải trong khoảng 0.0 đến 10.0.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Nhập số hợp lệ.");
            }
        }

        String className;
        System.out.print("Lớp sinh hoạt: ");
        className = sc.nextLine().trim();

        Student st = new Student(id, name, dob, major, gpa, className);
        students.add(st);
        System.out.println("Đã thêm sinh viên:");
        printHeader();
        System.out.println(st);
    }

    private void deleteStudentInteractive() {
        System.out.print("\nNhập mã sinh viên cần xóa: ");
        String id = sc.nextLine().trim();
        Student st = findById(id);
        if (st == null) {
            System.out.println("Không tìm thấy sinh viên với mã " + id);
            return;
        }
        students.remove(st);
        System.out.println("Đã xóa sinh viên: " + id);
    }

    private void editStudentInteractive() {
        System.out.print("\nNhập mã sinh viên cần sửa: ");
        String id = sc.nextLine().trim();
        Student st = findById(id);
        if (st == null) {
            System.out.println("Không tìm thấy sinh viên với mã " + id);
            return;
        }
        System.out.println("Để trống và nhấn Enter nếu không muốn thay đổi trường đó.");

        System.out.print("Họ tên (" + st.getName() + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) st.setName(normalizeName(name));

        System.out.print("Ngày sinh (" + st.getDob().format(DTF) + ") dd/MM/yyyy: ");
        String dobStr = sc.nextLine().trim();
        if (!dobStr.isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(dobStr, DTF);
                int age = calculateAge(dob);
                if (age < 15 || age > 110) {
                    System.out.println("Tuổi không hợp lệ. Bỏ thay đổi ngày sinh.");
                } else {
                    st.setDob(dob);
                }
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ. Bỏ thay đổi ngày sinh.");
            }
        }

        System.out.print("Ngành (" + st.getMajor() + "): ");
        String major = sc.nextLine().trim();
        if (!major.isEmpty()) {
            major = major.toUpperCase();
            if (!major.equals("CNTT") && !major.equals("KTPM")) {
                System.out.println("Ngành không hợp lệ. Bỏ thay đổi ngành.");
            } else if (!idStartsWithForMajor(st.getId(), major)) {
                System.out.println("Ngành mới không phù hợp với tiền tố mã hiện tại. Bỏ thay đổi ngành.");
            } else {
                st.setMajor(major);
            }
        }

        System.out.print("Điểm trung bình (" + st.getGpa() + "): ");
        String gpaStr = sc.nextLine().trim();
        if (!gpaStr.isEmpty()) {
            try {
                double gpa = Double.parseDouble(gpaStr);
                if (gpa < 0.0 || gpa > 10.0) {
                    System.out.println("Điểm không hợp lệ. Bỏ thay đổi điểm.");
                } else {
                    st.setGpa(gpa);
                }
            } catch (NumberFormatException e) {
                System.out.println("Số không hợp lệ. Bỏ thay đổi điểm.");
            }
        }

        System.out.print("Lớp sinh hoạt (" + st.getClassName() + "): ");
        String cls = sc.nextLine().trim();
        if (!cls.isEmpty()) st.setClassName(cls);

        System.out.println("Đã cập nhật:");
        printHeader();
        System.out.println(st);
    }

    private void printByClassInteractive() {
        System.out.print("\nNhập tên lớp sinh hoạt cần in: ");
        String cls = sc.nextLine().trim();
        List<Student> res = new ArrayList<>();
        for (Student s : students) if (s.getClassName().equalsIgnoreCase(cls)) res.add(s);
        if (res.isEmpty()) {
            System.out.println("Không có sinh viên trong lớp " + cls);
            return;
        }
        printHeader();
        res.forEach(System.out::println);
    }

    private void printAll() {
        System.out.println("\n--- TẤT CẢ SINH VIÊN ---");
        if (students.isEmpty()) {
            System.out.println("Chưa có sinh viên nào.");
            return;
        }
        printHeader();
        students.forEach(System.out::println);
    }

    private void printByMajorInteractive() {
        System.out.print("\nNhập ngành (CNTT hoặc KTPM): ");
        String major = sc.nextLine().trim().toUpperCase();
        if (!major.equals("CNTT") && !major.equals("KTPM")) {
            System.out.println("Ngành không hợp lệ.");
            return;
        }
        List<Student> res = new ArrayList<>();
        for (Student s : students) if (s.getMajor().equalsIgnoreCase(major)) res.add(s);
        if (res.isEmpty()) {
            System.out.println("Không có sinh viên ngành " + major);
            return;
        }
        printHeader();
        res.forEach(System.out::println);
    }

    private void printSortedByGpaInteractive() {
        if (students.isEmpty()) {
            System.out.println("Chưa có sinh viên.");
            return;
        }
        System.out.print("Sắp xếp theo điểm trung bình (1: tăng dần, 2: giảm dần). Chọn 1/2: ");
        String c = sc.nextLine().trim();
        List<Student> copy = new ArrayList<>(students);
        if (c.equals("1")) {
            copy.sort(Comparator.comparingDouble(Student::getGpa));
        } else {
            copy.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        }
        printHeader();
        copy.forEach(System.out::println);
    }

    private void printBornInMonthInteractive() {
        System.out.print("\nNhập tháng (1-12): ");
        String s = sc.nextLine().trim();
        int m;
        try {
            m = Integer.parseInt(s);
            if (m < 1 || m > 12) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Tháng không hợp lệ.");
            return;
        }
        List<Student> res = new ArrayList<>();
        for (Student st : students) if (st.getDob().getMonthValue() == m) res.add(st);
        if (res.isEmpty()) {
            System.out.println("Không có sinh viên sinh trong tháng " + m);
            return;
        }
        printHeader();
        res.forEach(System.out::println);
    }



    private static void printHeader() {
        System.out.printf("%-12s | %-22s | %-10s | %-5s | %-6s | %-8s\n",
                "MSSV", "Họ tên", "Ngày sinh", "Tuổi", "Ngành", "Lớp");
        System.out.println("--------------------------------------------------------------------------------");
    }

    private Student findById(String id) {
        for (Student s : students) if (s.getId().equals(id)) return s;
        return null;
    }

    private static boolean isValidIdFormat(String id) {
        if (id == null) return false;
        if (id.length() != 10) return false;
        for (char c : id.toCharArray()) if (!Character.isDigit(c)) return false;
        return true;
    }

    private static boolean idStartsWithForMajor(String id, String major) {
        if (id == null || major == null) return false;
        if (major.equalsIgnoreCase("CNTT")) return id.startsWith("455105");
        if (major.equalsIgnoreCase("KTPM")) return id.startsWith("455109");
        return false;
    }

    private static int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private static String normalizeName(String raw) {
        if (raw == null) return "";
        // loại bỏ khoảng trắng thừa, viết hoa chữ cái đầu mỗi từ
        String[] parts = raw.trim().toLowerCase().replaceAll("\\s+", " ").split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(Character.toUpperCase(p.charAt(0)));
            if (p.length() > 1) sb.append(p.substring(1));
            sb.append(" ");
        }
        return sb.toString().trim();
    }


    private void seedData() {
        // các id hợp lệ: CNTT -> 455105xxxx, KTPM -> 455109xxxx; đảm bảo 10 chữ số
        students.add(new Student("4551050001", normalizeName("nguyen van a"), LocalDate.of(2001, 5, 12), "CNTT", 8.5, "CNTTK41A"));
        students.add(new Student("4551050002", normalizeName("tran thi b"), LocalDate.of(2002, 3, 3), "CNTT", 7.2, "CNTTK41A"));
        students.add(new Student("4551050003", normalizeName("le van c"), LocalDate.of(2000, 12, 25), "CNTT", 9.1, "CNTTK41B"));
        students.add(new Student("4551090001", normalizeName("pham thi d"), LocalDate.of(1999, 7, 8), "KTPM", 6.8, "KTPM21"));
        students.add(new Student("4551090002", normalizeName("hoang van e"), LocalDate.of(2003, 11, 2), "KTPM", 7.5, "KTPM21"));
        students.add(new Student("4551050004", normalizeName("do thi f"), LocalDate.of(2001, 1, 17), "CNTT", 5.4, "CNTTK41B"));
        students.add(new Student("4551090003", normalizeName("ngo thi g"), LocalDate.of(2000, 9, 30), "KTPM", 8.0, "KTPM22"));
        students.add(new Student("4551050005", normalizeName("vu van h"), LocalDate.of(2002, 4, 5), "CNTT", 6.2, "CNTTK41A"));
        students.add(new Student("4551090004", normalizeName("le thi i"), LocalDate.of(2001, 6, 18), "KTPM", 9.0, "KTPM22"));
        students.add(new Student("4551050006", normalizeName("tran van k"), LocalDate.of(2003, 8, 9), "CNTT", 7.9, "CNTTK41B"));
    }

    private static class Student {
        private final String id;
        private String name;
        private LocalDate dob;
        private String major;
        private double gpa;
        private String className;

        public Student(String id, String name, LocalDate dob, String major, double gpa, String className) {
            this.id = id;
            this.name = name;
            this.dob = dob;
            this.major = major.toUpperCase();
            this.gpa = gpa;
            this.className = className;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public LocalDate getDob() { return dob; }
        public String getMajor() { return major; }
        public double getGpa() { return gpa; }
        public String getClassName() { return className; }

        public void setName(String name) { this.name = name; }
        public void setDob(LocalDate dob) { this.dob = dob; }
        public void setMajor(String major) { this.major = major.toUpperCase(); }
        public void setGpa(double gpa) { this.gpa = gpa; }
        public void setClassName(String className) { this.className = className; }

        @Override
        public String toString() {
            int age = calculateAge(dob);
            return String.format("%-12s | %-22s | %-10s | %-5d | %-6s | %-8s",
                    id, name, dob.format(DTF), age, major, className);
        }
    }
}
