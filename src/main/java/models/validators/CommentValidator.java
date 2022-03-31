package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.CommentView;
import constants.MessageConst;

public class CommentValidator {



    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(CommentView cv){

        List<String> errors = new ArrayList<String>();
        String contentError = validateContent(cv.getContent());

        //エラーメッセージが返されていたら、リストにエラー文を追加する
        if (!contentError.equals("")) {
            errors.add(contentError);

            System.out.println("バリデーションでエラーがあります");
        }

        //エラーの有無に関わらず、エラー文のリストを返す
        return errors;
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
