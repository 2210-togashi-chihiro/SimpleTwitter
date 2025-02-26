package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.dao.UserDao;
import chapter6.logging.InitApplication;
import chapter6.utils.CipherUtil;

/*★ユーザの登録機能★　アプリケーション層　Serviceクラス
 *サービスは、Userオブジェクトを引数にとり、それをDBに登録します。
 * パスワードをそのままDBに登録するということはセキュリティ上好ましくないので、
 * パスワードは暗号化して登録します。 */
public class UserService {


    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public UserService() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }
    /*■User登録時チェック用のselect■*/
    public User select(String account) {

        Connection connection = null;
        try {
            connection = getConnection();
            User user = new UserDao().select(connection, account);
            commit(connection);

            return user;
        } catch (RuntimeException e) {
            rollback(connection);
            throw e;
        } catch (Error e) {
            rollback(connection);
            throw e;
        } finally {
            close(connection);
        }
    }

    /*■User登録時のinsert■*/
    public void insert(User user) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        Connection connection = null;
        try {
            // パスワード暗号化　CipherUtilを呼出し文字列を暗号化→バイト配列(byte[])に変換
            String encPassword = CipherUtil.encrypt(user.getPassword());
            user.setPassword(encPassword);

            connection = getConnection();
            new UserDao().insert(connection, user);
            commit(connection);
        } catch (RuntimeException e) {
            rollback(connection);
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } catch (Error e) {
            rollback(connection);
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } finally {
            close(connection);
        }
    }

    /*■User表示用のselect■*/
    public User select(String accountOrEmail, String password) {

  	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
          " : " + new Object(){}.getClass().getEnclosingMethod().getName());

          Connection connection = null;
          try {
              // パスワード暗号化
              String encPassword = CipherUtil.encrypt(password);

              connection = getConnection();
              User user = new UserDao().select(connection, accountOrEmail, encPassword);
              commit(connection);

              return user;
          } catch (RuntimeException e) {
              rollback(connection);
  		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
              throw e;
          } catch (Error e) {
              rollback(connection);
  		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
              throw e;
          } finally {
              close(connection);
          }
      }
    /*■設定画面編集用のSelect■*/
    public User select(int userId) {

        log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        Connection connection = null;
        try {
            connection = getConnection();
            User user = new UserDao().select(connection, userId);
            commit(connection);

            return user;
        } catch (RuntimeException e) {
            rollback(connection);
    	  log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } catch (Error e) {
            rollback(connection);
    	  log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } finally {
            close(connection);
        }

    }

    /*■設定画面更新用のUpdate■*/
    public void update(User user) {

        log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        Connection connection = null;

        /*nullやら""やら" "やら考えないといけない、大変 → それらの条件を何個も書かなきゃ…そんな苦労から解放するメソッド
         *・isEmpty()：StringUtils.isEmpty()はnullと""の場合のみtrueを返す
         *・isBlank()：nullと""、" "（半角スペース）の全てにtrueを返す*/
        try {
        	if(!StringUtils.isBlank(user.getPassword())) {
        		// パスワード暗号化
        		String encPassword = CipherUtil.encrypt(user.getPassword());
        		user.setPassword(encPassword);
        	}
        	connection = getConnection();
        	new UserDao().update(connection, user);
            commit(connection);
        } catch (RuntimeException e) {
            rollback(connection);
    	  log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } catch (Error e) {
            rollback(connection);
    	  log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } finally {
            close(connection);
        }
    }
}