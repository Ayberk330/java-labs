import java.io.*;
import java.util.Scanner;

abstract class Employee {
    private String name;
    private int id;

    public Employee(String name, int id) {
        // FIX: Alan isimleriyle parametreler aynı olduğu için mutlaka "this." kullanmalıyız.
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public abstract double calculateSalary();
}

class FullTimeEmployee extends Employee {
    private double monthlySalary;

    public FullTimeEmployee(String name, int id, double salary) {
        super(name, id);
        this.monthlySalary = salary;
    }

    @Override
    public double calculateSalary() {
        return monthlySalary;
    }
}

class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee(String name, int id, int hours, double rate) {
        super(name, id);
        this.hoursWorked = hours;    // CHANGED: alanlara da "this." ile atama
        this.hourlyRate = rate;
    }

    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }
}

 class EmployeeManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter employee type (full/part): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();

        System.out.print("Enter employee ID: ");
        int id = scanner.nextInt();

        Employee emp = null;

        // FIX: String karşılaştırmalarında "==" değil equals() kullanılmalı
        if (type.equalsIgnoreCase("full")) {
            System.out.print("Enter monthly salary: ");
            double salary = scanner.nextDouble();
            emp = new FullTimeEmployee(name, id, salary);

        } else if (type.equalsIgnoreCase("part")) {
            System.out.print("Enter hours worked: ");
            int hours = scanner.nextInt();
            System.out.print("Enter hourly rate: ");
            double rate = scanner.nextDouble();
            emp = new PartTimeEmployee(name, id, hours, rate);

        } else {
            System.out.println("Unknown employee type. Exiting.");
            scanner.close();
            return;  // CHANGED: Geçersiz türde devam etmek yerine işten çık
        }

        double salary = emp.calculateSalary();

        // Dosyaya yazma
        try (FileWriter fw = new FileWriter("employees.txt", true)) {
            fw.write(emp.getName() + "," + emp.getId() + "," + salary + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }

        // Dosyayı okuyup ekrana basma
        try (BufferedReader reader = new BufferedReader(new FileReader("employees.txt"))) {
            String line;
            System.out.println("Saved employees:");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // FIX: Parçaların uzunluğunu kontrol etmek isabetli olabilir, ama basit tutuyoruz
                System.out.println("Name: " + parts[0] + ", ID: " + parts[1] + ", Salary: $" + parts[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        scanner.close();
    }
}
