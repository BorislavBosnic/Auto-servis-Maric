drop schema autoservismaric;

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
  `GodisteVozila` INT(11) NULL DEFAULT NULL,
  `Novo` TINYINT(1) NULL DEFAULT NULL,
  `VrstaGoriva` VARCHAR(20) NULL DEFAULT NULL,
  `TrenutnaCijena` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kolicina` INT(11) NULL DEFAULT NULL,
  `ZaSve` TINYINT(1) NULL DEFAULT NULL,
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
  `Naziv` VARCHAR(50) NOT NULL,
  `Telefon` VARCHAR(20) NULL DEFAULT NULL,
  `Adresa` VARCHAR(50) NULL DEFAULT NULL,
  `Grad` VARCHAR(50) NULL DEFAULT NULL,
  `Ime` VARCHAR(50) NULL DEFAULT NULL,
  `Prezime` VARCHAR(50) NULL DEFAULT NULL,
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
  `Placeno` TINYINT(1) NOT NULL,
  `DatumOtvaranjaNaloga` DATE NULL DEFAULT NULL,
  `DatumZatvaranjaNaloga` DATE NULL DEFAULT NULL,
  `IdVozilo` INT(11) NULL DEFAULT NULL,
  `Troskovi` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kilometraza` INT(11) NULL DEFAULT NULL,
  `OpisProblema` TEXT NULL DEFAULT NULL,
  `PredvidjenoVrijemeZavrsetka` DATE NULL DEFAULT NULL,
  `CijenaUsluge` DECIMAL(6,2) NULL DEFAULT NULL,
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
  PRIMARY KEY (`IdFaktura`),
  INDEX `R_21` (`IdRadniNalog` ASC),
  CONSTRAINT `faktura_ibfk_1`
    FOREIGN KEY (`IdRadniNalog`)
    REFERENCES `autoservismaric`.`radni_nalog` (`IdRadniNalog`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autoservismaric`.`prodan_dio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `autoservismaric`.`prodan_dio` (
  `IdDio` INT(11) NOT NULL,
  `CijenaProdaje` DECIMAL(6,2) NULL DEFAULT NULL,
  `Kolicina` INT(11) NULL DEFAULT NULL,
  `Datum` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`IdDio`),
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



INSERT INTO `autoservismaric`.`dio` (`idDio`,`Naziv`, `GodisteVozila`, `Novo`, `VrstaGoriva`, `TrenutnaCijena`, `Kolicina`, `ZaSve`) VALUES ('1', 'lamela', '2002', '1', 'Dizel', '50', '5', '1');
INSERT INTO `autoservismaric`.`dio` (`idDio`, `Naziv`, `GodisteVozila`, `Novo`, `VrstaGoriva`, `TrenutnaCijena`, `Kolicina`, `ZaSve`) VALUES ('2', 'alternator', '2000', '0', 'benzin', '100', '3', '0');
INSERT INTO `autoservismaric`.`dio` (`idDio`, `Naziv`, `Novo`, `VrstaGoriva`, `TrenutnaCijena`, `Kolicina`) VALUES ('3', 'Michelin guma', '1', '', '30', '8');
INSERT INTO `autoservismaric`.`dio` (`idDio`, `Naziv`, `Novo`, `TrenutnaCijena`, `Kolicina`) VALUES ('4', 'Pirelli guma', '1', '25', '10');
INSERT INTO `autoservismaric`.`dio` (`idDio`, `Naziv`, `Novo`, `TrenutnaCijena`, `Kolicina`) VALUES ('5', 'Dunlop guma', '1', '28', '12');


INSERT INTO `autoservismaric`.`vozilo` (`idVozilo`, `BrojRegistracije`) VALUES ('1', '444-A-333');
INSERT INTO `autoservismaric`.`vozilo` (`idVozilo`, `BrojRegistracije`) VALUES ('2', '222-B-111');


INSERT INTO `autoservismaric`.`prodan_dio` (`IdDio`, `CijenaProdaje`, `Kolicina`, `Datum`) VALUES ('1', '70', '1', '2017-11-10');
INSERT INTO `autoservismaric`.`prodan_dio` (`IdDio`, `CijenaProdaje`, `Kolicina`, `Datum`) VALUES ('2', '130', '1', '2017-11-21');
INSERT INTO `autoservismaric`.`prodan_dio` (`IdDio`, `CijenaProdaje`, `Kolicina`, `Datum`) VALUES ('3', '40', '2', '2017-10-10');

INSERT INTO `autoservismaric`.`radni_nalog` (`IdRadniNalog`, `Placeno`, `DatumOtvaranjaNaloga`, `DatumZatvaranjaNaloga`, `CijenaUsluge`) VALUES ('1', '1', '2017-07-09', '2017-07-10', '50');
INSERT INTO `autoservismaric`.`radni_nalog` (`IdRadniNalog`, `Placeno`, `DatumOtvaranjaNaloga`, `DatumZatvaranjaNaloga`, `CijenaUsluge`) VALUES ('2', '1', '2017-10-09', '2017-10-14', '70');
INSERT INTO `autoservismaric`.`radni_nalog` (`IdRadniNalog`, `Placeno`, `DatumOtvaranjaNaloga`, `CijenaUsluge`) VALUES ('3', '0', '2017-12-30', '30');

INSERT INTO `autoservismaric`.`faktura` (`IdFaktura`, `DatumIzdavanja`, `IdRadniNalog`) VALUES ('1', '2017-07-15', '1');
INSERT INTO `autoservismaric`.`faktura` (`IdFaktura`, `DatumIzdavanja`, `IdRadniNalog`) VALUES ('2', '2017-10-16', '2');



