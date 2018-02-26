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

/*IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve*/
INSERT INTO dio VALUES (1, "Motor", "657asdf215", 2002, true, "Dizel", 12, 5, false);
INSERT INTO dio VALUES (2, "Hladnjak", "asdf9842d7s", 2000, false, "Benzin", 18, 5, false);
INSERT INTO dio VALUES (3, "Alneser", "5d8d1g32g1", 1995, true, "Benzin", 10.5, 4, false);
INSERT INTO dio VALUES (4, "Brisaci", "9a5i1b6d3ys", 2005, false, "Svi", 50, 10, true);
INSERT INTO dio VALUES (5, "Guma Sava", "a4520yhn6as", 2010, false, "Svi", 35, 5, true);

/*IdProdanDio, IdDio, CijenaProdaje, Kolicina, Datum*/
INSERT INTO prodan_dio VALUES (1, 1, 30, 2, '2017-10-10');
INSERT INTO prodan_dio VALUES (2, 2, 20, 3, '2017-10-22');
INSERT INTO prodan_dio VALUES (3, 3, 15, 1, '2017-11-08');
INSERT INTO prodan_dio VALUES (4, 4, 80, 1, '2017-11-09');
INSERT INTO prodan_dio VALUES (5, 1, 30, 2, '2018-01-06');
INSERT INTO prodan_dio VALUES (6, 2, 20, 3, '2018-01-28');
INSERT INTO prodan_dio VALUES (7, 3, 15, 1, '2018-02-03');
INSERT INTO prodan_dio VALUES (8, 4, 80, 1, '2018-02-05');
INSERT INTO prodan_dio VALUES (9, 2, 20, 3, '2018-02-17');
INSERT INTO prodan_dio VALUES (10, 3, 15, 1, '2018-02-19');
INSERT INTO prodan_dio VALUES (11, 4, 80, 1, '2018-02-26');

/*IdKupac, Naziv, Telefon, Adresa, Grad, Ime, Prezime, Aktivan*/
INSERT INTO kupac VALUES (1,null,"065/995-599","Eustahija Brzića 33","Prnjavor","Miloš","Mišić", true);

/*IdModelVozila, Marka, Model, Aktivan*/
INSERT INTO model_vozila VALUES (1,"BMW","X6", true);
INSERT INTO model_vozila VALUES (2,"VW","Golf", true);

/*IdVozilo, BrojRegistracije, Kilovat, Kubikaza, Godiste, IdKupac, IdModelVozila, VrstaGoriva, Izbrisano*/
INSERT INTO vozilo VALUES (1,'A10-B-200',230,2.3,2010,1,1,"Benzin",false);
INSERT INTO vozilo VALUES (2,'B33-D-146',170,1.9,2005,1,1,"Dizel",false);

/*IdRadniNalog, Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, OpisProblema, PredvidjenoVrijemeZavrsetka, CijenaUsluge, Izbrisano*/
INSERT INTO radni_nalog VALUES (1, true, '2017-10-22', '2017-10-30', 1, 40.5, 300000, "Treba promjeniti motor, alneser i hladnjak.", '2018-10-31', 100, false);
INSERT INTO radni_nalog VALUES (2, true, '2017-11-23', '2018-01-14', 1, 0, 250000, "Treba promjeniti ulje.", '2018-01-29', 140, false);
INSERT INTO radni_nalog VALUES (3, false, '2017-12-24', '2018-02-15', 1, 0, 100000, "Treba promjeniti brisace.", '2018-02-27', 65, false);
INSERT INTO radni_nalog VALUES (4, false, '2017-12-25', '2018-02-20', 1, 0, 20000, "Treba popraviti mjenjač.", '2018-02-24', 125, false);
INSERT INTO radni_nalog VALUES (5, false, '2017-12-25', null, 1, 0, 250000, "Treba promjeniti akumulator.", '2018-02-25', 130, false);
INSERT INTO radni_nalog VALUES (6, true, '2018-01-17', '2018-01-26', 2, 85, 250000, "Treba promjeniti gume i brisace.", '2018-01-29', 140, false);
INSERT INTO radni_nalog VALUES (7, false, '2018-02-04', '2018-02-12', 2, 0, 100000, "Treba ofarbati auto u plavo.", '2018-02-07', 65, false);
INSERT INTO radni_nalog VALUES (8, false, '2018-02-07', null, 2, 0, 20000, "Treba zamjeniti remen.", '2018-02-16', 125, false);

/*IdFaktura, DatumIzdavanja, IdRadniNalog, Iznos, VrijemeRada*/
INSERT INTO faktura VALUES (1, '2017-11-02', 1, 140.5*1.17, 3);
INSERT INTO faktura VALUES (2, '2018-02-25', 4, 125*1.17, 4);
INSERT INTO faktura VALUES (3, '2018-02-17', 3, 65*1.17, 2);
INSERT INTO faktura VALUES (4, '2018-01-16', 2, 140*1.17, 5);
INSERT INTO faktura VALUES (5, '2018-01-28', 6, 225*1.17, 3);

/*IdRadniNalog, IdDio, Cijena, Kolicina*/
INSERT INTO radni_nalog_dio VALUES (1,1,12,1);
INSERT INTO radni_nalog_dio VALUES (1,2,18,1);
INSERT INTO radni_nalog_dio VALUES (1,3,10.5,1);
INSERT INTO radni_nalog_dio VALUES (6,4,50,1);
INSERT INTO radni_nalog_dio VALUES (6,5,35,1);

/*IdDio, IdModelVozila*/
INSERT INTO dio_model_vozila VALUES (1,1);
INSERT INTO dio_model_vozila VALUES (2,1);
INSERT INTO dio_model_vozila VALUES (3,1);
INSERT INTO dio_model_vozila VALUES (4,2);
INSERT INTO dio_model_vozila VALUES (5,2);

/*IdRadnik, Ime, Prezime, Telefon, Adresa, StrucnaSprema, ImeOca, BrojLicneKarte, DatumRodjenja, Funkcija, DatumOd, DatumDo*/
INSERT INTO radnik VALUES (1,"Marko","Marković","066/488-844","Cara Lazara 5","SSS","Vojin","A506605B",'1990-3-31',"Šljakar",'2000-1-1', null);

/*IdRadniNalog, IdRadnik, Opis*/
INSERT INTO radni_nalog_radnik VALUES (1,1,"Ima neki opis broj jedan");
INSERT INTO radni_nalog_radnik VALUES (2,1,"Ima neki opis broj dva");
INSERT INTO radni_nalog_radnik VALUES (3,1,"Ima neki opis broj tri");
INSERT INTO radni_nalog_radnik VALUES (4,1,"Ima neki opis broj četiri");
INSERT INTO radni_nalog_radnik VALUES (5,1,"Ima neki opis broj pet");
INSERT INTO radni_nalog_radnik VALUES (6,1,"Ima neki opis broj sest");
INSERT INTO radni_nalog_radnik VALUES (7,1,"Ima neki opis broj sedam");
INSERT INTO radni_nalog_radnik VALUES (8,1,"Ima neki opis broj osam");

/*IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja*/
INSERT INTO termin VALUES (1,'2017-8-10','9:00',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-7-4');
INSERT INTO termin VALUES (2,'2017-9-11','9:30',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-9-8');
INSERT INTO termin VALUES (3,'2017-10-12','8:00',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-9-12');
INSERT INTO termin VALUES (4,'2017-11-13','11:30',"BMW","X6","Nemanja","Bosnić","065/112-224",'2017-11-16');
INSERT INTO termin VALUES (5,'2017-12-14','12:00',"VW","Golf","Marko","Brkić","065/111-333",'2017-12-10');
INSERT INTO termin VALUES (6,'2018-1-10','13:45',"VW","Golf","Marko","Brkić","065/111-333",'2018-1-1');
INSERT INTO termin VALUES (7,'2018-1-11','15:00',"VW","Golf","Marko","Brkić","065/111-333",'2018-1-5');
INSERT INTO termin VALUES (8,'2018-2-15','11:15',"VW","Golf","Marko","Brkić","065/111-333",'2018-2-10');
