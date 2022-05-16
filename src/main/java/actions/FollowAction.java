package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import models.Follow;
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
     * 従業員をフォローする
     * @throws ServletException
     * @throws IOException
     */
    public void follow() throws ServletException,IOException{

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        int myId = ev.getId();

        //パラメータからフォローする従業員情報を取得する①
        int followId = toNumber(request.getParameter(AttributeConst.EMP_ID.getValue()));

        //①のID=フォローするIDから、フォロー先の従業員インスタンス作成
        EmployeeView evFollow = employeeService.findOne(followId);

        //新規に登録するフォローインスタンスを作成する
        FollowView fv = new FollowView(
                null,
                ev,
                evFollow,
                null);

        //DBからそれぞれの従業員IDが一致するレコードを取得する
        FollowView fvc = FollowConverter.toView(service.getOne(myId, followId));

        if(fvc==null) {

            //新規のフォロー情報をDBに登録する
            service.create(fv);
        }else {
            System.out.println("テスト：一致するデータがあります");

        }

        //在籍社員一覧を表示してフォロー状態を反映させる
        redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_ALL);

    }

    /**
     * フォローを外す
     * @throws ServletException
     * @throws IOException
     */
    public void remove() throws ServletException,IOException{

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        int myId = ev.getId();

        //パラメータからフォローを外す従業員のidを取得する
        int followId = toNumber(request.getParameter(AttributeConst.EMP_ID.getValue()));

        Follow f = service.getOne(myId, followId);

        if(f != null) {
            //DBからフォロー情報を１行削除する
            service.remove(f);
        }

        //お気に入り画面からフォローを外した場合は、お気に入り画面に戻る
        String forward = request.getParameter("display");

        if(forward != null && forward.equals("fav")) {
            redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_FAVORITE);

        } else {
            redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_ALL);
        }
    }

}
