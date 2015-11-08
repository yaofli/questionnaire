-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`               INT(11)          NOT NULL AUTO_INCREMENT,
  `name`             VARCHAR(50)      NOT NULL,
  `password`         VARCHAR(64)      NOT NULL,
  `autoLogin`        VARCHAR(128)     NOT NULL,
  `telephone`        VARCHAR(11)               DEFAULT NULL,
  `email`            VARCHAR(50)               DEFAULT NULL,
  `emailIsValidate`  BIT(1)                    DEFAULT NULL,
  `type`             CHAR(1)          NOT NULL DEFAULT 'g',
  `registerTime`     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastLoginTime`    DATETIME                  DEFAULT NULL,
  `lastLoginAddress` VARCHAR(50)               DEFAULT NULL,
  `lastLoginIP`      VARCHAR(40)               DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_User_Auto_Login` (`autoLogin`),
  UNIQUE KEY `IX_User_Telephone` (`telephone`),
  UNIQUE KEY `IX_User_Email` (`email`)
)
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for email_validate
-- ----------------------------
DROP TABLE IF EXISTS `email_validate`;
CREATE TABLE `email_validate` (
  `identityCode` varchar(128) NOT NULL,
  `userId` int(11) NOT NULL,
  `createTime` datetime NOT NULL,
  `type` varchar(3) NOT NULL,
  PRIMARY KEY (`identityCode`),
  CONSTRAINT `FX_User_Email_UserID` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for phone_captcha_count
-- ----------------------------
DROP TABLE IF EXISTS `phone_captcha_count`;
CREATE TABLE `phone_captcha_count` (
  `identity` varchar(40) NOT NULL,
  `count` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`identity`)
) DEFAULT CHARSET=utf8;
