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

import java.io.Serializable;

public class Option implements Serializable{

	private static final long serialVersionUID = 9153547998146059136L;

	private String option;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Option option1 = (Option) o;

		return !(option != null ? !option.equals(option1.option) : option1.option != null);

	}

	@Override
	public int hashCode() {
		return option != null ? option.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Option{" +
				"option='" + option + '\'' +
				'}';
	}
}
