package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
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

}
