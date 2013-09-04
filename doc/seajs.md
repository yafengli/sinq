

#### 包装nvd3.js为CMD模块

		define(function (require, exports, module) {
			//@start
			//原d3.v3.js代码
			//@end
			module.exports = d3;
		});

		define(function (require, exports, module) {
			var d3 = require("d3");
			//@start
			//原nv.d3.js代码
			//@end
			module.exports = nv;
		});