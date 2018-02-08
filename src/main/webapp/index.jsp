<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>upload</title>
</head>
<body>
<h2>Hello World!</h2>
springmvc上传文件
<form name="form1" action="/backManager/product/upload_file.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件"/>
</form>

富文本图片上传
<form name="form2" action="/backManager/product/ richText_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本图片上传"/>
</form>
</body>
</html>
