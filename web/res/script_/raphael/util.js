/**
 * rapheal.js 的工具类
 * hejian 2013-05-27
 */
var HMQ_R={
	N:'#BCD2EE',
	E:'#00FFFF',
	S:'#7FFF00',
	B:'#7FFF00',
	_textStyle:{'fill':'yellow','font-size':'12px','font-family':'Times New Roman,Georgia,Serif','text-anchor':'start'},
	_drawBox:null,//边界框对象
	_paper:null,//该值必须在最开始设置
	_glow:null,//存储光晕效果
	_msgRect:new Array(),//信息框对象,包括文本对象，图片对象，矩形框对象
	creatLine:function(rectF,rectS,color){
		var objF=rectF.getBBox(true);
		var objS=rectS.getBBox(true);
		//连接线，路径字符串
		var linePath="M"+(objF.x+objF.width/2)+" "+(objF.y+objF.height/2)+" L"+(objS.x+objS.width/2)+" "+(objS.y+objS.height/2);
		var _node=HMQ_R.findNode(rectF, linePath);
		var node_=HMQ_R.findNode(rectS, linePath);
		linePath="M"+_node.x+" "+_node.y+" L"+node_.x+" "+node_.y;
		HMQ_R._paper.path(linePath).attr({'stroke':color,'arrow-end':'open-narrow-long','stroke-width':1.5,'cursor':'help'});//线
	},
	/**
	 * 创建2个矩形间懂的退回连接线
	 * @param rectF 第一个矩形对象
	 * @param rectS 第二个矩形对象
	 * @author 何建 2013/07/18
	 */
	creatLine_:function(rectF,rectS){
		var objF=rectF.getBBox(true);//第一个
		var objS=rectS.getBBox(true);//第二个
		var linePath="M"+(objF.x+objF.width/2)+" "+(objF.y+objF.height/2)+" L"+(objS.x+objS.width/2)+" "+(objS.y+objS.height/2);
		var _node=HMQ_R.findNode(rectF, linePath);
		var node_=HMQ_R.findNode(rectS, linePath);
		if(_node.y==node_.y){//横线
			linePath="M"+_node.x+" "+(_node.y+5)+" L"+node_.x+" "+(node_.y+5);
		}else if(_node.x==node_.x){//竖线
			linePath="M"+(_node.x+5)+" "+_node.y+" L"+(node_.x+5)+" "+node_.y;
		}else{
			linePath="M"+(objF.x+objF.width/2)+" "+(objF.y+objF.height/2-10)+" L"+(objS.x+objS.width/2)+" "+(objS.y+objS.height/2-10);
			_node=HMQ_R.findNode(rectF, linePath);
			node_=HMQ_R.findNode(rectS, linePath);
			linePath="M"+_node.x+" "+_node.y+" L"+node_.x+" "+node_.y;
		}
		HMQ_R._paper.path(linePath).attr({'stroke':"red",'arrow-end':'open-narrow-long','stroke-width':1.5,'cursor':'help'});//线
	},
	/**
	 * 查找矩形框4条边与指定路径的交点
	 * @param rect 矩形对象
	 * @param path 路径字符串
	 * @author 何建 2013/07/18
	 */
	findNode:function(rect,path){
		var obj=rect.getBBox(true)
		var xLT=obj.x-2;var yLT=obj.y-2;//左上坐标
		var xRT=obj.x+obj.width+2;var yRT=obj.y-2;//右上坐标
		var xRB=obj.x2+2;var yRB=obj.y2+2;//右下坐标
		var xLB=obj.x-2;var yLB=obj.y+obj.height+2;//左下坐标
		var _rectHR="M"+xRT+" "+yRT+" L"+xRB+" "+yRB;//矩形右边竖线边框；
		var _rectHL="M"+xLT+" "+yLT+" L"+xLB+" "+yLB;//矩形左边竖线边框；
		var _rectVB="M"+xLB+" "+yLB+" L"+xRB+" "+yRB;//矩形框下边横线边框；
		var _rectVT="M"+xLT+" "+yLT+" L"+xRT+" "+yRT;//矩形框上边横线边框；
		var _node=Raphael.pathIntersection(path,_rectHR);
		if(_node.length>0)
			return _node[0];//返回的是数组，在本方法只取第一个；
		_node=Raphael.pathIntersection(path,_rectHL);
		if(_node.length>0)
			return _node[0];
		_node=Raphael.pathIntersection(path,_rectVB);
		if(_node.length>0)
			return _node[0];
		_node=Raphael.pathIntersection(path,_rectVT);
		if(_node.length>0)
			return _node[0];
	},
	/**
	 * 创建矩形框，且触发一些事件
	 * @param x x坐标
	 * @param y y坐标
	 * @param text 显示的文本
	 * @param color 矩形颜色
	 * @param img 图标
	 */
	createRect:function(x,y,text,color,img){
		if(img==null){
			img="edit";
		}
		var c="90-white-"+color;
		var rectFlow=HMQ_R._paper.rect(x,y,130,60,10).attr({'fill':c,'stroke':null})
		//HMQ_R._paper.image("/cms/raphael/"+img+".png",x+5,y+5,16,16);
		HMQ_R._paper.image("./util/"+img+".png",x+5,y+5,16,16);
		HMQ_R._paper.text(x+65,y+35,text).attr({'font-size':'12px'});
		//console.log(222);
		//if(color==HMQ_R.N||color==HMQ_R.B){
		//	return rectFlow;
		//}
		rectFlow.hover(function(){
			HMQ_R._glow=this.glow();
			console.log(this.id);
			if(HMQ_R._msgRect.length==0){
				HMQ_R._createMsg(this);
			}
		},function(handler){
			HMQ_R._glow.remove();
			for(var i=0;i<HMQ_R._msgRect.length;i++){
				HMQ_R._msgRect[i].remove();
			}
			HMQ_R._msgRect.splice(0,HMQ_R._msgRect.length);
		});
		return rectFlow;
	},
	/**
	 * 根据前一个矩形框与是否决策创建矩形框，且触发一些事件，且画出线条
	 * @param rect 前一个矩形对象
	 * @param color 颜色
	 * @param isDecision 是否决策
	 */
	createRectBefor:function(rect,color,isDecision){
		var rectF=rect.getBBox();
		var c="90-white-"+color;
		var rectFlow;
		if(isDecision){
			rectFlow=HMQ_R._paper.rect(x,y,150,70,10).attr({'fill':c,'stroke':null})
		}else{
			rectFlow=HMQ_R._paper.rect(x,y,150,70,10).attr({'fill':c,'stroke':null})
		}
		
		HMQ_R._paper.image("/cms/raphael/edit.png",x+5,y+5,16,16);
		HMQ_R._paper.text(x+60,y+25,text);
		if(color==HMQ_R.N||color==HMQ_R.B){
			return rectFlow;
		}
		rectFlow.hover(function(){
			HMQ_R._glow=this.glow();
			if(HMQ_R._msgRect.length==0){
				HMQ_R._createMsg(this);
			}
			
		},function(handler){
			HMQ_R._glow.remove();
			for(var i=0;i<HMQ_R._msgRect.length;i++){
				HMQ_R._msgRect[i].remove();
			}
			HMQ_R._msgRect.splice(0,HMQ_R._msgRect.length);
		});
		return rectFlow;
	},
	/**
	 * 创建信息框，且触发一些事件
	 * @param rectFlowx 矩形对象
	 */
	_createMsg:function(rectFlow){
		var c="90-#EE9A00-#1d3f7f";
		var _obj=rectFlow.getBBox();
		HMQ_R._msgRect.push(HMQ_R._paper.rect(_obj.x2,_obj.y2,200,100,10).attr({'fill':c,'stroke':null,'title':'ssssss'}));
		HMQ_R._msgRect.push(HMQ_R._paper.image("/cms/raphael/edit.png",_obj.x2+5,_obj.y2+5,16,16));
		HMQ_R._msgRect.push(HMQ_R._paper.text(_obj.x2+15,_obj.y2+30,"任务人:彭洁结").attr(HMQ_R._textStyle));
		HMQ_R._msgRect.push(HMQ_R._paper.text(_obj.x2+15,_obj.y2+50,"处理时间:2012-03-30 17:55:22").attr(HMQ_R._textStyle));
		return HMQ_R._msgRect;
	},
	drawBox:function(rectObj){
		var obj=rectObj.getBBox();
		var x=obj.x+obj.width+4;
		var y=obj.y+obj.height+4;
		return HMQ_R._paper.path("M"+(obj.x-4)+" "+(obj.y-4)+"H"+x+"V"+y+"H"+(obj.x-4)+"Z").attr("stroke","blue");
	}
}