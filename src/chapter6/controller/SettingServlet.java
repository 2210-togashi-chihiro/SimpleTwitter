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

import chapter6.beans.User;
import chapter6.exception.NoRowsUpdatedRuntimeException;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

/*■設定画面用Servlet■
 * ユーザ情報を編集する画面。
 * 情報はusersテーブルで管理されているため、編集（更新）する際はまずusersTBL情報を取得し、それを編集するべき。
 * 編集対象のユーザ情報を取得するためのメソッドをUserServiceクラス、UserDaoクラスに追記。*/
@WebServlet(urlPatterns = { "/setting" })
public class SettingServlet extends HttpServlet {

	/*
	* ロガーインスタンスの生成
	*/
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public SettingServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    /*■設定画面表示■*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        User user = new UserService().select(loginUser.getId());

        request.setAttribute("user", user);
        request.getRequestDispatcher("setting.jsp").forward(request, response);
    }

    /*■設定更新■*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        HttpSession session = request.getSession();
        List<String> errorMessages = new ArrayList<String>();

        User user = getUser(request);

        /*■更新処理（isValidがtrueの場合のみ続行）■*/
        if (isValid(user, errorMessages)) {
            try {
                new UserService().update(user);
            } catch (NoRowsUpdatedRuntimeException e) {
		    log.warning("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
                errorMessages.add("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
            }
        }


        /*■エラーメッセージが0でない場合、セットし画面へお返し■*/
        if (errorMessages.size() != 0) {
            request.setAttribute("errorMessages", errorMessages);
            request.setAttribute("user", user);
            request.getRequestDispatcher("setting.jsp").forward(request, response);
            return;
        }

        session.setAttribute("loginUser", user);
        response.sendRedirect("./");
    }

    /*■入力情報の取得■*/
    private User getUser(HttpServletRequest request) throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setName(request.getParameter("name"));
        user.setAccount(request.getParameter("account"));
        user.setPassword(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        user.setDescription(request.getParameter("description"));
        return user;
    }

    /*■バリデーションチェック■
     * 　true：エラー0
     * 　False：エラー1以上
    */
    private boolean isValid(User user, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        String name = user.getName();
        String account = user.getAccount();
/*        String password = user.getPassword();*/
        String email = user.getEmail();

        if (!StringUtils.isEmpty(name) && (20 < name.length())) {
            errorMessages.add("名前は20文字以下で入力してください");
        }
        if (StringUtils.isEmpty(account)) {
            errorMessages.add("アカウント名を入力してください");
        } else if (20 < account.length()) {
            errorMessages.add("アカウント名は20文字以下で入力してください");
        }
/*		if(user.getPassword() != null) {
 * 			if (StringUtils.isEmpty(password)) {
 * 				errorMessages.add("パスワードを入力してください");
 * 			}
 * 		}
 */
        if (!StringUtils.isEmpty(email) && (50 < email.length())) {
            errorMessages.add("メールアドレスは50文字以下で入力してください");
        }

        User checkAccount = new UserService().select(account);
        if(checkAccount != null && (user.getId() != checkAccount.getId())){
        	errorMessages.add("すでに存在するアカウントです");
        }

        if (errorMessages.size() != 0) {
            return false;
        }
        return true;
    }
}
