/*
 * Copyright 2016 LBK
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

import java.util.*;

public class SelectQuestion extends Question {

	private List<Option> options;
	private Integer style;

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;

		SelectQuestion that = (SelectQuestion) o;

		if(options != null ? !options.equals(that.options) : that.options != null) return false;
		return !(style != null ? !style.equals(that.style) : that.style != null);

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (options != null ? options.hashCode() : 0);
		result = 31 * result + (style != null ? style.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SelectQuestion{" +
				"options=" + options +
				", style=" + style +
				"} " + super.toString();
	}
}
