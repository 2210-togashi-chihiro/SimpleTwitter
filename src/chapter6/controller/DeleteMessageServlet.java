package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/deleteMessage" })
public class DeleteMessageServlet extends HttpServlet {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public DeleteMessageServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    /*
     *  doPost：post呼び出しされると実行。
     *  submitされたつぶやきのid情報をDaoまで送りDBから削除、削除が終わるとトップ画面（top.jsp）を再表示（再読み込み）。*/
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {

    	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
            " : " + new Object(){}.getClass().getEnclosingMethod().getName());

          	/* jspからidを取得 ■name(messageId)をキーとして、value=${message.id} を取得■*/
    	  	String messageId = request.getParameter("messageId");

          	/*idを引数に、serviceへ引き渡し*/
            new MessageService().delete(messageId);

            /*トップ画面へ戻る*/
            response.sendRedirect("./");
        }
}
