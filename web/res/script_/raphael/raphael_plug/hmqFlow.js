/**
 * rapheal.js 的工具类 hejian 2013-05-27
 */
var HMQ_FLOW = {
	paper_ : null,// 画布,该值必须在最开始设置
	textStyle : {// 文本字体样式
		'fill' : 'yellow',
		'font-size' : '12px',
		'font-family' : 'Times New Roman,Georgia,Serif',
		'text-anchor' : 'start'
	},
	taskType:{// 什么任务,枚举
		userTask:''
	}
	taskStyle:{// 任务样式
		fill : "90-#fff-#C0C0C0",
		stroke : "#000",
		"stroke-width" : 1
	},
	drawPaper : function(paperID) {// 初始化需调研的方法,先建立画布
		HMQ_FLOW.paper_= Raphael(paperID, 3000, 800);
	},
	/**
	 * 创建矩形框，且触发一些事件
	 * 
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 * @param text
	 *            显示的文本
	 * @param color
	 *            矩形颜色
	 * @param img
	 *            图标
	 */
	drawTask : function(x, y, text, color,img) {
		var taskImg = HMQ_R._paper.image("./util/" + img + ".png", x + 5,
				y + 5, 16, 16);//创建任务
		var taskText = HMQ_R._paper.text(x + 50, y + 25, text).attr( {
			'font-size' : '12px'
		});
		var rectFlow = HMQ_R._paper.rect(x, y, 101, 50, 5).attr(taskStyle);
		rectFlow.data("text", taskText).data("img", taskImg).click(function() {
			HMQ_R.drawBox(this);// 画边界框线
			}).drag(function(dx, dy, mx, my, handler) {
			if (handler.button == 0) {
				this.attr( {
					x : this.xx + dx,
					y : this.yy + dy
				});
				this.data("text").attr( {
					x : this.xx + dx + 55,
					y : this.yy + dy + 25
				});
				this.data("img").attr( {
					x : this.xx + dx + 5,
					y : this.yy + dy + 5
				});
				/***************************************************************
				 * else {// 没边界的时候是 画线 var linePath="M " +
				 * (this.xx+this.getBBox().width/2) + " " +
				 * (this.yy+this.getBBox().height/2) + " L" + (this.xx
				 * +dx+this.getBBox().width/2) + " " + (this.yy +
				 * dy+this.getBBox().height/2); if (HMQ_R._line == null) {
				 * HMQ_R._line = HMQ_R._paper.path(linePath).attr( { 'stroke' :
				 * 'green', 'arrow-end' : 'open-narrow-long', 'stroke-width' :
				 * 2, 'cursor' : 'pointer' }).mouseup(function(handler) { if
				 * (handler.button == 2) { HMQ_R._contextObj = this;
				 * HMQ_R.bindContextmenu(); } }); } else { HMQ_R._line.attr( {
				 * "path" : linePath }); } }
				 **************************************************************/
			}
		}, function() {
			this.xx = this.attr("x");
			this.yy = this.attr("y");
			/** this.animate( {"fill-opacity" : 2}, 500);* */
		}, function() {
			HMQ_R._line = null;
			/** this.animate( {"fill-opacity" : 1}, 300);* */
		}).mouseup(function(handler) {
			if (handler.button == 2) {
				// handler.button 0:左键，1中键，2右键
				HMQ_R._contextObj = this;// 存储对象
				HMQ_R.bindContextmenu();
			}
		}).toBack();
		return rectFlow;
	},

}