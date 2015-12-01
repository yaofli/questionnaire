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
/**
 * 本文件中的js负责创建设计问题区域, 并将用户的输入转化成相应的json格式
 */

(function (){
	window.designSurvey = window.designSurvey || {};

	/**
	 * 用于子类注册, 之后用户可以使用 createQuestion 来创建 Question 对象
	 * @type {{}} 键为可以处理的问题类型. 值为构造方法
	 */
	window.designSurvey.registeredQuestion = {};
	/**
	 * 创建一个设计问题对象
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据, 或者问题的类型.
	 * @param number 问题的题号
	 */
	window.designSurvey.createQuestion = function (selector, json, number){
		if(typeof json == 'string' || json instanceof String){
			json = designSurvey.createEmptyJson(json);
		}
		var fun = designSurvey.registeredQuestion[json.type];
		if( fun ){
			return new fun(selector, json, number);
		}
	};
	/**
	 * 创建一个初始化的问题json
	 * @param type 问题类型
	 */
	window.designSurvey.createEmptyJson = function(type){
		return {
			title: '',
			type: type,
			required: false
		};
	};

	/**
	 * 所有设计问题的父类. 该类中可用的属性有
	 * <ul>
	 *     <li>oDiv: 用来容纳设计区域和预览区域的 "div"</li>
	 *     <li>json: 问题的json数据 </li>
	 *     <li>number: 改题的题号</li>
	 *     <li>oVisualArea: 预览区域的div的引用</li>
	 *     <li>visualObj: 预览对象的引用</li>
	 *     <li>oDesignArea: 设计区域的div的引用</li>
	 * </ul>
	 * @param selector 用来容纳问题的div
	 * @param json 问题对应的json
	 * @param number 问题的题号
	 * @constructor 用来构造Question对象. 该对象并没有实际用处. 只是替子类实现了一些功能
	 */
	function Question(selector, json, number){
		this.json = json;
		this.oDiv = $(selector);
		this.number = number;
		var designAreaId = 'designQuestion' + this.number;
		this.oVisualArea = $('<div  data-am-collapse="{target: \'#' + designAreaId + '\'}"></div>');
		this.oDesignArea = $('<div id=' + designAreaId + '></div>').addClass('am-panel am-panel-default am-panel-collapse am-collapse');
		this.visualObj = visualSurvey.createQuestion(this.oVisualArea, this.json, this.number);
		this.oDiv.append(this.oVisualArea).append(this.oDesignArea);
	}

	/**
	 * 选择题的公共父类
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json
	 * @param number 问题的题号
	 * @constructor 用来构造 SelectQuestion 对象. 该对象并无法直接使用. 需要使用它的某个具体功能的子类.
	 */
	function SelectQuestion(selector, json, number){
		Question.call(this, selector, json, number);
		if(!json.options){
			json.options = [];
		}
		this.fillDesignArea();
	}
	tools.inheritPrototype(SelectQuestion, Question);
	SelectQuestion.style = '<select>'
			+ '<option value="0">横向自适应</option>'
			+ '<option value="1">竖向排列</option>'
			+ '<optgroup label="横向排列">'
				+ '<option value="2">每行2列</option>'
				+ '<option value="3">每行3列</option>'
				+ '<option value="4">每行4列</option>'
				+ '<option value="5">每行5列</option>'
				+ '<option value="6">每行6列</option>'
				+ '<option value="7">每行7列</option>'
				+ '<option value="8">每行8列</option>'
				+ '<option value="9">每行9列</option>'
				+ '<option value="10">每行10列</option>'
				+ '<option value="11">每行11列</option>'
				+ '<option value="12">每行12列</option>'
				+ '<option value="15">每行15列</option>'
				+ '<option value="20">每行20列</option>'
				+ '<option value="30">每行30列</option>'
			+ '</optgroup>'
		+ '</select>';

	SelectQuestion.prototype.fillDesignArea = function(){
		var json = this.json;
		var oTitleDiv = $('<div class="am-cf am-panel-bd"></div>');

		this.titleArea = $('<textarea class="am-u-sm-12" rows="3"></textarea>').val(json.title);
		$('<div class="am-u-sm-8"></div>').append($('<label>题目:</label>'))
			.append(this.titleArea).appendTo(oTitleDiv);

		this.oSettingArea = $('<div></div>').appendTo(oTitleDiv);
		this.fillSettingArea();
		oTitleDiv.appendTo(this.oDesignArea);

		var allOption = '';
		for(var i=0; i<json.options.length; i++){
			allOption += json.options[i].option + "\n";
		}
		this.optionArea = $('<textarea class="am-u-sm-12" rows="4"></textarea>').val(allOption);
		$('<div  class="am-panel-bd am-cf am-padding-horizontal-lg am-padding-top-0"></div>')
			.append($('<label>选项:</label>')).append(this.optionArea).appendTo(this.oDesignArea);
	};

	SelectQuestion.prototype.fillSettingArea = function(){
		var json = this.json;

		var oRequiredLabel = $('<label class="am-checkbox-inline">必答题</label>');
		this.oRequiredInput = $('<input type="checkbox"/>');
		if( json.required ){
			this.oRequiredInput.attr('checked', 'checked');
		}
		oRequiredLabel.append(this.oRequiredInput);
		this.oSettingArea.append(oRequiredLabel).append($('<br/>'));

		var settingLabel = $('<label class="am-checkbox-inline am-padding-left-0">排列方式:</label>');
		this.styleArea = $(SelectQuestion.style).appendTo(settingLabel);
		var styleOptions = this.styleArea.find('option')
		for(var i=0; i<styleOptions.length; i++){
			if(json.style == styleOptions[i].value){
				styleOptions[i].selected = 'selected';
			}
		}
		this.oSettingArea.append(settingLabel);
	};

	/**
	 * 单选题
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据
	 * @param number 问题的题号
	 * @constructor 构建一个单选题对象
	 */
	function RadioQuestion(selector, json, number){
		SelectQuestion.call(this, selector, json, number);
	}

	tools.inheritPrototype(RadioQuestion, SelectQuestion);
	window.designSurvey.registeredQuestion['radio'] = RadioQuestion;
	window.designSurvey.RadioQuestion = RadioQuestion;

	/**
	 * 单选题
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据
	 * @param number 问题的题号
	 * @constructor 构建一个单选题对象
	 */
	function CheckboxQuestion(selector, json, number){
		SelectQuestion.call(this, selector, json, number);
	}

	tools.inheritPrototype(CheckboxQuestion, SelectQuestion);
	window.designSurvey.registeredQuestion['checkbox'] = CheckboxQuestion;
	window.designSurvey.CheckboxQuestion = CheckboxQuestion;

})();

