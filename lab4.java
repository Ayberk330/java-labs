import java.util.*;

abstract class Package {
    private String senderName;
    private String recipientName;
    private double weight;
    private boolean isDelivered;
    private String destinationCity;
    private String destinationCountry;

    public Package() {
        this.senderName = "";
        this.recipientName = "";
        this.weight = 0.0;
        this.isDelivered = false;
        this.destinationCity = "";
        this.destinationCountry = "";
    }

    public Package(String senderName, String recipientName, double weight,
                   String destinationCity, String destinationCountry) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.weight = weight;
        this.destinationCity = destinationCity;
        this.destinationCountry = destinationCountry;
        this.isDelivered = false;
    }

    public abstract double calculateShippingCost();

    public void markDelivered() {
        isDelivered = true;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void printInfo() {
        System.out.println("Sender: " + senderName);
        System.out.println("Recipient: " + recipientName);
        System.out.println("Weight: " + weight + " kg");
        System.out.println("Destination: " + destinationCity + ", " + destinationCountry);
        System.out.println("Delivered: " + isDelivered);
    }

    public double getWeight() {
        return weight;
    }
}

interface Refundable {
    boolean requestRefund(String reason);

    double getRefundAmount();

    default void logRefundRequest(String packageIdentifier) {
        System.out.println("Refund requested for package: " + packageIdentifier);
    }
}

interface Trackable {
    String getTrackingInfo();

    void updateLocation(String newLocation);

    void setEstimatedDeliveryTime(String dateTime);

    String getEstimatedDeliveryTime();
}

interface Insurable {
    void insurePackage(double insuredValue);

    double getInsuredValue();

    boolean claimInsurance(String claimReason);

    default void logInsuranceClaim(String packageIdentifier, String reason) {
        System.out.println("Insurance claim for package: " + packageIdentifier + " Reason: " + reason);
    }
}

class StandardPackage extends Package implements Trackable {
    private String shippingType;
    private String currentLocation;
    private String estimatedDeliveryTime;

    public StandardPackage(String senderName, String recipientName, double weight,
                           String destinationCity, String destinationCountry) {
        super(senderName, recipientName, weight, destinationCity, destinationCountry);
        this.shippingType = "Ground";
        this.currentLocation = "Warehouse";
        this.estimatedDeliveryTime = "Not Set";
    }

    @Override
    public double calculateShippingCost() {
        return getWeight() * 2.0;
    }

    @Override
    public String getTrackingInfo() {
        return "Current Location: " + currentLocation + ", Estimated Delivery: " + estimatedDeliveryTime;
    }

    @Override
    public void updateLocation(String newLocation) {
        this.currentLocation = newLocation;
    }

    @Override
    public void setEstimatedDeliveryTime(String dateTime) {
        this.estimatedDeliveryTime = dateTime;
    }

    @Override
    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Shipping Type: " + shippingType);
        System.out.println("Tracking Info: " + getTrackingInfo());
    }
}

class ExpressPackage extends Package implements Trackable, Insurable {
    private int priorityLevel;
    private String currentLocation;
    private String estimatedDeliveryTime;
    private double insuredValue;

    public ExpressPackage(String senderName, String recipientName, double weight,
                          String destinationCity, String destinationCountry, int priorityLevel) {
        super(senderName, recipientName, weight, destinationCity, destinationCountry);
        this.priorityLevel = priorityLevel;
        this.currentLocation = "Express Hub";
        this.estimatedDeliveryTime = "Not Set";
        this.insuredValue = 0.0;
    }


    @Override
    public double calculateShippingCost() {
        return (getWeight() * 5.0) + 10.0;
    }

    @Override
    public String getTrackingInfo() {
        return "Current Location: " + currentLocation + ", Estimated Delivery: " + estimatedDeliveryTime;
    }

    @Override
    public void updateLocation(String newLocation) {
        this.currentLocation = newLocation;
    }

    @Override
    public void setEstimatedDeliveryTime(String dateTime) {
        this.estimatedDeliveryTime = dateTime;
    }

    @Override
    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    @Override
    public void insurePackage(double insuredValue) {
        this.insuredValue = insuredValue;
    }

    @Override
    public double getInsuredValue() {
        return insuredValue;
    }

    @Override
    public boolean claimInsurance(String claimReason) {
        if(claimReason.equalsIgnoreCase("lost") || claimReason.equalsIgnoreCase("damaged")) {
            return true;
        }
        return false;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Priority Level: " + priorityLevel);
        System.out.println("Tracking Info: " + getTrackingInfo());
        System.out.println("Insured Value: $" + insuredValue);
    }
}

class FragilePackage extends Package implements Trackable, Insurable, Refundable {
    private boolean requiresReinforcedBox;
    private boolean requiresTemperatureControl;
    private String currentLocation;
    private String estimatedDeliveryTime;
    private double insuredValue;
    private double refundAmount;

    public FragilePackage(String senderName, String recipientName, double weight, String destinationCity, String destinationCountry, boolean requiresReinforcedBox, boolean requiresTemperatureControl) {
        super(senderName, recipientName, weight, destinationCity, destinationCountry);
        this.requiresReinforcedBox = requiresReinforcedBox;
        this.requiresTemperatureControl = requiresTemperatureControl;
        this.currentLocation = "Fragile Goods Center";
        this.estimatedDeliveryTime = "Not Set";
        this.insuredValue = 0.0;
        this.refundAmount = 0.0;
    }

    @Override
    public double calculateShippingCost() {
        return (getWeight() * 2.0) + 8.0;
    }

    @Override
    public String getTrackingInfo() {
        return "Current Location: " + currentLocation + ", Estimated Delivery: " + estimatedDeliveryTime;
    }

    @Override
    public void updateLocation(String newLocation) {
        this.currentLocation = newLocation;
    }

    @Override
    public void setEstimatedDeliveryTime(String dateTime) {
        this.estimatedDeliveryTime = dateTime;
    }

    @Override
    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    @Override
    public void insurePackage(double insuredValue) {
        this.insuredValue = insuredValue;
    }

    @Override
    public double getInsuredValue() {
        return insuredValue;
    }

    @Override
    public boolean claimInsurance(String claimReason) {
        return claimReason.equals("lost") || claimReason.equals("damaged");
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isDelivered() && reason.equals("damaged")) {
            refundAmount = calculateShippingCost() * 0.8; // refund 80% of shipping cost
            return true;
        }
        return false;
    }

    @Override
    public double getRefundAmount() {
        return refundAmount;
    }

    @Override
    public void markDelivered() {
        super.markDelivered();
        System.out.println("Handle with care – Fragile item delivered!");
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Requires Reinforced Box: " + requiresReinforcedBox);
        System.out.println("Requires Temperature Control: " + requiresTemperatureControl);
        System.out.println("Tracking Info: " + getTrackingInfo());
        System.out.println("Insured Value: $" + insuredValue);
        System.out.println("Refund Amount: $" + refundAmount);
    }
}


class ShippingSystem {
    private List<Package> packages;

    public ShippingSystem() {
        packages = new ArrayList<>();
    }

    public void addPackage(Package pkg) {
        if (!packages.contains(pkg)) {
            packages.add(pkg);
        } else {
            System.out.println("Package already exists in the system.");
        }
    }

    public void removePackage(Package pkg) {
        if (packages.contains(pkg)) {
            packages.remove(pkg);
        }
        else{
            System.out.println("packages not found");
        }
    }
    public void printAllPackages() {
        for (Package pkg : packages) {
            pkg.printInfo();
            System.out.println("Shipping Cost: $" + pkg.calculateShippingCost());
            System.out.println("--------------------------");
        }
    }


    public void generateReport() {
        int totalPackages = packages.size();
        int deliveredCount = 0;
        double totalShippingCost = 0.0;

        for (Package pkg : packages) {
            if (pkg.isDelivered()) {
                deliveredCount++;
            }
            totalShippingCost += pkg.calculateShippingCost();
        }

        double averageShippingCost = totalPackages > 0 ? totalShippingCost / totalPackages : 0.0;

        System.out.println("----- Shipping System Report -----");
        System.out.println("Total Packages: " + totalPackages);
        System.out.println("Delivered Packages: " + deliveredCount);
        System.out.println("In-transit Packages: " + (totalPackages - deliveredCount));
        System.out.println("Average Shipping Cost: $" + averageShippingCost);
    }
}


class Main {
    public static void main(String[] args) {
        ShippingSystem system = new ShippingSystem();

        StandardPackage sp1 = new StandardPackage("Alice", "Bob", 2.5, "New York", "USA");
        StandardPackage sp2 = new StandardPackage("Carol", "Dave", 3.0, "Chicago", "USA");
        StandardPackage sp3 = new StandardPackage("Ellen", "Frank", 1.8, "Boston", "USA");

        ExpressPackage ep1 = new ExpressPackage("Grace", "Hank", 1.2, "Los Angeles", "USA", 1);
        ExpressPackage ep2 = new ExpressPackage("Ivy", "Jack", 2.0, "San Francisco", "USA", 2);

        FragilePackage fp = new FragilePackage("Kate", "Leo", 0.8, "Paris", "France", true, false);

        sp1.updateLocation("In Transit - NY Hub");
        sp1.setEstimatedDeliveryTime("2025-04-01 10:00 AM");

        sp2.updateLocation("In Transit - Chicago Center");
        sp2.setEstimatedDeliveryTime("2025-04-02 2:30 PM");

        sp3.updateLocation("In Transit - Boston Depot");
        sp3.setEstimatedDeliveryTime("2025-04-03 9:00 AM");

        ep1.updateLocation("Express Depot LA");
        ep1.setEstimatedDeliveryTime("2025-03-30 3:00 PM");

        ep2.updateLocation("Express Depot SF");
        ep2.setEstimatedDeliveryTime("2025-03-31 4:15 PM");

        fp.updateLocation("Fragile Goods Center");
        fp.setEstimatedDeliveryTime("2025-04-05 5:00 PM");

        System.out.println("Standard Package sp1 Tracking: " + sp1.getTrackingInfo());
        System.out.println("Express Package ep1 Tracking: " + ep1.getTrackingInfo());
        System.out.println("Fragile Package fp Tracking: " + fp.getTrackingInfo());

        ep1.insurePackage(150.0);
        ep2.insurePackage(200.0);
        fp.insurePackage(100.0);

        if(ep1.claimInsurance("damaged")){
            System.out.println("Express Package ep1: Insurance claim approved.");
        } else {
            System.out.println("Express Package ep1: Insurance claim denied.");
        }

        if(fp.requestRefund("damaged")){
            System.out.println("Fragile Package refund approved before delivery. (Unexpected)");
        } else {
            System.out.println("Fragile Package refund NOT approved before delivery.");
        }
        fp.markDelivered();
        if(fp.requestRefund("damaged")){
            fp.logRefundRequest("FragilePackage-FP001");
            System.out.println("Fragile Package refund approved. Refund Amount: $" + fp.getRefundAmount());
        } else {
            System.out.println("Fragile Package refund NOT approved after delivery.");
        }
        system.addPackage(sp1);
        system.addPackage(sp2);
        system.addPackage(sp3);
        system.addPackage(ep1);
        system.addPackage(ep2);
        system.addPackage(fp);
        System.out.println("\n--- All Packages in the System ---");
        system.printAllPackages();
        sp2.markDelivered();
        ep1.markDelivered();
        System.out.println("\n--- Shipping System Report ---");
        system.generateReport();
    }
}
