ALTER TABLE `members`
    ALTER COLUMN `enabled` SET DEFAULT false;

ALTER TABLE `members`    
    ADD CONSTRAINT email_unique_uk UNIQUE(email);