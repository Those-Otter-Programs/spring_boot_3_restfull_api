CREATE TABLE `members` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL, 
  `email` varchar(45) NOT NULL,
  `mobile_number` VARCHAR(20) NOT NULL,
  `password` varchar(200) NOT NULL,
  `enabled` boolean DEFAULT false,
  `account_not_expired` boolean DEFAULT true,
  `account_not_locked` boolean DEFAULT true,
  `credentials_not_expired` boolean DEFAULT true,
  `created_at` date DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` date DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB; 
