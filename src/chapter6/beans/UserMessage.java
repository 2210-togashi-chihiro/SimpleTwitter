package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

/*
 * 【messagesテーブル・usersテーブル】の情報を格納できるBean
 * メッセージエリア：つぶやきだけでなく、アカウント名等”誰がつぶやいたか”も一緒に表示されているため、両方を取得する必要がある。
*/
/*
 * 【補足】Beanの考え方
 * これまで作成したUser.javaとMessage.javaに加え、今回はUserMessage.javaを用意しました。
 * どのような断面（単位）でBeanを作成するべきなのでしょうか。
 *
 * 今回の簡易Twitterでは、”扱うテーブルに対応させる形”でBeanを作成しています。
 * ・ユーザ登録＝usersテーブルに登録する＝Userクラス（User.java）で扱う
 * ・つぶやき登録＝messagesテーブルに登録する=Messageクラス（Message.java）で扱う
 * つまり、「つぶやきの表示」では以下の関係性から、UserMessage.javaを作成しました。
 * ・つぶやきの表示＝usersテーブルとmessagesテーブルから取得＝UserMessageクラス（UserMessage.java）で扱う
 *
 * 実際の開発では、アプリケーションの要件やプロジェクトのルールによって変わってきますので
 * 都度確認し統一性を持たせるようにしましょう。
*/
public class UserMessage implements Serializable {

    private int id;
    private String account;
    private String name;
    private int userId;
    private String text;
    private Date createdDate;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

    // getter/setterは省略されているので、自分で記述しましょう。

}