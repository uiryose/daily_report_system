package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * コメントデータのDTOモデル
 *
 */

@Table(name = JpaConst.TABLE_COM)

@NamedQueries({
    @NamedQuery(     //指定したレポートのidでコメント情報を取得する
            name = JpaConst.Q_COM_GET_ALL_MINE,
            query = JpaConst.Q_COM_GET_ALL_MINE_DEF)
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.COM_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 日報を登録した従業員
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.COM_COL_REP, nullable = false)
    private Report report;

    /**
     * 日報を登録した従業員
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.COM_COL_EMP, nullable = false)
    private Employee employee;


    /**
     * 日報の内容
     */
    @Lob
    @Column(name = JpaConst.COM_COL_CONTENT, nullable = false)
    private String content;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.COM_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.COM_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 編集されたコメントかどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.COM_COL_EDIT_FLAG, nullable = false)
    private Integer editFlag;

    /**
     * 削除されたコメントかどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.COM_COL_DELETE_FLAG, nullable = false)
    private Integer deleteFlag;


}
