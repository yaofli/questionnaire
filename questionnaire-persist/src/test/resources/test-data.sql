INSERT INTO USER (name, password, auto_login, mobile, email, user_status, type, register_time)
VALUES
  ('abc', '1234567890', 'abcAutoLogin', NULL, 'abc@gmail.com', 'n', 'c', '2015-11-01 17:01:03'),
  ('123', '1234567890', '123AutoLogin', '12345678901', NULL, 'v', 'c', '2015-10-29 7:34:23'),
  ('zs', '1234567890', 'zsAutoLogin', '13145679381', 'zs@gmail.com', 'n', 'c', '2014-02-06 22:08:30'),
  ('survey', '1234567890', 'surveyAutoLogin', '13135679381', '', 'n', 'c', '2014-02-06 22:08:30');

-- 测试 deleteByUserIDAndType 方法的数据
INSERT INTO email_validate(identity_code, user_id ,create_time, type)
VALUES
('1', 1, getdate(), 'reg'),
('2', 2, getdate(), 'reg'),
('3', 1, getdate(), 'reg'),
('4', 1, getdate(), 'pwd');

-- 测试 deleteBeforeTime 方法的数据
INSERT INTO email_validate (identity_code, user_id, create_time, type)
VALUES
  ('11', 1, '2015-11-18 00:12:22', 'reg'),
  ('12', 2, '2015-11-20 12:23:12', 'pwd'),
  ('13', 3, '2015-11-19 12:23:12', 'reg'),
  ('14', 1, '2015-11-22 11:49:30', 'reg'),
  ('15', 1, '2015-11-21 20:50:12', 'reg'),
  ('16', 1, '2015-11-23 23:32:12', 'reg');

CREATE TABLE IF NOT EXISTS sms(
  id     INT(11)     NOT NULL AUTO_INCREMENT,
  mobile VARCHAR(11) NOT NULL,
  ip     VARCHAR(40) NOT NULL,
  type   INT(11)     NOT NULL DEFAULT '1',
  time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
)DEFAULT CHARSET = utf8;

INSERT INTO sms (mobile, ip, type, time)
VALUES
  ('12345678901', '127.0.0.1', 1, now()),
  ('12345678901', '127.0.0.2', 1, now()),
  ('12345678902', '127.0.0.3', 1, now()),
  ('12345678901', '127.0.0.1', 1, now());

INSERT INTO survey(user_id, title, create_time, modify_time, survey_status)
VALUES
  (3, '3', '2015-10-29 7:34:23', '2015-10-29 7:34:23', 0),
  (4, '4_1', '2015-10-20 9:34:23', '2015-10-29 15:34:23', 0),
  (4, '4_2', '2015-11-29 20:34:23', '2015-11-29 10:34:23', 0),
  (4, '4_3', '2015-10-14 7:44:23', '2015-10-20 19:34:23', 0);

INSERT INTO page(survey_id, title, questions, rank)
VALUES
  (2, '2_1', '2_1', 1),
  (2, '2_2', '2_2', 2),
  (1, '1_1', '1_1', 1),
  (1, '1_2', '1_2', 2);

INSERT INTO survey (user_id, title, create_time, modify_time, survey_status)
VALUES
(4, '4_4', '2015-10-14 7:44:23', '2015-10-20 19:34:23', 0),
(4, '4_4', '2015-10-14 7:44:23', '2015-10-20 19:34:23', 0);
INSERT INTO answer(survey_id, answer_time, ip, answer)
VALUES
  (5, '2015-10-14 7:44:23', '127.0.0.1', '1'),
  (5, '2015-10-14 7:44:23', '127.0.0.1', '2'),
  (6, '2015-10-14 7:44:23', '127.0.0.1', '1'),
  (6, '2015-10-14 7:44:23', '127.0.0.1', '2');

