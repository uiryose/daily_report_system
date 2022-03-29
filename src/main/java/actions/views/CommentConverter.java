package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Comment;

/**
 * コメントデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class CommentConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param cv
     * @return
     */
    public static Comment toModel(CommentView cv) {

        return new Comment(
                cv.getId(),
                ReportConverter.toModel(cv.getReport()),
                EmployeeConverter.toModel(cv.getEmployee()),
                cv.getContent(),
                cv.getCreatedAt(),
                cv.getUpdatedAt(),
                cv.getEditFlag(),
                cv.getDeleteFlag());
    }

    /**
     * DTOモデルのインスタンスComment cからViewモデルのインスタンスを作成する
     * @param c Commentのインスタンス
     * @return ReportViewのインスタンス
     */
    public static CommentView toView(Comment c) {

        if(c == null) {
            return null;
        }
        return new CommentView(
                c.getId(),
                ReportConverter.toView(c.getReport()),
                EmployeeConverter.toView(c.getEmployee()),
                c.getContent(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getEditFlag(),
                c.getDeleteFlag());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<CommentView> toViewList(List<Comment> list){

        List<CommentView> lcv = new ArrayList<CommentView>();

        for(Comment c : list) {
            lcv.add(toView(c));
        }
        return lcv;

    }


    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param c DTOモデル（コピー先）
     * @param cv Viewモデル（コピー元）
     */

    //update処理時に、View入力したデータをDTOモデルに上書きする
    public static void copyViewToModel(Comment c, CommentView cv) {

        c.setId(cv.getId());
        c.setReport(ReportConverter.toModel(cv.getReport()));
        c.setEmployee(EmployeeConverter.toModel(cv.getEmployee()));
        c.setContent(cv.getContent());
        c.setCreatedAt(cv.getCreatedAt());
        c.setUpdatedAt(cv.getUpdatedAt());
        c.setEditFlag(cv.getEditFlag());
        c.setDeleteFlag(cv.getDeleteFlag());
    }



}
