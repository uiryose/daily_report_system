package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.CommentView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import services.CommentService;
import services.ReportService;

public class CommentAction extends ActionBase {

    private CommentService service;
    private ReportService reportService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new CommentService();
        reportService = new ReportService();

        //メソッドの追加
        invoke();
        service.close();
        reportService.close();
    }

    /**
     * コメントを追加する
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //idを条件に日報データをDBから取得する
        ReportView rv = reportService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //パラメータの値を元にコメント情報のインスタンスを作成する
        CommentView cv = new CommentView(
                null,
                rv,
                ev,
                getRequestParam(AttributeConst.COM_CONTENT),
                null,
                null,
                0,
                0);

        //コメント情報登録。上記cvを元に、CommentServiceクラスのcreate()メソッドでDB登録
        List<String> errors = service.create(cv);

        if(errors.size() > 0) {
            //内容未入力でエラーが有った場合
            putRequestScope(AttributeConst.ERR, errors);
        }
        //元の詳細画面にリダイレクト
        redirect(ForwardConst.ACT_REP, ForwardConst.CMD_SHOW);

    }

    /**
     * コメント編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException{

        //リクエストパラメータのコメントidを元に、DBからデータを取得する
        CommentView cv = service.findOne(toNumber(getRequestParam(AttributeConst.COM_ID)));
        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = getSessionScope(AttributeConst.LOGIN_EMP);

        if(cv == null || ev.getId() != cv.getEmployee().getId()) {
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            //CSRF対策用トークン
            putRequestScope(AttributeConst.TOKEN, getTokenId());
            //DBから取得したコメントデータ
            putRequestScope(AttributeConst.COMMENT, cv);

            //編集画面を表示
            forward(ForwardConst.FW_COM_EDIT);
        }
    }


    /**
     * コメントを編集する
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException{

        //CSRF対策 tokenのチェック
        if(checkToken()) {

            //コメントのidを条件に、DBからコメントデータを取得
            CommentView cv = service.findOne(toNumber(getRequestParam(AttributeConst.COM_ID)));

            //編集画面に入力されたコメント内容をcvに上書きで設定する
            cv.setContent(getRequestParam(AttributeConst.COM_CONTENT));
            cv.setEditFlag(1);

            //DBのコメントデータを更新する
            List<String> errors = service.update(cv);

            if(errors.size() > 0) {
                //更新中にエラーが有った場合(コメントが未入力だった場合)
                putRequestScope(AttributeConst.TOKEN, getTokenId());
                putRequestScope(AttributeConst.COMMENT, cv);
                putRequestScope(AttributeConst.ERR, errors);

                forward(ForwardConst.FW_COM_EDIT);

            } else {

                //コメントした日報詳細ページにリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_SHOW, toNumber(getRequestParam(AttributeConst.COM_REP_ID)) );
            }
        }
    }


    /**
     * コメントの論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException{

      //CSRF対策 tokenのチェック
        if(checkToken()) {

            //コメントのidを条件に論理削除を行う
            service.destroy(toNumber(getRequestParam(AttributeConst.COM_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
//            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //コメントした日報詳細ページにリダイレクト
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_SHOW, toNumber(getRequestParam(AttributeConst.COM_REP_ID)) );

        }


    }


}
