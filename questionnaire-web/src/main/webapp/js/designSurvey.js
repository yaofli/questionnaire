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
	 * 调查对象. 该对象的属性有:
	 * <ul>
	 *     <li>oDiv: 该对象所在的'div'</li>
	 *     <li>json: 该调查对象对应的json数据</li>
	 *     <li>pages: 该调查对象所包含的页面对象</li>
	 *     <li>insertPlace: 下一次插入问题的页面编号</li>
	 *     <li>pagesDiv: 页面区的div引用</li>
	 * </ul>
	 * @param {string, object} selector 调查对象所在的 'div'
	 * @param {object} json 调查对象的 json
	 */
	function Survey(selector, json){
		this.oDiv = $(selector);
		this.json = json;
		this.pages = [];
		this.insertPlace = 0;
		this.init();
	}

	/**
	 * 初始化调查对象
	 */
	Survey.prototype.init = function (){
		this.createBtnGroup();
		this.createHead();
		this.createPageAreaAndFill();
	};

	/**
	 * @private
	 * 创建页面顶部的按钮组. 包括添加问题, 页面 以及 完成编辑等按钮
	 */
	Survey.prototype.createBtnGroup = function (){
		var btnToolbar = $('<div data-am-sticky></div>');
		var oCenterDiv = $('<div class="am-padding-vertical-sm am-margin-horizontal-sm"></div>').css('background', '#DDDDDD');

		this.createAddQuestionButtons(oCenterDiv);
		this.createCompleteBtn(oCenterDiv);

		btnToolbar.append(oCenterDiv).appendTo(this.oDiv);
	};

	/**
	 * @private
	 * 创建添加问题、页面按钮
	 * @param oBtnGroup 用于容纳按钮的div
	 */
	Survey.prototype.createAddQuestionButtons = function (oBtnGroup){
		var _this = this;
		var btnClass = 'am-btn am-btn-primary am-radius am-margin-horizontal-sm';
		var radioBtn = $('<button type="button" class="' + btnClass + '">单选</button>');
		radioBtn.appendTo(oBtnGroup).click(function (){
			_this.addQuestion('radio');
		});
		var checkboxBtn = $('<button type="button" class="' + btnClass + '">多选</button>');
		checkboxBtn.appendTo(oBtnGroup).click(function (){
			_this.addQuestion("checkbox");
		});
		var pageBtn = $('<button type="button" class="' + btnClass + '">添加页面</button>');
		pageBtn.appendTo(oBtnGroup).click(function (){
			_this.addPage();
		});
	};

	/**
	 * @private
	 * 创建完成按钮
	 */
	Survey.prototype.createCompleteBtn = function (oBtnGroup){
		var _this = this;
		var oRightFloatDiv = $('<div class="am-fr am-margin-right-lg"></div>');
		var oDropDownDiv = $('<div class="am-dropdown" data-am-dropdown></div>');
		var oDropDownToggleBtn = $('<button class="am-btn am-btn-primary am-dropdown-toggle" data-am-dropdown-toggle>完成编辑<span class="am-icon-caret-down"></span></button>');
		var oDropDownContentUl = $('<ul class="am-dropdown-content"></ul>');

		var oCompleteBtn = $('<a href="javascript:;">完成编辑</a>');
		oCompleteBtn.click(function (){
			_this.completeEdit();
			oDropDownDiv.dropdown('close');
		});
		$('<li></li>').append(oCompleteBtn).appendTo(oDropDownContentUl);

		oDropDownDiv.append(oDropDownToggleBtn).append(oDropDownContentUl).appendTo(oRightFloatDiv);
		oRightFloatDiv.appendTo(oBtnGroup);
	};

	/**
	 * @private
	 * 添加调查的标题和描述输入框
	 */
	Survey.prototype.createHead = function (){
		this.anchorId = 'designSurvey' + new Date().getTime();
		var _this = this;

		var oHead = $('<div class="am-panel am-panel-default am-margin-horizontal-sm am-padding-horizontal-sm"></div>');

		var oTitleDiv = $('<div class="am-cf am-margin-bottom-sm"></div>');
		oTitleDiv.append($('<label><a id="' + this.anchorId + '">调查标题:</a></label>'));
		var oTitleTextarea = $('<textarea class="am-u-sm-12" rows="2"></textarea>').val(this.json.title).appendTo(oTitleDiv);
		oTitleTextarea.change(function (){
			_this.json.title = this.value();
		});

		var oDescribeDiv = $('<div class="am-cf am-margin-bottom-sm"></div>');
		oDescribeDiv.append($('<label>调查描述:</label>').appendTo(oDescribeDiv));
		var oDescribeTextarea = $('<textarea class="am-u-sm-12" rows="4"></textarea>').val(this.json.detailDescribe).appendTo(oDescribeDiv);
		oDescribeTextarea.change(function (){
			_this.json.detailDescribe = this.value;
		});

		oHead.append(oTitleDiv).append(oDescribeDiv).appendTo(this.oDiv);
	};

	/**
	 * @private
	 * 创建页面区, 并添加页面
	 */
	Survey.prototype.createPageAreaAndFill = function (){
		var json = this.json;
		var pages = json.pages;
		this.pagesDiv = $('<div></div>');

		json.pages = [];
		for( var i = 0; i < pages.length; i++ ){
			this.addPage(pages[i]);
		}

		this.pagesDiv.appendTo(this.oDiv);
	};

	/**
	 * 完成编辑. 检查数据数据是否有效, 如果有效保存数据. 否则提醒用户
	 */
	Survey.prototype.completeEdit = function (){
		var notValidAnchorId = this.getFirstNotValidPlace();
		if( notValidAnchorId ){
			console.log(this.json);
			layer.msg('输入不合法, 请检查后重试');
			return;
		}
		console.log(this.json);
		this.saveSurvey(function (){
			layer.msg('保存成功', function(){
				window.location = "/survey/mySurvey";
			});
		}, function (){
			layer.msg('保存失败');
		}, true);
	};

	/**
	 * 获取该调查第一个输入不合法的位置.
	 * @return {string} 返回不合法元素对应的a标签的id; 如果全部合法, 则返回空字符串
	 */
	Survey.prototype.getFirstNotValidPlace = function (){
		if( !this.json.title ){
			return this.anchorId;
		}
		for( var i = 0; i < this.pages.length; i++ ){
			var notValidAnchorId = this.pages[i].getFirstNotValidPlace();
			if( notValidAnchorId ){
				return notValidAnchorId;
			}
		}
		return '';
	};

	/**
	 * 保存调查对象
	 * @param success {function} 保存成功时的回调函数
	 * @param fail 保存失败时的回调函数
	 * @param showLoadLayer {boolean} 是否显示"加载"层
	 */
	Survey.prototype.saveSurvey = function (success, fail, showLoadLayer){
		if( showLoadLayer ){
			var loading = layer.load(1, {
				shade: [0.1, '#fff']
			});
		}
		function closeLayer(){
			if( showLoadLayer ){
				layer.close(loading);
			}
		}
		var data = {
			_method: 'PUT',
			survey : JSON.stringify(this.json)
		};
		$.ajax({
			type: "post",
			url: '/survey/'+this.json.id,
			data: data,
			success: function (status){
				closeLayer();
				if( status == 'success' ){
					success();
				}
				else if( fail ){
					fail(status);
				}
			},
			error: function (err){
				closeLayer();
				console.log(err);
			}
		});
	};

	/**
	 * 设置下一次插入问题或者页面的位置
	 * @param {number} insertPlace 插入的位置
	 */
	Survey.prototype.setInsertPlace = function (insertPlace){
		this.insertPlace = insertPlace;
	};

	/**
	 * 添加页面.
	 * @param {Object} [json] 页面的json数据.
	 */
	Survey.prototype.addPage = function (json){
		var place = this.pages ? this.pages.length : 0;
		if( !json ){
			json = createEmptyPageJson(place);
		}
		var oPageDiv = $('<div class="am-panel-group am-margin-lg"></div>').appendTo(this.pagesDiv);
		var page = new Page(oPageDiv, this, json, 0);
		this.pages.splice(place, 0, page);
		this.json.pages.splice(place, 0, json);
		this.updateAllQuestionNumber();
		this.setInsertPlace(this.pages.length - 1);
	};

	/**
	 * 添加问题
	 * @param {string, object}type 问题类型, 或者问题的json数据
	 */
	Survey.prototype.addQuestion = function (type){
		this.pages[this.insertPlace].addQuestion(type);
		this.insertPlace = this.pages.length - 1;
	};

	/**
	 * 更新所有问题的题号
	 */
	Survey.prototype.updateAllQuestionNumber = function (){
		var startQuestionNumber = 1;
		for( var i = 0; i < this.pages.length; i++ ){
			this.pages[i].setRank(i);
			this.pages[i].setStartQuestionNumber(startQuestionNumber);
			startQuestionNumber += this.pages[i].getQuestionCount();
		}
	};

	window.designSurvey.Survey = Survey;

	/**
	 * 创建一个初始化的页面json
	 * @param {number} rank 页面的次序
	 * @return {object} 空白页面的json数据
	 */
	function createEmptyPageJson(rank){
		return {
			title: '',
			questions: [],
			rank: rank
		};
	}

	window.designSurvey.createEmptyPageJson = createEmptyPageJson;

	/**
	 * 页面对象. 该对象的属性有:
	 * <ul>
	 *     <li>oDiv: 该页面对象所处的div</li>
	 *     <li>parent: 包含该页面对象的调查对象</li>
	 *     <li>json: 该页面对象对应的json数据</li>
	 *     <li>anchorId: 该问题的锚点id</li>
	 *     <li>questions: 该页面对象包含的问题对象</li>
	 *     <li>insertPlace: 接下来要插入的问题的位置</li>
	 *     <li>startQuestionNumber: 该页面问题的开始题号</li>
	 * </ul>
	 * @param {object, string} selector 页面对象所处的 "div" 的选择器.
	 * @param {Survey} parent 调查对象.
	 * @param {object} json 该页面对应的.
	 * @param {number} startQuestionNumber 该页面的题目的起始题号.
	 */
	function Page(selector, parent, json, startQuestionNumber){
		this.oDiv = $(selector);
		this.parent = parent;
		this.json = handleInvalidPageJson(json);
		this.questions = [];
		this.insertPlace = this.questions.length;
		this.startQuestionNumber = startQuestionNumber;
		this.init();
	}

	/**
	 * 处理无效的页面json
	 * @param {object} json 需要处理的json
	 * @returns {object} 如果json有效, 则直接返回. 否则添加相应的元素, 之后返回
	 */
	function handleInvalidPageJson(json){
		if( !json ){
			json = createEmptyPageJson(json.rank);
		}
		if( !json.questions ){
			json.questions = [];
		}
		return json;
	}

	/**
	 * 初始化页面对象
	 */
	Page.prototype.init = function (){
		var _this = this;
		var oHead = $('<div class="am-cf am-margin-bottom-sm"></div>');

		this.anchorId = 'designPage' + new Date().getTime() + this.json.rank;
		this.oRank = $('<h2>第' + (this.json.rank + 1) + '页/共' + this.parent.pages.length + '页</h2>');

		var oTitleLabel = $('<label><a id="' + this.anchorId + '">页面标题:</a></label>');
		var oTitle = $('<textarea class="am-u-sm-12" rows="3"></textarea>').val(this.json.title);
		oTitle.change(function (){
			_this.json.title = this.value;
		});
		oHead.append(this.oRank).append(oTitleLabel).append(oTitle).appendTo(this.oDiv);
		var questions = this.json.questions;
		this.json.questions = [];
		for( var i = 0; i < questions.length; i++ ){
			this.addQuestion(questions[i]);
		}
	};

	/**
	 * 更新该页面的页码
	 * @param {number} rank 页码
	 */
	Page.prototype.setRank = function (rank){
		this.json.rank = rank;
		this.oRank.text('第' + (rank + 1) + '页/共' + this.parent.pages.length + '页');
	};

	/**
	 * 设置本页问题开始的题号. 之后使用该题号更新该页面的所有问题的题号
	 * @param {number} startQuestionNumber 该页面问题的开始题号
	 */
	Page.prototype.setStartQuestionNumber = function (startQuestionNumber){
		this.startQuestionNumber = startQuestionNumber;
		this.updateQuestionNumber();
	};

	/**
	 * 更新本页问题的题号
	 */
	Page.prototype.updateQuestionNumber = function (){
		for( var i = 0; i < this.questions.length; i++ ){
			this.questions[i].setNumber(this.startQuestionNumber + i);
		}
		this.setSelfInsertPlace();
	};

	/**
	 * 获取该页面中的问题数量
	 */
	Page.prototype.getQuestionCount = function (){
		return this.questions.length;
	};

	/**
	 * 获取该页面第一个输入不合法的位置.
	 * @return {string} 如果该页面本身不合法, 则返回该页面a标签的id; 如果存在题目不合法, 则返回第一个不合法的问题的a标签的id; 如果全部合法, 则返回空字符串
	 */
	Page.prototype.getFirstNotValidPlace = function (){
		var json = this.json;
		if( !json.title ){
			return this.anchorId;
		}
		// 此处不检查该页面是否有题目. 只检查每个题目(如果有的话)是否合法
		for( var i = 0; i < this.questions.length; i++ ){
			if( !this.questions[i].isValid() ){
				return this.questions[i].anchorId;
			}
		}
		return '';
	};

	/**
	 * 设置插入点, 设置下次插入问题时的插入位置. 并通知调查对象, 下次的插入点在本页面
	 * <ul>
	 *  <li>如果没有参数, 则更新插入点为当前页面的最后一个问题的最后面.</li>
	 *  <li>如果只有一个参数, 并且该参数为Question类型的, 则设置为该参数的下一个位置. 否则设置插入点为该参数指定的位置</li>
	 *  <li>如果有两个参数, 则设置为insertPlace指定的问题的前面或者后面的位置</li>
	 * </ul>
	 * @param {Question, number} [insertPlace] 插入位置对应的问题对象
	 * @param {boolean} [after] 是否在该问题之后插入
	 */
	Page.prototype.setInsertPlace = function (insertPlace, after){
		this.setSelfInsertPlace(insertPlace, after);
		this.parent.setInsertPlace(this.json.rank);
	};

	/**
	 * 设置插入点, 设置下次插入问题时的插入位置.
	 * <ul>
	 *  <li>如果没有参数, 则更新插入点为当前页面的最后一个问题的最后面.</li>
	 *  <li>如果只有一个参数, 并且该参数为Question类型的, 则设置为该参数的下一个位置. 否则设置插入点为该参数指定的位置</li>
	 *  <li>如果有两个参数, 则设置为insertPlace指定的问题的前面或者后面的位置</li>
	 * </ul>
	 * @param {Question, number} [insertPlace] 插入位置对应的问题对象
	 * @param {boolean} [after] 是否在该问题之后插入
	 */
	Page.prototype.setSelfInsertPlace = function (insertPlace, after){
		if( arguments.length == 0 ){
			this.insertPlace = this.questions.length;
		}
		else if( arguments.length == 1 && !insertPlace instanceof Question ){
			this.insertPlace = insertPlace;
			return;
		}

		var index = tools.indexOfArray(this.questions, insertPlace);
		if( index == -1 ){
			return;
		}

		if( arguments.length == 1 ){
			this.insertPlace = index;
		}
		else{
			this.insertPlace = index + after;
		}
	};

	/**
	 * 拷贝本页面的问题到问卷中的下一个插入点
	 * @param {object} json 问题的json数据
	 */
	Page.prototype.copyQuestionToSurvey = function (json){
		this.parent.addQuestion(json);
	};

	/**
	 * 添加问题
	 * @param {object, string} json 问题的json数据, 或者问题的类型.
	 */
	Page.prototype.addQuestion = function (json){
		var oQuestionDiv = $('<div class="am-panel am-panel-default"></div>');
		var question = createQuestion(oQuestionDiv, this, json, this.startQuestionNumber + this.insertPlace);
		this.insertQuestion(question, this.insertPlace);
		this.setSelfInsertPlace();
		this.parent.updateAllQuestionNumber();
	};

	/**
	 * @private
	 * 添加问题到指定位置, 该方法并不更新问题的题号, 所以如果之后不调用更新题号的方法, 可能会导致题号错乱
	 * @param {Question} question 问题对象
	 * @param {number} place 需要插入的位置
	 */
	Page.prototype.insertQuestion = function (question, place){
		if( place >= this.questions.length ){
			this.oDiv.append(question.oDiv);
			this.questions.push(question);
			this.json.questions.push(question.json);
		}
		else{
			question.oDiv.insertBefore(this.questions[place].oDiv);
			this.questions.splice(place, 0, question);
			this.json.questions.splice(place, 0, question.json);
		}
	};

	/**
	 * 删除指定的问题对象
	 * @param {Question} question 需要删除的问题对象
	 */
	Page.prototype.deleteQuestion = function (question){
		var index = tools.indexOfArray(this.questions, question);
		if( index == -1 ){
			return;
		}
		this.questions[index].oDiv.remove();
		this.questions.splice(index, 1);
		this.json.questions.splice(index, 1);
		this.parent.updateAllQuestionNumber();
	};

	/**
	 * 上移问题.
	 * @param {Question} question 需要上移的问题对象
	 */
	Page.prototype.shiftUpQuestion = function (question){
		var index = tools.indexOfArray(this.questions, question);
		if( index < 1 ){
			return;
		}
		this.questions.splice(index, 1);
		this.insertQuestion(question, index - 1);
		this.updateQuestionNumber();
	};

	/**
	 * 下移问题.
	 * @param {Question} question 需要下移的问题对象
	 */
	Page.prototype.shiftDownQuestion = function (question){
		var index = tools.indexOfArray(this.questions, question);
		if( index == -1 || index == this.questions.length - 1 ){
			return;
		}
		this.shiftUpQuestion(this.questions[index + 1]);
	};

	/**
	 * 将问题提到本页面第一个问题
	 * @param {Question} question 需要提前的问题
	 */
	Page.prototype.toFirstQuestion = function (question){
		var index = tools.indexOfArray(this.questions, question);
		if( index < 1 ){
			return;
		}
		this.questions.splice(index, 1);
		this.insertQuestion(question, 0);
		this.updateQuestionNumber();
	};

	/**
	 * 将问题降到本页面最后一个问题
	 * @param {Question} question 需要后调的问题
	 */
	Page.prototype.toLastQuestion = function (question){
		var index = tools.indexOfArray(this.questions, question);
		if( index == -1 || index == this.questions.length - 1 ){
			return;
		}
		this.questions.splice(index, 1);
		this.insertQuestion(question, this.questions.length);
		this.updateQuestionNumber();
	};

	window.designSurvey.Page = Page;

	/**
	 * 用于子类注册, 之后用户可以使用 createQuestion 来创建 Question 对象
	 * @type {{}} 键为可以处理的问题类型. 值为构造方法
	 */
	window.designSurvey.registeredQuestion = {};

	/**
	 * 创建一个设计问题对象
	 * @param {object, string} selector 用来容纳问题的div
	 * @param parent 该问题的父页面
	 * @param {object, string} json 问题的json数据, 或者问题的类型.
	 * @param {number} number 问题的题号
	 */
	function createQuestion(selector, parent, json, number){
		if( typeof json == 'string' || json instanceof String ){
			json = designSurvey.createEmptyQuestionJson(json);
		}
		var fun = designSurvey.registeredQuestion[json.type];
		if( fun ){
			return new fun(selector, parent, json, number);
		}
	}

	window.designSurvey.createQuestion = createQuestion;

	/**
	 * 创建一个初始化的问题json
	 * @param {string} type 问题类型
	 */
	function createEmptyQuestionJson(type){
		return {
			title: '',
			type: type,
			required: false
		};
	}

	window.designSurvey.createEmptyQuestionJson = createEmptyQuestionJson;

	/**
	 * 所有设计问题的父类. 该类中可用的属性有
	 * <ul>
	 *     <li>oDiv: 用来容纳设计区域和预览区域的 "div"</li>
	 *     <li>json: 问题的json数据 </li>
	 *     <li>number: 该题的题号</li>
	 *     <li>parent: 该问题所属的页面的引用</li>
	 *     <li>anchorId: 该问题的锚点id</li>
	 *     <li>visualObj: 预览对象的引用</li>
	 *     <li>oVisualArea: 预览区域的div的引用</li>
	 *     <li>oDesignArea: 设计区域的div的引用</li>
	 *     <li>oSettingArea: 设计区域中的选项div的引用</li>
	 * </ul>
	 * 继承自该类的对象需要重写 fillDesignBody 方法填充设计的主题区域
	 *
	 * @param {object, string} selector 用来容纳问题的div
	 * @param {Page} parent 页面对象
	 * @param {object, string} json 问题对应的json
	 * @param {number} number 问题的题号
	 * @constructor 用来构造Question对象. 该对象并没有实际用处. 只是替子类实现了一些功能
	 */
	function Question(selector, parent, json, number){
		this.json = json;
		this.oDiv = $(selector);
		this.number = number;
		this.parent = parent;
		this.init();
	}

	/**
	 * @private
	 * 初始化该问题对应的div, 添加相应的html标记
	 */
	Question.prototype.init = function (){
		var baseId = 'designQuestion' + new Date().getTime() + this.number;
		this.anchorId = baseId + "anchor";
		$('<a id="' + this.anchorId + '"></a>').appendTo(this.oDiv);

		var designAreaId = baseId + 'collapsePanel';
		this.oVisualArea = $('<div data-am-collapse="{target: \'#' + designAreaId + '\'}"></div>');
		this.oDesignArea = $('<div id=' + designAreaId + '></div>').addClass('am-panel am-panel-default am-panel-collapse am-collapse');
		this.visualObj = visualSurvey.createQuestion(this.oVisualArea, this.json, this.number);

		this.fillDesignArea();
		this.oDiv.append(this.oVisualArea).append(this.oDesignArea);
		this.addControlButtons();
	};

	/**
	 * 重新设置题号
	 * @param {number} number 问题编号
	 */
	Question.prototype.setNumber = function (number){
		this.number = number;
		this.visualObj.setNumber(number);
	};

	/**
	 * @protected
	 * 填充设计区域.
	 * 会依次调用 fillDesignHeader, fillDesignBody, fillDesignFooter 方法
	 */
	Question.prototype.fillDesignArea = function (){
		this.fillDesignHeader();
		this.fillDesignBody();
		this.fillDesignFooter();
	};

	/**
	 * @protected
	 * 填充设计头部区域. 依次调用 fillDesignTitle, fillCommonSettingArea, fillSpecificSettingArea(如果有的话) 方法.
	 * 如果需要添加自己特有的选项, 可以复写 fillSpecificSettingArea 方法. 向 this.oSettingArea 中添加选项
	 */
	Question.prototype.fillDesignHeader = function (){
		var oHeadDiv = $('<div class="am-cf am-panel-bd"></div>');
		this.fillDesignTitle(oHeadDiv);
		this.fillCommonSettingArea(oHeadDiv);
		if( this.fillSpecificSettingArea ){
			this.fillSpecificSettingArea();
		}
		oHeadDiv.appendTo(this.oDesignArea);
	};

	/**
	 * @protected
	 * 填充设计区域的题目部分.
	 * @param {object} oHeadDiv 题目部分将被添加到该div中
	 */
	Question.prototype.fillDesignTitle = function (oHeadDiv){
		var _this = this;
		var json = this.json;

		var titleArea = $('<textarea class="am-u-sm-12" rows="3"></textarea>').val(json.title);
		titleArea.change(function (){
			_this.json.title = this.value;
			_this.visualObj.reFill();
		});

		$('<div class="am-u-sm-8"></div>').append($('<label>题目:</label>'))
			.append(titleArea).appendTo(oHeadDiv);
	};

	/**
	 * @protected
	 * 在设置区域填充公共设置
	 * @param {object} oHeadDiv 题目部分将被添加到该div中
	 */
	Question.prototype.fillCommonSettingArea = function (oHeadDiv){
		var _this = this;

		this.oSettingArea = $('<div></div>').appendTo(oHeadDiv);
		var oRequiredLabel = $('<label class="am-checkbox-inline">必答题</label>');
		var oRequiredInput = $('<input type="checkbox"/>');
		oRequiredInput.change(function (){
			_this.json.required = this.checked;
			_this.visualObj.reFill();
		});
		if( this.json.required ){
			oRequiredInput.attr('checked', 'checked');
		}
		oRequiredLabel.append(oRequiredInput);
		this.oSettingArea.append(oRequiredLabel).append($('<br/>'));
	};

	/**
	 * @private
	 * 在设计区域的下方添加完成编辑按钮
	 */
	Question.prototype.fillDesignFooter = function (){
		var _this = this;
		var oComplete = $('<button class="am-btn am-btn-primary am-btn-block">完成编辑</button>');
		oComplete.click(function (){
			_this.oDesignArea.collapse('close');
		});
		this.oDesignArea.append(oComplete);
	};

	/**
	 * @private
	 * 在问题的下面添加控制按钮组
	 */
	Question.prototype.addControlButtons = function (){
		var _this = this;
		var oBtnToolbar = $('<div class="am-center"></div>');
		var buttonClass = 'am-btn am-radius am-margin-horizontal-sm am-margin-top-sm';

		var oInsertBtn = $('<button type="button" class="' + buttonClass + '">在此题前插入新题</button>');
		oInsertBtn.click(function (){
			_this.parent.setInsertPlace(_this, false);
		});
		var oAppendBtn = $('<button type="button" class="' + buttonClass + '">在此题后插入新题</button>');
		oAppendBtn.click(function (){
			_this.parent.setInsertPlace(_this, true);
		});
		oBtnToolbar.append(oInsertBtn).append(oAppendBtn);

		var oEditBtn = $('<button type="button" class="' + buttonClass + '">编辑</button>');
		oEditBtn.click(function (){
			_this.oDesignArea.collapse('open');
		});
		var oCopyBtn = $('<button type="button" class="' + buttonClass + '">复制</button>');
		oCopyBtn.click(function (){
			_this.parent.copyQuestionToSurvey(_this.json);
		});
		var oDeleteBtn = $('<button type="button" class="' + buttonClass + '">删除</button>');
		oDeleteBtn.click(function (){
			_this.parent.deleteQuestion(_this);
		});
		oBtnToolbar.append(oEditBtn).append(oCopyBtn).append(oDeleteBtn);

		var oUpBtn = $('<button type="button" class="' + buttonClass + '">上移</button>');
		oUpBtn.click(function (){
			_this.parent.shiftUpQuestion(_this);
		});
		var oDownBtn = $('<button type="button" class="' + buttonClass + '">下移</button>');
		oDownBtn.click(function (){
			_this.parent.shiftDownQuestion(_this);
		});
		oBtnToolbar.append(oUpBtn).append(oDownBtn);

		var oToFirstBtn = $('<button type="button" class="' + buttonClass + '">最前</button>');
		oToFirstBtn.click(function (){
			_this.parent.toFirstQuestion(_this)
		});
		var oToLastBtn = $('<button type="button" class="' + buttonClass + '">最后</button>');
		oToLastBtn.click(function (){
			_this.parent.toLastQuestion(_this);
		});
		oBtnToolbar.append(oToFirstBtn).append(oToLastBtn);

		this.oDiv.append(oBtnToolbar);
	};

	/**
	 * 判断该问题数据是否合法.. 该方法只检查标题, 子类需要根据情况进行更详细的检查
	 * @return {boolean} 有效返回true, 否则返回false
	 */
	Question.prototype.isValid = function (){
		return !this.json.title;
	};

	/**
	 * 选择题. 支持 单选题, 多选题
	 * @param {object, string} selector 用来容纳问题的div
	 * @param {Page} parent 调查页面对象
	 * @param {object} json 问题的json
	 * @param {number} number 问题的题号
	 * @constructor 用来构造 SelectQuestion 对象.
	 */
	function SelectQuestion(selector, parent, json, number){
		if( !json.options ){
			json.options = [];
		}
		Question.call(this, selector, parent, json, number);
	}

	tools.inheritPrototype(SelectQuestion, Question);

	/**
	 * 选项的排列类型
	 */
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

	/**
	 * 填充选项区域
	 */
	SelectQuestion.prototype.fillSpecificSettingArea = function (){
		var _this = this;

		var settingLabel = $('<label class="am-checkbox-inline am-padding-left-0">排列方式:</label>');
		var styleArea = $(SelectQuestion.style).appendTo(settingLabel);
		var styleOptions = styleArea.find('option');
		if(!this.json.style){
			this.json.style = 0;
		}
		for( var i = 0; i < styleOptions.length; i++ ){
			if( this.json.style == styleOptions[i].value ){
				styleOptions[i].selected = 'selected';
			}
		}
		styleArea.change(function (){
			_this.json.style = this.value;
			_this.visualObj.reFill();
		});
		this.oSettingArea.append(settingLabel);
	};

	/**
	 * 填充设计区域的主题部分
	 */
	SelectQuestion.prototype.fillDesignBody = function (){
		var _this = this;
		var json = this.json;

		var allOption = '';
		for( var i = 0; i < json.options.length; i++ ){
			allOption += json.options[i].option + "\n";
		}
		var optionArea = $('<textarea class="am-u-sm-12" rows="4"></textarea>').val(allOption);
		optionArea.change(function (){
			var options = this.value.split('\n');
			_this.json.options = [];
			for( var i = 0; i < options.length; i++ ){
				var option = options[i].trim();
				if( option != '' ){
					_this.json.options.push({option: options[i]});
				}
			}
			_this.visualObj.reFill();
		});
		$('<div  class="am-panel-bd am-cf am-padding-horizontal-lg am-padding-top-0"></div>')
			.append($('<label>选项(每个选项一行):</label>')).append(optionArea).appendTo(this.oDesignArea);
	};

	/**
	 * 判断该问题数据是否合法.
	 * @return {boolean} 有效返回true, 否则返回false
	 */
	SelectQuestion.prototype.isValid = function (){
		var json = this.json;
		if( !json.title ){
			return false;
		}
		return json.options && json.options.length != 0;
	};

	window.designSurvey.SelectQuestion = SelectQuestion;
	window.designSurvey.registeredQuestion['checkbox'] = SelectQuestion;
	window.designSurvey.registeredQuestion['radio'] = SelectQuestion;

})();

