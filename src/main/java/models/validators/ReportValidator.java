package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.ReportView;
import constants.MessageConst;

public class ReportValidator {


    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(ReportView rv){

        List<String> errors = new ArrayList<String>();

        //タイトルのチェック validateTitle(String)は下で別途定義
        String titleError = validateTitle(rv.getTitle());

        //エラーメッセージが返されていたら、リストにエラー文を追加する
        if (!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(rv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        //エラーの有無に関わらず、エラー文のリストを返す
        return errors;
    }

    /**
     * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージ("タイトルを入力してください。")を返却
     * @param title タイトル
     * @return エラーメッセージ
     */

    //上のReportValidatorクラスでしか使わないからprivate？
    private static String validateTitle(String title) {
        if (title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }
        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param content 内容
     * @return エラーメッセージ
     */
    private static String validateContent(String content) {
        if(content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
        }
        //入力値がある場合は空文字を返却
        return "";
    }
}
