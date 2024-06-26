 CREATE TABLE IF NOT EXISTS `LOG`.`Server_Log`
(
    `Time`    TIMESTAMP(6) NULL DEFAULT NULL,
    `Type`    VARCHAR(30)       DEFAULT NULL,
    `Content` VARCHAR(300)      DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `LOG`.`Client_Log`
(
    `Time`    TIMESTAMP(6) NULL DEFAULT NULL,
    `Type`    VARCHAR(30)       DEFAULT NULL,
    `Content` VARCHAR(300)      DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `USERS`.`Accounts`
(

    `Username` VARCHAR(30) PRIMARY KEY,
    `Password` VARCHAR(60) NOT NULL,
    `Role` VARCHAR(30)
);

 CREATE TABLE IF NOT EXISTS `IPS`.`Whitelistip`
 (
     `ip` VARCHAR(15) NOT NULL,
     `active` BOOLEAN NOT NULL
);
