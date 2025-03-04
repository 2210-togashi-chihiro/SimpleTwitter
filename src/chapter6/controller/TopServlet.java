package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;


@WebServlet(urlPatterns = { "/index.jsp" }) //「http://localhost8080/SimpleTwitter/｣にアクセスしたときに実行
public class TopServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*↓ログが出力できるようにするための実装*/
    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public TopServlet() {
    	InitApplication application = InitApplication.getInstance();
    	application.init();
    }

    /*↑ログが出力できるようにするための実装ここまで*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException {

    	/*ログを出力*/
    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
    			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

    	/*isShowMessageForm…つぶやき投稿エリアの宣言*/
    	boolean isShowMessageForm = false;

    	/*セッションからログインユーザーを取得*/
    	User user = (User) request.getSession().getAttribute("loginUser");

    	/*つぶやき投稿エリア判定*/
    	if (user != null) {
    		isShowMessageForm = true;
    	}

    	/*特定のユーザーのつぶやきだけ表示(user_Id搾りの時)*/
    	String userId = request.getParameter("user_id");
    	List<UserMessage> messages = new MessageService().select(userId);

    	/* サーブレットからJSPへ値を渡す
    	 * 構文：request.setAttribute("データの名前", 登録するデータ);  */
        request.setAttribute("messages", messages);
    	request.setAttribute("isShowMessageForm", isShowMessageForm);

    	/*遷移先を指定  forward…目的のリソースに直接アクセスする*/
    	request.getRequestDispatcher("/top.jsp").forward(request, response);
    }
}