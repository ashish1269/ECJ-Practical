<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Send Email</title>
</head>
<body>

<h2>Send e-mail</h2>

<form action="send-mail" method="post">
	<table>
		<tr>
			<td><label for="send-to">To: </label></td>
			<td><input type="text" name="send-to" id="send-to" ></td>
		</tr>
		<tr>
			<td><label for="subject">Subject: </label></td>
			<td><input type="text" name="subject" id="subject"></td>
		</tr>
		<tr>
			<td><label for="send-to">Message: </label></td>
			<td><textarea name="message" id="message" rows="10"></textarea></td>
		</tr>
	<tr>
		<td> </td><td><button type="submit">Send Mail</button></td>
	</tr>
	</table>
</form>

</body>
</html>