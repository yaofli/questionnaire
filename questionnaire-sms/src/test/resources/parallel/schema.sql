SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sms_count
-- ----------------------------
DROP TABLE IF EXISTS sms_count;
CREATE TABLE sms_count (
  identity varchar(40) NOT NULL,
  count int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`identity`)
);
