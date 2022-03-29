package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
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
    public void remove(Follow f) {
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
    public Follow getOne(int myId, int followId) {
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

    /**
     * 自分のフォローデータを取得し、FollowViweのリストで返却する
     * @return 一覧画面に表示するデータのリスト
     */
    public List<FollowView> getAllMine(EmployeeView employee) {

        List<Follow> follows = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL_MINE, Follow.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getResultList();
        return FollowConverter.toViewList(follows);
    }







}
