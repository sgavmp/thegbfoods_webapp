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
-- Temporary view structure for view `ESTADISTICAS`
--

DROP TABLE IF EXISTS `ESTADISTICAS`;
/*!50001 DROP VIEW IF EXISTS `ESTADISTICAS`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `ESTADISTICAS` AS SELECT 
 1 AS `FECHA`,
 1 AS `NOTICIAS`,
 1 AS `TOTAL`,
 1 AS `RIESGO`,
 1 AS `ALERTA`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alert` (
  `id` bigint(20) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `words` longtext NOT NULL,
  `type` int(11) DEFAULT NULL,
  `title_en` varchar(255) DEFAULT NULL,
  `words_negative` longtext,
  `ultima_recuperacion` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `alert_score`
--

DROP TABLE IF EXISTS `alert_score`;
/*!50001 DROP VIEW IF EXISTS `alert_score`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `alert_score` AS SELECT 
 1 AS `alert_detect_id`,
 1 AS `title`,
 1 AS `fecha`,
 1 AS `num`,
 1 AS `score_avg`,
 1 AS `score_min`,
 1 AS `score_max`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `configuracion`
--

DROP TABLE IF EXISTS `configuracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracion` (
  `id` varchar(255) NOT NULL,
  `day_risks` int(11) DEFAULT NULL,
  `path_index_clavin` varchar(255) DEFAULT NULL,
  `path_index_news` varchar(255) DEFAULT NULL,
  `run_clavin` bit(1) NOT NULL,
  `run_search` bit(1) NOT NULL,
  `run_service` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `count_alertas`
--

DROP TABLE IF EXISTS `count_alertas`;
/*!50001 DROP VIEW IF EXISTS `count_alertas`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `count_alertas` AS SELECT 
 1 AS `FECHA`,
 1 AS `TOTAL`,
 1 AS `ALERTA`,
 1 AS `RIESGO`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `estadisticas`
--

DROP TABLE IF EXISTS `estadisticas`;
/*!50001 DROP VIEW IF EXISTS `estadisticas`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `estadisticas` AS SELECT 
 1 AS `FECHA`,
 1 AS `NOTICIAS`,
 1 AS `TOTAL`,
 1 AS `RIESGO`,
 1 AS `ALERTA`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `fechas`
--

DROP TABLE IF EXISTS `fechas`;
/*!50001 DROP VIEW IF EXISTS `fechas`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `fechas` AS SELECT 
 1 AS `FECHA`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed`
--

DROP TABLE IF EXISTS `feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed` (
  `id` bigint(20) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `accepted` bit(1) DEFAULT b'1',
  `actived` bit(1) DEFAULT b'1',
  `char_set` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `date_first_news` datetime DEFAULT NULL,
  `date_format` varchar(255) DEFAULT NULL,
  `isrss` bit(1) DEFAULT b'0',
  `languaje` varchar(255) DEFAULT NULL,
  `last_news_link` longtext,
  `min_refresh` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `news_link` varchar(255) DEFAULT NULL,
  `num_new_news` int(11) DEFAULT NULL,
  `selector_content` varchar(255) DEFAULT NULL,
  `selector_content_meta` bit(1) DEFAULT NULL,
  `selector_pub_date` varchar(255) DEFAULT NULL,
  `selector_pub_date_meta` bit(1) DEFAULT NULL,
  `selector_title` varchar(255) DEFAULT NULL,
  `selector_title_meta` bit(1) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `ultima_recuperacion` datetime DEFAULT NULL,
  `url_news` longtext,
  `url_site` longtext,
  `for_alerts` bit(1) DEFAULT b'1',
  `for_risks` bit(1) DEFAULT b'1',
  `feed_type` int(11) DEFAULT NULL,
  `extraction_type` int(11) DEFAULT NULL,
  `is_auto` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_crawler_news`
--

DROP TABLE IF EXISTS `feed_crawler_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_crawler_news` (
  `feed_id` bigint(20) NOT NULL,
  `crawler_news` longtext,
  KEY `FK_iamav9hnska2x68289ox3jmeh` (`feed_id`),
  CONSTRAINT `FK_iamav9hnska2x68289ox3jmeh` FOREIGN KEY (`feed_id`) REFERENCES `feed` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_feed_place`
--

DROP TABLE IF EXISTS `feed_feed_place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_feed_place` (
  `feed_id` bigint(20) NOT NULL,
  `feed_place` int(11) DEFAULT NULL,
  KEY `FK_99ghh6kfapepblcdwlg75489d` (`feed_id`),
  CONSTRAINT `FK_99ghh6kfapepblcdwlg75489d` FOREIGN KEY (`feed_id`) REFERENCES `feed` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `country` int(11) DEFAULT NULL,
  `distance` int(11) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `query` varchar(255) DEFAULT NULL,
  `ultima_recuperacion` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `content` longtext,
  `pub_date` datetime DEFAULT NULL,
  `site` bigint(20) DEFAULT NULL,
  `title` longtext,
  `url` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news_detect`
--

DROP TABLE IF EXISTS `news_detect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news_detect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `date_pub` datetime DEFAULT NULL,
  `false_positive` bit(1) DEFAULT NULL,
  `history` bit(1) DEFAULT NULL,
  `link` longtext,
  `title` longtext,
  `alert_detect_id` bigint(20) DEFAULT NULL,
  `site_code_name` bigint(20) DEFAULT NULL,
  `fal_positive` bit(1) NOT NULL,
  `mark` bit(1) NOT NULL,
  `score` double DEFAULT '0',
  `score_lucene` double DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_4bnxhj3cynx0d5u1d9w3by23h` (`site_code_name`),
  CONSTRAINT `FK_4bnxhj3cynx0d5u1d9w3by23h` FOREIGN KEY (`site_code_name`) REFERENCES `feed` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1179656 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER update_score BEFORE INSERT ON news_detect
    FOR EACH ROW BEGIN
    SET @prov = (COALESCE((Select 1 from news_detect_locations where news LIKE NEW.link limit 1),0));
	SET @feed = (COALESCE((SELECT feed.type from feed where feed.id = NEW.site_code_name limit 1),0));
	SET @alert = (COALESCE((select COALESCE(alert.type,risk.type) as val from alert, risk where alert.id = NEW.alert_detect_id or risk.id = NEW.alert_detect_id limit 1),0));
    SET NEW.score_lucene = NEW.score;
	SET NEW.score = NEW.score * (@feed+1) * (@alert+1) * (1+@prov*2);
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `news_detect_locations`
--

DROP TABLE IF EXISTS `news_detect_locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news_detect_locations` (
  `location_id` bigint(20) NOT NULL,
  `news` longtext,
  KEY `FK_4bh4kg5rjmrkujtedlftd7pig` (`location_id`),
  CONSTRAINT `FK_4bh4kg5rjmrkujtedlftd7pig` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `news_locations`
--

DROP TABLE IF EXISTS `news_locations`;
/*!50001 DROP VIEW IF EXISTS `news_locations`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `news_locations` AS SELECT 
 1 AS `id`,
 1 AS `create_date`,
 1 AS `update_date`,
 1 AS `version`,
 1 AS `date_pub`,
 1 AS `false_positive`,
 1 AS `history`,
 1 AS `link`,
 1 AS `title`,
 1 AS `alert_detect_id`,
 1 AS `site_code_name`,
 1 AS `fal_positive`,
 1 AS `mark`,
 1 AS `score`,
 1 AS `location_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `risk`
--

DROP TABLE IF EXISTS `risk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `risk` (
  `id` bigint(20) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `words` longtext NOT NULL,
  `title_en` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `words_negative` longtext,
  `ultima_recuperacion` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `risk_score`
--

DROP TABLE IF EXISTS `risk_score`;
/*!50001 DROP VIEW IF EXISTS `risk_score`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `risk_score` AS SELECT 
 1 AS `alert_detect_id`,
 1 AS `title`,
 1 AS `fecha`,
 1 AS `num`,
 1 AS `score_avg`,
 1 AS `score_min`,
 1 AS `score_max`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scrap_statistics`
--

DROP TABLE IF EXISTS `scrap_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scrap_statistics` (
  `fecha` date NOT NULL,
  `total` int(11) DEFAULT NULL,
  PRIMARY KEY (`fecha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic` (
  `title` varchar(255) CHARACTER SET latin1 NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `words` longtext CHARACTER SET utf8mb4,
  PRIMARY KEY (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topic_references`
--

DROP TABLE IF EXISTS `topic_references`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic_references` (
  `topic_title` varchar(255) CHARACTER SET latin1 NOT NULL,
  `references_title` varchar(255) CHARACTER SET latin1 NOT NULL,
  KEY `FK_hd1weefc5ba25bi7k0xok63k0` (`references_title`),
  KEY `FK_hwx2kujykc5pa14rqcd0yq97c` (`topic_title`),
  CONSTRAINT `FK_hd1weefc5ba25bi7k0xok63k0` FOREIGN KEY (`references_title`) REFERENCES `topic` (`title`),
  CONSTRAINT `FK_hwx2kujykc5pa14rqcd0yq97c` FOREIGN KEY (`topic_title`) REFERENCES `topic` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `user_name` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_admin` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_name`),
  UNIQUE KEY `UK_aqh772jkrnchd3vbx8n1qa7aw` (`user_name`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `ESTADISTICAS`
--

/*!50001 DROP VIEW IF EXISTS `ESTADISTICAS`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `ESTADISTICAS` AS select `fechas`.`FECHA` AS `FECHA`,(case when `fechas`.`FECHA` in (select `scrap_statistics`.`fecha` from `scrap_statistics` where (`scrap_statistics`.`fecha` = `fechas`.`FECHA`)) then (select `scrap_statistics`.`total` from `scrap_statistics` where (`scrap_statistics`.`fecha` = `fechas`.`FECHA`)) else (select `count_alertas`.`TOTAL` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) end) AS `NOTICIAS`,(case when `fechas`.`FECHA` in (select `count_alertas`.`FECHA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) then (select `count_alertas`.`TOTAL` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) else 0 end) AS `TOTAL`,(case when `fechas`.`FECHA` in (select `count_alertas`.`FECHA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) then (select `count_alertas`.`RIESGO` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) else 0 end) AS `RIESGO`,(case when `fechas`.`FECHA` in (select `count_alertas`.`FECHA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) then (select `count_alertas`.`ALERTA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) else 0 end) AS `ALERTA` from `fechas` order by `fechas`.`FECHA` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `alert_score`
--

/*!50001 DROP VIEW IF EXISTS `alert_score`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `alert_score` AS select `news_detect`.`alert_detect_id` AS `alert_detect_id`,`alert`.`title` AS `title`,cast(`news_detect`.`date_pub` as date) AS `fecha`,count(0) AS `num`,avg(`news_detect`.`score`) AS `score_avg`,min(`news_detect`.`score`) AS `score_min`,max(`news_detect`.`score`) AS `score_max` from (`alert` left join `news_detect` on((`alert`.`id` = `news_detect`.`alert_detect_id`))) where (`news_detect`.`alert_detect_id` <> '') group by cast(`news_detect`.`date_pub` as date),`news_detect`.`alert_detect_id` order by cast(`news_detect`.`date_pub` as date) desc,avg(`news_detect`.`score`) desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `count_alertas`
--

/*!50001 DROP VIEW IF EXISTS `count_alertas`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `count_alertas` AS select cast(`news_detect`.`date_pub` as date) AS `FECHA`,count(0) AS `TOTAL`,sum((case when `news_detect`.`alert_detect_id` in (select `alert`.`id` from `alert`) then 1 else 0 end)) AS `ALERTA`,sum((case when `news_detect`.`alert_detect_id` in (select `risk`.`id` from `risk`) then 1 else 0 end)) AS `RIESGO` from `news_detect` group by cast(`news_detect`.`date_pub` as date) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `estadisticas`
--

/*!50001 DROP VIEW IF EXISTS `estadisticas`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `estadisticas` AS select `fechas`.`FECHA` AS `FECHA`,(case when `fechas`.`FECHA` in (select `scrap_statistics`.`fecha` from `scrap_statistics` where (`scrap_statistics`.`fecha` = `fechas`.`FECHA`)) then (select `scrap_statistics`.`total` from `scrap_statistics` where (`scrap_statistics`.`fecha` = `fechas`.`FECHA`)) else (select `count_alertas`.`TOTAL` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) end) AS `NOTICIAS`,(case when `fechas`.`FECHA` in (select `count_alertas`.`FECHA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) then (select `count_alertas`.`TOTAL` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) else 0 end) AS `TOTAL`,(case when `fechas`.`FECHA` in (select `count_alertas`.`FECHA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) then (select `count_alertas`.`RIESGO` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) else 0 end) AS `RIESGO`,(case when `fechas`.`FECHA` in (select `count_alertas`.`FECHA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) then (select `count_alertas`.`ALERTA` from `count_alertas` where (`count_alertas`.`FECHA` = `fechas`.`FECHA`)) else 0 end) AS `ALERTA` from `fechas` order by `fechas`.`FECHA` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `fechas`
--

/*!50001 DROP VIEW IF EXISTS `fechas`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `fechas` AS select cast(`scrap_statistics`.`fecha` as date) AS `FECHA` from `scrap_statistics` union select cast(`news_detect`.`date_pub` as date) AS `FECHA` from `news_detect` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `news_locations`
--

/*!50001 DROP VIEW IF EXISTS `news_locations`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `news_locations` AS select distinct `news_detect`.`id` AS `id`,`news_detect`.`create_date` AS `create_date`,`news_detect`.`update_date` AS `update_date`,`news_detect`.`version` AS `version`,`news_detect`.`date_pub` AS `date_pub`,`news_detect`.`false_positive` AS `false_positive`,`news_detect`.`history` AS `history`,`news_detect`.`link` AS `link`,`news_detect`.`title` AS `title`,`news_detect`.`alert_detect_id` AS `alert_detect_id`,`news_detect`.`site_code_name` AS `site_code_name`,`news_detect`.`fal_positive` AS `fal_positive`,`news_detect`.`mark` AS `mark`,`news_detect`.`score` AS `score`,`news_detect_locations`.`location_id` AS `location_id` from (`news_detect` join `news_detect_locations` on((convert(`news_detect_locations`.`news` using utf8mb4) = convert(`news_detect`.`link` using utf8mb4)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `risk_score`
--

/*!50001 DROP VIEW IF EXISTS `risk_score`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `risk_score` AS select `news_detect`.`alert_detect_id` AS `alert_detect_id`,`risk`.`title` AS `title`,cast(`news_detect`.`date_pub` as date) AS `fecha`,count(0) AS `num`,avg(`news_detect`.`score`) AS `score_avg`,min(`news_detect`.`score`) AS `score_min`,max(`news_detect`.`score`) AS `score_max` from (`risk` left join `news_detect` on((`risk`.`id` = `news_detect`.`alert_detect_id`))) where (`news_detect`.`alert_detect_id` <> '') group by cast(`news_detect`.`date_pub` as date),`news_detect`.`alert_detect_id` order by cast(`news_detect`.`date_pub` as date) desc,avg(`news_detect`.`score`) desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

--
-- Dumping data for table `configuracion`
--

LOCK TABLES `configuracion` WRITE;
/*!40000 ALTER TABLE `configuracion` DISABLE KEYS */;
INSERT INTO `configuracion` VALUES ('conf',7,'/tomcatfolder/app/lucene/clavin','/tomcatfolder/app/lucene/news','\0','','');
/*!40000 ALTER TABLE `configuracion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('jmvizcaino@ucm.es',NULL,NULL,0,'jmvizcaino@ucm.es','','$2a$10$RFsqRy8Kyjt5peoIvHe0iO36Eh3GPxxU6iIJvqfPnMIJsEZ7Cb.R.'),('user',NULL,NULL,0,'sga.vmp@gmail.com','','$2a$10$RFsqRy8Kyjt5peoIvHe0iO36Eh3GPxxU6iIJvqfPnMIJsEZ7Cb.R.'),('usergb1',NULL,NULL,0,'usergb1@gbfoods.com','','$2a$10$RFsqRy8Kyjt5peoIvHe0iO36Eh3GPxxU6iIJvqfPnMIJsEZ7Cb.R.'),('usergb2',NULL,NULL,0,'usergb2@gbfoods.com','','$2a$10$RFsqRy8Kyjt5peoIvHe0iO36Eh3GPxxU6iIJvqfPnMIJsEZ7Cb.R.'),('usergb3',NULL,NULL,0,'usergb3@gbfoods.com','','$2a$10$RFsqRy8Kyjt5peoIvHe0iO36Eh3GPxxU6iIJvqfPnMIJsEZ7Cb.R.');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
