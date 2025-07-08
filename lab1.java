import java.io.*;
import java.util.*;

class Employee {
    private int id;
    private String name;
    private double monthlySalary;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
        this.monthlySalary = 0.0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getMonthlySalary() { return monthlySalary; }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }


}

class Manager {
    private int id;
    private String name;
    private double salaryLimit;

    public Manager(int id, String name) {
        this.id = id;
        this.name = name;
        this.salaryLimit = 0.0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalaryLimit() { return salaryLimit; }

    public void setSalaryLimit(double salaryLimit) {
        this.salaryLimit = salaryLimit;
    }
}

class EMS {
    private String companyName;
    private Manager manager;
    private Set<Employee> employees;
    private Map<String, Double> salaryMap;

    public EMS(String companyName, Manager manager) {
        this.companyName = companyName;
        this.manager = manager;
        this.employees = new HashSet<>();
        this.salaryMap = new HashMap<>();
    }

    public void registerEmployee(Employee employee) {
        employees.add(employee);
        salaryMap.put(employee.getName(), 0.0);

    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        salaryMap.remove(employee.getName());
    }

    public void calculateSalaries(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Map<String, Double> dailySalaries = new HashMap<>();
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 2) continue;

                String name = parts[0].trim();
                try {
                    double dailySalary = Double.parseDouble(parts[1].trim());
                    dailySalaries.put(name, dailySalaries.getOrDefault(name, 0.0) + dailySalary);
                } catch (NumberFormatException ignored) {}
            }

            for (Map.Entry<String, Double> entry : dailySalaries.entrySet()) {
                String name = entry.getKey();
                double monthlySalary = entry.getValue() ;
                Employee employee = findEmployeeByName(name);
                if (employee == null) {
                    employee = new Employee(employees.size() + 1, name);
                    registerEmployee(employee);
                }
                employee.setMonthlySalary(monthlySalary);
                salaryMap.put(name, monthlySalary);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Employee findEmployeeByName(String name) {
        for (Employee employee : employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                return employee;
            }
        }
        return null;
    }

    public void askForSalaryLimit() {
        double salaryLimit = manager.getSalaryLimit();
        if (salaryLimit > 0) {
            employees.removeIf(employee -> salaryMap.get(employee.getName()) > salaryLimit);
            salaryMap.entrySet().removeIf(entry -> entry.getValue() > salaryLimit);
        }
    }

    public Set<Employee> getEmployees() {
        return employees;
    }
}
public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager(1, "John");
        manager.setSalaryLimit(10000000000000.0F);
        Employee helena  = new Employee(1, "Helena");
        Employee sisyphos= new Employee(2, "Sisyphos");
        Employee sophie  = new Employee(3, "Sophie");
        helena.setMonthlySalary(1000.0F);
        sisyphos.setMonthlySalary(2000.0F);
        sophie.setMonthlySalary(3000.0F);
        EMS ems = new EMS("MyCompany", manager);
        ems.registerEmployee(helena);
        ems.registerEmployee(sisyphos);
        ems.registerEmployee(sophie);
        ems.calculateSalaries("LAB3.csv");
        ems.askForSalaryLimit();
        System.out.println("Tüm çalışanlar:");
        for (Employee emp : ems.getEmployees()) {
            System.out.println(emp.getName() + ": " + emp.getMonthlySalary());
        }
    }

}
