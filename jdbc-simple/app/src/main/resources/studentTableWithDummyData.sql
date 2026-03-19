DROP TABLE IF EXISTS student;

CREATE TABLE student(
    studnr INT NOT NULL PRIMARY KEY,
    naam VARCHAR(200) NOT NULL,
    voornaam VARCHAR(200),
    goedbezig BOOLEAN
);

INSERT INTO student (studnr, naam, voornaam, goedbezig) VALUES
    (123, 'Trekhaak', 'Jaak', 0),
    (456, 'Peeters', 'Jos', 0),
    (890, 'Dongmans', 'Ding', 1);