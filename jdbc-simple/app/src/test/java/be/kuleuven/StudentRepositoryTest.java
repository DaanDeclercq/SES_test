package be.kuleuven;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class StudentRepositoryTest {
  private final String CONNECTIONSTRING_TO_TEST_DB = "jdbc:mysql://localhost:3306/school_test?allowMultiQueries=true";
  private final String USER_OF_TEST_DB = "root";
  private final String PWD_OF_TEST_DB = "";

  private StudentRepository studentRepository;

  @Before
  public void createDatabaseAndInitializeConnectionManager() {
    ConnectionManager connectionManager = new ConnectionManager(CONNECTIONSTRING_TO_TEST_DB, USER_OF_TEST_DB,
        PWD_OF_TEST_DB);
    connectionManager.initTables();
    connectionManager.verifyTableContentOfInit();
    try {
      connectionManager.getConnection().commit();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    studentRepository = new StudentRepository(connectionManager.getConnection());
  }

  @After
  public void closeConnections() {
    try {
      studentRepository.getConnection().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void givenNewStudent_whenAddStudentToDb_assertThatStudentIsInDb() {
    // Arrange
    Student newStudent = new Student(333, "TestNaam", "TestVoornaam", true);

    // Act
    studentRepository.addStudentToDb(newStudent);

    // Assert
    Student studentToCheck = studentRepository.getStudentsByStudnr(333);
    assertThat(studentToCheck).isEqualTo(newStudent);
  }

  @Test
  public void givenNewStudenThatAlreadyInDb_whenAddStudentToDb_assertThrowsRuntimeException() {
    // Arrange
    int studnr = 123;
    Student newStudent = new Student(studnr, "newNaam", "newVoornaam", false);

    // Act
    Throwable thrown = catchThrowable(() -> {
      studentRepository.addStudentToDb(newStudent);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Duplicate entry '" + studnr + "' for key 'PRIMARY'");

  }

  @Test
  public void given123_whenGetStudentsByStudnr_assertThatStudentIsJaakTrekhaak() {
    // Arrange
    Student studentSolution = new Student(123, "Trekhaak", "Jaak", false);

    // Act
    Student studentToCheck = studentRepository.getStudentsByStudnr(123);

    // Assert
    assertThat(studentToCheck).isEqualTo(studentSolution);
  }

  @Test
  public void givenWrongStudnr_whenGetStudentsByStudnr_assertThatThrowsInvalidStudentException() {
    // Arrange
    int studnr = 9999;

    // Act
    Throwable thrown = catchThrowable(() -> {
      studentRepository.getStudentsByStudnr(studnr);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Student " + studnr + " not found in DB");
  }

  @Test
  public void whenGetAllStudents_assertThat3correctStudentsPresent() {
    // Arrange
    List<Student> studentsSolution = Arrays.asList(
        new Student(123, "Trekhaak", "Jaak", false),
        new Student(456, "Peeters", "Jos", false),
        new Student(890, "Dongmans", "Ding", true));

    // Act
    List<Student> studentsToCheck = studentRepository.getAllStudents();
    // Assert
    assertThat(studentsToCheck).usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(studentsSolution);
  }

  @Test
  public void givenStudent123updateToJacqueline_whenUpdateStudentInDb_assertThatStudentIsInDb() {
    // Arrange
    int studnr = 123;
    Student updateStudent = new Student(studnr, "Trekhaad", "Jacqueline", true);

    // Act
    studentRepository.updateStudentInDb(updateStudent);

    // Assert
    Student studentToCheck = studentRepository.getStudentsByStudnr(studnr);
    assertThat(studentToCheck).isEqualTo(updateStudent);
  }

  @Test
  public void givenStudentNotInDb_whenUpdateStudentInDb_assertThatThrowsInvalidStudentException() {
    // Arrange
    int studnr = 9999;
    Student newStudent = new Student(studnr, "newNaam", "newVoornaam", false);

    // Act
    Throwable thrown = catchThrowable(() -> {
      studentRepository.updateStudentInDb(newStudent);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Student " + studnr + " not found in DB");
  }

  @Test
  public void givenStudent123delete_whenDeleteStudentInDb_assertThatStudentIsNoLongerInDb() {
    // Arrange
    int studnr = 123;

    // Act
    studentRepository.deleteStudentInDb(studnr);

    // Assert
    Throwable thrown = catchThrowable(() -> {
      studentRepository.getStudentsByStudnr(studnr);
    });

    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Student " + studnr + " not found in DB");
  }

  @Test
  public void givenStudentNotInDb_whenDeleteStudentInDb_assertThatThrowsInvalidStudentException() {
    // Arrange
    int studnr = 9999;

    // Act
    Throwable thrown = catchThrowable(() -> {
      studentRepository.deleteStudentInDb(studnr);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Student " + studnr + " not found in DB");
  }

}
