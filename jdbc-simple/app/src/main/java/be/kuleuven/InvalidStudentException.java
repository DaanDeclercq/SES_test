package be.kuleuven;

public class InvalidStudentException extends RuntimeException {
  public InvalidStudentException(String studentIndentifier) {
    super("Student " + studentIndentifier + " not found in DB");
  }
}
