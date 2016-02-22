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

package git.lbk.questionnaire.entity.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * 所有调查问题的公共父类, 包含公有的属性
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "type")
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = SingleSelectQuestion.class, name = SingleSelectQuestion.TYPE),
		@JsonSubTypes.Type(value = MultiplySelectQuestion.class, name = MultiplySelectQuestion.TYPE)
})
public abstract class Question implements Serializable {

	private static final long serialVersionUID = -2432519629118327310L;

	private boolean required;
	private String title;

	/**
	 * 获取该类型的标识码. 每个子类的返回值, 需要与上面JsonSubTypes.Type注解中的一致
	 * @return 该类型的标识码
	 */
	public abstract String getType();

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Question question = (Question) o;

		if(required != question.required) return false;
		return !(title != null ? !title.equals(question.title) : question.title != null);

	}

	@Override
	public int hashCode() {
		int result = (required ? 1 : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Question{" +
				"required=" + required +
				", title='" + title + '\'' +
				'}';
	}
}
