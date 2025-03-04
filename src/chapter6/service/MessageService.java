package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.UserMessage;
import chapter6.dao.MessageDao;
import chapter6.dao.UserMessageDao;
import chapter6.logging.InitApplication;

public class MessageService {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public MessageService() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }
/*■つぶやきの登録■　messageServletから呼び出される*/
    public void insert(Message message) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        Connection connection = null;
        try {
    		/*接続情報（connection）を用意 - DBに接続できるようになる*/
            connection = getConnection();

    		/*Dao呼び出し*/
            new MessageDao().insert(connection, message);

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

/*■つぶやきの表示■　TopServletから呼び出される*/
    public List<UserMessage> select(String userId) {

    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
    			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

    	final int LIMIT_NUM = 1000;

    	Connection connection = null;
    	try {
    		/*接続情報（connection）を用意*/
    		connection = getConnection();

    	    /*idをnullで初期化 - userIdの値が渡ってきていたら整数型に型変換、idに代入*/
    	    Integer id = null;
    	    if(!StringUtils.isEmpty(userId)) {
    	        id = Integer.parseInt(userId);
    	    }

    		/* Dao呼び出し
    	     * 検索結果：idがnull…全件取得　　idがnull以外…その値に対応するユーザーIDの投稿を取得*/
    		List<UserMessage> messages = new UserMessageDao().select(connection, id, LIMIT_NUM);
    		commit(connection);
    		return messages;

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

    /*■つぶやきの削除■　deleteMessageServletから呼び出される*/
    public void delete(String messageId) {

    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
    			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

    	Connection connection = null;
    	try {
    		/*接続情報（connection）を用意 - DBに接続できるようになる*/
    		connection = getConnection();

    		/*Dao呼び出し*/
    		new MessageDao().delete(connection, Integer.parseInt(messageId));

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

    /*■つぶやきの編集を表示■　EditServletから呼び出される*/
    public Message select(int messageId) {

    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
    			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

    	Connection connection = null;
    	try {
    		/*接続情報（connection）を用意*/
    		connection = getConnection();

    		/*Dao呼び出し*/
    		Message messages = new MessageDao().select(connection, messageId);

    		/*DBの操作を確定(commit;)*/
    		commit(connection);

    		/*取得結果をServletへ返却*/
    		return messages;

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

    /*■つぶやきを編集(更新)■　EditServletから呼び出される*/
    public void update(Message message) {

    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
    			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

    	Connection connection = null;
    	try {
    		/*接続情報（connection）を用意*/
    		connection = getConnection();

    		/*Dao呼び出し*/
    		new MessageDao().update(connection, message);

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

}