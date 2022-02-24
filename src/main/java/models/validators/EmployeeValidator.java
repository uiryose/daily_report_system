package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import constants.MessageConst;
import services.EmployeeService;

public class EmployeeValidator {

    /**
     * 従業員インスタンスの各項目についてバリデーションを行う
     * @param service 呼び出し元Serviceクラスのインスタンス
     * @param ev EmployeeServiceのインスタンス
     * @param codeDuplicateCheckFlag 社員番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @param passwordCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーのリスト
     */

    public static List<String> validate(
            EmployeeService service, EmployeeView ev, Boolean codeDuplicateCheckFlag, boolean passwordCheckFlag) {

        List<String> errors = new ArrayList<String>();

        //社員番号をチェック
        //validateCodeは下で別途設定
        String codeError = validateCode(service, ev.getCode(), codeDuplicateCheckFlag);
        if (!codeError.equals("")) {
            errors.add(codeError);
        }

        //氏名のチェック
        String nameError = validateName(ev.getName());
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        //パスワードのチェック
        String passError = validatePassword(ev.getPassword(), passwordCheckFlag);
        if (!passError.equals("")) {
            errors.add(passError);
        }

        return errors;

        //従業員登録メソッドEmployeeService.create()の中では、validate(this, ev, true, true）の条件でバリデーションを行う
        //従業員codeの重複チェックはvalidateCode(this, ev.getCode(), true)で行うことになる
        //if(Flag=true)の条件式で、重複チェックを行っている。(DBから番号が重複する件数を返し、0以上かで判断)
        //引数がfalseなら、重複チェックはせず、入力があるかだけのエラー文を返す

        //従業員更新メソッドEmployeeService.update()の中では、validate(this, savedEmp(DB情報), validateCode, validatePass)の条件でバリデーションを行う
        //社員コード、パスワードを入力したかどうかで、バリデーションのON/OFFを切り替える
        //従業員codeの重複チェックはvalidateCode(this, ev.getCode(), true)で行うことになる
        //if(Flag=true)の条件式で、重複チェックを行っている。(DBから番号が重複する件数を返し、0以上かで判断)
        //引数がfalseなら、重複チェックはせず、入力があるかだけのエラー文を返す

    }

    /**
     * 社員番号の入力チェックを行い、エラーメッセージを返却
     * @param service インスタンス
     * @param code 社員番号
     * @param codeDuplicateCheckFlag 重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージを返す
     */
    private static String validateCode(EmployeeService service, String code, Boolean codeDuplicateCheckFlag) {

        //codeに入力値がなければ、社員番号を入力を促すエラーメッセージを返す
        if (code == null || code.equals("")) {
            //"社員番号を入力してください。"という文字列を取得
            return MessageConst.E_NOEMP_CODE.getMessage();
        }

        if (codeDuplicateCheckFlag) {
            //社員番号の重複チェックを実施
            //isDuplicateEmployee()は下で別途定義
            long employeesCount = isDuplicateEmployee(service, code);

            //同一社員番号が既に登録されている場合は、エラメッセージを返す
            if (employeesCount > 0) {
                //"入力された社員番号の情報は既に存在しています。"という文字列を取得
                return MessageConst.E_EMP_CODE_EXIST.getMessage();
            }

        }
        //全くエラーがなければ、空文字を返却。
        return "";

    }

    /**
     * 社員番号の重複チェックに使用するメソッド
     * @param service EmployeeServiceのインスタンス
     * @param code 社員番号
     * @return 従業員テーブルに登録されている同一社員番号のデータ件数
     */
    private static long isDuplicateEmployee(EmployeeService service, String code) {
        //countByCodeはEmployeeServiceクラスで別途定義
        //社員番号(引数code)を条件に該当するデータの件数を取得します
        long employeesCount = service.countByCode(code);
        return employeesCount;
    }

    /**
     * 氏名に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param name 氏名
     * @return エラーメッセージ
     */
    private static String validateName(String name) {

        if (name == null || name.equals("")) {
            //"氏名を入力してください。"という文字列を取得
            return MessageConst.E_NONAME.getMessage();
        }
        return "";
    }

    /**
     * パスワードの入力チェックを行い、エラーメッセージを返却
     * @param password パスワード
     * @param passwordCheckFlag パスワードの入力チェックを実施するかどうか（実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validatePassword(String password, Boolean passwordCheckFlag) {
        //もしパスワードチェックがTrueで、パスワードが空ならエラメッセージを返す
        if (passwordCheckFlag && (password == null || password.equals(""))) {
            return MessageConst.E_NOPASSWORD.getMessage();
        }
        return "";
    }

}
