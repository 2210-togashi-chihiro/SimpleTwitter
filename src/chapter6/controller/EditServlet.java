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

		/*sessionの取得*/
		HttpSession session = request.getSession();

      	/* jspからidを取得 ■name(messageId)をキーとして、value=${message.id} を取得■*/
	  	String messageId = request.getParameter("messageId");
		Message message = null;

		/*messageIdチェック*/
		if(!(StringUtils.isBlank(messageId)) && (messageId.matches("^[0-9]*$"))) {
      	/*idを引数に、serviceへ引き渡し*/
			int id = Integer.parseInt(messageId);
			message = new MessageService().select(id);
		}

		/*取得結果チェック*/
		if(message == null ) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

	  	/*サーブレットからJSPへ値を渡す*/
		request.setAttribute("message", message);

		/*遷移先を指定  forward…目的のリソースに直接アクセスする*/
		request.getRequestDispatcher("edit.jsp").forward(request, response);

	}

    /*
     *  doPost：入力したデータをサーバーに転送 */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {

        	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

        	/*jspからtextを取得 */
        	String text = request.getParameter("text");

        	/* jspからmessageIdを取得 …name(messageId)をキーとし、value=${message.id} を取得 */
    		int messageId = Integer.parseInt(request.getParameter("messageId"));

        	/*インスタンスを生成、jspから取得した値を代入*/
        	Message message = new Message();
        	message.setId(messageId);
        	message.setText(text);

        	/*serviceへ引渡し*/
        	new MessageService().update(message);

        	/*遷移先を指定  Redirect…目的のリソースに間接的にアクセスする*/
        	response.sendRedirect("./");
        }

}
