package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.EmployeeService;

/**
 * 従業員に関わる処理を行うActionクラス
 *
 */
public class EmployeeAction extends ActionBase {

    private EmployeeService service; //この変数には何が入る？ざっくりしすぎてイメージできない。

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

        //メソッドを実行。パラメータからcommandを取得し、それを実行する。不正なコマンドならエラー画面にフォワード
        invoke();

        service.close(); //emのクローズ

    }

    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage(); //パラメータから"page"に対応する情報を数値で取得する
        List<EmployeeView> employees = service.getPerPage(page); //1-15,16-30ごと従業員情報を取得？

        //全ての従業員データの件数を取得
        long employeeCount = service.countAll();

        //リクエストスコープにそれぞれパラメータを保存
        putRequestScope(AttributeConst.EMPLOYEES, employees);//取得した従業員データ
        putRequestScope(AttributeConst.EMP_COUNT, employeeCount);//全ての従業員データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);//1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        //各アクションで「登録が完了しました」や「更新が完了しました」、「削除が完了しました」といった内容を一覧画面にフラッシュメッセージとして表示します。

        //いつflush文をセッションスコープに保存しているのか？
        String flush = getSessionScope(AttributeConst.FLUSH);

        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush); //文字列flushがあったらリクエストスコープに保存
            removeSessionScope(AttributeConst.FLUSH); //セッションスコープからは削除する
        }

        //一覧画面を表示     "employees/index"にフォワード
        forward(ForwardConst.FW_EMP_INDEX);

    }

}
