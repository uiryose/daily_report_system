package services;

import javax.persistence.EntityManager;

import utils.DBUtil;

//各Serviceクラスのスーパークラスとなるクラスです。
//SQL実行に共通で必要となる EntityManager インスタンスの作成やクローズ処理を実装します。

/**
* DB接続に関わる共通処理を行うクラス
*/
public class ServiceBase {

    /**
     * EntityManagerインスタンス
     */
    protected EntityManager em = DBUtil.createEntityManager();

//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    EntityManager em = DBUtil.createEntityManager(); //DBの接続
//    今までなら上記のやり方。なぜprotectedをここで設定するのか？するならemを共有するclose()もprotectedしたほうが良いのでは？

    /**
     * EntityManagerのクローズ
     */
    public void close() {
        if(em.isOpen()) {
            em.close();
        }
    }

}
