package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Report;
import models.validators.ReportValidator;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class ReportService extends ServiceBase {

    //登録、更新、検索等、日報テーブルの操作に関わるすべての処理をそれぞれメソッドとして定義します

    /**
     * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得し、ReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getMinePerPage(EmployeeView employee, int page){

//employeeのときと一緒。中身の疑問点を確認
        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }


    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

//employeeのときと一緒。中身の疑問点を確認。SQL文："SELECT COUNT(r) FROM Report AS r WHERE r.employee = : employee"
        long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
              //.setParameter(employee, EVインスタンスをDTOモデルにしたもの）
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();
        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getAllPerPage(int page){

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page -1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }


    /**
     * 日報テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long reports_count =(long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return reports_count;

    }

    /**
     * idを条件に取得したデータをReportViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public ReportView findOne(int id) {
                                    //findOneInternalは下で別途定義。idを条件にDBからレポートを1件取得する
        return ReportConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(ReportView rv){

        List<String> errors = ReportValidator.validate(rv); //レポートのタイトルと内容に入力があるかチェック
        if (errors.size() == 0){
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);  //下で別途定義。トランザクション開始、DB登録、コミットを行う

        }
        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された日報の登録内容を元に、日報データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(ReportView rv){

        //バリデーションを行う
        List<String> errors = ReportValidator.validate(rv);

        if(errors.size()==0) {

            //更新日時の設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);
            updateInternal(rv);
        }
        return errors;

    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Report findOneInternal(int id) {
        return em.find(Report.class, id);     //enum・・？構文の確認
    }

    /**
     * 日報データを1件登録する
     * @param rv 日報データ
     */
    private void createInternal(ReportView rv) {
        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();
    }

    /**
     * 日報データを更新する
     * @param rv 日報データ
     */
    private void updateInternal(ReportView rv) {
        em.getTransaction().begin();              //DB接続開始
        Report r = findOneInternal(rv.getId());   //画面のレポート情報rvのidを元に、DBから検索してrに格納

//【質問】画面の情報rvを、rに上書きしていく。id,employee,createAtは変更無いが毎回上書きする無駄な処理が発生。仕方ない？
        ReportConverter.copyViewToModel(r, rv);

        em.getTransaction().commit();             //
    }
}