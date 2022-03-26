package services;

import java.time.LocalDateTime;

import javax.persistence.NoResultException;

import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.JpaConst;
import models.Follow;

/**
 * フォローテーブルの操作に関わる処理を行うクラス
 */
public class FollowService extends ServiceBase {

    public void create(FollowView fv) {

        //引数のidをフォローテーブルに追加する
        LocalDateTime now = LocalDateTime.now();
        fv.setCreatedAt(now);
        createInternal(fv);
    }


//    public Follow findOne(int myId, int followId) {
//        //引数のidに一致するフォロー情報を1件取得
//        return em.find(Follow.class, myId)
//    }

    /**
     * idを条件に取得したデータをFollowViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public FollowView findOne(int id) {
        return FollowConverter.toView(findOneInternal(id));
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Follow findOneInternal(int id) {
        return em.find(Follow.class, id);
    }

    /**
     * フォローデータを１件登録する
     * @param fv フォローデータ
     */
    public void createInternal(FollowView fv) {

        em.getTransaction().begin();
        em.persist(FollowConverter.toModel(fv));
        em.getTransaction().commit();
    }


    /**
     * フォローデータを削除する   (コンバーター入れてるがなぜか失敗する)
     * @param fv フォローデータ
     */
    public void remove(FollowView fv) {
        //フォローテーブルから引数のレコードを削除する

        em.getTransaction().begin();
        em.remove(FollowConverter.toModel(fv));  //データ削除
        em.getTransaction().commit();

    }
    /**
     * フォローデータを削除する
     * @param f フォローデータ
     */
    public void removeDTO(Follow f) {
        //フォローテーブルから引数のレコードを削除する

        em.getTransaction().begin();
        em.remove(f);  //データ削除
        em.getTransaction().commit();
    }


    /**
     * 検索したIDでフォローしていればインスタンスを返却
     * @param myId
     * @param followId
     * @return
     */
    public FollowView getOne(int myId, int followId) {
        Follow f = null;

        try {
            f = em.createNamedQuery(JpaConst.Q_FOL_GET_ONE,Follow.class)
                    .setParameter(JpaConst.JPQL_PARM_ID, myId)
                    .setParameter(JpaConst.JPQL_PARM_ID_FOL, followId)
                    .getSingleResult();

        } catch (NoResultException e) {
        }

        return FollowConverter.toView(f);
    }

    public Follow getOneDTO(int myId, int followId) {
        Follow f = null;

        try {
            f = em.createNamedQuery(JpaConst.Q_FOL_GET_ONE,Follow.class)
                    .setParameter(JpaConst.JPQL_PARM_ID, myId)
                    .setParameter(JpaConst.JPQL_PARM_ID_FOL, followId)
                    .getSingleResult();

        } catch (NoResultException e) {
        }

        return f;
    }

//    /** 作り直し？？？
//     * 一覧画面に表示するフォローデータを取得し、FollowViweのリストで返却する
//     * @return 一覧画面に表示するデータのリスト
//     */
//    public List<FollowView> getAll() {
//
//        List<Follow> follows = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL_MINE, Follow.class)
//                .getResultList();
//        return FollowConverter.toViewList(follows);
//    }

}
