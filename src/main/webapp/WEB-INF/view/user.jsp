<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="BASE" value="${pageContext.request.contextPath}" />

<html>
<head>
	<title>用户管理</title>
</head>
<body>
	<h1>用户列表</h1>
	
	<table>
		<tr>
			<th>名称</th>
			<th>电话</th>
			<th>邮箱</th>
			<th>操作</th>
		</tr>
		<c:forEach var="user" items="${userList}">
		<tr>
			<td>${user.name}</td>
			<td>${user.mobile}</td>
			<td>${user.email}</td>
			<td>
				<a href="${BASE}/user_edit?id=${user.id}">编辑</a>
				<a href="${BASE}/user_delete?id=${user.id}">删除</a>
			</td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>