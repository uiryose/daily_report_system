package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * フォローデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_FOL)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_ONE,
            query = JpaConst.Q_FOL_GET_ONE_DEF)
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Follow {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.FOL_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * フォロー元の従業員id
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FOL_COL_MY_ID, nullable = true)
    private Employee myEmployee;

    /**
     * フォロー先の従業員id
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FOL_COL_FOL_ID, nullable = true)
    private Employee followEmployee;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.FOL_COL_CREATED_AT, nullable = true)
    private LocalDateTime createdAt;



}