package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.CommentConverter;
import actions.views.CommentView;
import constants.JpaConst;
import models.Comment;
import models.validators.CommentValidator;

public class CommentService extends ServiceBase {


    /**
     * 指定したレポートIDに関わるコメント情報を取得し、Viewリストで返却する
     * @param id レポートのID
     * @return
     */
    public  List<CommentView> getAllMine(int id){

        List<Comment> comments = em.createNamedQuery(JpaConst.Q_COM_GET_ALL_MINE, Comment.class)
                .setParameter(JpaConst.JPQL_PARM_ID, id)
                .getResultList();

        return CommentConverter.toViewList(comments);

    }


    /**
     * idを条件に取得したデータをCommentViewのインスタンスで返却する
     * @param id
     * @return
     */
    public CommentView findOne(int id) {
        return CommentConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力されたコメント内容を元にデータを1件作成し、コメントテーブルに登録する
     * @param cv コメントの内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(CommentView cv){

        List<String> errors = CommentValidator.validate(cv);

        if(errors.size() == 0) {

            LocalDateTime ldt = LocalDateTime.now();
            cv.setCreatedAt(ldt);
            cv.setUpdatedAt(ldt);
            createInternal(cv);

        }
        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;

    }


    /**
     * 画面から入力されたコメントの登録内容を元に、コメントデータを更新する
     * @param cv コメントの更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(CommentView cv){

        List<String> errors = CommentValidator.validate(cv);

        if(errors.size() == 0) {

            LocalDateTime ldt = LocalDateTime.now();
            cv.setUpdatedAt(ldt);
            updateInternal(cv);
        }

        return errors;
    }


    /**
     * コメントIDを条件にコメントデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Comment findOneInternal(int id) {
        return em.find(Comment.class, id);
    }


    /**
     * コメントデータを1件登録する
     * @param ev コメントデータ
     */
    private void createInternal(CommentView ev) {

        em.getTransaction().begin();
        em.persist(CommentConverter.toModel(ev));
        em.getTransaction().commit();
    }

    /**
     * コメントデータを1件更新する
     * @param ev コメントデータ
     */
    private void updateInternal(CommentView cv) {

        em.getTransaction().begin();
        Comment c = findOneInternal(cv.getId());
        CommentConverter.copyViewToModel(c, cv);
        em.getTransaction().commit();
    }

}
