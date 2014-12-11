/**
 * rapheal.js 的工具类 hejian 2013-05-27
 */
var HMQ_R = {
	N : '#BCD2EE',
	E : '#00FFFF',
	S : '#7FFF00',
	B : '#7FFF00',
	_textStyle : {
		'fill' : 'yellow',
		'font-size' : '12px',
		'font-family' : 'Times New Roman,Georgia,Serif',
		'text-anchor' : 'start'
	},
	_imgPath : "./res/css/flow/icon/",
	_drawBox : null,// 边界对象
	_paper : null,// 该值必须在最开始设置
	_glow : null,// 存储光晕效果
	_startPoint : null,//
	_line : null,// 储存画的线条
	_contextObj : null,// 存储右键点击的对象
	_msgRect : new Array(),// 信息框对象,包括文本对象，图片对象，矩形框对象
	_isDrag : false,
	drawPaper : function(paperID) {
		HMQ_R._paper = Raphael(paperID, 3000, 800);
	},
	creatLine : function(rectF, rectS, color) {
		var objF = rectF.getBBox(true);
		var objS = rectS.getBBox(true);
		// 连接线，路径字符串
		var linePath = "M" + (objF.x + objF.width / 2) + " "
				+ (objF.y + objF.height / 2) + " L" + (objS.x + objS.width / 2)
				+ " " + (objS.y + objS.height / 2);
		var _node = HMQ_R.findNode(rectF, linePath);
		var node_ = HMQ_R.findNode(rectS, linePath);
		linePath = "M" + _node.x + " " + _node.y + " L" + node_.x + " "
				+ node_.y;
		HMQ_R._paper.path(linePath).attr( {
			'stroke' : color,
			'arrow-end' : 'open-narrow-long',
			'stroke-width' : 1.5,
			'cursor' : 'help'
		});// 线
	},
	/**
	 * 创建2个矩形间懂的退回连接线
	 * 
	 * @param rectF
	 *            第一个矩形对象
	 * @param rectS
	 *            第二个矩形对象
	 * @author 何建 2013/07/18
	 */
	creatLine_ : function(rectF, rectS) {
		var objF = rectF.getBBox(true);// 第一个
		var objS = rectS.getBBox(true);// 第二个
		var linePath = "M" + (objF.x + objF.width / 2) + " "
				+ (objF.y + objF.height / 2) + " L" + (objS.x + objS.width / 2)
				+ " " + (objS.y + objS.height / 2);
		var _node = HMQ_R.findNode(rectF, linePath);
		var node_ = HMQ_R.findNode(rectS, linePath);
		if (_node.y == node_.y) {// 横线
			linePath = "M" + _node.x + " " + (_node.y + 5) + " L" + node_.x
					+ " " + (node_.y + 5);
		} else if (_node.x == node_.x) {// 竖线
			linePath = "M" + (_node.x + 5) + " " + _node.y + " L"
					+ (node_.x + 5) + " " + node_.y;
		} else {
			linePath = "M" + (objF.x + objF.width / 2) + " "
					+ (objF.y + objF.height / 2 - 10) + " L"
					+ (objS.x + objS.width / 2) + " "
					+ (objS.y + objS.height / 2 - 10);
			_node = HMQ_R.findNode(rectF, linePath);
			node_ = HMQ_R.findNode(rectS, linePath);
			linePath = "M" + _node.x + " " + _node.y + " L" + node_.x + " "
					+ node_.y;
		}
		HMQ_R._paper.path(linePath).attr( {
			'stroke' : "red",
			'arrow-end' : 'open-narrow-long',
			'stroke-width' : 1.5,
			'cursor' : 'help'
		});// 线
	},
	/**
	 * 查找矩形框4条边与指定路径的交点
	 * 
	 * @param rect
	 *            矩形对象
	 * @param path
	 *            路径字符串
	 * @author 何建 2013/07/18
	 */
	findNode : function(rect, path) {
		var obj = rect.getBBox(true)
		var xLT = obj.x - 2;
		var yLT = obj.y - 2;// 左上坐标
		var xRT = obj.x + obj.width + 2;
		var yRT = obj.y - 2;// 右上坐标
		var xRB = obj.x2 + 2;
		var yRB = obj.y2 + 2;// 右下坐标
		var xLB = obj.x - 2;
		var yLB = obj.y + obj.height + 2;// 左下坐标
		var _rectHR = "M" + xRT + " " + yRT + " L" + xRB + " " + yRB;// 矩形右边竖线边框；
		var _rectHL = "M" + xLT + " " + yLT + " L" + xLB + " " + yLB;// 矩形左边竖线边框；
		var _rectVB = "M" + xLB + " " + yLB + " L" + xRB + " " + yRB;// 矩形框下边横线边框；
		var _rectVT = "M" + xLT + " " + yLT + " L" + xRT + " " + yRT;// 矩形框上边横线边框；
		var _node = Raphael.pathIntersection(path, _rectHR);
		if (_node.length > 0)
			return _node[0];// 返回的是数组，在本方法只取第一个；
		_node = Raphael.pathIntersection(path, _rectHL);
		if (_node.length > 0)
			return _node[0];
		_node = Raphael.pathIntersection(path, _rectVB);
		if (_node.length > 0)
			return _node[0];
		_node = Raphael.pathIntersection(path, _rectVT);
		if (_node.length > 0)
			return _node[0];
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
	drawTask : function(x, y, text, color, img) {
		if (img == null) {
			img = "edit";
		}
		var c = "90-white-" + HMQ_R.S;
		var taskImg = HMQ_R._paper.image("./util/" + img + ".png", x + 5,
				y + 5, 16, 16);
		var taskText = HMQ_R._paper.text(x + 50, y + 25, text).attr( {
			'font-size' : '12px'
		});
		var rectFlow = HMQ_R._paper.rect(x, y, 101, 50, 5).attr( {
			fill : "90-#fff-#C0C0C0",
			stroke : "#000",
			"stroke-width" : 1
		});
		rectFlow.data("text", taskText).data("img", taskImg).click(function() {
			//$("#desginXml").layout("panel","south").panel("refresh","./core/workflow/design/flowProperty.jsp");
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
	/**
	 * 画任务周边的虚线框
	 */
	drawBox : function(rectObj) {
		var obj = rectObj.getBBox();
		var x = obj.x + obj.width + 6;
		var y = obj.y + obj.height + 6;
		if (HMQ_R._drawBox != null) {// 在创建边界的时候同时删除上一个边界
			if (HMQ_R._drawBox.data("rect") != null) {
				HMQ_R._drawBox.data("rect").removeData("objBox");
			}
			HMQ_R._drawBox.remove();
			HMQ_R._drawBox = null;
		}
		// 画边框虚线
		var objBox = HMQ_R._paper.path(
				"M" + (obj.x - 6) + " " + (obj.y - 6) + "H" + x + "V" + y + "H"
						+ (obj.x - 6) + "Z").attr( {
			"stroke" : "#000",
			"fill" : "none",
			"stroke-width" : 2,
			"stroke-dasharray" : "--.",
			"stroke-linecap" : "butt"// string[“butt”, “square”, “round”]
		});
		HMQ_R._drawBox = objBox;
		objBox.data("rect", rectObj);
		rectObj.data("objBox", objBox);
		// return HMQ_R._drawBox;
	},
	drowLittleCircle : function(x, y, r) {
		var c = "90-white-green";
		return HMQ_R._paper.circle(x, y, 5).attr( {
			'fill' : c,
			'stroke' : null
		}).hover(function() {
			HMQ_R._glow = this.glow();
		}, function(handler) {
			HMQ_R._glow.remove();
		}).drag(
				function(dx, dy) {
					if (HMQ_R._line == null) {
						HMQ_R._line = HMQ_R._paper.path(
								"M " + this.lcX + " " + this.lcY + " L"
										+ (this.lcX + dx) + " "
										+ (this.lcY + dy)).attr( {
							'stroke' : 'green',
							'arrow-end' : 'open-narrow-long',
							'stroke-width' : 2,
							'cursor' : 'pointer'
						}).mouseup(function(handler) {
							// handler.button 0:左键，1中键，2右键
								if (handler.button == 2) {
									HMQ_R._contextObj = this;// 存储对象
									HMQ_R.bindContextmenu();
								}
							});

					} else {
						HMQ_R._line.attr( {
							"path" : "M " + this.lcX + " " + this.lcY + " L"
									+ (this.lcX + dx) + " " + (this.lcY + dy)
						});
					}
				}, function() {
					this.lcX = this.attr("cx");
					this.lcY = this.attr("cy");
					this.animate( {
						"fill-opacity" : .2
					}, 500);
				}, function() {// 拖动结束
					this.animate( {
						"fill-opacity" : 1
					}, 300);

					HMQ_R._line = null;
				});
		// return rectFlow;
	},
	bindContextmenu : function() {
		$("#paper").bind('contextmenu', function(e) {
			e.preventDefault();
			$('#flowMenu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		});
	},
	objectRemove : function() {
		if (HMQ_R._contextObj.data("text") != null) {
			if (HMQ_R._contextObj.data("objBox") != null) {
				HMQ_R._contextObj.data("objBox").remove();// 删除边界
}
HMQ_R._contextObj.data("text").remove();// 删除文本
	HMQ_R._contextObj.data("img").remove();// 删除
}
HMQ_R._contextObj.remove();// 销毁任务图形
}
}