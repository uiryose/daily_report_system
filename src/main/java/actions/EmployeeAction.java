package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.EmployeeService;
import services.FollowService;

/**
 * 従業員に関わる処理を行うActionクラス
 *
 */
//@MultipartConfig(location="C:/tmp", maxFileSize=1048576)
@MultipartConfig(location="C:/tmp", maxFileSize=1000000, maxRequestSize=1000000, fileSizeThreshold=1000000)
public class EmployeeAction extends ActionBase {

    private EmployeeService service;
    private FollowService followService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();
        followService = new FollowService();

        //メソッドを実行。パラメータからcommandを取得し、それを実行する。不正なコマンドならエラー画面にフォワード
        invoke();

        service.close();
        followService.close();

    }

    /**
     * 従業員の一覧画面を表示する。見れるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */

    public void index() throws ServletException, IOException {


        //管理者かどうかのチェック(追記)
        if(checkAdmin()) {

            //指定されたページ数の一覧画面に表示するデータを取得
            int page = getPage(); //パラメータから"page"に対応する情報を数値で取得する
            List<EmployeeView> employees = service.getPerPage(page, true); //1-15,16-30ごと従業員情報を取得

            //全ての従業員データの件数を取得
            long employeeCount = service.countAll(true);

            //リクエストスコープにそれぞれパラメータを保存
            putRequestScope(AttributeConst.EMPLOYEES, employees);//取得した従業員データ
            putRequestScope(AttributeConst.EMP_COUNT, employeeCount);//全ての従業員データの件数
            putRequestScope(AttributeConst.PAGE, page); //ページ数
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);//1ページに表示するレコードの数

            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }

            //searchメソッドからのアクセスと区別する
            String callMethod = "index";
            putRequestScope(AttributeConst.CALL_METHOD, callMethod);
            //一覧画面を表示
            forward(ForwardConst.FW_EMP_INDEX);
        }
    }

    /**
     * 従業員の新規登録画面を表示する。登録できるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException{

        //管理者かどうかのチェック(追記)
        if(checkAdmin()) {

            //CSRF対策用トークン
            putRequestScope(AttributeConst.TOKEN, getTokenId());

            //空の従業員インスタンス
            //リクエストスコープに"employee"=従業員インスタンスを保存する
            putRequestScope(AttributeConst.EMPLOYEE, new EmployeeView());

            //新規登録画面を表示
            forward(ForwardConst.FW_EMP_NEW);
        }
    }

    /**
     * 従業員の新規登録を行う。登録できるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException{

        //CSRF対策 tokenのチェックと管理者権限のチェック
        if(checkToken() && checkAdmin()) {

            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)), //文字列0or1を取得し、toNumberでIntegerにする
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue(),
                    toNumber(getRequestParam(AttributeConst.EMP_POSITION_FLG)),
                    getRequestParam(AttributeConst.EMP_PROFILE_URL));

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst.PEPPER);  //contextが絡む処理が理解できていない…pepperて何？

            //従業員情報登録
            List<String> errors = service.create(ev, pepper);

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
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクトする
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 従業員情報の詳細画面を表示する。見れるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */

    public void show() throws ServletException, IOException {

        //管理者かどうかのチェック //追記
        if (checkAdmin()) {

            //idを条件に従業員データを取得する
            EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            //もしevが空か、evが削除済になっていたら、
            if(ev ==null || ev.getDeleteFlag()==AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

                //データが取得できなかった、または論理削除されている場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            //取得した従業員情報をリクエストスコープに保存
            putRequestScope(AttributeConst.EMPLOYEE, ev);

            //詳細画面show.jspにフォワード
            forward(ForwardConst.FW_EMP_SHOW);
        }
    }

    /**
     * 従業員情報の編集画面を表示する。見れるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //管理者かどうかのチェック //追記
        if (checkAdmin()) {

            //idを条件に従業員データを取得する
            EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            if(ev==null || ev.getDeleteFlag()==AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

                //データが取得できなかった、または論理削除されている場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            //CSRF対策用トークン
            putRequestScope(AttributeConst.TOKEN, getTokenId());

            //DBからID検で取得したev情報をリクエストスコープに保存する
            putRequestScope(AttributeConst.EMPLOYEE, ev);

            //編集画面を表示する
            forward(ForwardConst.FW_EMP_EDIT);
        }
    }


    /**
     * 従業員情報の更新を行う。更新できるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException{

        //画像を保存する処理を行う
        //name属性がEMP_PROFILE_URLのファイルをPartオブジェクトとして取得
        Part part = request.getPart(AttributeConst.EMP_PROFILE_URL.getValue());

        //従業員IDを元に画像のURLを取得する
        String imageName = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID))).getProfileUrl();

        if(part.getSubmittedFileName() != null || part.getSubmittedFileName().equals("")) { //ここが機能していない
            //新しいファイル名を取得(先頭に従業員番号を加える)
            imageName = getRequestParam(AttributeConst.EMP_CODE) + part.getSubmittedFileName(); //ie対応が不要な場合
            //フォルダに画像の書き込み
            part.write(context.getRealPath("/upload") + "/" + imageName);
        }


        //CSRF対策 tokenのチェックと管理者権限のチェック
        if(checkToken() && checkAdmin()) {
            //パラメータの値を元に従業員情報のインスタンスを作成する

            EmployeeView ev = new EmployeeView(
                    toNumber(getRequestParam(AttributeConst.EMP_ID)),
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue(),
                    toNumber(getRequestParam(AttributeConst.EMP_POSITION_FLG)),
                    imageName);

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst.PEPPER);

            //従業員情報更新
            List<String> errors = service.update(ev, pepper);

            if(errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId());
                putRequestScope(AttributeConst.EMPLOYEE, ev);  //編集用で入力された従業員情報
                putRequestScope(AttributeConst.ERR, errors);   //update()中に蓄積されたエラー

                //編集画面を再表示して、上記の注意文を表示
                forward(ForwardConst.FW_EMP_EDIT);
            } else {
                //エラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクトする
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX); //redirect(employee, index)
            }
        }
    }

    /**
     * 論理削除を行う 削除できるのは管理者のみ
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException{

        //CSRF対策 tokenのチェックと管理者権限のチェック
        if(checkToken() && checkAdmin()) {

            //idを条件に論理削除を行う
            service.destroy(toNumber(getRequestParam(AttributeConst.EMP_ID)));
            //destroy()メソッド内の、setAdminFLG(1)のセッターで論理削除上書き。更新日時も設定。DBに登録する

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクトする
            redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * ログイン中の従業員が管理者かどうかチェックし、管理者でなければエラー画面を表示
     * @return 管理者:true  管理者ではない:false
     * @throws ServletException
     * @throws IOException
     */
    private boolean checkAdmin() throws ServletException, IOException{

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //管理者でなければエラー画面を表示
        if(ev.getAdminFlag() != AttributeConst.ROLE_ADMIN.getIntegerValue()){

         forward(ForwardConst.FW_ERR_UNKNOWN);
         return false;

     } else {

         return true;
     }
    }

    /**
     * 在籍社員の一覧を取得して表示する
     * @throws ServletException
     * @throws IOException
     */
    public void all() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<EmployeeView> employees = service.getPerPage(page, false);

        //全ての従業員データの件数を取得
        long employeeCount = service.countAll(false);

        //リクエストスコープにそれぞれパラメータを保存
        putRequestScope(AttributeConst.EMPLOYEES, employees);//取得した従業員データ
        putRequestScope(AttributeConst.EMP_COUNT, employeeCount);//全ての従業員データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);//1ページに表示するレコードの数

        String flush = getSessionScope(AttributeConst.FLUSH);

        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush); //文字列flushがあったらリクエストスコープに保存
            removeSessionScope(AttributeConst.FLUSH); //セッションスコープからは削除する
        }

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //フォロー状態を反映するために、フォロー一覧のリストを作成する
        List<FollowView> follows = followService.getAllMine(ev);

      //取得したフォローデータをリクエストスコープに保存んする
        putRequestScope(AttributeConst.FOLLOWS, follows);

        //一覧画面を表示
        forward(ForwardConst.FW_EMP_ALL);
    }


    /**
     * フォロー従業員の一覧を取得して、お気に入り画面に表示する
     * @throws ServletException
     * @throws IOException
     */
    public void favorite() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //フォロー中の従業員件数を取得
        long folCount = followService.countAll(ev);

        //フォロー状態を反映するために、フォロー一覧のリストを作成する
        List<FollowView> follows = followService.getAllMine(ev);

        //取得したフォローデータをリクエストスコープに保存する
        putRequestScope(AttributeConst.FOLLOWS, follows);
        putRequestScope(AttributeConst.FOL_COUNT, folCount);


        //一覧画面を表示
        forward(ForwardConst.FW_EMP_FAVORITE);
    }


    /**
     * 検索項目に一致するデータを表示する
     * @throws ServletException
     * @throws IOException
     */
    public void search() throws ServletException, IOException{

        String code = getRequestParam(AttributeConst.EMP_CODE);
        String name = getRequestParam(AttributeConst.EMP_NAME);
        int adminFlag = toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG));

 System.out.println("テストcode:" + code);
 System.out.println("テストname:" + name);
 System.out.println("テストadminFlag:" + adminFlag);


        //指定された検索項目に一致するデータを取得
        List<EmployeeView> employeesSearch = service.getSearchFINAL(code, name, adminFlag);

        putRequestScope(AttributeConst.EMPLOYEES_SEARCH, employeesSearch);

        //この検索メソッドから一覧画面が表示された場合は、通常の一覧表示とJSPの表示を分ける
        String callMethod = "search";
        putRequestScope(AttributeConst.CALL_METHOD, callMethod);

        //一覧画面を表示
        forward(ForwardConst.FW_EMP_INDEX);
    }



}
