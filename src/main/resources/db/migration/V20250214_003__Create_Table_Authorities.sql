CREATE TABLE `authorities` (
  `id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  CONSTRAINT `authorities_fk` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`)
) ENGINE=InnoDB;