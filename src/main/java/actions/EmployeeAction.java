package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
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


    public void entryNew() throws ServletException, IOException{

        //CSRF対策用トークン
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //空の従業員インスタンス
        //   putR-S==>request.setAttribute(key.getValue(), value);
        //リクエストスコープに"employee"=従業員インスタンスを保存する
        putRequestScope(AttributeConst.EMPLOYEE, new EmployeeView());

        forward(ForwardConst.FW_EMP_NEW);   //    "/WEB-INF/views/%s.jsp"の%sに"employees/new"を当てる
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException{

        //CSRF対策 tokenのチェック
        if(checkToken()) {

            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)), //文字列0or1を取得し、toNumberでIntegerにする
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());  //this.iでDEL_FLAG_FALSEの0を返す。

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst.PEPPER);  //contextが絡む処理が理解できていない…pepperて何？

            //従業員情報登録
            List<String> errors = service.create(ev, pepper);  //奥が深い…create(*,*)ハッシュ化したパス+日時をevに追加。エラーがあれば返す
                                                               //エラーがなければcreate(*)でDBに登録までする

            if(errors.size() > 0) {
                //登録中にエラーがあった場合。リクエストスコープに３種の情報を保存する。
                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン  …エラーなのに取得してどうする？
                putRequestScope(AttributeConst.EMPLOYEE, ev ); //入力された従業員情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_EMP_NEW);
            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                                                        //  I_REGISTERED==>("登録が完了しました。"),
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクトする
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }
        }
    }



}
