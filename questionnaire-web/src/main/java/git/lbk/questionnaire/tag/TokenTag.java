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

package git.lbk.questionnaire.tag;

import git.lbk.questionnaire.util.TokenHelper;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 生成包含token的hidden类型的input
 */
public class TokenTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		JspContext context = super.getJspContext();
		JspWriter out = context.getOut();
		String token = TokenHelper.createToken();
		context.setAttribute(TokenHelper.buildTokenSessionAttributeName(), token, PageContext.SESSION_SCOPE);
		out.write("<input type=\"hidden\" value=\"" + token + "\" name=\""+TokenHelper.DEFAULT_TOKEN_NAME+"\" />");

	}
}
