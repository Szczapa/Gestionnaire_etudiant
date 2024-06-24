package org.example.Modele;

import org.example.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Student {
    private final String firstname;
    private final String lastname;
    private final int classNumber;
    private final LocalDate diplomaDate;

    public Student(String firstname, String lastname, int ClassNumber, LocalDate diplomaDate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.classNumber = ClassNumber;
        this.diplomaDate = diplomaDate;
    }

    public static void getAllStudents(ConnectionManager connectionManager) {
        String request = "SELECT * FROM Student";
        try {
            Connection connection = connectionManager.getConnection();
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(request);
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id")
                            + " / " + "Firstname: " + resultSet.getString("first_name")
                            + " / " + "Lastname: " + resultSet.getString("last_name")
                            + " / " + "ClassNum: " + resultSet.getInt("classNumber")
                            + " / " + "Diploma Date: " + resultSet.getDate("diplomaDate").toLocalDate());
                }
                statement.close();
                resultSet.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void getByClass(ConnectionManager connectionManager, Scanner scanner) {
        int classNumber = 0;
        boolean validInput = false;
        do {
            System.out.println("Enter the class number from 1 to 3: ");
            if (scanner.hasNextLine()) {
                try {
                    classNumber = Integer.parseInt(scanner.nextLine());
                    if (classNumber < 1 || classNumber > 3) {
                        System.out.println("Invalid class number. Please enter a number between 1 and 3.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid class number. Please enter a valid number.");
                }
            }
        } while (!validInput && scanner.hasNextLine());

        try (Connection connection = connectionManager.getConnection()) {
            if (connection != null) {
                String request = "SELECT * FROM Student WHERE classNumber = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
                    preparedStatement.setInt(1, classNumber);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (!resultSet.isBeforeFirst()) {
                            System.out.println("No student found for class number " + classNumber);
                        } else {
                            System.out.println("Result ok");
                            while (resultSet.next()) {
                                System.out.println("ID: " + resultSet.getInt("id")
                                        + " / " + "Firstname: " + resultSet.getString("first_name")
                                        + " / " + "Lastname: " + resultSet.getString("last_name")
                                        + " / " + "ClassRoom: " + resultSet.getInt("classNumber")
                                        + " / " + "Diploma Date: " + resultSet.getDate("diplomaDate").toLocalDate());
                            }
                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static void deleteStudent(ConnectionManager connectionManager, Scanner scanner) {
        getAllStudents(connectionManager);
        int id = 0;
        boolean validInput = false;
        do {
            System.out.println("Enter the student ID to delete: ");
            if (scanner.hasNextLine()) {
                try {
                    id = Integer.parseInt(scanner.nextLine());
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID. Please enter a valid number.");
                }
            }
        } while (!validInput && scanner.hasNextLine());

        try (Connection connection = connectionManager.getConnection()) {
            if (connection != null) {
                String request = "DELETE FROM Student WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
                    preparedStatement.setInt(1, id);
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        System.out.println("Student with ID " + id + " deleted successfully.");
                    } else {
                        System.out.println("No student found with ID " + id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createStudent(ConnectionManager connectionManager, Scanner scanner) {
        int classNumber = 0;
        boolean validInput = false;

        System.out.println("Enter the student's first name: ");
        String first_name = scanner.nextLine();

        System.out.println("Enter the student's last name: ");
        String last_name = scanner.nextLine();

        System.out.println("New class number? (y/n)");
        String newClass = scanner.nextLine();

        if (newClass.equals("y")) {
            System.out.println("Enter the class number: ");
            try {
                classNumber = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid class number. Please enter a valid number.");
            }
        } else {
            do {
                System.out.println("Enter the class number from 1 to 3: ");
                if (scanner.hasNextLine()) {
                    try {
                        classNumber = Integer.parseInt(scanner.nextLine());
                        if (classNumber < 1 || classNumber > 3) {
                            System.out.println("Invalid class number. Please enter a number between 1 and 3.");
                        } else {
                            validInput = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid class number. Please enter a valid number.");
                    }
                }
            } while (!validInput && scanner.hasNextLine());
        }

        try (Connection connection = connectionManager.getConnection()) {
            if (connection != null) {
                String request = "INSERT INTO Student (first_name, last_name, classNumber, diplomaDate) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
                    preparedStatement.setString(1, first_name);
                    preparedStatement.setString(2, last_name);
                    preparedStatement.setInt(3, classNumber);
                    preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));

                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        System.out.println("Student " + first_name + " " + last_name + " added successfully"
                                + " in class " + classNumber);
                    } else {
                        System.out.println("Failed to add student " + first_name + " " + last_name);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}