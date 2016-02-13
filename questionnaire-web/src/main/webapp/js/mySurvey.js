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

$(function (){
	$(".deleteSurvey").click(function (){
		var _this = $(this);

		function confirm(){
			$.post(_this.attr('href'), {_method: 'DELETE'}, function (){
				_this.parent().parent().remove();
				layer.msg('删除成功', {time: 1500, closeBtn: 1});
			});
		}

		layer.confirm("您确认删除该调查吗?", {btn: ['确定', '取消']}, confirm, function (){
		});
		return false;
	});

	$('.reverseDesigning').click(function (){
		var _this = $(this);
		$.post(_this.attr('href'), {_method: 'PUT'}, function (){
			var originText = _this.text();
			if( originText.trim() == '开放问卷' ){
				_this.text('关闭问卷');
			}
			else{
				_this.text('开放问卷');
			}
			layer.msg('修改成功', {time: 1500, closeBtn: 1});
		});
		return false;
	});

});