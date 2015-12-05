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

	/**
	 * 使subType继承superType的原型
	 * @param subType 子类
	 * @param superType 父类
	 */
	window.tools.inheritPrototype = function(subType, superType){
		var prototype = object(superType.prototype); //创建对象
		prototype.constructor = subType; //增强对象
		subType.prototype = prototype; //指定对象
	};

	/**
	 * 查找元素在数组中第一次出现的位置
	 * @param {Array} array 数组
	 * @param {*} element 需要查找的元素
	 */
	window.tools.indexOfArray = function(array, element){
		for(var i=0; i<array.length; i++){
			if(array[i] == element){
				return i;
			}
		}
		return -1;
	};

})();