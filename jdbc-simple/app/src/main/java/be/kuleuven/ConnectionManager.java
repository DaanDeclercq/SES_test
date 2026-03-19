package be.kuleuven;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class ConnectionManager {
  private String connectionString;
  private Connection connection;

  public ConnectionManager(String connectionString, String user, String pwd) {
    try {
      this.connectionString = connectionString;
      this.connection = (Connection) DriverManager.getConnection(connectionString, user, pwd);
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      System.out.println("Error connecting to database with connectionstring: " + connectionString + ", and user: "
          + user + ", and the given password.");
      e.printStackTrace();
      // IMPORTANT SQLException is a "Checked exception" which forces immediate handling, which is not always practical => wrap inside RuntimeException
      throw new RuntimeException(e);
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public String getConnectionString() {
    return connectionString;
  }

  public void flushConnection() {
    try {
      connection.commit();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void initTables() {
    try {
      URI path = Objects.requireNonNull(App.class.getClassLoader().getResource("studentTableWithDummyData.sql"))
          .toURI();
      String sql = new String(Files.readAllBytes(Paths.get(path)));
      // System.out.println(sql);
      Statement statement = (Statement) connection.createStatement();
      statement.executeUpdate(sql);
      statement.close();
      connection.commit();
    } catch (Exception e) {
      System.out.println("An Error occurred when trying to initialize database table");
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public void verifyTableContentOfInit() {
    try {
      Statement statement = (Statement) connection.createStatement();
      var result = statement.executeQuery("SELECT COUNT(*) as cnt FROM student;");
      while (result.next()) {
        // System.out.println("Assert that number of rows is 3: " +
        // (result.getInt("cnt") == 3));
        assert result.getInt("cnt") == 3;
      }
      statement.close();
    } catch (AssertionError a) {
      System.out.println("The assertion of #rows == 3 failed");
      a.printStackTrace();
      throw new RuntimeException(a);
    } catch (SQLException e) {
      System.out.println("Error when trying to verify initialized table");
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public void dropStudentTable() {
    try {
      Statement statement = (Statement) connection.createStatement();
      statement.executeUpdate("DROP TABLE IF EXISTS student;");
      statement.close();
      connection.commit();
    } catch (Exception e) {
      System.out.println("Error ");
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
