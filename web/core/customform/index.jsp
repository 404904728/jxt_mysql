<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="./res/ckeditor/ckeditor.js"></script>

<title>Insert title here</title>
</head>
<body>
<textarea  cols="80" id="editor1" name="editor1" rows="10">
</textarea>
<script>

			CKEDITOR.replace( 'editor1', {
				on: {//事件监听
		        		instanceReady: function() {
				            //alert( this.name ); // 'editor1'
				        },
				        key: function() {
					        //...
				        }
			    }
			});

		</script>
</body>
</html>