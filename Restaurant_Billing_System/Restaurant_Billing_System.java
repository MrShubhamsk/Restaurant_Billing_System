
import java.util.ArrayList;
import java.util.Scanner;

class FoodItem {
    int id;
    String name;
    double price;

    FoodItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class OrderItem {
    FoodItem item;
    int quantity;

    OrderItem(FoodItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    double getTotal() {
        return item.price * quantity;
    }
}

public class RestaurantBillingSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<FoodItem> menu = new ArrayList<>();
    static int nextId = 1;

    static final double GST_RATE = 5.0;

    public static void main(String[] args) {

        // Default food menu
        addDefaultItems();

        System.out.println("==================================");
        System.out.println("   RESTAURANT BILLING SYSTEM");
        System.out.println("==================================");

        while (true) {

            System.out.println("\n1. Display Food Menu");
            System.out.println("2. Add New Food Item");
            System.out.println("3. Remove Food Item");
            System.out.println("4. Place Order & Generate Bill");
            System.out.println("5. Exit");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    displayMenu();
                    break;

                case 2:
                    addFoodItem();
                    break;

                case 3:
                    removeFoodItem();
                    break;

                case 4:
                    placeOrder();
                    break;

                case 5:
                    System.out.println("\nThank you for visiting!");
                    System.out.println("Visit Again!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Add default food items
    static void addDefaultItems() {

        addItem("Pizza", 150);
        addItem("Burger", 100);
        addItem("Sandwich", 80);
        addItem("French Fries", 70);
        addItem("Cold Coffee", 60);
    }

    // Add item to menu
    static void addItem(String name, double price) {
        menu.add(new FoodItem(nextId++, name, price));
    }

    // Display menu
    static void displayMenu() {

        System.out.println("\n========== FOOD MENU ==========");

        if (menu.isEmpty()) {
            System.out.println("No food items available.");
            return;
        }

        System.out.printf("%-8s %-20s %-10s%n",
                "ID", "Food Name", "Price");

        for (FoodItem item : menu) {
            System.out.printf("%-8d %-20s Rs. %.2f%n",
                    item.id, item.name, item.price);
        }

        System.out.println("================================");
    }

    // Add new food item
    static void addFoodItem() {

        System.out.print("Enter food name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Food name cannot be empty.");
            return;
        }

        double price = readDouble("Enter food price: ");

        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        addItem(name, price);

        System.out.println("Food item added successfully!");
    }

    // Remove food item
    static void removeFoodItem() {

        displayMenu();

        if (menu.isEmpty()) {
            return;
        }

        int id = readInt("Enter Food ID to remove: ");

        boolean removed = false;

        for (int i = 0; i < menu.size(); i++) {

            if (menu.get(i).id == id) {

                menu.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Food item removed successfully!");
        } else {
            System.out.println("Food ID not found!");
        }
    }

    // Place order
    static void placeOrder() {

        if (menu.isEmpty()) {
            System.out.println("Food menu is empty!");
            return;
        }

        ArrayList<OrderItem> cart = new ArrayList<>();

        displayMenu();

        System.out.println("\nEnter Food ID and Quantity.");
        System.out.println("Enter 0 as Food ID to finish order.");

        while (true) {

            int id = readInt("\nEnter Food ID: ");

            if (id == 0) {
                break;
            }

            FoodItem selectedItem = null;

            for (FoodItem item : menu) {

                if (item.id == id) {
                    selectedItem = item;
                    break;
                }
            }

            if (selectedItem == null) {
                System.out.println("Invalid Food ID!");
                continue;
            }

            int quantity = readInt("Enter Quantity: ");

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                continue;
            }

            boolean found = false;

            for (OrderItem order : cart) {

                if (order.item.id == id) {
                    order.quantity += quantity;
                    found = true;
                    break;
                }
            }

            if (!found) {
                cart.add(new OrderItem(selectedItem, quantity));
            }

            System.out.println("Item added to order!");
        }

        if (cart.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return;
        }

        generateBill(cart);
    }

    // Generate final bill
    static void generateBill(ArrayList<OrderItem> cart) {

        double subtotal = 0;

        System.out.println("\n");
        System.out.println("========================================");
        System.out.println("          RESTAURANT RECEIPT");
        System.out.println("========================================");

        System.out.printf("%-18s %5s %8s %10s%n",
                "Item", "Qty", "Price", "Total");

        System.out.println("----------------------------------------");

        for (OrderItem order : cart) {

            double total = order.getTotal();
            subtotal += total;

            System.out.printf("%-18s %5d %8.2f %10.2f%n",
                    order.item.name,
                    order.quantity,
                    order.item.price,
                    total);
        }

        double gst = subtotal * GST_RATE / 100;
        double grandTotal = subtotal + gst;

        System.out.println("----------------------------------------");

        System.out.printf("Subtotal:       Rs. %.2f%n", subtotal);
        System.out.printf("GST (5%%):       Rs. %.2f%n", gst);

        System.out.println("----------------------------------------");

        System.out.printf("GRAND TOTAL:    Rs. %.2f%n", grandTotal);

        System.out.println("========================================");
        System.out.println("       THANK YOU! VISIT AGAIN!");
        System.out.println("========================================");
    }

    // Read integer safely
    static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Read decimal number safely
    static double readDouble(String message) {

        while (true) {

            System.out.print(message);

            try {
                double value = Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(value)) {
                    return value;
                }

                System.out.println("Please enter a valid price.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}

