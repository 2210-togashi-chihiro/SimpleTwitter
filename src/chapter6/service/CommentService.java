package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Comment;
import chapter6.beans.UserComment;
import chapter6.dao.CommentDao;
import chapter6.logging.InitApplication;

public class CommentService {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public CommentService() {
        InitApplication application = InitApplication.getInstance();
        application.init();
    }

    /*■つぶやきの登録■　commentServletから呼び出される*/
    public void insert(Comment comment) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        Connection connection = null;
        try {
    		/*接続情報（connection）を用意*/
            connection = getConnection();

    		/*Dao呼び出し*/
            new CommentDao().insert(connection, comment);

    		/*DBの操作を確定(commit;)*/
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

/*■返信の表示■　TopServletから呼び出される*/
    public List<UserComment> select() {

    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
    			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

    	final int LIMIT_NUM = 1000;

    	Connection connection = null;
    	try {
    		/*接続情報（connection）を用意*/
    		connection = getConnection();

    		/*Dao呼び出し*/
    		List<UserComment> comments = new UserCommentDao().select(connection, LIMIT_NUM);

    		/*DB操作を確定(commit;)*/
    		commit(connection);

    		return comments;

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
