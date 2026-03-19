package be.kuleuven;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class ConnectionManagerTest {
  private final String CONNECTIONSTRING_TO_TEST_DB = "jdbc:mysql://localhost:3306/school_test?allowMultiQueries=true";
  private final String USER_OF_TEST_DB = "root";
  private final String PWD_OF_TEST_DB = "";

  @Test
  public void whenInitializingDb_assertThatNoErrorIsThrown() {
    ConnectionManager cm = new ConnectionManager(CONNECTIONSTRING_TO_TEST_DB, USER_OF_TEST_DB, PWD_OF_TEST_DB);
    cm.initTables();
    cm.verifyTableContentOfInit();
    cm.flushConnection();
  }

  // @Test
  // public void whenVerifyTableContentOfInit_givenDropStudentTable_assertThatThrowsRuntimeException() {
  //   // Assign
  //   ConnectionManager cm = new ConnectionManager(CONNECTIONSTRING_TO_TEST_DB, USER_OF_TEST_DB, PWD_OF_TEST_DB);
  //   cm.initTables();
  //   cm.verifyTableContentOfInit();
  //   cm.dropStudentTable();

  //   // Act
  //   Throwable thrown = catchThrowable(() -> {
  //     cm.verifyTableContentOfInit();
  //   });

  //   // Assert
  //   assertThat(thrown).isInstanceOf(RuntimeException.class)
  //       .hasMessageContaining("Table 'school_test.student' doesn't exist");
  // }
}
