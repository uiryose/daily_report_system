package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * コメント情報について画面の入力値・出力値を扱うViewモデル
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CommentView {

    /**
     * id
     */
    private Integer id;

    /**
     * 日報を登録した従業員
     */
    private ReportView report;

    /**
     * 日報を登録した従業員
     */
    private EmployeeView employee;


    /**
     * 日報の内容
     */
    private String content;

    /**
     * 登録日時
     */
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    private LocalDateTime updatedAt;

    /**
     * 編集されたコメントかどうか（現役：0、削除済み：1）
     */
    private Integer editFlag;

    /**
     * 削除されたコメントかどうか（現役：0、削除済み：1）
     */
    private Integer deleteFlag;


}
