package be.kuleuven;

import java.util.Objects;

public class Student {
  private int studnr;
  private String naam, voornaam;
  private boolean goedBezig;

  public Student() {

  }

  public Student(int studnr, String naam, String voornaam, boolean goedBezig) {
    this.studnr = studnr;
    this.naam = naam;
    this.voornaam = voornaam;
    this.goedBezig = goedBezig;
  }

  public int getStudnr() {
    return studnr;
  }

  public void setStudnr(int studnr) {
    this.studnr = studnr;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getVoornaam() {
    return voornaam;
  }

  public void setVoornaam(String voornaam) {
    this.voornaam = voornaam;
  }

  public boolean isGoedBezig() {
    return goedBezig;
  }

  public void setGoedBezig(boolean goedBezig) {
    this.goedBezig = goedBezig;
  }

  @Override
  public String toString() {
    return "Student{" +
        "studnr=" + studnr +
        ", naam='" + naam + '\'' +
        ", voornaam='" + voornaam + '\'' +
        ", goedBezig=" + goedBezig +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Student student = (Student) o;
    return studnr == student.studnr && Objects.equals(naam, student.naam) &&
        Objects.equals(voornaam, student.voornaam)
        && Objects.equals(goedBezig, student.goedBezig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studnr, naam, voornaam, goedBezig);
  }
}
