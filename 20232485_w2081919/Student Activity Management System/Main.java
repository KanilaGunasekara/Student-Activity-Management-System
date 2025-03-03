import java.io.*;
import java.util.*;

public class Main {
    private static final int MAX_STUDENTS = 100;
    private static Student[] students = new Student[MAX_STUDENTS];
    private static int studentCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> checkSeats();
                case 2 -> regStudent(scanner);
                case 3 -> delStudent(scanner);
                case 4 -> findStudent(scanner);
                case 5 -> storeDetails();
                case 6 -> loadDetails();
                case 7 -> viewByName();
                case 8 -> manageResults(scanner);
                case 0 -> System.out.println("Exiting");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Check available seats");
        System.out.println("2. Register student (with ID)");
        System.out.println("3. Delete student");
        System.out.println("4. Find student (with student ID)");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from the file to the system");
        System.out.println("7. View the list of students based on their names");
        System.out.println("8. Manage student results");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void manageResults(Scanner scanner) {
        char subchoice;
        do {
            displaySubMenu();
            subchoice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

            switch (subchoice) {
                case 'a' -> addNameAndMarks(scanner);
                case 'b' -> generateSummary();
                case 'c' -> generateReport();
                case '0' -> System.out.println("Back to Main Menu");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (subchoice != '0');
    }

    private static void displaySubMenu() {
        System.out.println("\nSub-Menu:");
        System.out.println("a. Add student name and marks");
        System.out.println("b. Generate a summary of the system");
        System.out.println("c. Generate complete report");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
    }

    private static void addNameAndMarks(Scanner scanner) {
        System.out.print("Enter student ID: ");
        String studentID = scanner.nextLine();

        for (Student student : students) {
            if (student != null && student.getStudentID().equals(studentID)) {
                System.out.print("Enter student name: ");
                student.setStudentName(scanner.nextLine());

                for (int i = 0; i < 3; i++) {
                    System.out.print("Enter mark for the Module " + (i + 1) + ": ");
                    student.setModuleMark(i, scanner.nextInt());
                }
                scanner.nextLine();

                System.out.println("Student details updated successfully.");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void generateSummary() {
        int totalRegistrations = 0;
        int[] studentsAbove40 = new int[3];

        for (Student student : students) {
            if (student != null) {
                totalRegistrations++;
                for (int i = 0; i < 3; i++) {
                    if (student.getModuleMark(i) > 40) studentsAbove40[i]++;
                }
            }
        }

        System.out.println("Total student registrations: " + totalRegistrations);
        for (int i = 0; i < 3; i++) {
            System.out.println("Students scored more than 40 marks in Module " + (i + 1) + ": " + studentsAbove40[i]);
        }
    }

    private static void generateReport() {
        List<Student> studentList = new ArrayList<>();
        for (Student student : students) {
            if (student != null) {
                studentList.add(student);
            }
        }

        // Sort students by average mark in descending order
        studentList.sort(Comparator.comparingDouble(Student::getAverageMark).reversed());

        for (Student student : studentList) {
            int totalMarks = 0;
            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                marks[i] = student.getModuleMark(i);
                totalMarks += marks[i];
            }
            double averageMark = student.getAverageMark();
            String grade = student.getGrade();

            System.out.printf("Student ID: %s, Name: %s, Module Marks: [%d, %d, %d], Total: %d, Average: %.2f, Grade: %s%n",
                    student.getStudentID(), student.getStudentName(),
                    marks[0], marks[1], marks[2],
                    totalMarks, averageMark, grade);
        }
    }

    private static void viewByName() {
        List<Student> studentList = Arrays.stream(students)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Student::getStudentName))
                .toList();

        studentList.forEach(System.out::println);
    }

    // Change here: Update the file name to results.txt
    private static void loadDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader("results.txt"))) {
            String line;
            studentCount = 0;
            while ((line = reader.readLine()) != null) {
                students[studentCount++] = Student.fromCSV(line);
            }
            System.out.println("Student details loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading student details: " + e.getMessage());
        }
    }

    // Change here: Update the file name to results.txt
    private static void storeDetails() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            for (Student student : students) {
                if (student != null) {
                    writer.write(student.toCSV());
                    writer.newLine();
                }
            }
            System.out.println("Student details stored successfully.");
        } catch (IOException e) {
            System.err.println("Error storing student details: " + e.getMessage());
        }
    }

    private static void findStudent(Scanner scanner) {
        System.out.print("Enter student ID to find: ");
        String studentID = scanner.nextLine();

        for (Student student : students) {
            if (student != null && student.getStudentID().equals(studentID)) {
                System.out.println("Student found: " + student);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void delStudent(Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        String studentID = scanner.nextLine();

        for (int i = 0; i < students.length; i++) {
            if (students[i] != null && students[i].getStudentID().equals(studentID)) {
                students[i] = null;
                studentCount--;
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void regStudent(Scanner scanner) {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("No available seats.");
            return;+/+
        }
        System.out.print("Enter student ID: ");
        String studentID = scanner.nextLine();

        for (Student student : students) {
            if (student != null && student.getStudentID().equals(studentID)) {
                System.out.println("Student ID already exists. Try again!");
                return;
            }
        }

        students[studentCount++] = new Student(studentID);
        System.out.println("Student registered successfully.");
    }

    private static void checkSeats() {
        System.out.println("Available seats: " + (MAX_STUDENTS - studentCount));
    }
}
