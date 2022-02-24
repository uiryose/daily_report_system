package actions.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
import models.Employee;

/**
 * 従業員データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class EmployeeConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param ev EmployeeViewのインスタンス
     * @return Employeeのインスタンス
     */

    public static Employee toModel(EmployeeView ev) {

        //値を代入する変数 = 条件式 ? 変数1 : 変数2;
        return new Employee(
                ev.getId(),
                ev.getCode(),
                ev.getName(),
                ev.getPassword(),   //それぞれ、ev.get**しているが具体的に何をgetter経由で取得できるのか？

                ev.getAdminFlag() == null
                //ev.getAdminFlag()がnullなら、nullを代入
                        ? null
                //nullでなければ、次の条件式→adminFlagのgetter発動。管理者フラグ(1:ROLR_ADMIN)から1を取得。getIntegerValue()はreturn this.iを返す
                        : ev.getAdminFlag() == AttributeConst.ROLE_ADMIN.getIntegerValue()
                        //adminFlag(0or1)と管理者フラグが一致するなら、ROLE_ADOMIN(1)を代入
                                ? JpaConst.ROLE_ADMIN
                        //一致しなければ、ROLE_GENERAL(0)を代入
                                : JpaConst.ROLE_GENERAL,

                ev.getCreatedAt(),
                ev.getUpdatedAt(),
                ev.getDeleteFlag() == null
                //ev.getDeleteFlag()がnullなら、nullを代入
                        ? null
                           //従業員evの削除フラグを取得＝＝削除フラグ(1が設定済み)の数値を取得
                        : ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                           //削除フラグが一致しなければ、EMP_DEL_TRUE(1)を設定
                                ? JpaConst.EMP_DEL_TRUE
                           //一致しなければ、EMP_DEL_FALSE(0)を代入
                                : JpaConst.EMP_DEL_FALSE);
        //結局何がreturnされているのか？
        //ev.getId()はEmployee evのIDを返す。そのIDはどこで設定している？

    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param e Employeeのインスタンス
     * @return EmployeeViewのインスタンス
     */
    public static EmployeeView toView(Employee e) {

        if(e == null) {
            return null;
        }

        return new EmployeeView(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getPassword(),
                e.getAdminFlag() == null
                        ? null
                        : e.getAdminFlag() == JpaConst.ROLE_ADMIN
                                ? AttributeConst.ROLE_ADMIN.getIntegerValue()
                                : AttributeConst.ROLE_GENERAL.getIntegerValue(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getDeleteFlag() == null
                        ? null
                        : e.getDeleteFlag() == JpaConst.EMP_DEL_TRUE
                                ? AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                                : AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    //EmployeeViewクラスは画面に表示する従業員と日付の情報を格納
    public static List<EmployeeView> toViewList(List<Employee> list){
      //ざっくりとしたリストをインスタンス化
        List<EmployeeView> evs = new ArrayList<>();
      //既に個別情報が格納されているlistから、toView(e)ごと改めてリストに格納←ここがわからない…
        for (Employee e : list) {
            evs.add(toView(e));
        }
        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param e DTOモデル(コピー先)
     * @param ev Viewモデル(コピー元)
     */
    public static void copyViewToModel(Employee e, EmployeeView ev) {
        e.setId(ev.getId());
        e.setCode(ev.getCode());
        e.setName(ev.getName());
        e.setPassword(ev.getPassword());
        e.setAdminFlag(ev.getAdminFlag());
        e.setCreatedAt(ev.getCreatedAt());
        e.setUpdatedAt(ev.getUpdatedAt());
        e.setDeleteFlag(ev.getDeleteFlag());

    }
}
