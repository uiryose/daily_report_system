package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Follow;

/**
 * フォローデータのDTOモデルとViewモデルの変換を行うクラス
 *
 */
public class FollowConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param fv FollowViewのインスタンス
     * @return Followのインスタンス
     */
    public static Follow toModel(FollowView fv) {

        return new Follow(
                fv.getId(),
                fv.getMyId(),
                fv.getFollowId(),
                fv.getCreatedAt());
    }

    /**
    * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
    * @param f Followのインスタンス
    * @return FollowViewのインスタンス
    */
    public static FollowView toView(Follow f) {

        if (f == null) {
            return null;
        }

        return new FollowView(
                f.getId(),
                f.getMyId(),
                f.getFollowId(),
                f.getCreatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する。　必要？？
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<FollowView> toViewList(List<Follow> list) {
        List<FollowView> fvs = new ArrayList<>();

        for (Follow f : list) {
            fvs.add(toView(f));
        }

        return fvs;
    }


    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param f DTOモデル(コピー先)
     * @param fv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Follow f, FollowView fv) {
        f.setId(fv.getId());
        f.setMyId(fv.getMyId());
        f.setFollowId(fv.getFollowId());
        f.setCreatedAt(fv.getCreatedAt());
    }






}
