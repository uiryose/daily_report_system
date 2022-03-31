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
            putRequestScope(AttributeConst.ERR, errors);//エラーのリスト
        }
        //元の詳細画面にリダイレクト
        redirect(ForwardConst.ACT_REP, ForwardConst.CMD_SHOW);

    }

}
