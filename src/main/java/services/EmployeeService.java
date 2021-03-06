package services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Employee;
import models.validators.EmployeeValidator;
import utils.EncryptUtil;

public class EmployeeService extends ServiceBase {


    /**
     * 指定されたページ数の一覧画面に表示するデータを取得し、EmployeeViewのリストで返却する
     * @param page ページ数
     * @param delFlag 論理削除を含める場合:true 含めない場合;false
     * @return 表示するデータのリスト
     */
    public List<EmployeeView> getPerPage(int page, boolean delFlag) {
                                        //ENTITY_EMP + ".getAll"; //name
        //「JpaConst.Q_EMP_GET_ALL」という名前でSQL文"SELECT e FROM Employee AS e ORDER BY e.id DESC"を定義している
        if(delFlag) {

            List<Employee> employees = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL, Employee.class)
                    .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))    //SQLでDB検索をし、従業員データからid順で、1番目から
                    .setMaxResults(JpaConst.ROW_PER_PAGE)                  //15個までを取得し、
                    .getResultList();                                       //クエリーインスタンスに結果をリストで格納する

            return EmployeeConverter.toViewList(employees);     //DB用のリストをビュー用に変換する
        }else {

            //論理削除を含めない在籍社員
            List<Employee> employees = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL_REM_DEL, Employee.class)
                    .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))    //SQLでDB検索をし、従業員データからid順で、1番目から
                    .setMaxResults(JpaConst.ROW_PER_PAGE)                  //15個までを取得し、
                    .getResultList();                                       //クエリーインスタンスに結果をリストで格納する

            return EmployeeConverter.toViewList(employees);     //DB用のリストをビュー用に変換する
        }
    }


    /**
     * 従業員テーブルのデータの件数を取得し、返却する
     * @deleteFlag 論理削除を含める場合はtrue 含めない場合はfalse
     * @return 従業員テーブルのデータの件数
     */
    public long countAll(boolean deleteFlag) {

        if(deleteFlag) {

            //「JpaConst.Q_EMP_COUNT」という名前でSQL文"SELECT COUNT(e) FROM Employee AS e"を定義している
            long empCount = (long) em.createNamedQuery(JpaConst.Q_EMP_COUNT, Long.class).getSingleResult();
            return empCount;

        } else {

            //論理削除を除いた件数
            long empCount = (long) em.createNamedQuery(JpaConst.Q_EMP_COUNT_REM_DEL, Long.class).getSingleResult();
            return empCount;
        }
    }

    /**
     * 社員番号、パスワードを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param code 社員番号
     * @param plainPass パスワード文字列
     * @param pepper pepper文字列
     * @return 取得データのインスタンス 取得できない場合null
     */
    public EmployeeView findOne(String code, String plainPass, String pepper) {
        Employee e = null;

        try {
            //パスワードのハッシュ化
            String pass = EncryptUtil.getPasswordEncrypt(plainPass,pepper);

            //社員番号とハッシュ化済パスワードを条件に未削除の従業員を1件取得する
            //「JpaConst.Q_EMP_GET_BY_CODE_AND_PASS」という名前でSQL文"SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD"を定義している
            e = em.createNamedQuery(JpaConst.Q_EMP_GET_BY_CODE_AND_PASS, Employee.class)
                                        //"code"という文字列
                    .setParameter(JpaConst.JPQL_PARM_CODE, code)
                                        //"password"という文字列
                    .setParameter(JpaConst.JPQL_PARM_PASSWORD, pass)
                    .getSingleResult();

        } catch (NoResultException ex) {

        }
        return EmployeeConverter.toView(e);

    }


    /**
     * idを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public EmployeeView findOne(int id) {
        Employee e = findOneInternal(id);
        return EmployeeConverter.toView(e);
    }

    /**
     * 社員番号を条件に該当するデータの件数を取得し、返却する
     * @param code 社員番号
     * @return 該当するデータの件数
     */
    public long countByCode(String code) {
        //SQL文 "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODEを定義
        long employees_count = em.createNamedQuery(JpaConst.Q_EMP_COUNT_RESISTERED_BY_CODE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_CODE, code)
                .getSingleResult();
        return employees_count;
    }


    /**
     * 画面から入力された従業員の登録内容を元にデータを1件作成し、従業員テーブルに登録する
     * @param ev 画面から入力された従業員の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */

    public List<String> create(EmployeeView ev, String pepper){

        //パスワードをハッシュ化して設定
        String pass = EncryptUtil.getPasswordEncrypt(ev.getPassword(),pepper);
        ev.setPassword(pass);

        //登録日時、更新日時は現在時刻を設定する
        LocalDateTime now = LocalDateTime.now();
        ev.setCreatedAt(now);
        ev.setUpdatedAt(now);

        //登録内容のバリデーションを行う
        List<String> errors = EmployeeValidator.validate(this, ev, true, true);

        //バリデーションエラーがなければデータを登録する
        if(errors.size() == 0) {
            create(ev);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;

    }

    /**
     * 画面から入力された従業員の更新内容を元にデータを1件作成し、従業員テーブルを更新する
     * @param ev 画面から入力された従業員の登録内容
     * @param pepper pepper文字列
     * @return バリデーションや更新処理中に発生したエラーのリスト
     * @throws ServletException
     * @throws IOException
     */


    public List<String> update(EmployeeView ev, String pepper) throws IOException, ServletException{

        //idを条件に登録済みの従業員情報を取得する。ビュー画面の従業員情報からDBのID検索で情報を格納
        EmployeeView savedEmp = findOne(ev.getId());

        boolean validateCode = false;
        if(!savedEmp.getCode().equals(ev.getCode())) {
            //社員番号を更新する場合。DBと社員番号が異なったら、validateCodeのON/OFFを切り替える

            //社員番号についてのバリデーションを行う
            validateCode = true;
            //変更後の社員番号を設定する
            savedEmp.setCode(ev.getCode());
        }

        boolean validatePass = false;
          //ビューのパスが入力されている場合
        if(ev.getPassword() !=null && !ev.getPassword().equals("")) {
            validatePass = true;
            //変更後のパスワードをハッシュ化し設定する
            savedEmp.setPassword(EncryptUtil.getPasswordEncrypt(ev.getPassword(),pepper));
        }

        savedEmp.setName(ev.getName());

        savedEmp.setAdminFlag(ev.getAdminFlag());  //変更後の管理者フラグを設定する

        savedEmp.setPositionFlag(ev.getPositionFlag());  //変更後の役職フラグを設定する

        //更新日時に現在時刻を設定する
        LocalDateTime today = LocalDateTime.now();
        savedEmp.setUpdatedAt(today);

        if(ev.getProfileUrl() != null) {
            savedEmp.setProfileUrl(ev.getProfileUrl());  //画像を設定する
        }

        //更新内容についてバリデーションを行う
        List<String> errors = EmployeeValidator.validate(this, savedEmp, validateCode, validatePass);

        if(errors.size() ==0) {
            update(savedEmp);
        }

        return errors;
    }


    /**
     * idを条件に従業員データを論理削除する
     * @param id
     */
    public void destroy(Integer id) {

        //idを条件に登録済みの従業員情報を取得する
        EmployeeView savedEmp = findOne(id);

        //更新日時に現在時刻を設定する
        LocalDateTime today = LocalDateTime.now();
        savedEmp.setUpdatedAt(today);

        //論理削除フラグをたてる。[1]を設定
        savedEmp.setDeleteFlag(JpaConst.EMP_DEL_TRUE);

        //更新処理を行う
        update(savedEmp);
    }


    /**
     * 社員番号とパスワードを条件に検索し、データが取得できるかどうかで認証結果を返却する
     * @param code 社員番号
     * @param plainPass パスワード
     * @param pepper pepper文字列
     * @return 認証結果を返却する(成功:true 失敗:false)
     */

    public Boolean validateLogin(String code, String plainPass, String pepper) {

        boolean isValidEmployee = false;

        //それぞれきちんと入力されていれば、社員番号とパスを条件にデータを取得し、evに格納する
        if (code != null && !code.equals("") && plainPass != null && !plainPass.equals("")) {
            EmployeeView ev = findOne(code, plainPass, pepper);

            if (ev != null && ev.getId() != null) {

                //データが取得できた場合、認証成功
                isValidEmployee = true;
            }
        }
        return isValidEmployee;
    }

    /**
     * 検索フォームに入力された条件で検索結果を返す。CASE文を使用
     * @param code
     * @param name
     * @param adminFlag
     * @return
     */
    public List<EmployeeView> getSearchFINAL(String code, String name, int adminFlag) {

        //検索項目が未入力の場合、代入する値も空にしておく
        String serchConditionCode = "";
        if(code != null && !code.equals("")) {
          serchConditionCode = "%" + code + "%";
        }
        String serchConditionName = "";
        if(name != null && !name.equals("")) {
          serchConditionName = "%" + name + "%";
        }

        List<Employee> employees = em.createNamedQuery(JpaConst.Q_EMP_GET_SEARCH_FINAL, Employee.class)
                .setParameter(JpaConst.JPQL_PARM_CODE, serchConditionCode)
                .setParameter(JpaConst.JPQL_PARM_NAME, serchConditionName)
                .setParameter(JpaConst.JPQL_PARM_ADMIN_FLAG, adminFlag)
                .getResultList();

        return EmployeeConverter.toViewList(employees);
    }



    /**
     * 検索フォームに入力された条件で検索結果を返す。検索フォームに入力された条件によってJPQL文を判断(条件8個中4個までのお試し実装)
     * @param code
     * @param name
     * @param adminFlag
     * @return
     */
    public List<EmployeeView> getSearchAAA(String code, String name, int adminFlag) {

        List<Employee> employees = new ArrayList<Employee>();

        if(code == null && name != null && adminFlag != Integer.MIN_VALUE) { //codeとnameとadminFlagを入力した場合

            employees = em.createNamedQuery(JpaConst.Q_EMP_GET_SEARCH, Employee.class)
                    .setParameter(JpaConst.JPQL_PARM_CODE, "%" + code + "%")
                    .setParameter(JpaConst.JPQL_PARM_NAME, "%" + name + "%")
                    .setParameter(JpaConst.JPQL_PARM_ADMIN_FLAG, adminFlag)
                .getResultList();
            return EmployeeConverter.toViewList(employees);

        } else if(code != null && name != null && adminFlag == Integer.MIN_VALUE) { //codeとnameを入力した場合2

            employees = em.createNamedQuery(JpaConst.Q_EMP_GET_SEARCH_2, Employee.class)
                    .setParameter(JpaConst.JPQL_PARM_CODE, "%" + code + "%")
                    .setParameter(JpaConst.JPQL_PARM_NAME, "%" + name + "%")
                    .getResultList();
            return EmployeeConverter.toViewList(employees);

        } else if (code != null && name == null && adminFlag != Integer.MIN_VALUE) { //codeとadminFlagを入力した場合3

            employees = em.createNamedQuery(JpaConst.Q_EMP_GET_SEARCH_3, Employee.class)
                    .setParameter(JpaConst.JPQL_PARM_CODE, "%" + code + "%")
                    .setParameter(JpaConst.JPQL_PARM_ADMIN_FLAG, adminFlag)
                    .getResultList();
            return EmployeeConverter.toViewList(employees);

        } else if (code == null && name != null && adminFlag != Integer.MIN_VALUE) {  //nameとadminFlagを入力した場合4

            employees = em.createNamedQuery(JpaConst.Q_EMP_GET_SEARCH_4, Employee.class)
                    .setParameter(JpaConst.JPQL_PARM_NAME, "%" + name + "%")
                    .setParameter(JpaConst.JPQL_PARM_ADMIN_FLAG, adminFlag)
                    .getResultList();
            return EmployeeConverter.toViewList(employees);

        }

        return EmployeeConverter.toViewList(employees);

    }

    /**
     * idを条件にデータを1件取得し、Employeeのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Employee findOneInternal(int id) {
    //テーブル内を検索するfind(DTOクラス名.class、第2引数にテーブル検索時のID値)メソッド
        Employee e = em.find(Employee.class, id);

        return e;
    }


    /**
     * 従業員データを1件登録する
     * @param ev 従業員データ
     * @return 登録結果(成功:true 失敗:false)
     */
    private void create(EmployeeView ev) {
        em.getTransaction().begin();
        em.persist(EmployeeConverter.toModel(ev));
        em.getTransaction().commit();
    }


    /**
     * 従業員データを更新する
     * @param ev 画面から入力された従業員の登録内容
     */
    private void update(EmployeeView ev) {
        em.getTransaction().begin();
        Employee e = findOneInternal(ev.getId());
        EmployeeConverter.copyViewToModel(e, ev);
        em.getTransaction().commit();
    }
}
