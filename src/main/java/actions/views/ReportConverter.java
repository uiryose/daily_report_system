package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Report;

/**
 * 日報データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class ReportConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv ReportViewのインスタンス
     * @return Reportのインスタンス
     */
    public static Report toModel(ReportView rv) {

                  //Report(id,employee,reportDate,title,content,createdAt,updatedAt)
        return new Report(
                rv.getId(),
                EmployeeConverter.toModel(rv.getEmployee()),
                rv.getReportDate(),
                rv.getTitle(),
                rv.getContent(),
                rv.getCreatedAt(),
                rv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスReport rからViewモデルのインスタンスを作成する
     * @param r Reportのインスタンス
     * @return ReportViewのインスタンス
     */
    public static ReportView toView(Report r) {

        if(r== null) {
            return null;
        }
                //ReportView(id, employee,reportDate,title,content,createdAt,updatedAt)
        return new ReportView(
                r.getId(),
                EmployeeConverter.toView(r.getEmployee()),
                r.getReportDate(),
                r.getTitle(),
                r.getContent(),
                r.getCreatedAt(),
                r.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
                      //ReportViewは画面表示用に変数を一式用意している
                                                  //Report rはDB用のテーブル情報を覚えておくクラス
    public static List<ReportView> toViewList(List<Report> list){

        List<ReportView> evs = new ArrayList<>();     //DBから都度取れるのにあえてList化する用途を考える

        for (Report r : list) {
            evs.add(toView(r));   //id～apdatedAtまでReportクラスからReportViewクラスに変換しながら、List<RV>に格納
        }
        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル（コピー先）
     * @param rv Viewモデル（コピー元）
     */

    //疑問：View画面からパラメータ取得→DTOモデルにコピー→データベースに登録の流れ・・・？
    public static void copyViewToModel(Report r, ReportView rv) {

        r.setId(rv.getId());   //（rvのViewモデルのidを取得）して、DTOモデルのrにセットする
        r.setEmployee(EmployeeConverter.toModel(rv.getEmployee())); //Emp-C.toModelでViewインスタンスをDTOモデルに変換
        r.setReportDate(rv.getReportDate());
        r.setTitle(rv.getTitle());
        r.setContent(rv.getContent());
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());
    }


}
