package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet  extends HttpServlet {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public EditServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    /* doget：画面表示*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

      	/* jspからidを取得 ■name(messageId)をキーとして、value=${message.id} を取得■*/
	  	String messageId = request.getParameter("messageId");


      	/*idを引数に、serviceへ引き渡し*/
	  	Message message = new MessageService().Select(messageId);

		request.setAttribute("message", message);
		 //遷移先を指定
		request.getRequestDispatcher("edit.jsp").forward(request, response);

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
            new MessageService().select(messageId);

            /*トップ画面へ戻る*/
            response.sendRedirect("./");
        }

}
