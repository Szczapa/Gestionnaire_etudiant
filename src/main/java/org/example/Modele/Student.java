package org.example.Modele;

import org.example.Util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Student {
    private final String firstname;
    private final String lastname;
    private final int classNumber;
    private final LocalDate diplomaDate;

    public Student(String firstname, String lastname, int classNumber, LocalDate diplomaDate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.classNumber = classNumber;
        this.diplomaDate = diplomaDate;
    }

    public static void getAllStudents(ConnectionManager connectionManager) {
        String request = "SELECT * FROM Student";
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(request)) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id")
                        + " / " + "Firstname: " + resultSet.getString("first_name")
                        + " / " + "Lastname: " + resultSet.getString("last_name")
                        + " / " + "ClassNum: " + resultSet.getInt("classNumber")
                        + " / " + "Diploma Date: " + resultSet.getDate("diplomaDate").toLocalDate());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
    }

    public static void getByClass(ConnectionManager connectionManager, Scanner scanner) {
        int classNumber = getClassNumber(scanner);

        String request = "SELECT * FROM Student WHERE classNumber = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(request)) {

            preparedStatement.setInt(1, classNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No student found for class number " + classNumber);
                } else {
                    while (resultSet.next()) {
                        System.out.println("ID: " + resultSet.getInt("id")
                                + " / " + "Firstname: " + resultSet.getString("first_name")
                                + " / " + "Lastname: " + resultSet.getString("last_name")
                                + " / " + "ClassNum: " + resultSet.getInt("classNumber")
                                + " / " + "Diploma Date: " + resultSet.getDate("diplomaDate").toLocalDate());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students by class: " + e.getMessage());
        }
    }

    public static void deleteStudent(ConnectionManager connectionManager, Scanner scanner) {
        getAllStudents(connectionManager);
        int id = getId(scanner, "Enter the student ID to delete: ");

        String request = "DELETE FROM Student WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(request)) {

            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Student with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No student found with ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    public static void createStudent(ConnectionManager connectionManager, Scanner scanner) {
        String firstName = getInput("Enter the student's first name: ", scanner);
        String lastName = getInput("Enter the student's last name: ", scanner);
        int classNumber = getClassNumber(scanner);
        LocalDate diplomaDate = getDiplomaDate(scanner);

        String request = "INSERT INTO Student (first_name, last_name, classNumber, diplomaDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(request)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, classNumber);
            preparedStatement.setDate(4, Date.valueOf(diplomaDate));

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Student " + firstName + " " + lastName + " added successfully"
                        + " in class " + classNumber);
            } else {
                System.out.println("Failed to add student " + firstName + " " + lastName);
            }
        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
        }
    }

    private static String getInput(String prompt, Scanner scanner) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private static int getClassNumber(Scanner scanner) {
        int classNumber;
        do {
            classNumber = Integer.parseInt(getInput("Enter the class number from 1 to 3: ", scanner));
            if (classNumber < 1 || classNumber > 3) {
                System.out.println("Invalid class number. Please enter a number between 1 and 3.");
            }
        } while (classNumber < 1 || classNumber > 3);
        return classNumber;
    }

    private static int getId(Scanner scanner, String prompt) {
        int id = 0;
        boolean validInput = false;
        do {
            System.out.println(prompt);
            if (scanner.hasNextLine()) {
                try {
                    id = Integer.parseInt(scanner.nextLine());
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID. Please enter a valid number.");
                }
            }
        } while (!validInput && scanner.hasNextLine());
        return id;
    }

    private static LocalDate getDiplomaDate(Scanner scanner) {
        String newDate = getInput("Diploma date is today? (y/n)", scanner);
        if (newDate.equalsIgnoreCase("n")) {
            String dateInput;
            do {
                dateInput = getInput("Enter the diploma date (yyyy-mm-dd): ", scanner);
                if (!dateInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println("Invalid date format. Please enter a valid date (yyyy-mm-dd).");
                    continue;
                }

                if (Integer.parseInt(dateInput.substring(5, 7)) < 1 || Integer.parseInt(dateInput.substring(5, 7)) > 12) {
                    System.out.println("Invalid date. Please enter a valid month.");
                    continue;
                }


                if (Integer.parseInt(dateInput.substring(8, 10)) < 1 || Integer.parseInt(dateInput.substring(8, 10)) > 31) {
                    System.out.println("Invalid date. Please enter a valid day.");
                    continue;
                }

                LocalDate parsedDate = LocalDate.parse(dateInput);
                if (parsedDate.isAfter(LocalDate.now())) {
                    System.out.println("Invalid date. Please enter a date in the past.");
                    continue;
                }
                return parsedDate;
            } while (true);
        } else {
            return LocalDate.now();
        }
    }
}
