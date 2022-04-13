package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.CommentView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.CommentService;
import services.ReportService;

public class ReportAction extends ActionBase{

    private ReportService service;
    private CommentService commentService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();
        commentService = new CommentService();

        //メソッドの追加
        invoke();
        service.close();
        commentService.close();

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

    /**
     * 詳細ページを表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException{

        //idを条件に日報データをDBから取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //リクエストパラメータの日報idを取得する
        int rep_id = toNumber(getRequestParam(AttributeConst.REP_ID));

        //日報idを元に、該当するコメントデータを取得する
        List<CommentView> comments = commentService.getAllMine(rep_id);

        String errors = getRequestParam(AttributeConst.ERR);

        if(rv==null) {
            //該当するデータがなければエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            //リクエストスコープの"report"にDBのレポート情報(idひとつ分)を保存する。
            //show.jspではリクエストスコープから情報を読み込む
            putRequestScope(AttributeConst.REPORT, rv);

        }

        if(comments.size() > 0) {
            //リクエストスコープにコメント情報を保存する
            putRequestScope(AttributeConst.COMMENTS, comments);

        }
        //詳細画面を表示
        forward(ForwardConst.FW_REP_SHOW);
    }


    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException{

        //リクエストパラメータのidを条件にDBから日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = getSessionScope(AttributeConst.LOGIN_EMP);


        //該当の日報データが存在しない、またはログインしている従業員が日報の作成者でない場合はエラー画面を表示
        if(rv == null || ev.getId() != rv.getEmployee().getId()) {
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            //CSRF対策用トークン
            putRequestScope(AttributeConst.TOKEN, getTokenId());
            //DBから取得した日報データ
            putRequestScope(AttributeConst.REPORT, rv);

            //編集画面を表示
            forward(ForwardConst.FW_REP_EDIT);
        }
    }

    /**
     * レポートの更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException{

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件にDBから日報データを取得する
            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //編集画面に入力された日報内容(日付、タイトル、内容)をrvに上書きで設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            //DBの日報データを更新する
            List<String> errors = service.update(rv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合
                //編集画面を再表示する。この画面にリクエストスコープに保存した更新できなかったデータを再表示

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                forward(ForwardConst.FW_REP_EDIT);

            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);   //("Report","index")
            }
        }
    }

}
