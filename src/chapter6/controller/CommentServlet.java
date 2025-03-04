package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public CommentServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();
    }

	/* doPost：
	 * 入力された返信の情報をログイン情報と併せてDBに登録、登録が終わるとトップ画面（top.jsp）にリダイレクト*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        HttpSession session = request.getSession();
        List<String> errorMessages = new ArrayList<String>();

        /*入力値を取得*/
        String text = request.getParameter("text");
        int messageId = Integer.parseInt(request.getParameter("messageId"));

        /*バリデーションチェック*/
        if (!isValid(text, errorMessages)) {
            session.setAttribute("errorMessages", errorMessages);
            /*遷移先(top.jsp)でエラーメッセージを表示 */
            response.sendRedirect("./");
            return;
        }

        /*インスタンス化*/
        Comment comment = new Comment();

        /*入力値を格納*/
        comment.setText(text);
        comment.setMessageId(messageId);

        /*ログイン情報取得、ユーザIDの格納*/
        User user = (User) session.getAttribute("loginUser");
        comment.setUserId(user.getId());

        /*Service呼び出し*/
        new CommentService().insert(comment);
        response.sendRedirect("./");
    }

 /* isValid：バリデーションチェック
  * 入力値が不正な場合falseをreturn*/
    private boolean isValid(String text, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        if (StringUtils.isBlank(text)) {
            errorMessages.add("メッセージを入力してください");
        } else if (140 < text.length()) {
            errorMessages.add("140文字以下で入力してください");
        }

        if (errorMessages.size() != 0) {
            return false;
        }
        return true;
    }

}
