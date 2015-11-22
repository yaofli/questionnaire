INSERT INTO USER (name, password, autoLogin, mobile, email, status, type, registerTime, lastLoginTime, lastLoginAddress, lastLoginIP)
VALUES
  ('abc', '1234567890', 'abcAutoLogin', NULL, 'abc@gmail.com', 'n', 'c', '2015-11-01 17:01:03', NULL, NULL, NULL),
  ('123', '1234567890', '123AutoLogin', '12345678901', NULL, 'i', 'c', '2015-10-29 7:34:23', NULL, NULL, NULL),
  ('zs', '1234567890', 'zsAutoLogin', '13145679381', 'zs@gmail.com', 'n', 'c', '2014-02-06 22:08:30', NULL, NULL,
   NULL);

-- 测试 deleteByUserIDAndType 方法的数据
INSERT INTO email_validate(identityCode, userId ,createTime, type)
VALUES
('1', 1, getdate(), 'reg'),
('2', 2, getdate(), 'reg'),
('3', 1, getdate(), 'reg'),
('4', 1, getdate(), 'pwd');

-- 测试 deleteBeforeTime 方法的数据
INSERT INTO email_validate (identityCode, userId, createTime, type)
VALUES
  ('11', 1, '2015-11-18 00:12:22', 'reg'),
  ('12', 2, '2015-11-20 12:23:12', 'pwd'),
  ('13', 3, '2015-11-19 12:23:12', 'reg'),
  ('14', 1, '2015-11-22 11:49:30', 'reg'),
  ('15', 1, '2015-11-21 20:50:12', 'reg'),
  ('16', 1, '2015-11-23 23:32:12', 'reg');

INSERT INTO sms_count(identity, count)
VALUES
  ('1', 1),
  ('2', 2),
  ('3', 3),
  ('4', 4),
  ('5', 5),
  ('6', 6);
