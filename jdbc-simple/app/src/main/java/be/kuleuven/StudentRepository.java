package be.kuleuven;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class StudentRepository {
  private final Connection connection;

  public StudentRepository(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }

  // CREATE
  public void addStudentToDb(Student student) {
    // Check if student is already in DB
    try {
      // // WITHOUT prepared statement
      // Statement s = (Statement) connection.createStatement();
      // int goedBezig = student.isGoedBezig() ? 1 : 0;
      // s.executeUpdate("INSERT INTO student (studnr, naam, voornaam, goedbezig) VALUES (" + student.getStudnr() + ", '" + student.getNaam() + "', '" + student.getVoornaam() + "', " + goedBezig + ");");
      // s.close();

      // WITH prepared statement
      PreparedStatement prepared = (PreparedStatement) connection
          .prepareStatement("INSERT INTO student (studnr, naam, voornaam, goedbezig) VALUES (?, ?, ?, ?);");
      prepared.setInt(1, student.getStudnr()); // First questionmark
      prepared.setString(2, student.getNaam()); // Second questionmark
      prepared.setString(3, student.getVoornaam()); // Third questionmark
      prepared.setBoolean(4, student.isGoedBezig()); // Fourth questionmark
      prepared.executeUpdate();

      prepared.close();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // READ
  public Student getStudentsByStudnr(int studnr) {
    Student found_student = null;
    try {
      Statement s = (Statement) connection.createStatement();
      String stmt = "SELECT * FROM student WHERE studnr = '" + studnr + "'";
      ResultSet result = s.executeQuery(stmt);

      while (result.next()) {
        int studnrFromDb = result.getInt("studnr");
        String naam = result.getString("naam");
        String voornaam = result.getString("voornaam");
        boolean goedbezig = result.getBoolean("goedbezig");

        found_student = new Student(studnrFromDb, naam, voornaam, goedbezig);
      }
      if (found_student == null) {
        throw new InvalidStudentException(studnr + "");
      }
      result.close();
      s.close();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return found_student;
  }

  // READ: Extra
  public List<Student> getAllStudents() {
    ArrayList<Student> resultList = new ArrayList<Student>();
    try {
      Statement s = (Statement) connection.createStatement();
      String stmt = "SELECT * FROM student";
      ResultSet result = s.executeQuery(stmt);

      while (result.next()) {
        int studnr = result.getInt("studnr");
        String naam = result.getString("naam");
        String voornaam = result.getString("voornaam");
        boolean goedbezig = result.getBoolean("goedbezig");

        resultList.add(new Student(studnr, naam, voornaam, goedbezig));
      }
      result.close();
      s.close();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return resultList;
  };

  // UPDATE
  public void updateStudentInDb(Student student) {
    // Check if student is already in DB
    getStudentsByStudnr(student.getStudnr());
    try {
      // WITH prepared statement
      PreparedStatement prepared = (PreparedStatement) connection
          .prepareStatement("UPDATE student SET naam = ?, voornaam = ?, goedbezig = ? WHERE studnr = ?;");
      prepared.setInt(4, student.getStudnr()); // Fourth questionmark
      prepared.setString(1, student.getNaam()); // First questionmark
      prepared.setString(2, student.getVoornaam()); // Second questionmark
      prepared.setBoolean(3, student.isGoedBezig()); // Third questionmark
      prepared.executeUpdate();

      prepared.close();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  // DELETE

  public void deleteStudentInDb(int studnr) {
    // Check if student is already in DB
    getStudentsByStudnr(studnr);
    try {
      // WITH prepared statement
      PreparedStatement prepared = (PreparedStatement) connection
          .prepareStatement("DELETE FROM student WHERE studnr = ?");
      prepared.setInt(1, studnr); // First questionmark
      prepared.executeUpdate();

      prepared.close();
      connection.commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
