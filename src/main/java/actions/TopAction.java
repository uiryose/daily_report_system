package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

public class TopAction extends ActionBase {

    //   ReportService：登録、更新、検索等、日報テーブルの操作に関わるすべての処理をそれぞれメソッドとして定義したクラス
    //   トップページに自分の日報一覧表示するために追記
    private ReportService service;

    /**
     * indexメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        invoke();

        service.close();
    }


    /**
     * 一覧画面を表示する
     */

   public void index() throws ServletException, IOException{

       //セッションからログイン中の従業員情報を取得                  "login_employee"の文字列
       //AuthActionクラスのlogin()メソッドでログイン成功時にセッションにLOGIN_EMPのオブジェクト情報を保存している。
       EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

       //ログイン中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
       int page = getPage();   //なんのPageを取得する？
       List<ReportView> reports = service.getMinePerPage(loginEmployee, page); //よくわからない・・・。

       //ログイン中の従業員が作成した日報データの件数を取得
       long myReportsCount = service.countAllMine(loginEmployee);

       putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
       putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //ログイン中の従業員が作成した日報の数
       putRequestScope(AttributeConst.PAGE, page); //ページ数
       putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数




       //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
       String flush = getSessionScope(AttributeConst.FLUSH);

       if(flush != null) {
           putRequestScope(AttributeConst.FLUSH, flush);
           removeSessionScope(AttributeConst.FLUSH);
       }

       //一覧画面の表示
       forward(ForwardConst.FW_TOP_INDEX);
   }

}
