package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import services.EmployeeService;
import services.FollowService;

/**
 * フォローに関する処理を行うActionクラス
 *
 */
public class FollowAction extends ActionBase {

    FollowService service;
    EmployeeService employeeService;

    @Override
    public void process() throws ServletException, IOException {

        service = new FollowService();

        employeeService = new EmployeeService();

        invoke();
        service.close();

        employeeService.close();

    }



    /**
     * フォロー一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
//    public void index() throws ServletException,IOException{
//
//        List<FollowView> follows = service.getAll();
//
//        //リクエストスコープに情報を保存する
//        putRequestScope(AttributeConst.FOLLOWS, follows);
//
//        //フォロー一覧に移動する
//        forward(ForwardConst.FW_FOL_INDEX);
//    }
//


    public void follow() throws ServletException,IOException{

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //パラメータからフォローする従業員情報を取得する①
        int followId = toNumber(request.getParameter(AttributeConst.EMP_ID.getValue()));

        //①のID=フォローするIDから、フォロー先の従業員インスタンス作成
        EmployeeView evFollow = employeeService.findOne(followId);

        //
        FollowView fv = new FollowView(
                null,
                ev,
                evFollow,
                null);

        //FollowインスタンスfをDBに登録する
        service.create(fv);

        //在籍社員一覧を表示してフォロー状態を反映させる
        redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_ALL);

    }





//    public void remove() throws ServletException,IOException{
//
//        // セッションスコープからEmployeeViewのIDを取得して,該当のIDのメッセージ1件のみをデータベースから取得
//        Follow m = em.find(Follow.class, (Integer) getSessionScope(AttributeConst.LOGIN_EMP));
//
//
//
//      //セッションからログイン中の従業員情報を取得
//        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
//        int myId = ev.getId();
//
//        //パラメータからフォローする従業員のIDを取得する
//        int followId = toNumber(getRequestParam(AttributeConst.EMP_ID));
//
////        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
//
//        //2つのIDを元にフォローインスタンスを取得する
//        FollowView fv = new FollowView(
//                null,
//
//                );
//
//        //DBからフォロー情報を１行削除する
//        service.remove(fv);
//
//        //在籍社員一覧を表示してフォロー状態を反映させる
//        forward(ForwardConst.FW_EMP_ALL);
//    }
//


}
