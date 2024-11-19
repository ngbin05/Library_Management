-- MySQL dump 10.13  Distrib 9.0.1, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: thuvien
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `username` varchar(50) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES ('aaa','aaa','aaa','aaa','aaa','2024-11-06 23:11:20'),('aaaaa','aaaaa','aaaaa','aaaaa','aaaa','2024-11-08 01:23:48'),('bbbb','kjhjkh','hjghjgh','uygyugtfyu','bbbb','2024-11-06 23:17:50'),('ngbinh','binh','binh@gmail.com','090909090','ngbinh','2024-11-06 23:13:42');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id_sach` int NOT NULL AUTO_INCREMENT,
  `ten_sach` varchar(255) NOT NULL,
  `tac_gia` varchar(255) NOT NULL,
  `nha_xuatban` varchar(255) DEFAULT NULL,
  `ngay_xuatban` varchar(12) DEFAULT NULL,
  `the_loai` varchar(255) DEFAULT NULL,
  `so_luong` int NOT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_sach`),
  UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (2,'Tên sách:Tuyển tập truyện cười dân gian đặc sắc','MeoCon','Tran A','2023-11-28','Humor',5,'Unknown ISBN'),(3,'Tên sách:Truyện Kiều','Du Nguyễn','Unknown publisher','1974-01-01','Unknown genre',5,'OCLC:1015057742'),(21,'Vui Cười Lên Cùng Chứng Khoán - Cú Thông Thái','Cú Thông Thái','Alpha Books','2024-07-19','Business & Economics',5,'9786048992941'),(22,'Giải Bài Tập Tiếng Anh Lớp 2 Chân Trời Sáng Tạo','LAM HUYNH','LAM HUYNH','2024-01-11','Education',5,'isbn'),(23,'21 Cách Học Tiếng Anh Du Kích (song ngữ)','Fususu','Nguyen Chu Nam Phuong','2019-05-04','Self-Help',5,'9786048440756'),(24,'Buon Giorno, Arezzo','Suzette R. Grillot','University of Oklahoma Press','2016-05-18','History',5,'9780806156002');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow` (
  `borrow_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `id_sach` int DEFAULT NULL,
  `ngay_muon` date NOT NULL,
  `ngay_tra` date NOT NULL,
  `tinh_trang` enum('BORROWING','RETURNED') NOT NULL,
  PRIMARY KEY (`borrow_id`),
  KEY `book_borrow` (`id_sach`),
  KEY `user_borrow` (`user_id`),
  CONSTRAINT `book_borrow` FOREIGN KEY (`id_sach`) REFERENCES `books` (`id_sach`) ON UPDATE CASCADE,
  CONSTRAINT `user_borrow` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `CCCD` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `CCCD` (`CCCD`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (7,'nnnnnnn','nnnnnnn','nnnnnnn','nnnnnnn'),(8,'yyyyy','yyyyy','yyyyy','yyyyy'),(9,'iiiii','iiiii','iiiiii','iiiii'),(10,'1231231','123123123','12312312','123123'),(11,'ooooo','ooooo','oooo','oooo'),(12,'ppppp','ppppp','ppppp','ppppp'),(13,'aaaaa','bbbbbb','cccccc','ddddd'),(14,'aaaaa','cccccc','cccccc','dddddd'),(15,'jkguykgyuigug','iouyoiuyoiyuiyio','987697868','98768968969866');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-19 12:40:40
