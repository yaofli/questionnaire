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

package git.lbk.questionnaire.email;

/**
 * 邮件信息
 */
public class MailMessage {

	private String to;
	private String subject;
	private String message;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		MailMessage that = (MailMessage) o;

		if(to != null ? !to.equals(that.to) : that.to != null) return false;
		if(subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
		return !(message != null ? !message.equals(that.message) : that.message != null);

	}

	@Override
	public int hashCode() {
		int result = to != null ? to.hashCode() : 0;
		result = 31 * result + (subject != null ? subject.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "MailMessage{" +
				"to='" + to + '\'' +
				", subject='" + subject + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
