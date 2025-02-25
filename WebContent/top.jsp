<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
		<link href="./css/style.css" rel="stylesheet" type="text/css">
		<title>簡易Twitter</title>
	</head>
	<body>
		<div class="main-contents"> <!-- 画面のパーツのまとまりを <div> タグで囲っておくと、CSSが適用しやすくなる。 -->
		<!--<div class="header">
				<a href="login">ログイン</a>
	            今いるリソースから相対位置で「～/signup」というURLにアクセスすることを指す
	            formタグのaction属性と似た形でServletとのマッピングを行うことができる。この場合はdoGetが実行されます。
				<a href="signup">登録する</a>
			</div> -->
		<div class="header">
		    <c:if test="${ empty loginUser }"> <!-- ログインユーザ情報が無ければ「ログイン」「登録する」を表示 -->
		        <a href="login">ログイン</a>
		        <a href="signup">登録する</a>
		    </c:if>
		    <c:if test="${ not empty loginUser }"> <!-- ログインユーザ情報があれば「ホーム」｢設定｣「ログアウト」を表示 -->
		        <a href="./">ホーム</a>
		        <a href="setting">設定</a>
		        <a href="logout">ログアウト</a>
		    </c:if>
		</div>
		<c:if test="${ not empty loginUser }">
		    <div class="profile">
		        <div class="name"><h2><c:out value="${loginUser.name}" /></h2></div>
		        <div class="account">@<c:out value="${loginUser.account}" /></div>
		        <div class="description"><c:out value="${loginUser.description}" /></div>
		    </div>
		</c:if>

		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

		<div class="form-area">
			<c:if test="${ isShowMessageForm }">
				<form action="message" method="post"> <!-- POSTするURL（action属性）にmessageを指定 -->
					いま、どうしてる？<br />
					<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
					<br /> <input type="submit" value="つぶやく">（140文字まで）
				</form>
			</c:if>
		</div>
		<div class="messages">
			<c:forEach items="${messages}" var="message">
				<div class="message">
					<div class="account-name">
						<span class="account">
							<a href="./?user_id=<c:out value="${message.userId}"/> ">　<!-- c:outタグ：変数を出力 -->
								<c:out value="${message.account}" />
							</a>
						</span>
						<span class="name"><c:out value="${message.name}" /></span>
					</div>
					<div class="text">
						<c:out value="${message.text}" />
					</div>
					<div class="date">
						<fmt:formatDate value="${message.createdDate}"
							pattern="yyyy/MM/dd HH:mm:ss" />
					</div>
					<!-- action属性でURL:deleteMessageを指定・id="deleteMessage"は一旦消し-->
					<form action="deleteMessage" method="post">
						<!-- name&value…キー & バリューの関係。name:user.id としてサーバーにリクエストされる ・onClick="return Check()"は一旦消し-->
						<input name="messageId" value="${message.id}" type="hidden" />
						<input type="submit" value="削除">
					</form>
				</div>
			</c:forEach>
		</div>
		<div class="copyright"> Copyright(c)togashi</div>
	        </div>
	</body>
</html>