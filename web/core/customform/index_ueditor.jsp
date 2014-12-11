<%-- 
    Document   : index
    Created on : 2013-10-16, 18:50:03
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="./res/script_/hmq/commonEasy.js"></script>
        <script type="text/javascript" charset="utf-8" src="./res/ueditor1_2_6_1-src/ueditor.config.js"></script>
        <script type="text/javascript" charset="utf-8" src="./res/ueditor1_2_6_1-src/_examples/editor_api.js"></script>
        <script type="text/javascript" charset="utf-8" src="./res/ueditor1_2_6_1-src/hmqPlugins/inputhmq.js"></script>
        <title>自定义表单开发</title>
    </head>
    <body>
        <!--style给定宽度可以影响编辑器的最终宽度-->
        <script type="text/plain" id="myEditor">
            <p>这里我可以写一些输入提示</p>
        </script>
        <script type="text/javascript">
            UE.getEditor('myEditor',{
            //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
            //toolbars:[['FullScreen', 'Undo', 'Redo','Bold','inserttable','test']],
            //focus时自动清空初始化时的内容
            autoClearinitialContent:true,
            //关闭字数统计
            wordCount:false,
            //关闭elementPath
            elementPathEnabled:false,
            /**右键菜单contextMenu:[
            	{ 
                    label:'属性', 
                    icon:'',
                    cmdName:'bold'
                } 
            ],**/
            //默认的编辑区域高度
            initialFrameHeight:300
            //更多其他参数，请参考ueditor.config.js中的配置项
            })
        </script>
    </body>
</html>