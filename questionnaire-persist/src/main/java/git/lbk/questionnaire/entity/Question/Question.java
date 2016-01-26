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

/**
 * 所有调查问题的公共父类, 包含公有的属性
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "type", visible = true)
@JsonSubTypes(value={
		@JsonSubTypes.Type(value = SingleSelectQuestion.class, name="radio"),
		@JsonSubTypes.Type(value = MultiplySelectQuestion.class, name = "checkbox")
})
public abstract class Question {

	private String type;
	private boolean required;
	private String title;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
		if(type != null ? !type.equals(question.type) : question.type != null) return false;
		return !(title != null ? !title.equals(question.title) : question.title != null);

	}

	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (required ? 1 : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Question{" +
				"type='" + type + '\'' +
				", required=" + required +
				", title='" + title + '\'' +
				'}';
	}
}
