package actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constants.AttributeConst;
import constants.ForwardConst;
import constants.PropertyConst;


/**
 * 各Actionクラスの親クラス。共通処理を設定しておく
 *
 */

public abstract class ActionBase {

    protected ServletContext context;

    //doGet doPostメソッドの引数に利用。パラメータ受渡し、JSPの呼び出し、リダイレクト処理でも活用
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    /**
     * 初期化処理：サーブレットコンテキスト、リクエスト、レスポンスを暮らすフィールドに設定
     * @param servletContext
     * @param servletRequest
     * @param servletResponse
     */

    //上記のフィールドの考え方？がよくわからない

    public void init(
            ServletContext servletContext,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
            ) {
        this.context = servletContext;
        this.request = servletRequest;
        this.response = servletResponse;
    }

//    public void init1(ServletContext servletContext1) {
//        this.context = servletContext1;
//  //initメソッドで引数を渡したら、フィールドのcontextが引数の値になる。
     //ServletContextとはどんな引数なのか・・・？init()はどう使うのか？
//    }


    /**
     * フロントコントローラから呼び出されるメソッド
     * @throws ServletException
     * @throws IOException
     */
    public abstract void process() throws ServletException, IOException;


    /**
     * パラメータのcommandの値に該当するメソッドを実行する
     * @throws ServletException
     * @throws IOException
     */

    protected void invoke() throws ServletException, IOException {

        Method commandMethod;
        try {
            //パラメータからcommandを取得
                                                 //ForwardConst.CMD="command"の文字列を取得
            String command = request.getParameter(ForwardConst.CMD.getValue());

            //commandに該当するメソッドを実行する
            //(例: action=Employee command=show の場合 EmployeeActionクラスのshow()メソッドを実行する)
            //getDeclareMethod(xx)でパラメータで指定されたメソッドを、クラスから呼び出す
            commandMethod = this.getClass().getDeclaredMethod(command, new Class[0]);
            //Method型の変数commandMethodには何が入っている・・・？

            commandMethod.invoke(this, new Object[0]);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NullPointerException e) {

            e.printStackTrace();
                   //FW_ERR_UNKNOWN("error/unknown")
            //forward()メソッドも下で定義
            forward(ForwardConst.FW_ERR_UNKNOWN);

        }
    }
    /**
     * 指定されたjspの呼び出しを行う
     * @param target 遷移先jsp画面のファイル名(拡張子を含まない)
     * @throws ServletException
     * @throws IOException
     */
                            //enum型
    protected void forward(ForwardConst target) throws ServletException, IOException {

        //jspファイルの相対パスを作成
                                                        //%s:toString()として出力する
        String forward = String.format("/WEB-INF/views/%s.jsp", target.getValue());
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);

        //jspファイルの呼び出し
        dispatcher.forward(request, response);

    }



    protected void redirect(ForwardConst action, ForwardConst command)throws ServletException, IOException{

      //URLを構築             //daily_report_system/?action=、     actionは引数から
        String redirectUrl = request.getContextPath()+"/?action="+action.getValue();
        if (command != null) {                       //commandにはcommandそのものの文字列を取得している
            redirectUrl = redirectUrl + "&command=" + command.getValue();
        }
      //URLへリダイレクト
        response.sendRedirect(redirectUrl);

      //今までは→  response.sendRedirect(request.getContextPath()+"/index");
    }

    /**
     * CSRF対策 token不正の場合はエラー画面を表示
     * @return true: token有効 false: token不正
     * @throws ServletException
     * @throws IOException
     */
    protected boolean checkToken() throws ServletException, IOException{
      //パラメータからtokenの値を取得
         //getRequestParamは下で別途定義。AttributeConst.TOKEN =="_token"の文字列
        String _token = getRequestParam(AttributeConst.TOKEN);

                                            //getTokenId()は下で別途定義
        if(_token == null || !(_token.equals(getTokenId()))) {

          //tokenが設定されていない、またはセッションIDと一致しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return false;
        }else {
            return true;
        }
    //  今まではこんな感じ
    //  if(_token != null && _token.equals(request.getSession().getId())) {
    }

    /**
     * セッションIDを取得する
     * @return セッションID
     */
    protected String getTokenId() {
        return request.getSession().getId();
    }


    /**
     * リクエストから表示を要求されているページ数を取得し、返却する
     * @return 要求されているページ数(要求がない場合は1)
     */
    protected int getPage() {
        int page;
             //toNumberは下で別途定義。int化するだけ  //AttributeConst.PAGE=="page"という文字列をgetValue()で取得
        page = toNumber(request.getParameter(AttributeConst.PAGE.getValue()));

                //Integerの最小値－21億？と比較。なぜ？
        if(page == Integer.MIN_VALUE) {
            page = 1;
        }
        return page;
    }

    /**
     * 文字列を数値に変換する
     * @param strNumber 変換前文字列
     * @return 変換後数値
     */
    protected int toNumber(String strNumber){
        int number =0;
        try {
            number = Integer.parseInt(strNumber);
        } catch (Exception e) {
            number = Integer.MIN_VALUE;  //エラーチェックの便宜上int型の最小値を代入
        }
        return number;
    }

    /**
     * 文字列をLocalDate型に変換
     * @param strDate 変換前文字列
     * @return 変換後LocalDateインスタンス
     */
    protected LocalDate toLocalDate(String strDate) {
        if (strDate == null || strDate.equals("")) {
            return LocalDate.now();
        }
        return LocalDate.parse(strDate);
    }


    /**
     * リクエストスコープから指定されたパラメータの値を取得し、返却する
     * @param key パラメータ名
     * @return パラメータの値
     */
    protected String getRequestParam(AttributeConst key) {
                              //keyにはEMP_ID、REP_COUNTなどA-Constで設定したものが来る
        return request.getParameter(key.getValue());
    }

    /**
     * リクエストスコープにパラメータを設定する
     * @param key パラメータ名
     * @param value パラメータの値
     */
            //<V>はString,intなんでもOK
    protected <V> void putRequestScope(AttributeConst key, V value) {
        request.setAttribute(key.getValue(), value);
    }

    /**
     * セッションスコープから指定されたpラメータの値を取得し、返却する
     * @param key パラメータ名
     * @return パラメータの値
     */
    //メッセージボードの場合はどうなっていたのか？
    @SuppressWarnings("unchecked")
    protected <R> R getSessionScope(AttributeConst key) {
        return (R) request.getSession().getAttribute(key.getValue());
    }

    /**
     * セッションスコープにパラメータを設定する
     * @param key パラメータ名
     * @param value パラメータの値
     */
    protected <V> void putSessionScope(AttributeConst key, V value) {
        request.getSession().setAttribute(key.getValue(),value);
    }

    /**
     * セッションスコープから指定された名前のパラメータを除去する
     * @param key パラメータ名
     */
    protected void removeSessionScope(AttributeConst key) {
        request.getSession().removeAttribute(key.getValue());
    }

    /**
     * アプリケーションスコープから指定されたパラメータの値を取得し、返却
     * @param key パラメータ名
     * @return パラメータの値
     */
    @SuppressWarnings("unchecked")
    protected <R> R getContextScope(PropertyConst key) {
        return (R) context.getAttribute(key.getValue());
    }

}
