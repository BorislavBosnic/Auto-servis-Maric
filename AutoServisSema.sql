drop schema if exists autoservismaric;

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema autoservismaric
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema autoservismaric
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `autoservismaric` DEFAULT CHARACTER SET utf8 ;
USE `autoservismaric` ;

-- -----------------------------------------------------
-- Table `autoservismaric`.`dio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`dio` (
  `IdDio` INT(11) NOT NULL AUTO_INCREMENT,
  `Naziv` VARCHAR(50) NULL DEFAULT NULL,
  `Sifra` VARCHAR(20) NULL DEFAULT NULL,
  `GodisteVozila` INT(11) NULL DEFAULT NULL,
  `Novo` BOOLEAN NULL DEFAULT NULL,/*KARPA MIJENJAO*/
  `VrstaGoriva` VARCHAR(20) NULL DEFAULT NULL,
  `TrenutnaCijena` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kolicina` INT(11) NULL DEFAULT NULL,
  `ZaSve` BOOLEAN NULL DEFAULT NULL,/*KARPA MIJENJAO*/
  PRIMARY KEY (`IdDio`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`model_vozila`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`model_vozila` (
  `IdModelVozila` INT(11) NOT NULL AUTO_INCREMENT,
  `Marka` VARCHAR(20) NULL DEFAULT NULL,
  `Model` VARCHAR(20) NULL DEFAULT NULL,
  `Aktivan` BOOLEAN NULL DEFAULT TRUE,
  PRIMARY KEY (`IdModelVozila`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`dio_model_vozila`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`dio_model_vozila` (
  `IdDio` INT(11) NOT NULL,
  `IdModelVozila` INT(11) NOT NULL,
  PRIMARY KEY (`IdDio`, `IdModelVozila`),
  INDEX `R_20` (`IdModelVozila` ASC),
  CONSTRAINT `dio_model_vozila_ibfk_1`
    FOREIGN KEY (`IdDio`)
    REFERENCES `autoservismaric`.`dio` (`IdDio`),
  CONSTRAINT `dio_model_vozila_ibfk_2`
    FOREIGN KEY (`IdModelVozila`)
    REFERENCES `autoservismaric`.`model_vozila` (`IdModelVozila`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`kupac`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`kupac` (
  `IdKupac` INT(11) NOT NULL AUTO_INCREMENT,
  `Naziv` VARCHAR(50) NULL DEFAULT NULL,
  `Telefon` VARCHAR(20) NULL DEFAULT NULL,
  `Adresa` VARCHAR(50) NULL DEFAULT NULL,
  `Grad` VARCHAR(50) NULL DEFAULT NULL,
  `Ime` VARCHAR(50) NULL DEFAULT NULL,
  `Prezime` VARCHAR(50) NULL DEFAULT NULL,
  `Aktivan` BOOLEAN NULL DEFAULT TRUE, 
  PRIMARY KEY (`IdKupac`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`vozilo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`vozilo` (
  `IdVozilo` INT(11) NOT NULL AUTO_INCREMENT,
  `BrojRegistracije` VARCHAR(20) NOT NULL,
  `Kilovat` INT(11) NULL DEFAULT NULL,
  `Kubikaza` DECIMAL(6,2) NULL DEFAULT NULL,
  `Godiste` INT(11) NULL DEFAULT NULL,
  `IdKupac` INT(11) NULL DEFAULT NULL,
  `IdModelVozila` INT(11) NULL DEFAULT NULL,
  `VrstaGoriva` VARCHAR(20) NULL DEFAULT NULL,
   `Izbrisano` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`IdVozilo`),
  INDEX `R_5` (`IdKupac` ASC),
  INDEX `R_14` (`IdModelVozila` ASC),
  CONSTRAINT `vozilo_ibfk_1`
    FOREIGN KEY (`IdKupac`)
    REFERENCES `autoservismaric`.`kupac` (`IdKupac`),
  CONSTRAINT `vozilo_ibfk_2`
    FOREIGN KEY (`IdModelVozila`)
    REFERENCES `autoservismaric`.`model_vozila` (`IdModelVozila`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`radni_nalog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`radni_nalog` (
  `IdRadniNalog` INT(11) NOT NULL AUTO_INCREMENT,
  `Placeno` BOOLEAN NOT NULL,/*KARPA MIJENJAO*/
  `DatumOtvaranjaNaloga` DATE NULL DEFAULT NULL,
  `DatumZatvaranjaNaloga` DATE NULL DEFAULT NULL,
  `IdVozilo` INT(11) NULL DEFAULT NULL,
  `Troskovi` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kilometraza` INT(11) NULL DEFAULT NULL,
  `OpisProblema` TEXT NULL DEFAULT NULL,
  `PredvidjenoVrijemeZavrsetka` DATE NULL DEFAULT NULL,
  `CijenaUsluge` DECIMAL(6,2) NULL DEFAULT NULL,
   `Izbrisano` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`IdRadniNalog`),
  INDEX `R_6` (`IdVozilo` ASC),
  CONSTRAINT `radni_nalog_ibfk_1`
    FOREIGN KEY (`IdVozilo`)
    REFERENCES `autoservismaric`.`vozilo` (`IdVozilo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`faktura`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`faktura` (
  `IdFaktura` INT(11) NOT NULL AUTO_INCREMENT,
  `DatumIzdavanja` DATE NULL DEFAULT NULL,
  `IdRadniNalog` INT(11) NULL DEFAULT NULL,
  `Iznos` DECIMAL(8,2) NULL DEFAULT NULL,
  `VrijemeRada` INT DEFAULT NULL,
  PRIMARY KEY (`IdFaktura`),
  INDEX `R_21` (`IdRadniNalog` ASC),
  CONSTRAINT `faktura_ibfk_1`
    FOREIGN KEY (`IdRadniNalog`)
    REFERENCES `autoservismaric`.`radni_nalog` (`IdRadniNalog`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `autoservismaric`.`termin`/*KARPA DODAO*/
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`termin` (
  `IdTermin` INT(11) NOT NULL AUTO_INCREMENT,
  `Datum` DATE NOT NULL,
  `Vrijeme` TIME NOT NULL,
  `Marka` VARCHAR(20) NOT NULL,
  `Model` VARCHAR(20) NOT NULL,
  `Ime` VARCHAR(20) NOT NULL,
  `Prezime` VARCHAR(20) NOT NULL,
  `BrojTelefona` VARCHAR(20) NULL DEFAULT NULL,
  `DatumZakazivanja` DATE NOT NULL,
  PRIMARY KEY (`IdTermin`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



-- -----------------------------------------------------
-- Table `autoservismaric`.`prodan_dio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`prodan_dio` (
  `IdProdanDio` INT(11) NOT NULL AUTO_INCREMENT,
  `IdDio` INT(11) NOT NULL,
  `CijenaProdaje` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kolicina` INT(11) NULL DEFAULT NULL,
  `Datum` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`IdProdanDio`),
  CONSTRAINT `prodan_dio_ibfk_1`
    FOREIGN KEY (`IdDio`)
    REFERENCES `autoservismaric`.`dio` (`IdDio`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`radni_nalog_dio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`radni_nalog_dio` (
  `IdRadniNalog` INT(11) NOT NULL,
  `IdDio` INT(11) NOT NULL,
  `Cijena` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kolicina` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`IdRadniNalog`, `IdDio`),
  INDEX `R_17` (`IdDio` ASC),
  CONSTRAINT `radni_nalog_dio_ibfk_1`
    FOREIGN KEY (`IdRadniNalog`)
    REFERENCES `autoservismaric`.`radni_nalog` (`IdRadniNalog`),
  CONSTRAINT `radni_nalog_dio_ibfk_2`
    FOREIGN KEY (`IdDio`)
    REFERENCES `autoservismaric`.`dio` (`IdDio`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`radnik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`radnik` (
  `IdRadnik` INT(11) NOT NULL AUTO_INCREMENT,
  `Ime` VARCHAR(50) NOT NULL,
  `Prezime` VARCHAR(50) NOT NULL,
  `Telefon` VARCHAR(20) NULL DEFAULT NULL,
  `Adresa` VARCHAR(50) NULL DEFAULT NULL,
  `StrucnaSprema` VARCHAR(50) NULL DEFAULT NULL,
  `ImeOca` VARCHAR(50) NULL DEFAULT NULL,
  `BrojLicneKarte` VARCHAR(20) NULL DEFAULT NULL,
  `DatumRodjenja` DATE NULL DEFAULT NULL,
  `Funkcija` TEXT NULL,
  `DatumOd` DATE NULL,
  `DatumDo` DATE NULL,
  PRIMARY KEY (`IdRadnik`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`radni_nalog_radnik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`radni_nalog_radnik` (
  `IdRadniNalog` INT(11) NOT NULL ,
  `IdRadnik` INT(11) NOT NULL,
  `Opis` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`IdRadniNalog`, `IdRadnik`),
  INDEX `R_10` (`IdRadnik` ASC),
  CONSTRAINT `radni_nalog_radnik_ibfk_1`
    FOREIGN KEY (`IdRadniNalog`)
    REFERENCES `autoservismaric`.`radni_nalog` (`IdRadniNalog`),
  CONSTRAINT `radni_nalog_radnik_ibfk_2`
    FOREIGN KEY (`IdRadnik`)
    REFERENCES `autoservismaric`.`radnik` (`IdRadnik`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

/*KARPA DODAO*/
INSERT INTO dio VALUES (1, "Kurbla", "657asdf215", 2002, true, "Dizel", 12, 11, false);
INSERT INTO dio VALUES (2, "Gurtna", "asdf9842d7", 2000, false, "Benzin", 18, 5, false);
INSERT INTO dio VALUES (3, "Rotor", "5d8d1g32g1", 1995, true, "Benzin", 10.5, 3, false);
INSERT INTO dio VALUES (4, "Stator", "9a5i1b6d3y", 2005, false, null, 50, 2, true);
INSERT INTO dio VALUES (5, "Felna", "a4520yhn6a", 2010, false, null, 35, 1, true);

INSERT INTO prodan_dio VALUES (1, 1, 55, 2, '2017-10-10');
INSERT INTO prodan_dio VALUES (2, 2, 12, 3, '2017-10-22');
INSERT INTO prodan_dio VALUES (3, 3, 13, 1, '2017-11-08');
INSERT INTO prodan_dio VALUES (4, 4, 55, 1, '2017-11-09');

INSERT INTO kupac VALUES (1,null,"065/995-599","Eustahija Brzića 33","Prnjavor","Miloš","Mišić", true);

INSERT INTO model_vozila VALUES (1,"BMW","X6", true);
INSERT INTO model_vozila VALUES (2,"VW","Golf", true);

INSERT INTO vozilo VALUES (1,'A10-B-200',230,2.3,2010,1,1,"Benzin",false);
INSERT INTO vozilo VALUES (2,'B33-D-146',170,1.9,2005,1,1,"Dizel",false);

INSERT INTO radni_nalog VALUES (1, true, '2017-10-22', '2017-10-30', 1, 50, 300000, "Tu nešto piše jedan", '2018-10-31', 100, false);
INSERT INTO radni_nalog VALUES (2, true, '2017-11-23', '2018-01-14', 1, 70, 250000, "Tu nešto piše dva", '2018-01-29', 140, false);
INSERT INTO radni_nalog VALUES (3, false, '2017-12-24', '2018-02-15', 1, 30, 100000, "Tu nešto piše tri", '2018-02-27', 65, false);
INSERT INTO radni_nalog VALUES (4, false, '2017-12-25', '2018-02-20', 1, 60, 20000, "Tu nešto piše četiri", '2018-02-24', 125, false);
INSERT INTO radni_nalog VALUES (5, false, '2017-12-26', '2018-02-21', 1, 65, 30500, "Tu nešto piše pet", '2018-02-25', 226, false);

INSERT INTO faktura VALUES (1, '2017-11-02', 1, 100, 2);
INSERT INTO faktura VALUES (2, '2018-02-25', 4, 200, 1);
INSERT INTO faktura VALUES (3, '2018-02-17', 3, 300, 2);
INSERT INTO faktura VALUES (4, '2018-01-16', 2, 400, 1);


INSERT INTO radni_nalog_dio VALUES (1,1,10,1);
INSERT INTO radni_nalog_dio VALUES (1,2,12.5,1);
INSERT INTO radni_nalog_dio VALUES (1,3,0.15,1);
INSERT INTO radni_nalog_dio VALUES (1,4,11,1);
INSERT INTO radni_nalog_dio VALUES (1,5,11.75,1);

INSERT INTO dio_model_vozila VALUES (1,1);
INSERT INTO dio_model_vozila VALUES (2,1);
INSERT INTO dio_model_vozila VALUES (3,1);
INSERT INTO dio_model_vozila VALUES (4,2);
INSERT INTO dio_model_vozila VALUES (5,2);

INSERT INTO radnik VALUES (1,"Marko","Marković","066/488-844","Cara Lazara 5","SSS","Vojin","A506605B",'1990-3-31',"Šljakar",'2000-1-1','2018,1-1');

INSERT INTO radni_nalog_radnik VALUES (1,1,"Ima neki opis broj jedan");
INSERT INTO radni_nalog_radnik VALUES (2,1,"Ima neki opis broj dva");
INSERT INTO radni_nalog_radnik VALUES (3,1,"Ima neki opis broj tri");
INSERT INTO radni_nalog_radnik VALUES (4,1,"Ima neki opis broj četiri");

INSERT INTO termin VALUES (1,'2017-8-10','9:00',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-7-4');
INSERT INTO termin VALUES (2,'2017-9-11','9:30',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-9-8');
INSERT INTO termin VALUES (3,'2017-10-12','8:00',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-9-12');
INSERT INTO termin VALUES (4,'2017-11-13','11:30',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-11-16');
INSERT INTO termin VALUES (5,'2017-12-14','12:00',"VW","Golf","Marko","Brkić","065/111-333",'2017-12-10');
INSERT INTO termin VALUES (6,'2018-1-10','13:45',"VW","Golf","Marko","Brkić","065/111-333",'2018-1-1');
INSERT INTO termin VALUES (7,'2018-1-11','15:00',"VW","Golf","Marko","Brkić","065/111-333",'2018-1-5');
INSERT INTO termin VALUES (8,'2018-2-15','11:15',"VW","Golf","Marko","Brkić","065/111-333",'2018-2-10');
