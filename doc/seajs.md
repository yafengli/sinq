

#### 包装nvd3.js为CMD模块

		define(function (require, exports, module) {
			
			//原d3.v3.js代码
			
			module.exports = d3;
		});

		define(function (require, exports, module) {
			var d3 = require("d3");
			
			//原nv.d3.js代码
			
			module.exports = nv;
		});
		
#### Seajs Blogs

+ http://blog.codinglabs.org/articles/modularized-javascript-with-seajs.html		