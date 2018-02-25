-- MySQL dump 10.13  Distrib 5.7.19, for Win32 (AMD64)
--
-- Host: localhost    Database: autoservismaric
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `autoservismaric`
--

/*!40000 DROP DATABASE IF EXISTS `autoservismaric`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `autoservismaric` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `autoservismaric`;

--
-- Table structure for table `dio`
--

DROP TABLE IF EXISTS `dio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dio` (
  `IdDio` int(11) NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(50) DEFAULT NULL,
  `Sifra` varchar(20) DEFAULT NULL,
  `GodisteVozila` int(11) DEFAULT NULL,
  `Novo` tinyint(1) DEFAULT NULL,
  `VrstaGoriva` varchar(20) DEFAULT NULL,
  `TrenutnaCijena` decimal(6,2) DEFAULT NULL,
  `Kolicina` int(11) DEFAULT NULL,
  `ZaSve` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`IdDio`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dio`
--

LOCK TABLES `dio` WRITE;
/*!40000 ALTER TABLE `dio` DISABLE KEYS */;
INSERT INTO `dio` VALUES (1,'Kurbla','657asdf215',2002,1,'Dizel',12.00,11,0),(2,'Gurtna','asdf9842d7',2000,0,'Benzin',18.00,5,0),(3,'Rotor','5d8d1g32g1',1995,1,'Benzin',10.50,3,0),(4,'Stator','9a5i1b6d3y',2005,0,NULL,50.00,2,1),(5,'Felna','a4520yhn6a',2010,0,NULL,35.00,1,1),(6,'Kurba','657asdf215s',NULL,0,'Svi',1.00,10,1);
/*!40000 ALTER TABLE `dio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dio_model_vozila`
--

DROP TABLE IF EXISTS `dio_model_vozila`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dio_model_vozila` (
  `IdDio` int(11) NOT NULL,
  `IdModelVozila` int(11) NOT NULL,
  PRIMARY KEY (`IdDio`,`IdModelVozila`),
  KEY `R_20` (`IdModelVozila`),
  CONSTRAINT `dio_model_vozila_ibfk_1` FOREIGN KEY (`IdDio`) REFERENCES `dio` (`IdDio`),
  CONSTRAINT `dio_model_vozila_ibfk_2` FOREIGN KEY (`IdModelVozila`) REFERENCES `model_vozila` (`IdModelVozila`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dio_model_vozila`
--

LOCK TABLES `dio_model_vozila` WRITE;
/*!40000 ALTER TABLE `dio_model_vozila` DISABLE KEYS */;
INSERT INTO `dio_model_vozila` VALUES (1,1),(2,1),(3,1),(4,2),(5,2),(6,3);
/*!40000 ALTER TABLE `dio_model_vozila` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faktura`
--

DROP TABLE IF EXISTS `faktura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `faktura` (
  `IdFaktura` int(11) NOT NULL AUTO_INCREMENT,
  `DatumIzdavanja` date DEFAULT NULL,
  `IdRadniNalog` int(11) DEFAULT NULL,
  `Iznos` decimal(8,2) DEFAULT NULL,
  `VrijemeRada` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdFaktura`),
  KEY `R_21` (`IdRadniNalog`),
  CONSTRAINT `faktura_ibfk_1` FOREIGN KEY (`IdRadniNalog`) REFERENCES `radni_nalog` (`IdRadniNalog`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faktura`
--

LOCK TABLES `faktura` WRITE;
/*!40000 ALTER TABLE `faktura` DISABLE KEYS */;
INSERT INTO `faktura` VALUES (1,'2017-11-02',1,100.00,2),(2,'2018-02-25',4,200.00,1),(3,'2018-02-17',3,300.00,2),(4,'2018-01-16',2,400.00,1);
/*!40000 ALTER TABLE `faktura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kupac`
--

DROP TABLE IF EXISTS `kupac`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kupac` (
  `IdKupac` int(11) NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(50) DEFAULT NULL,
  `Telefon` varchar(20) DEFAULT NULL,
  `Adresa` varchar(50) DEFAULT NULL,
  `Grad` varchar(50) DEFAULT NULL,
  `Ime` varchar(50) DEFAULT NULL,
  `Prezime` varchar(50) DEFAULT NULL,
  `Aktivan` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdKupac`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kupac`
--

LOCK TABLES `kupac` WRITE;
/*!40000 ALTER TABLE `kupac` DISABLE KEYS */;
INSERT INTO `kupac` VALUES (1,NULL,'065/995-599','Eustahija Brzića 33','Prnjavor','Miloš','Mišić',1);
/*!40000 ALTER TABLE `kupac` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_vozila`
--

DROP TABLE IF EXISTS `model_vozila`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `model_vozila` (
  `IdModelVozila` int(11) NOT NULL AUTO_INCREMENT,
  `Marka` varchar(20) DEFAULT NULL,
  `Model` varchar(20) DEFAULT NULL,
  `Aktivan` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdModelVozila`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_vozila`
--

LOCK TABLES `model_vozila` WRITE;
/*!40000 ALTER TABLE `model_vozila` DISABLE KEYS */;
INSERT INTO `model_vozila` VALUES (1,'BMW','X6',1),(2,'VW','Golf',1),(3,'Svi','Svi',1);
/*!40000 ALTER TABLE `model_vozila` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodan_dio`
--

DROP TABLE IF EXISTS `prodan_dio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodan_dio` (
  `IdProdanDio` int(11) NOT NULL AUTO_INCREMENT,
  `IdDio` int(11) NOT NULL,
  `CijenaProdaje` decimal(6,2) DEFAULT NULL,
  `Kolicina` int(11) DEFAULT NULL,
  `Datum` date DEFAULT NULL,
  PRIMARY KEY (`IdProdanDio`),
  KEY `prodan_dio_ibfk_1` (`IdDio`),
  CONSTRAINT `prodan_dio_ibfk_1` FOREIGN KEY (`IdDio`) REFERENCES `dio` (`IdDio`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodan_dio`
--

LOCK TABLES `prodan_dio` WRITE;
/*!40000 ALTER TABLE `prodan_dio` DISABLE KEYS */;
INSERT INTO `prodan_dio` VALUES (1,1,55.00,2,'2017-10-10'),(2,2,12.00,3,'2017-10-22'),(3,3,13.00,1,'2017-11-08'),(4,4,55.00,1,'2017-11-09');
/*!40000 ALTER TABLE `prodan_dio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radni_nalog`
--

DROP TABLE IF EXISTS `radni_nalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radni_nalog` (
  `IdRadniNalog` int(11) NOT NULL AUTO_INCREMENT,
  `Placeno` tinyint(1) NOT NULL,
  `DatumOtvaranjaNaloga` date DEFAULT NULL,
  `DatumZatvaranjaNaloga` date DEFAULT NULL,
  `IdVozilo` int(11) DEFAULT NULL,
  `Troskovi` decimal(6,2) DEFAULT NULL,
  `Kilometraza` int(11) DEFAULT NULL,
  `OpisProblema` text,
  `PredvidjenoVrijemeZavrsetka` date DEFAULT NULL,
  `CijenaUsluge` decimal(6,2) DEFAULT NULL,
  `Izbrisano` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`IdRadniNalog`),
  KEY `R_6` (`IdVozilo`),
  CONSTRAINT `radni_nalog_ibfk_1` FOREIGN KEY (`IdVozilo`) REFERENCES `vozilo` (`IdVozilo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radni_nalog`
--

LOCK TABLES `radni_nalog` WRITE;
/*!40000 ALTER TABLE `radni_nalog` DISABLE KEYS */;
INSERT INTO `radni_nalog` VALUES (1,1,'2017-10-22','2017-10-30',1,50.00,300000,'Tu aaaaaaaa aaaaaaaa aaaaaaaa aaaaaaaa aaaaaaaa aaaaaaaa nešto piše jedan','2018-10-31',100.00,0),(2,1,'2017-11-23','2018-01-14',1,70.00,250000,'Tu nešto piše dva','2018-01-29',140.00,0),(3,0,'2017-12-24','2018-02-15',1,30.00,100000,'Tu nešto piše tri','2018-02-27',65.00,0),(4,0,'2017-12-25','2018-02-20',1,60.00,20000,'Tu nešto piše četiri','2018-02-24',125.00,0),(5,0,'2017-12-26','2018-02-21',1,65.00,30500,'Tu nešto piše pet','2018-02-25',226.00,0);
/*!40000 ALTER TABLE `radni_nalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radni_nalog_dio`
--

DROP TABLE IF EXISTS `radni_nalog_dio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radni_nalog_dio` (
  `IdRadniNalog` int(11) NOT NULL,
  `IdDio` int(11) NOT NULL,
  `Cijena` decimal(6,2) DEFAULT NULL,
  `Kolicina` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdRadniNalog`,`IdDio`),
  KEY `R_17` (`IdDio`),
  CONSTRAINT `radni_nalog_dio_ibfk_1` FOREIGN KEY (`IdRadniNalog`) REFERENCES `radni_nalog` (`IdRadniNalog`),
  CONSTRAINT `radni_nalog_dio_ibfk_2` FOREIGN KEY (`IdDio`) REFERENCES `dio` (`IdDio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radni_nalog_dio`
--

LOCK TABLES `radni_nalog_dio` WRITE;
/*!40000 ALTER TABLE `radni_nalog_dio` DISABLE KEYS */;
INSERT INTO `radni_nalog_dio` VALUES (1,1,10.00,1),(1,2,12.50,1),(1,3,0.15,1),(1,4,11.00,1),(1,5,11.75,1);
/*!40000 ALTER TABLE `radni_nalog_dio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radni_nalog_radnik`
--

DROP TABLE IF EXISTS `radni_nalog_radnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radni_nalog_radnik` (
  `IdRadniNalog` int(11) NOT NULL,
  `IdRadnik` int(11) NOT NULL,
  `Opis` text,
  PRIMARY KEY (`IdRadniNalog`,`IdRadnik`),
  KEY `R_10` (`IdRadnik`),
  CONSTRAINT `radni_nalog_radnik_ibfk_1` FOREIGN KEY (`IdRadniNalog`) REFERENCES `radni_nalog` (`IdRadniNalog`),
  CONSTRAINT `radni_nalog_radnik_ibfk_2` FOREIGN KEY (`IdRadnik`) REFERENCES `radnik` (`IdRadnik`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radni_nalog_radnik`
--

LOCK TABLES `radni_nalog_radnik` WRITE;
/*!40000 ALTER TABLE `radni_nalog_radnik` DISABLE KEYS */;
INSERT INTO `radni_nalog_radnik` VALUES (1,1,'Ima neki opis broj jedan'),(2,1,'Ima neki opis broj dva'),(3,1,'Ima neki opis broj tri'),(4,1,'Ima neki opis broj četiri');
/*!40000 ALTER TABLE `radni_nalog_radnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radnik`
--

DROP TABLE IF EXISTS `radnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radnik` (
  `IdRadnik` int(11) NOT NULL AUTO_INCREMENT,
  `Ime` varchar(50) NOT NULL,
  `Prezime` varchar(50) NOT NULL,
  `Telefon` varchar(20) DEFAULT NULL,
  `Adresa` varchar(50) DEFAULT NULL,
  `StrucnaSprema` varchar(50) DEFAULT NULL,
  `ImeOca` varchar(50) DEFAULT NULL,
  `BrojLicneKarte` varchar(20) DEFAULT NULL,
  `DatumRodjenja` date DEFAULT NULL,
  `Funkcija` text,
  `DatumOd` date DEFAULT NULL,
  `DatumDo` date DEFAULT NULL,
  PRIMARY KEY (`IdRadnik`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radnik`
--

LOCK TABLES `radnik` WRITE;
/*!40000 ALTER TABLE `radnik` DISABLE KEYS */;
INSERT INTO `radnik` VALUES (1,'Marko','Marković','066/488-844','Cara Lazara 5','SSS','Vojin','A506605B','1990-03-31','Šljakar','2000-01-01','2018-01-01');
/*!40000 ALTER TABLE `radnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `termin`
--

DROP TABLE IF EXISTS `termin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `termin` (
  `IdTermin` int(11) NOT NULL AUTO_INCREMENT,
  `Datum` date NOT NULL,
  `Vrijeme` time NOT NULL,
  `Marka` varchar(20) NOT NULL,
  `Model` varchar(20) NOT NULL,
  `Ime` varchar(20) NOT NULL,
  `Prezime` varchar(20) NOT NULL,
  `BrojTelefona` varchar(20) DEFAULT NULL,
  `DatumZakazivanja` date NOT NULL,
  PRIMARY KEY (`IdTermin`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `termin`
--

LOCK TABLES `termin` WRITE;
/*!40000 ALTER TABLE `termin` DISABLE KEYS */;
INSERT INTO `termin` VALUES (1,'2017-08-10','09:00:00','BMW','X6','Nemanja','Bosnić','065/112-224','2017-07-04'),(2,'2017-09-11','09:30:00','BMW','X6','Nemanja','Bosnić','065/112-224','2017-09-08'),(3,'2017-10-12','08:00:00','BMW','X6','Nemanja','Bosnić','065/112-224','2017-09-12'),(4,'2017-11-13','11:30:00','BMW','X6','Nemanja','Bosnić','065/112-224','2017-11-16'),(5,'2017-12-14','12:00:00','VW','Golf','Marko','Brkić','065/111-333','2017-12-10'),(6,'2018-01-10','13:45:00','VW','Golf','Marko','Brkić','065/111-333','2018-01-01'),(7,'2018-01-11','15:00:00','VW','Golf','Marko','Brkić','065/111-333','2018-01-05'),(8,'2018-02-15','11:15:00','VW','Golf','Marko','Brkić','065/111-333','2018-02-10');
/*!40000 ALTER TABLE `termin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vozilo`
--

DROP TABLE IF EXISTS `vozilo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vozilo` (
  `IdVozilo` int(11) NOT NULL AUTO_INCREMENT,
  `BrojRegistracije` varchar(20) NOT NULL,
  `Kilovat` int(11) DEFAULT NULL,
  `Kubikaza` decimal(6,2) DEFAULT NULL,
  `Godiste` int(11) DEFAULT NULL,
  `IdKupac` int(11) DEFAULT NULL,
  `IdModelVozila` int(11) DEFAULT NULL,
  `VrstaGoriva` varchar(20) DEFAULT NULL,
  `Izbrisano` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`IdVozilo`),
  KEY `R_5` (`IdKupac`),
  KEY `R_14` (`IdModelVozila`),
  CONSTRAINT `vozilo_ibfk_1` FOREIGN KEY (`IdKupac`) REFERENCES `kupac` (`IdKupac`),
  CONSTRAINT `vozilo_ibfk_2` FOREIGN KEY (`IdModelVozila`) REFERENCES `model_vozila` (`IdModelVozila`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vozilo`
--

LOCK TABLES `vozilo` WRITE;
/*!40000 ALTER TABLE `vozilo` DISABLE KEYS */;
INSERT INTO `vozilo` VALUES (1,'A10-B-200',230,2.30,2010,1,1,'Benzin',0),(2,'B33-D-146',170,1.90,2005,1,1,'Dizel',0);
/*!40000 ALTER TABLE `vozilo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-23 23:37:15
