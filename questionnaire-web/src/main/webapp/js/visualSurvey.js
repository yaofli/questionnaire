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
	 * 调查对象
	 * @param selector 用来容纳页面的div选择符 或者 引用
	 * @param json 该调查对象的json数据
	 * @constructor
	 */
	function Survey(selector, json){
		this.oDiv = $(selector);
		this.json = json;
		this.pages = [];
		this.init();
	}

	window.visualSurvey.Survey = Survey;

	/**
	 * 初始化页面
	 */
	Survey.prototype.init = function (){
		this.oDiv.addClass('am-margin-horizontal');
		$('<h1 class="am-text-center"></h1>').text(this.json.title).appendTo(this.oDiv);
		$('<h2 class="am-text-center"></h2>').text(this.json.detailDescribe).appendTo(this.oDiv);
		this.createPages();
		this.createCommitButton();
	};

	/**
	 * @private
	 * 创建该调查的所有页面
	 */
	Survey.prototype.createPages = function(){
		var iPageNum = this.json.pages.length;
		var pageLinkWidth = 100;
		var oBody = $('<div></div>');
		this.oTabNav = $('<div class="am-btn-group am-center am-cf" style="width: '
			+ pageLinkWidth * iPageNum + 'px"></div>');
		this.oTabBody = $('<div></div>');
		var iStartNum = 1;
		for( var i = 0; i < this.json.pages.length; i++ ){
			this.createPage(i, iStartNum);
			iStartNum += this.pages[i].questions.length;
		}
		oBody.append(this.oTabBody).append(this.oTabNav).appendTo(this.oDiv);
		this.oTabNav.find('button:first').click();
	};

	/**
	 * @private
	 * 创建页面
	 */
	Survey.prototype.createPage = function (index, iStartNum){
		var _this = this;
		var oPageDiv = $('<div></div>');
		var oPageLink = $('<button class="am-btn am-margin-horizontal">第' + (index + 1) + '页</button>');
		oPageLink.appendTo(this.oTabNav);
		this.pages.push(new visualSurvey.Page(oPageDiv, this.json.pages[index], iStartNum));
		oPageDiv.css('display', 'none').appendTo(this.oTabBody);
		oPageLink.click(function (){
			_this.oTabNav.children().removeClass('am-btn-primary').addClass('am-btn-default');
			_this.oTabBody.children().css('display', 'none');
			oPageLink.removeClass('am-btn-default').addClass('am-btn-primary');
			oPageDiv.css('display', 'block');
		});
	};

	/**
	 * 创建提交按钮
	 */
	Survey.prototype.createCommitButton = function(){
		var _this = this;
		var oDiv = $('<div></div>');
		var oCommitBtn = $('<button>提交</button>')
			.addClass('am-btn am-btn-primary am-margin-vertical am-center')
			.css('display', 'block');
		oCommitBtn.click(function (){
			_this.commit();
		});
		oDiv.append(oCommitBtn).appendTo(this.oDiv);
	};

	/**
	 * 提交用户回答的答案
	 */
	Survey.prototype.commit = function(){
		var strInput = '';
		for(var i=0; i<this.pages.length; i++){
			var pageInput = this.pages[i].getUserAnswer();
			if(pageInput == ''){
				layer.msg('请检查输入');
				return;
			}
			strInput += pageInput;
		}
		var loading = layer.load(1, {
			shade: [0.1, '#fff']
		});
		$.post('/survey/commitAnswer/'+this.json.id, {answer: strInput}, function(data){
			if(data){
				window.location = "/participateSuccess";
				return;
			}
			layer.close(loading);
			layer.msg('输入有误, 请重新输入');
		});
	};

	/**
	 * 页面对象
	 * @param selector 用来容纳页面的div选择符 或 引用
	 * @param json 该页面的json数据
	 * @param startNumber 该页面问题的起始题号
	 * @constructor
	 */
	function Page(selector, json, startNumber){
		this.oDiv = $(selector);
		this.json = json;
		this.startNumber = startNumber;
		this.questions = [];
		this.init();
	}

	window.visualSurvey.Page = Page;

	/**
	 * 初始化页面对象, 并创建相应的页面元素
	 */
	Page.prototype.init = function (){
		this.oDiv.removeClass().addClass("am-panel-group");
		$('<h3></h3>').text(this.json.title).appendTo(this.oDiv);
		for( var i = 0; i < this.json.questions.length; i++ ){
			var oQuestionDiv = $('<div></div>');
			this.questions.push(visualSurvey.createQuestion(oQuestionDiv, this.json.questions[i], this.startNumber + i));
			this.oDiv.append(oQuestionDiv);
		}
	};

	/**
	 * 获得用户输入的内容. 如果有某几项输入不合法, 则返回undefined
	 */
	Page.prototype.getUserAnswer = function(){
		var userAnswer = '';
		for(var i=0; i<this.questions.length; i++){
			var questionAnswer = this.questions[i].getUserAnswer();
			if( questionAnswer == ''){
				return '';
			}
			userAnswer += questionAnswer;
		}
		return userAnswer;
	};

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
	 * 覆写getUserInput方法. 获得用户输入的字符串形式, 如果用户输入不合法, 返回undefined.
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

	Question.prototype.setJson = function (json){
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
	 * 获得用户输入的内容以及题号组成的字符串.
	 * @returns {string} 用户输入和题号, 如果用户输入不合法, 则返回空.
	 */
	Question.prototype.getUserAnswer = function(){
		var userInput = this.getUserInput();
		if(userInput == undefined){
			return '';
		}
		return String.fromCharCode(30) + this.number + String.fromCharCode(31) + userInput;
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
		this.aInput = [];
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
		if( !options ){
			return;
		}
		for( var i = 0; i < options.length; i++ ){
			var oLi = $('<li style="list-style: none"></li>').addClass('am-padding-left-0');
			if( style == 0 ){
				oLi.addClass(this.getLabelClass());
			}
			var oLabel = $('<label></label>').addClass(this.getLabelClass());
			var oInput = $('<input/>').attr({
				type: this.getInputType(),
				id: 'q' + number + '_' + i,
				value: i,
				name: 'q' + number
			});
			this.aInput.push(oInput);
			oLabel.text(options[i].option).append(oInput).appendTo(oLi);
			oUl.append(oLi);
		}
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
	RadioQuestion.prototype.getUserInput = function(){
		for(var i=0; i< this.aInput.length; i++){
			if(this.aInput[i].prop('checked')){
				return i+'';
			}
		}
		if(!this.json.required){
			return '';
		}
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
	CheckboxQuestion.prototype.getUserInput = function (){
		var select = [];
		for( var i = 0; i < this.aInput.length; i++ ){
			if( this.aInput[i].prop('checked') ){
				select.push(i);
			}
		}
		if(select.length == 0 && this.json.required){
			return undefined;
		}
		return ',' + select.join(',') + ',';
	};
	window.visualSurvey.CheckboxQuestion = CheckboxQuestion;

})();