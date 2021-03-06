/*
 * Copyright 2015 LBK
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package git.lbk.questionnaire.dao.impl;

import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("emailValidateDao")
public class EmailValidateDaoImpl extends BaseDaoImpl<EmailValidate> {

	/**
	 * 根据用户id和验证码类型删除记录
	 * @param user 用户对象
	 * @param type 验证码类型
	 */
	public void deleteByUserIDAndType(User user, String type){
		updateEntityByHQL("delete from EmailValidate where user=? and type=?", user, type);
	}

	/**
	 * 删除早于指定时间创建的验证码
	 * @param date 在该时间之前的验证码均会被删除
	 */
	public void deleteBeforeTime(Date date){
		updateEntityByHQL("delete from EmailValidate where createTime<?", date);
	}

}
