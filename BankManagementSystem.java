import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class BankManagementSystem {
    private static final String USER_FILE = "users.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static HashMap<String, User> users = new HashMap<>();

    // User class to hold user information
    static class User {
        String password;
        double balance;

        User(String password, double balance) {
            this.password = password;
            this.balance = balance;
        }
    }

    public static void main(String[] args) {
        loadUsers(); // Load user data from file
        while (true) {
            System.out.println("\n=== Banking Management System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Thank you for using the Banking Management System!");
                    saveUsers(); // Save user data before exiting
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // Load user data from file
    private static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    users.put(username, new User(password, balance));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing user data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading user data.");
        }
    }

    // Save user data to file
    private static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String username : users.keySet()) {
                User user = users.get(username);
                bw.write(username + "," + user.password + "," + user.balance + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving user data.");
        }
    }

    // Register a new user
    private static void register() {
        System.out.println("\n=== Register ===");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists! Try a different one.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        users.put(username, new User(password, 0.0));
        System.out.println("Registration successful!");
        saveUsers();
    }

    // Login function
    private static void login() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            System.out.println("Login successful! Welcome, " + username + ".");
            userDashboard(username);
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    // User dashboard after successful login
    private static void userDashboard(String username) {
        while (true) {
            System.out.println("\n--- Services ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Close Account");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> deposit(username);
                case "2" -> withdraw(username);
                case "3" -> checkBalance(username);
                case "4" -> {
                    if (closeAccount(username)) return;
                }
                case "5" -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Deposit function
    private static void deposit(String username) {
        System.out.print("Enter the amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());
        users.get(username).balance += amount;
        System.out.println("Deposit successful! New balance: " + users.get(username).balance);
        saveUsers();
    }

    // Withdraw function
    private static void withdraw(String username) {
        System.out.print("Enter the amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());
        User user = users.get(username);
        if (user.balance >= amount) {
            user.balance -= amount;
            System.out.println("Withdrawal successful! Remaining balance: " + user.balance);
            saveUsers();
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    // Check balance function
    private static void checkBalance(String username) {
        System.out.println("Your current balance is: " + users.get(username).balance);
    }

    // Close account function
    private static boolean closeAccount(String username) {
        System.out.print("Are you sure you want to close your account? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            users.remove(username);
            saveUsers();
            System.out.println("Account closed successfully!");
            return true;
        } else {
            System.out.println("Account closing cancelled.");
            return false;
        }
    }
}
