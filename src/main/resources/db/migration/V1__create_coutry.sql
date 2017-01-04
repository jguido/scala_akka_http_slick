CREATE TABLE IF NOT EXISTS `COUNTRIES` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(2) NOT NULL,
  `taxe` double NOT NULL,
  `currency` varchar(50) NOT NULL,
  `symbol` varchar(5) DEFAULT NULL,
  `shortCurrency` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
);