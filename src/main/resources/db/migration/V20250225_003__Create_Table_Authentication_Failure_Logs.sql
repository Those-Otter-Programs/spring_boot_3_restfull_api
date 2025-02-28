CREATE TABLE `authentication_failure_logs` (
	`id` int NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(100) NOT NULL, 
	`event_result` ENUM('SUCCESS', 'FAILURE') NOT NULL,
    `remote_address` VARCHAR(100) NOT NULL,	
	`message` VARCHAR(200) NOT NULL,
	`auth_time` TIMESTAMP NOT NULL,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
PRIMARY KEY (`id`)
) ENGINE=InnoDB; 