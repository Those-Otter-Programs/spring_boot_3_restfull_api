ALTER TABLE `members`
    ADD COLUMN `enabled` boolean DEFAULT true AFTER `password`;