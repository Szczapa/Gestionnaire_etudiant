package org.example.Util;

import org.example.Modele.Student;
import java.util.Scanner;

public class StudentApp {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        ConnectionManager connectionManager = new ConnectionManager();
        if (connectionManager.getConnection() != null) {
            System.out.println("Connected to the PostgreSQL server successfully.");
        } else {
            System.out.println("Failed to connect to the PostgreSQL server.");
            return; // Exit the method if the connection failed
        }

        int choice = 0;
        do {
            StudentCreatorMenu.studentMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        Student.getAllStudents(connectionManager);
                        break;
                    case 2:
                        Student.getByClass(connectionManager, scanner);
                        break;
                    case 3:
                        Student.deleteStudent(connectionManager, scanner);
                        break;
                    case 4:
                        Student.createStudent(connectionManager, scanner);
                        break;
                    case 0:
                        System.out.println("Ciao.");
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice");
            }
        } while (choice != 0);
        scanner.close();
    }
}
