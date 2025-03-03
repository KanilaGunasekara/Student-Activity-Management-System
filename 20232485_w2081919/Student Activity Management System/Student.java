import java.util.Arrays;
import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String studentName;
    private String studentID;
    private int[] moduleMarks = new int[3];

    public Student(String studentID) {
        this.studentID = studentID;
    }

    // Getter for studentID
    public String getStudentID() {
        return studentID;
    }

    // Getter for studentName
    public String getStudentName() {
        return studentName;
    }

    // Setter for studentName
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    // Getter for moduleMarks
    public int getModuleMark(int moduleIndex) {
        if (moduleIndex >= 0 && moduleIndex < moduleMarks.length) {
            return moduleMarks[moduleIndex];
        }
        throw new IndexOutOfBoundsException("Invalid module details");
    }

    // Setter for moduleMarks
    public void setModuleMark(int moduleIndex, int mark) {
        if (moduleIndex >= 0 && moduleIndex < moduleMarks.length) {
            this.moduleMarks[moduleIndex] = mark;
        } else {
            throw new IndexOutOfBoundsException("Invalid module details");
        }
    }

    // Calculate average mark
    public double getAverageMark() {
        return Arrays.stream(moduleMarks).average().orElse(0);
    }

    // Convert student details to CSV format
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(studentID).append(",");
        sb.append(studentName != null ? studentName : "").append(",");
        for (int i = 0; i < 3; i++) {
            sb.append(moduleMarks[i]);
            if (i < 2) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    // Create student from CSV format
    public static Student fromCSV(String csv) {
        String[] parts = csv.split(",");
        String studentID = parts[0];
        Student student = new Student(studentID);
        student.setStudentName(parts[1]);
        for (int i = 0; i < 3; i++) {
            student.setModuleMark(i, Integer.parseInt(parts[i + 2]));
        }
        return student;
    }

    // Determine grade based on average mark
    public String getGrade() {
        double average = getAverageMark();
        if (average >= 80) {
            return "Distinction";
        } else if (average >= 70) {
            return "Great Pass";
        } else if (average >= 40) {
            return "Simple Pass";
        } else {
            return "Fail";
        }
    }

    // Generate a report for this student
    public String generateReport() {
        double average = getAverageMark();
        String grade = getGrade();
        return String.format("Student ID: %s, Name: %s, Module Marks: [%d, %d, %d], Total: %d, Average: %.2f, Grade: %s",
                studentID, studentName, moduleMarks[0], moduleMarks[1], moduleMarks[2],
                Arrays.stream(moduleMarks).sum(), average, grade);
    }

    public String toString() {
        return "Student{" +
                "studentID='" + studentID + '\'' +
                ", studentName='" + studentName + '\'' +
                ", moduleMarks=" + Arrays.toString(moduleMarks) +
                '}';
    }
}
