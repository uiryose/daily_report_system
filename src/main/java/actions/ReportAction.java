package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ReportService;

public class ReportAction extends ActionBase{

    private ReportService service;
    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        //メソッドの追加
        invoke();
        service.close();

    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException{

        //指定されたページ数の一覧画面に表示する日報データを取得
            //リクエストパラメータから"page"の数値を取得
        int page = getPage();
        //1ページ目、2ページ目と対応するページのレポートを取得してリストに格納する
        List<ReportView> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
        long reportsCount = service.countAll();

        //リクエストスコープに情報を保存する
        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, reportsCount); //すべての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコード数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException{

      //CSRF対策用トークン
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv);  //日付のみ設定済みの日報インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);
    }

    /**
     * 日報を登録する
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException{

        //CSRF対策 tokenのチェック
        if(checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if(getRequestParam(AttributeConst.REP_DATE)==null
                    || getRequestParam(AttributeConst.REP_DATE).equals("")){
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE)); //parse(**)で文字列**をLocalDateのインスタンスを取得
            }

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //パラメータの値を元に日報情報のインスタンスを作成する
            ReportView rv = new ReportView(
                  //id,employee(id,code,name,password,adminFlag,+++),reportDate,title,content,createdAt,updatedAt
                    null,
                    ev,    //セッションから取得したログインしている従業員を、日報作成者として登録する
                    day,   //上で設定した日報の日付
                    getRequestParam(AttributeConst.REP_TITLE),
                    getRequestParam(AttributeConst.REP_CONTENT),
                    null,
                    null);

            //日報情報登録。上記rvを元に、ReportServiceクラスのcreate()メソッドでDB登録
            List<String> errors = service.create(rv);

            if(errors.size()>0) {
                //DB登録の中でエラーがあった場合、リクエストスコープに情報を保存

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv);//入力された日報情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_REP_NEW);

            } else {
                //DB登録中にエラーがなかった場合

                //セッションスコープに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }


}
