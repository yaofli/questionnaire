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

$(function(){
	var oLogin = $('#userLogin');
	var loginPage = '';
	var loading;

	oLogin.click(function (){
		loading = layer.load(1, {
			shade: [0.1, '#fff']
		});

		if(loginPage == ''){
			$.get('/user/loginPage', function (content){
				loginPage = content;
				openLoginPage();
			}, 'html');
		}
		else{
			openLoginPage();
		}
		return false;
	});

	function openLoginPage(){
		layer.close(loading);
		layer.open({
			type: 1,
			skin: 'layui-layer-rim',
			area: ['450px', '350px'],
			content: loginPage
		});
	}

});
