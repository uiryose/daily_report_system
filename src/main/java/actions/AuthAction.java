package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.EmployeeService;


/**
 * 認証に関する処理を行うActionクラス
 *
 */
public class AuthAction extends ActionBase{

    private EmployeeService service;

    /**
     * メソッドを実行する
     */
    @Override
    //この処理が本当にわからない・・・
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

        //メソッドを実行
        invoke();

        service.close();
    }


    /**
     * ログイン画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void showLogin() throws ServletException, IOException{

        //CSRF対策用トークンを設定
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //セッションにフラッシュメッセージが登録されている場合はリクエストスコープに設定する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH,flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //ログイン画面を表示
        forward(ForwardConst.FW_LOGIN);
    }

    public void login() throws ServletException, IOException{

        //ログイン画面から送られるIDとパスワードのリクエストパラメータの値を取得する
        String code = getRequestParam(AttributeConst.EMP_CODE);
        String plainPass = getRequestParam(AttributeConst.EMP_PASS);
        String pepper = getContextScope(PropertyConst.PEPPER);

        //有効な従業員か判断する
        Boolean isValidEmployee = service.validateLogin(code, plainPass, pepper);

        if(isValidEmployee) {
            //認証成功の場合

            //CSRF対策 tokenのチェック
            if(checkToken()) {

                //ログインした従業員のDBデータを取得
                //セッションにログインした従業員を設定
                //セッションにログイン完了のフラッシュメッセージを設定
                //トップページへリダイレクト

                EmployeeView ev = service.findOne(code, plainPass, pepper);
                putSessionScope(AttributeConst.LOGIN_EMP, ev);   //LOGIN_EMP==>"login_employee"
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGINED.getMessage());
                redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX); // action=top&command=indexでリダイレクト
            }
        } else {
            //認証失敗の場合

            //CSRF対策トークンを設定・・・この認証成功＆失敗のケースにおけるトークンについて解説を聞きたい。
            //認証失敗エラーメッセージ表示フラグをたてる
            //入力された従業員コードを設定
            //ログイン画面を表示

            putRequestScope(AttributeConst.TOKEN, getTokenId());
            putRequestScope(AttributeConst.LOGIN_ERR, true);
            putRequestScope(AttributeConst.EMP_CODE, code);  //認証失敗時はlogin.jspの<input value="初期値">用に${code}を設定しておく

            forward(ForwardConst.FW_LOGIN);

        }

    }



}
