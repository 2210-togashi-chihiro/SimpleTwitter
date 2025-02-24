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

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

// ★ユーザの登録機能★　トップ画面にあるリンク「登録する」をクリック した先の画面を実装
@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {


   /**
   * ロガーインスタンスの生成
   */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public SignUpServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }


    /*①doGet：get呼び出しされると実行され、signup.jspを表示します。*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  /*doGetメソッドからユーザ登録画面(signup.jsp）へ遷移している部分。　forward＝遷移
	   * ※ピリオド区切りでメソッドを連続して呼び出す書き方はメソッドチェーン（method chain）と言います*/
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    /*②doPost：post呼び出しされると実行されます。
     * リクエストparamを Userオブジェクトにセット、Serviceのメソッドを呼び出しDBへ登録。
     * 登録完了したら再びトップ画面を表示*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        List<String> errorMessages = new ArrayList<String>();

        User user = getUser(request);
        if (!isValid(user, errorMessages)) {
            request.setAttribute("errorMessages", errorMessages);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }
        new UserService().insert(user);
        response.sendRedirect("./");
    }


    /*③getUser：ユーザー登録画面（signup.jsp ※後述）からの入力値（リクエストパラメータ）を取得します。*/
    private User getUser(HttpServletRequest request) throws IOException, ServletException {


	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        User user = new User();
        user.setName(request.getParameter("name"));
        user.setAccount(request.getParameter("account"));
        user.setPassword(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        user.setDescription(request.getParameter("description"));
        return user;
    }

    /*④isValid：入力値に対するバリデーションを行います。
     * 入力値が不正な場合には再度、自画面(signup)を表示するようにしています。 */
    private boolean isValid(User user, List<String> errorMessages) {


	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        String name = user.getName();
        String account = user.getAccount();
        String password = user.getPassword();
        String email = user.getEmail();

        if (!StringUtils.isEmpty(name) && (20 < name.length())) {
            errorMessages.add("名前は20文字以下で入力してください");
        }

        if (StringUtils.isEmpty(account)) {
            errorMessages.add("アカウント名を入力してください");
        } else if (20 < account.length()) {
            errorMessages.add("アカウント名は20文字以下で入力してください");
        }

        if (new UserService().select(account) != null) {
            errorMessages.add("アカウントが重複しています");
        }

        if (StringUtils.isEmpty(password)) {
            errorMessages.add("パスワードを入力してください");
        }

        if (!StringUtils.isEmpty(email) && (50 < email.length())) {
            errorMessages.add("メールアドレスは50文字以下で入力してください");
        }

        if (errorMessages.size() != 0) {
            return false;
        }
        return true;
    }
}

