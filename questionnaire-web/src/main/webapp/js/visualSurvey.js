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
 * 该文件用于解析json格式的调查信息, 并显示到页面上
 */
(function (){
	window.visualSurvey = window.visualSurvey || {};

	/**
	 * 用于子类注册, 之后用户可以使用 createQuestion 来创建 Question 对象
	 * @type {{}} 键为可以处理的问题类型. 值为构造方法
	 */
	window.visualSurvey.registeredQuestion = {};
	/**
	 * 创建一个问题的预览对象
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据
	 * @param number 问题的题号
	 */
	window.visualSurvey.createQuestion = function (selector, json, number){
		var fun = visualSurvey.registeredQuestion[json.type];
		if( fun ){
			return new fun(selector, json, number);
		}
	};

	/**
	 * 所有问题的父类. 该类中可用的属性有
	 * <ul>
	 *     <li>oDiv: 用来容纳问题的 "div".</li>
	 *     <li>json: 问题的json数据.</li>
	 *     <li>*oNumber: 题号对应的html标签的引用. 该属性在 createTitle 方法中创建.</li>
	 *     <li>*oTitle: 题目对应的html标签的引用. 该属性在 createTitle 方法中创建.</li>
	 *     <li>*oBody: 答题区的div的引用. 该属性在 createBody 方法中创建.</li>
	 * </ul>
	 * 继承自该类的类需要重写 fillBody 方法来填充 问题的body 区域
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据
	 * @param number 问题的题号
	 * @constructor 用来构造Question对象. 该对象并没有实际用处. 只是替子类实现了一些功能
	 */
	function Question(selector, json, number){
		this.oDiv = $(selector);
		this.setJson(json);
		this.number = number;
	}

	Question.prototype.setJson = function(json){
		this.json = json;
	};
	Question.prototype.setNumber = function (number){
		this.number = number;
		this.oNumber.text(number + '.');
	};

	/**
	 * 重新填充该问题对应的区域
	 */
	Question.prototype.reFill = function (){
		this.oDiv.empty();
		this.oDiv.removeClass().addClass("am-panel am-panel-default");
		this.createTitle();
		this.createBody();
	};

	/**
	 * 初始化标题区
	 */
	Question.prototype.createTitle = function (){
		var oTitleAll = $('<div class="am-panel-hd am-cf">');

		this.oNumber = $('<span class="am-fl">' + this.number + ".</span>");
		this.oTitle = $('<h3 class="am-fl am-panel-title am-margin-horizontal-sm">' + this.json.title + '</h3>');

		oTitleAll.append(this.oNumber).append(this.oTitle);

		if( this.json.required ){
			oTitleAll.append($('<span  class="am-fl am-text-lg am-text-danger">*</span>'));
		}
		this.oDiv.append(oTitleAll);
	};

	/**
	 * 初始化答题区. 该方法里会创建一个div, 并设置好class. 然后添加到this.oDiv中, 同时赋值给 this.oBody. 之后可以直接向 this.oBody中添加答题标签(如选项).
	 * 该方法会调用 fillBody 方法
	 */
	Question.prototype.createBody = function (){
		this.oBody = $('<div class="am-panel-bd"></div>');
		this.oDiv.append(this.oBody);
		this.fillBody();
	};

	/**
	 * 选择题的公共父类. 继承自该类的类需要重写 getLabelClass 和 getInputType 方法
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据
	 * @param number 问题的题号
	 * @constructor 用来构造 SelectQuestion 对象. 该对象并无法直接使用. 需要使用它的某个具体功能的子类.
	 */
	function SelectQuestion(selector, json, number){
		Question.call(this, selector, json, number);
	}

	tools.inheritPrototype(SelectQuestion, Question);

	/**
	 * 使用选项填充答题区
	 */
	SelectQuestion.prototype.fillBody = function (){
		var options = this.json.options;
		var number = this.number;
		var style = this.json.style ? this.json.style : 0;
		var oUl = $('<ul></ul>').addClass('am-padding-left-0');
		if( style > 0 ){
			oUl.addClass('am-avg-sm-' + style);
		}
		this.oBody.append(oUl);
		if(!options){
			return;
		}
		for( var i = 0; i < options.length; i++ ){
			var oLi = $('<li style="list-style: none"></li>').addClass('am-padding-left-0');
			if(style == 0){
				oLi.addClass(this.getLabelClass());
			}
			var oLabel = $('<label></label>').addClass(this.getLabelClass());
			var oInput = $('<input/>').attr({
				type: this.getInputType(),
				id: 'q'+number + '_' + i,
				value: i,
				name: 'q'+number
			});
			oLabel.text(options[i].option).append(oInput).appendTo(oLi);
			oUl.append(oLi);
		}
	};

	/**
	 * 获取选项对应的label的class
	 */
	SelectQuestion.prototype.getLabelClass = function (){
	};
	/**
	 * 获取选项对应的input的class
	 */
	SelectQuestion.prototype.getInputType = function (){
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
		this.reFill();
	}
	tools.inheritPrototype(RadioQuestion, SelectQuestion);
	visualSurvey.registeredQuestion.radio = RadioQuestion;

	RadioQuestion.prototype.getLabelClass = function (){
		return "am-radio-inline";
	};
	RadioQuestion.prototype.getInputType = function (){
		return "radio";
	};
	window.visualSurvey.RadioQuestion = RadioQuestion;

	/**
	 * 单选题
	 * @param selector 用来容纳问题的div
	 * @param json 问题的json数据
	 * @param number 问题的题号
	 * @constructor 构建一个单选题对象
	 */
	function CheckboxQuestion(selector, json, number){
		SelectQuestion.call(this, selector, json, number);
		this.reFill();
	}
	tools.inheritPrototype(CheckboxQuestion, SelectQuestion);
	visualSurvey.registeredQuestion.checkbox = CheckboxQuestion;

	CheckboxQuestion.prototype.getLabelClass = function (){
		return "am-checkbox-inline";
	};
	CheckboxQuestion.prototype.getInputType = function (){
		return "checkbox";
	};
	window.visualSurvey.CheckboxQuestion = CheckboxQuestion;

})();