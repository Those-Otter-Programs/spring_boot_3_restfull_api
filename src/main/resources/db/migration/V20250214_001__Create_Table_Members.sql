CREATE TABLE `members` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL, 
  `email` varchar(45) NOT NULL,
  `mobile_number` VARCHAR(20) NOT NULL,
  `password` varchar(200) NOT NULL,
  `created_at` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB; 
