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

(function (){

	window['tools'] = window['tools'] || {};

	// 下面两个函数摘自《JavaScript高级程序设计》（第3版）

	function object(o){
		function F(){
		}

		F.prototype = o;
		return new F();
	}

	function inheritPrototype(subType, superType){
		var prototype = object(superType.prototype); //创建对象
		prototype.constructor = subType; //增强对象
		subType.prototype = prototype; //指定对象
	}
	window['tools']['inheritPrototype'] = inheritPrototype;

})();