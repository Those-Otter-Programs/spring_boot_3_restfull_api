ALTER TABLE `members`
    ADD COLUMN `account_not_expired` boolean DEFAULT true AFTER `password`;
ALTER TABLE `members`
    ADD COLUMN `account_not_locked` boolean DEFAULT true AFTER `account_not_expired`;
ALTER TABLE `members`
    ADD COLUMN `credentials_not_expired` boolean DEFAULT true AFTER `account_not_locked`;