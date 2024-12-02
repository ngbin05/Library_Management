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
  `image` blob,
  `description` text,
  PRIMARY KEY (`id_sach`),
  UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (21,'Vui Cười Lên Cùng Chứng Khoán - Cú Thông Thái','Cú Thông Thái','Alpha Books','2024-07-19','Business & Economics',5,'9786048992941',NULL,NULL),(22,'Giải Bài Tập Tiếng Anh Lớp 2 Chân Trời Sáng Tạo','LAM HUYNH','LAM HUYNH','2024-01-11','Education',5,'isbn',NULL,NULL),(23,'21 Cách Học Tiếng Anh Du Kích (song ngữ)','Fususu','Nguyen Chu Nam Phuong','2019-05-04','Self-Help',5,'9786048440756',NULL,NULL),(24,'Buon Giorno, Arezzo','Suzette R. Grillot','University of Oklahoma Press','2016-05-18','History',5,'9780806156002',NULL,NULL),(25,'Moral Panics, Sex Panics','Gilbert Herdt','NYU Press','2009-06-01','Social Science',5,'9780814737323',_binary '�\��\�\0JFIF\0\0\0\0\0\0�\�\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342�\�\0C			\r\r2!!22222222222222222222222222222222222222222222222222��\0\0�\0�\"\0�\�\0\0\0\0\0\0\0\0\0\0\0	\n�\�\0�\0\0\0}\0!1AQa\"q2���#B��R\�\�$3br�	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz�����������������������������������\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�����\�\0\0\0\0\0\0\0\0	\n�\�\0�\0\0w\0!1AQaq\"2�B����	#3R\�br\�\n$4\�%\�\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz������������������������������������\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�����\�\0\0\0?\0\���Q\�\�\�UZG,�����\�H�2ȃ\�ڶR\�\�;��s\�b\�>�S\�Y�\�m,�\�f��\��\0z�+X!XM\Z9�\�\�?\�\Z�JڑJ7\�\�\���S1�f8�U\�sZ\�-�[@e#FH<��\�R\��z}_\�u��h��\'g����\�\�N?�\�i�h^`��[�\��vs\�\�\��\n\�\�77�\����8Ѥ�\��\\\�\�\�<�\Z�3�8>\�Fgp:�\�\�zE���5k:D-ue\���\�\�^\'𕽄+�i\\\�˂ʹm�\�A�\�\�o�\�.�e\�8\��?�\�Aw\�io8\�ڶQ㻄\�ɓԃ]��uo\��.\�km&\�V�?(^\�J�e\�KJt�\�\�X\"�E\�=�\�Z�\'\�\�W\�.u\�\�]^\�\�Ţ\�\�=��G\�F�ך;��DG\��V\�]=\Z���\�[���\����\�G\�M�\Z,ڋ�1�\�q�Ƿ�1�\�$\Z�f�\���\Z�5)nk\�`I/*֐�x�\0�� �]\�N8\�<?үi\�\Z\����\�\�\�\�\��\�y���.\0�x�e�7\�Y\��\��#\�:QZ?\��\0֧X�����\�LӤĭ\�\�8�\�4]�{\�\�w|V\�(\�-a#�\0\�O��j����U�FXA�?���U\�?YIO\�\��0?J��3���Y��hڇ���PZBz\�H��d׌\���k�\�s$�d\�~f\'�\�{ޣ\�\�\�J\�H,-�\0�&|\�-�ޟ;,�22�\�\�v\�\�\�\�\��\�O�Ld)\����\�_2\�>��~U7F�\�\��kȬ�8�\�a\�F��S\�d�i\�k\�JG>NY\�1\�\�\�\��+�\�FQ�#NQZ��\�9$Ҵ\�\Z \�?\�\\ƕ�\�w\�Ѣ@\�\�^FBG~Mu�\'\�+�oH\�\�a\�~_�s�9�Ox�<G�P�\�*U%�\�2q�x\�:�Kڨ�\\\�*:nJ:?�x/\��Kvl\��\�S����\0��v\�\r��#�ӭY�>Lv-\�ˁ\\>x� �����YD�\����\�+կ\�\�\�\�ۛ\��c\�\���\�\�$k��aB�\��j\ZPS�\�H���\�\�4\���o\�M:�ZD\�Ap_\�\�������W\�\'`�}21]\�\�.��q�\0R�\0��j>\�%\��|\��\�\�\�=\�n4\�O��R�O.E\�M\� zv���@٣�ʰ[��\�;\0O�\��\�C�\�6\�֠!�e\�$ߜR�do��}��\�X�+��WW\ri�	O���+�\�8�\0>\��\�	\�k�n-]�\'ޞ\�WAПWj�4�\�C$\�\�Fq�1\�\�\�иW�%\����Yy=\��d��\09gF\�o��/\�BtT\�ֆv\�\�J\�20?�\�\'�\�ַ\�d�KC$ғ\�y�̗#�}�\0\�j�wv�\\���\�H�\\<�H\�\"�P�\�\�)\�JSW�\��}�K�\�zDQ䘎p?\����nK\�{\�ޒ\�J���C�\0֮\�\�\�\�+\�#	$�\�`�\��j�*��n��hs\�6\�1�����>�j�/w�1\�*F���\�썬|u#\�\�;3�\�s\\\�O>\�\�\�ًy\�\�\�\�Klf� ��cdl-�\�q\�|u\�\�ZH@X\�T\�<֐\\�N*�s��\�;B\�OЫ\�\��\�}�\�K(\�S���dS�G_z�$q�����SW7bܶI3�(9\�\�oa,h\\�;rx<�\0*�\��\0\�o΃#�\�؃ؓSgܫ�\�\�f#\�G�\�$\�\�U<�u�\Z�\�/ƫZ\�\�̣q*H4�v�\�W�L&\�\�v���4\�	�pI�U�E\0�M�\�\�5m\�\�nm\�\\l\�\�?�7\'\�\\�\�3�0?ua�Ԗ�l\�[\�]�w��\0X՝Ed�\�\�~a�\�\�5gEi\\n\n8�ϭ[�\�3P\\\�f4\�3E+#�W�班\"��/\�\�1\�޶\�Q\�\�R\�_5Gι�\�k7$\���\�N2�&QQd\�Zt\�\�\�\�6�\�\�\�����\�0ﴇ<bOO­\�3\�m\�m$g98\��k1\�%��ŷvy\�\�j�&\�r\���\�Y$}����\0��\"Ү\�i\�I&��&\�t\�V�~�\�\��\�k��M(c\�\�}o`��\�\���\�fQ\'�5,\�L>A�8ɪ\�\�W~\��x5�pLV��\0�m�#:l�\�CU\Z9\n7Q\���� �����I<��=\�W\�\�\r[\�w��¼z\�1Z-�	fuy\�֗Qf\r	z\Z�I\'�O��I�\\�L��^�%\�\�\����w\�\�~\�!`7|�8�X[\�7\���*\�f�#\�g��\0^��\�&V��\�}�\���H0\�x�E�\�\�\�\�g�\�V �_\�J�hooҫ\���p�s�\��\�57�Z�2e}:\�[\�yRH69\�#5_S�tً\�\�l�\�\��\�#��l�\�~\�ħV\�2�\0�A�\�#��2��\�\ZK^�f�\�$��k7)V\�\�\�\�V�\�v\�\��\�Eg\�4�Zs�\�\��5�\�kf\�9G\r�\�X\�A\�\�H\�\�+�=�\�O�bzm\�f3\�a[&�\�!��#��n�Q\�\�\�\�\���\�z�FJ\�¢w��Tf��n\�bL��ֲo\�\�\�ՠ0h\�\�1m3A�\��q\�\�Iё����[Ve�M�\���I��\��\�]��];\�\�\�\�\�%�M��g��\�%��b[��\�����pU9\�~URp\�%F[)�\'�RH\�M\�\'�Z��Cwn\�i\�\rc#\�:�\�\\m\0\�\��楱\�-����M��F~��\�\�nX\�b\�\�du`%G�\��z}�\�九�,[7>cϷ\�^.;%��8��:(�B\�\�E@0?\Z�m;[L�/N�{Vv\�>w��vm\�\�=\��1Rq,\r\��e�� �ǜ��\��N:��:Tc��f\�yח�Xm\�w�辻�\�\�4@��9 c�ֹ\�-\��{�[t�\�r\n��TG��\�ԑ\�=�\"K��\�>�Q�\ZP�\n�pkr�֥�[\�l�珩\\�\0#Zq\�X\\(c\�?�*\�\�)E\�\�\�3 �.\��\'?{�\��\�)�\�\�ג1�\��\�}����Te*8�I?u�_�6o�lRD��� t�I��{I\0\�\"\�zg�\0\�Xw\�9U\�^ՌT�\�x\\\�A�&2[\��\0���)g�nbp�\\u�\'�Qa\�QTIV\�\�\n��k�\�%\�Gˁ��V\�\�\�v\�M�	J\�t�i\�lae�ϔ�\�<\��Tl\�g��h#l|�����i\�j�n#;�\�0;�\�kKs#Ǌ\�\�\�\�\rU�Mu:ao��t�#�\�4�w)�ӡ�\�\r�[\�c{bH�\�\�\�֨i,\�P�OO��¬j0\�\�\��F^9U\�\�\�J�Ԩ\�%�Z\�M>�𲭳�\nq\�\�\�iQ[ȭ��\�\�\�}��A*)f�E�� \n�n\�2F\�/<sY\�v��\�\�s\�\�ѷ,N\�J�VI.&Q�\0�����]V.#�~�=�z/\�X�[hp\0\�?JڝS\�>\�\�\�nЉ�zU�,��s\�ڭ\n�rw&z皶@�Z\�9�\�){\�R\��bQE��\�|�\�?\"v\�6~R�M0�5\�c\'���Z�\�)��}\�ʿ#��+b9!\�ad�6Ƚ�>��\r>\�2sl\�v\��\�,\�@\0�\�e2\�&Q���\�\�[�Ks�T\�څ\�ol�F:�zTv�\�j\��±c��\�\�\�\\\�y$��T\�#\�\�S\n\�ZN\�\�\�OK�������r�`\�U#��\�֮�.\����0�\0\Z��Z�\�u§��sgԛ�Y�\�tH�l\�\�5�������\�%�ߜ4R\�v=\�\�\�\�r����U\�c\�\�&�\\^2{�׫N\n�R[#9{\�F/߉OP{�b�\'���S6e�\�*D���fHZ){\�R((\�R\��d�\�;\ny���\�)]̤\�4o(A\\�1\�\�i\�D\�]J��\�UԒm�=ǯP*�/rFծ\"�8C�\���\�\�\��*����0�+���¦@\�s�?SKkw�\�[�p	 �\�\�F�N�\� \0\�@�\0\�\�BKtK�\�܅/^	\'ur�\�wH�99���qjwr�\�N\�\�\'���^9�E+\�^C��\�Wy\�\�y\�\�=�\"%VF�2\�^�dz\�\�nw&�x�1\�[̐��\�\��\0�*���))1	p��p8�\0\��\�V\�+Ɂ�-7m\�H�\�Gow8�@�|)%A_\��Q�\r_s,\�\�\'xݜ@Hp:\�8#\�5f�Et\�\�+,�����\��\0[�U�\�3���\�s\��\�\�\�2���\�\\\�q\��\�q(�\�s>[[���\�s��8Q�%V\�\�\�Y[�9E>��?-ě\n�ޝj\\�Z�.�h�\"\�r>\�SQK���q\�)]f]�8���\��h�r%\�\�i\�6\�|~�XX\�\�`z�R�}Jw��4�^i\�\�\0t\�\�\��\�v$��a�\��2\�\�\�\���Ip~\�`�tf\��\�C\�q/�\�9�����\rP�\�2\�\��Z�`\�l@~�\�k�\�uht\�\�x\�夙c\�9�+tD\�VgW4\�\�p\�.�H\�\�Q\�W�x\�N\�u)\�.R���ȩ푃�\�ƪ�F\�\�t�\�3\�\�3G�(\�\�9\�8�jύ�\�\�^\���T4�ܯ������1Њ��\�\�~GC\�\�\�\�\�]\�źJ�o\�ï?w>��\�ֵe�i\�3Y\�]#l>F6\�ҹ\0\�Wzo�5ȥ��I2P���қ\�\�\�\�\�\�\�ع�\�\�{��>�\n�\���u�m�W\r\��1\�K\�w��\�\�V\�i*ă���\�\�ޭ�ëk\�\\�\�Ř��Q�*\�L�Ðzb�j\�;�T�\�5g\�#9\�\�\�2\�$\�̕���CQ�U�K�!�\�Q�;Q-V���V1�fv-\�0\��\�C�\�z\�浆\�\�\�O8*���/�\�3߱��)����___©��\���o§��8��]�On���\�BZ���^}�`\�\�٭+�ŎB� �{��gq��\�\�X�(��\�N�\�\�\�\�\�ؓ�л�feO!P�\�A�4\�Ŝ\�|CӈɆw�D\�\�\r\�\�\���Q�\�g�[��:�ֱ5-{J\�5��w\�\�\�\�#ʞSq\�x\��\�\��l\�1�4\�f\�E�\0č�[9M\�\�p}>bi|-���\�\�3`A1�sٿ��*_\��uZ��\0�nH#��+[i\�\0�#�E\\U\�fc98\�s�\�Y\�>=V�-�|y\� \��\�	�\�>��\�\�?\�ڒ\�Ė�\�\�ܻ.��C\�ボw\�ex{W�\�t\�خ\�6I!\�\r�\�\�\������<�\�\�̵\�\�8��u�#�P:.{;��ʳ<?=\�	,n��[N�+��ֵ-�\�d�\�M�F,�\�~}�Wck\�\�\rx��Bk+�°�\�\����\�?u�m�9\�j.\�kOSJy\�Zo^A�j\�Jlt�\Z�\�\"�\0=�*�\r�,��IϥK�֢���b�N��7\�+\�(\�c\�l?Jl�\�C��\�\�2w�=\���\�3K�^�����\�-\�_\�\�\�\��\�c\�֐\�EQ�V�)\�\�\�\0�_;�r\0\�\�G\�M�a�p�$I��\\r\�\�6J\�\�\�)<\��9+*`aO9 �\0�θ}7W�\�C\�O0/#�\�q�k\�$E��PWЌ�\0��\�\'ҟL\�\�t�6\��\��{�_֮-���Z\�G@��.mv�~A<`p=;�\�\�ϩ��yv\�Dd*z��\��\0|�\�W+�\�&ЬW�lc\�}?Z\�x\'M\�IR:g�\�:��3徂Ty-�~s\�r\0\�=��\Z\�7{�ŀ\�,�\'�\�1\����\�Nv�m�\�����ZG^�\�Ȩ\�\�͗UQo2,m�\�^\n�3�q\�\�==i<4]���\�$\�\�\�\�zz!\�ߨ�\�+�\�\�AG9\�x\�\��\�\�ZZ\�X$\��\�\�\�)�{��h}Ȥ����q~\�\r2���\� ��:�U\��qu\���F�\�\�?�oʣ�Oݜz\�mBِ��m\�}ݏZ\�\�zq��j.�	�X\r���v�zg\�\�[ch�v\�(ⱼ;��d�\�E�쭼\�ֺ:\�r��\�G�\�',NULL),(26,'The Psychology of Sex Differences','Eleanor E. Maccoby','Stanford University Press','1978','Psychology',5,'0804709742',_binary '�\��\�\0JFIF\0\0\0\0\0\0�\�\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342�\�\0C			\r\r2!!22222222222222222222222222222222222222222222222222��\0\0\�\0�\"\0�\�\0\0\0\0\0\0\0\0\0\0\0	\n�\�\0�\0\0\0}\0!1AQa\"q2���#B��R\�\�$3br�	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz�����������������������������������\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�����\�\0\0\0\0\0\0\0\0	\n�\�\0�\0\0w\0!1AQaq\"2�B����	#3R\�br\�\n$4\�%\�\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz������������������������������������\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�����\�\0\0\0?\0\�Siz�յԒ+m#�	�����r��z���qWJ\��=]��1��v:\�\�:^�j~\��\�KƱ�I\��\n\�\�\Zai��I���G�rG\�+qћ<5��\��(�|3x5/�\�\�y�O\�ۜUc��\�d���MLU�B�:r:��\�X�Ofd\�\�n�\�IKI[�)���\�Gj(\0�R���\0�R���連�In\�c�\0xTY�\r6\��匶*�\�\�n\\��\�U$�۱pM�\r��\0\����r?\�ӵ�t=\���\�\�\�%�7,\'�O��\�\�q\�T�T��0�\\3*ઐ0��⸩\�n0I�\�s�uR��OSЋ�\\\'��`Mϟ\�+�����D�\�!��\�x� ���\��\�ߙVSu\'��u�\�sj7w4O1\�ٷ2��\0*i`�O{=�\�\�F[_�\\�))ѡ�E@Tݎ\�Vo\�۝2T�\�*��\�+g�\�\\⤢ެ\�Qm^\�sK�gJr.\�\n\n�\�c��\�=���_\�:o�\��U2T\�֩\�\'�\�\����q@�\��P���&h\����\0\�\�\����\�\�k�\�\�[Ks-\�\�Č�T��3\\�\�\�3�\r�T\\\�\�X�y\�n�Fג��	�\�\��\�T\�\�,m%��m>X\�?q8͎\�ZǷ�&�\nM(�\Z@|đx\�rzWP�\�Z5�\0\�\�k?,�2\'�\�\\U#*s�]\�n�?\��jq�[i\�\�լ,\�4\��kl���\�c�3��_ʵ��t\�\��\�.�I�y,N^\�j��\����ag5¬\�\\��6;~G�Oi�\��\0\�C�\�<���1�ʍ��[8�\�\�\��EF4\�J\�ߑ�ϦM�y��z&cFb\ndz��\�]���\"����\�|\�\�Ir6\�qҰSK�\r-�\�X\��\�$3.6\�ry�\�Σuk/�\���\�\�\�\�1�q*�>\��]K9��\�\��t\"Q\��Ё|?gީ,�\�[\�\�c\�ۻ��S]:\�P\�\�\�\�p,\�AN\��\'\�\����Z\�q�[}�\0�K�_\�R�a}x\�Y\�ҥ��.�\�ur\�1\"\�\'�:w��J��n\��\�m\��\�\�-5�\0�jj�jjzƝo!\"5���\�6�\0R+.\�\�O\�\�K�?\�ûʒ6m\�iۓ�\�j\�j�p\�Vs�Z%�wI��A��Z\�\�\�\r3W\�o�.a\�w�VEc&[w\0\ZΜ��\�\�O[�%;龾�+\\閚^�ks%�\�O+�r\�\�\���z>�m�YZ}�nCd�v?�\�O�\�m\�d���I�\�\�&\�	c\�A#�\�\�m5��ڊ\� �\rȸ\�\�;\Z�Nr���\�\�\�\�L������b\�i��\�K\�)�S\�\�#?��\�s\�k��\�\�wD�9U�\��\�\�k2���k?�\�n���O*\�	\�0m͹;\�\�ڜؔ�d�\�ҷ4�2ٴk�N\��VDq� g\��u\�V���SsvF\"#I\"���~5z�F�ӣ-;C��r�r2;{\Zٳ���N�\�\�T�\'Ut\�O͌�������\��_,n\�\�\�\�\�z\�X�R��t\��\0#wAB���Bi[� ��\�\�H\�l3\\\\\�.=?�ej6W67[.�\�H<̫gp=�\0\Z\�\�,\�\�~\�ɣ\�#�x�\�\�\�����v\�]n\�\��\�ρ`%FӵH\�\�ƞ&1�����Z\�\�Ɠ�)EF:�,\�\�rY�<W[�\�Ԗ];\�\��\��=}zTPh\�v��\�ym�c�\0�e\���t�u>��\0Ø}Vofs�5=\�\�=ӛHLPtPX�~�j�\�]q�2��s�g`ɣ4RUғR�\�\�\�\�$(#Y#�fTt`�t\\\�u��mv��\�bۏ}Ǐγ�+��f�\�f���}7\�[\�om\��X�\'ڳ�Ep�+D�M\�q�\�-\"\�	|={�4�햌;\rӿ\�~�\�ⴴ�MYd\�g�Z?���Ǳǵc��/7kQrR\�U\�nY\�Ce�Ae,\����]�\�\\\"\��~���S,W:���h��\�U\�H<�\�kM\�\�\�k{ʼ�lI�)�Z,�Ww\�ʖ\�B�a\'��r\�\�Q\�\�׮�ͧ*���.�\�Z�\�l\�#�}>{DS�7rr\�+bz\�-^\�\�M�\�&sз\��\0\���\�\��9����y\�Q[\�\�]\�\�\�dT\�h(\��ݽ\�x\�T\�l�3\�\��)T�\'\�ԓv\�\�%Q{Ѝ��>\�\�j2]\�F�}��\�\�N=j\�a�\\G���\�����\�)n\�>��\�)\"\�\�0`�M\�n\�񴥡�\�r�\�jZ\�m����!�9a\�Qу|��V\�\�\�Һ��RM\�\�n\�\�B)1KGNj\�b���c�SE����c\��\0u�r�\��9�\�\�p\�3\�\�@B1\�^��q\�S���:\�\�*����̺\��^�Lq\�k�[\�x�\�9`�@�+�\�C}~�\�\�\Z~�3^<�\0h�F\"�4a۩,*΍���u}}8W�`\"�\�\��W&�\'8���\�oFQ�Qo�\�\Z,��\0��\�c�7`��\�w\�ޖ\��&yvŸI�yk�\�/\�\�R\��\�\�E��{�2\�n\�ʍ�\�\�v�;\�$\�̚oڗ\�fݸ#m0>�2�ܛ\�\�\�)MY+\�f�\��zeƛl��\�!@d\�w~5%���n�O\"i�H\�\�\n�)\�g\�\�N�\�X63=\�\�\�L��r8\�@\��-5M&\�MC\�*�	\ZێOJ\�Ө\�w\�\�+��-�\0\0�F\Z~���\0�ĒZ\�.��\�\�\�z�\� \�6�pn\�k\�}А\�͎8�b�\�u;}.\�x�}\�N�\�I�z��L�L��9��\�ʨ؍x\�q\�\�[:MM\�m-\��\�2U�\��\�\�Լ�M{L�[u�^\�K\�\��\0ק�6\�\�\�X�H#\�\n~\�J\�)ٸ\�\�5��k7z��\�s6\�vԡ\�:Tϯ\�K=�m+�/��;B��\0\��gUF6Om~�\0\�6\�wv�\�\�+ir\�kYY\�A�\�7g�B�o\\��~UqZ\�jڄ\Z�ւ̍�ǹ~N\��P�Դ\�*O,,\�+!2ʈG\�s\�H\�\�5֑��w}+���\\�&#�߈�\�ZJ�svM.��\�\�\�3J*\�_�\����\�ӥ��.��I\0]ӡ N;�z\�%a�\\\�\�o0�񭈮t��\�\�)�ݠQ\�F��\�\�\�ߞ���]��Q�\�&\�1�l\�F3Uk�Un���wo\�BqF+\�@\�\�\�Q�\��i�t��\�\���\0�)�\0+5����ͱF+\���ϔ_��\�\ZO��\���\��R�\�}B}\�\���\�\�\��@\�\�q�\�x�\0�\'�#\�A�\0�8�\0\��4jS\�\�\�\�9\�7�#�\�$�\0˚g�\��\0\Z_�G4��\�O�\�Ɵ\��>\�>�>\�\�lsJEzG�#�O��/�\�\�\�G�#�W��\'�\�\�\�G\��.\�_P�ty��zפ�\0\�;��\0ϔ��\0\Z?\�\�\�\�?\��\0�ڔ�0�ϟty�)1^��\0\��\0>Q�g�h�\0�I�\0�(�3G\��?\�a\�	\�G�\�\�_\��\�_\�\�\��4\�\�\�(�>\�\� �y���O�X��>\�\�S���\�\n\��\�ܻȲ2ƭ����I>�{�5\��,�\Z\�˨�i�\0֤�\0��\�P\�e\�1�\0]?�\�\�$z�԰�R\�V��1��(T\�\"\�}?��\�	\��7+=\�c(�)+�d\�陜\��\0\�_0\�\�\�<x�Q�\0���)Ŀ��I��\0\�A�\�}o�n�3\�K?�9����\�X�Ӧ\��\�\\~\rӢF@\�{al\�B\�>�xy\��\0\�e�\���mG��\��g���\�ڏ��\�Hg\�zv�o�ܛ�w�v��ݝ\�rB�8�����Z�\�|K\�?\�3\�?\�\��\0�G�,�\�ͨ\�\���\0}�Fk\��Y~5�j?\�\��\0�G�,�\Z�����\0m�\�\�Fh���\�d�\���mK���O�Y>5|M�\�\�\0�+4f�5�\0��\�_��/��K�\0+Ɯ\�M�\�\�\0�\'4\�y��?\�d�ӏ��\�/��O�\�g�bt�|I~Y[p\�~�s\�#\�d�WEa\�;�\\k�7S!�魼�\�\�O�]O\�Frp=\�\�sf�,|K�i\�a��5�2��=�nC�\�XRG\�H\�֘��V�k�i��\�[\\@\�4\�U����E\�<�\�T�@\�\�R�\0�q�ys0�̼k�&\�\��\0�\�6\�\�Ԏ���\�~,\�5~\�u%\�鐘�v���[qQ�<��ܞ9�?�\�[I&�\�\rl�\�\�Eb�T,B6\�	��\��%\�íV\�\�\�\�\�\�N�_1ev���$\�\�\�\�6=�\�Ēx\Z9`ѡ���\0O\�\�\�\�fP��e/��n<���q\�ro|c�\�6�\�W\�Hn3框0�\�	\�G%�s\�\��\�<S\�t�Y�d&(R\�\�K8\�E̪�\�O]\�9=��@[?�@c\"\�O(\��\�\��\�\�?\'x�)f�s�A��Ձ�\�,�\�D`$n	GQ*c�8\�A?��]h&?h�A�%Y*�{�\�9^3\�\�QU���B�կ\�$�}�o�P�6�\0u`�\0tzP��uإ1<\�w�\�~kd~\��\�v\�\�!\�\�#��\��:\���\�\�~U�DG�1�\�\�\�\�X�[\�\�\�p\�\�\�t���r-R@���f�O���Z�m\�~-G\�6�\�-\�	\�w\�c\0`vV*\0㞔���5��wM2\�.�+5�\�\�(v\�̌\�\0w=ǭg]x\Z\�\�]�Դ\�\�[�f�����\�\�W\�%��\�/<\�\�\�\�ysi\���`[Kٮ\�2<��\�ʓ���w�\�x\�Ğu\�W��30��Y��\�hÓ,�#��\�\�\�Ǌ\�9�m\�\�N�\�\��m7\�.�v\�=q�,\rk��Oy=�uEXىb\�,�eG@\�{ԇ\�>$\��G\�/\�\�\�\��\�3w\��\��l\�\�9\�U]_\�7�Ɲ�Xܒc\�\�hЖ\�\�N\'\�*�=��\0\�#\�\��\0X\�z\�\�ڀ$\�N\�\�\�zχ\�/sgv�ײ��/n��s	F�H9\�\�rk�\'\�`z\�^;\�|:�w�[42Gw椱\�(a�i6GT*?\�8�\�[Ѯ\��\�\�\��\�t订�-�J\�\�T�9ݵ�v�qئ\�4�ze�\0��4\�cyy�)\�;�\�pL,>\�`c��Ş�\�5k->6�{�%[�k\����D?\�F@\�\��^u�Fx�G�\�\�R�I\�c�k��ŵ\�\��._)d\� \�\�r�ִ�\"\�Z�\�$\�\�>\�.~\�$0���iRSn\�$*\�i9\��\�\�HN~�\�\����\��\0\�\�eʶ����>Yy\�̉�(\�Vn��\�\�j=CŞ\Z��X�C[\\��t\�b�f$P��ݔ\�ު+\�I��\��\�x\�\�W\Z\�n.��\��&�2ެ\�\�\�;�z�v��\n\�ּQ�\��v�\�\���u\01�iA\'�|�\����\�\nFi�\�@\�@�\�ފ\0:�J\\\�?�(#��\Z\0���)�\�3�i�\�B9\�<)�\�ޔv�\��\0��\�ځ	�\�K\�A恇j3�\�9\��R��@�\�I�\�\��\n\0\0\��\0Z��p+\�~\Z�5�\�k}o[��	\�6��9��\0m��\��\�ӫJ\�y�3�zcjOOΐ�\�ҁ�z\�H\�ӱ��\��iCH�#�Z1���\0\�~T�\���=h��\�3I�Fx\�\�F:\�;�(R�,�B�Ǡu�׫|\�<1��~�k\n\�rI��q*\�[?�\�ޞ�^�+���\\\�\n�\��\�0i��\�l�4���\0g�׺hOs#_\�F�``F�\��>Y_\�\��%�|\�?�\�]�1,1^������\� =EV`4�B0	\�\�E�#�ddqE\0\�\�`i�f�(\0Q�cQ@\0V�Rb�(ʻ�5�\�\�\�\�o\�XGs\�m�\�ү\�U_\��\0�\�E >�Ь\��`�\�N:�\�h��w�\�',NULL),(27,'Painless, Foolproof, Really Works Way to Teach Your Kids About Sex','Carole Marsh','Carole Marsh Books','1997','AIDS (Disease)',5,'9781556092473',_binary '�\��\�\0JFIF\0\0\0\0\0\0�\�\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342�\�\0C			\r\r2!!22222222222222222222222222222222222222222222222222��\0\0�\0�\"\0�\�\0\0\0\0\0\0\0\0\0\0\0	\n�\�\0�\0\0\0}\0!1AQa\"q2���#B��R\�\�$3br�	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz�����������������������������������\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�����\�\0\0\0\0\0\0\0\0	\n�\�\0�\0\0w\0!1AQaq\"2�B����	#3R\�br\�\n$4\�%\�\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz������������������������������������\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�����\�\0\0\0?\0\��(��\n(��\n(��\n(��\n(��\n(��\n(��\n(��\n(��\n(��3uho\��J�\�;���\�=�\�\�\�^6��{\�j\�8\�X��H_�\�8\�\�\�]�R�\�m5U��\�U\��z\�P-yx�D\�ڌa�}\�\�I\�q!#\�\�\�\�Eƥs��H\�B�3\�+|͵8	�,\�\�x玈\�:n͆\�*[qۓ\�y\��\0�=)\�\�\�$�$]\�36\�\�\�vz�\�\��h\�w\\3\�j\�0H�����\�NN\�\�0\�\�y\�u�]�tMj\�?��ZBG\�Nл{�� �\0NMt\�\�I*\�\�nu9\�v<\�\�\�\'\�>��DӋBM�&-\�\�a\'$�x\�9s�\�\\L\ZfP\�v���\�\�OO\�Z�sj��jI\r\�N\�T�\�V,n8o�A\�\�\�\�Jҏ\��\\EvZ\�ۗ\�)\�<u?�Z���\�3�{�H\�O=;�\n\0,\�8H�m\�]�\�\�\'��*\�PEPEPEPEPP\�\�$\�yIUf\�	\�\�5WY��\r\Z\�]65�\�b&a�\�ڹ+\�c\��h�-\"�-�I�B\�\�\���lt�rr0\0�\�2\���L�e�aq($.>`�\'�\0v�k��O6\�\�Cq\\4ڷ���\�R葭ڼB\�$e|\��\�\�a\�zqڗ�g\�	rPi\�Ь\�[i9�\�\�g较�YI\�\0;\�*yV\�\�9\�0Y0ܥO>�\�\�\0QE\0QE\0QE\0QE\0Uk��{D/q<P�fG�\�}Y��[J�\�4�\�\�\�6�\�\\M\��\�h�\�\�5k\�5��\�H�݄1_I�\���\�dn\�Ȫ\r�x�7Y\\���\�\�3acRـ\��es)�\\���\0\n\�M\�\�F�P6׎ZhL���癌�O^	\'\'�\�\�l2h�6�-^\�Z{,��\�\�mf�q\�\�Dd\�\�̿\�\0�\�W�3\\}�\�y�u�;�<��X*�\�p\�&p\0\��⫵���Y�Uf@�\�U�bs�\'>�\�>\�^js��cZ\�l4�W�c�k�<�\�\�9\0`\�\�v ����\0d\�k}\�:��w:�Es\�`\�X�\�否@\�)+>#\0g��\�\�a\r���\�upQO5\�\��\\|ꡎp6\�}O\�\�<3�heI�\�r�\��NB�+\�\�\��\�\�\�3,	۝�\�\\�q\�N��{KH,�X��QG]��ǦO�\0Z��(\0��(\0��(\0��(\�_���Ț\�\��\�;vo0�f�H\�\�ߚ-��k�\�P\�^�\�EųlV\�\�8�/l��TӮc\�\�\�\�\��8<\�\�1\��B��ѵ+kǙm\�g\�\�G\�eU\0\��0W�};P�״\�\�瀖K�w\'�\��\0���dO/�.w\�\�\�&�6��\�lVI#�# �L�	�\�m-\�\�\�1J��ª�Ag1��\�\'\���&�j6z��.\"\��y-\Z�Lۤv n\�(=99\�@4l\�\�l/\�\�\�\�a�@#P�1 `d<�j�o\�\�v��\�7\�aTO�=�z�\�@y\�\�V�\�cc����_ �\�&yq`O-�8\��\�\�_]_J�\�\�b�\�V,L�{\�>�\�\�@�\��\�s\� \�R<eD��Rz\�\�x\�\�Z}�N�	z�\r\�\�\� \�l�N�q���t\�F��_0�\�\�PǪ�y\�\�<1t]K~�j������C!/)\�c��OqӜ�\rm?]�Ԅ&%�<\�\��G�c<dzV�sZg��ӧҥ^H 1\�<���qQ����YGWK@Q@Q@\�J\�#Rs\\����_Z�\���i�\�l\�\�\'8\n<\�\�rz\�@\�T\�a�\�\Z�\�\�\�i,\���ma�M�@\'C\�\�@\����r\���,\�F\���ŗ?{<\�jX��f�Y K7Ux#�6]\�@$����I\�f\�\�\��(\�=\�е�9m\�\�VEbq\����\�R\�x~\��\01�\�\�v�A�\�\"���7��\�\0kڔ�u�\"\�3%\�҈\�9ڽIf�\��L\�T.u\�{g\�bV	�3ű��\0�\�8#gS\�\�5�s�\�^\�\�\\\�&xch\�e\�2p{\���Z����\�{yL����!ر*H\�\�=����j��\�j{[x���U+\�3�J�\0>\�9\�A����j]J\��\0Q6\�ȤrC��[�\�1��<\�:{@�m�\��k,\r����W�b��C\�9\�9�d\�\����\�\�[�c�bų,��� �\�z\�U���]Y\�\�%ݤrjq\�\�u�aai\�\�Tc����\�_\�\�n-��(\�fy\Z!�\��{�VF�h�a\�!�,F �OʣaA�)4�\�xK��\�	a�O��P\�~��\�qo�&,\�\\䣪�*GS�C\�M�j6\�v\�\�\�\�\�\�q�K��+r�\�h\�	\�p2If/\r�o	i{yl\�	?x�9v\�\�Sչ\�\�T�\��E�i��\�k�`X<�_s��\�\��\'�\�$�`]A\n7r{�u1�$����@Q@Q@Q@/�\�+Vkd6T\0Fx,2q�\�\'�f>��vh9�\�n��}=�\�\�h\�\�y�Ae\�\�FK	B��`\�F\�Qq��C�/�\�\��Pz�xo\�]2$�6ţ,��)Q\�zv\����=O^(\�M��J\�7p;\�\� \��\�\�PXi�\�n��s�?w\�\�Gڵ\0��\0fe��\����<��\�0��9܁�ϭY��\�\�J\�\�\�y8\�\�\�\��j�\n(��\n(��\n(��\n(��\n(�wV\�m��{%�\�7�@\�3�8\�:�\�\�1\�\�EErm\�\�.!1����U��\�\0s��8\�鞣$�?\�\"-��X$�[0��\��\�\�\�3@e\�\���\�\�\�\�	�o�ۼh\�9~0G<\�9\�\�l�x\�Mɽ\�w\r\�O�Ҁ:j+��\�:l�?\�\�\�-�y`�\��\�\\qWt\�\�\�\�\�\�\�+�\�Q���\�\0|��\��\0��\0ݢ�(\0��(\0��(\0��(\0��(��\�A�c\�>�(\0��(\0��(\0��(\0��\�\�u�[�߼s�#��>�\�\�ףJ\�j\�E�(�߷^o��0s�18<\�\��\�\�]b4��@\�o�g��{\��\0\�\�@\Z4Vi��ۑ�ܟc$`�\0\�X�%�\�\�f]/\�H.рOa\�\Z\0Т��\�v6�\0Į\�\�\\�IӖ\�.�6�q\�	vC�~\��+8_\�\�&\�8\�\� �>o\�}�\�q\�\�\�W,�p8<�\0�\0_��$��NWJ��\�\��u��\�\�\�Ti����\�D\�\�\�\0Ц�R\�@�5�/��_��˓\�S\��\�k6{�\�usռ\��z0h�\0C\�\�)�p/^jЋIg�A\�J�/��^�CA��\�V\�.P#88���\�P+:Y_\�\��%�|\�?�\�]�1,1^��^\�%QY�(��\0(��\0(��\0(��\0(��\0++\\�^\�aG�X*���Q@\�V	kH0]�\'Os[4QM���\�',NULL);
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

-- Dump completed on 2024-11-25 11:12:22