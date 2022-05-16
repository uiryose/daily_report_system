package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 従業員データのDTOモデル
 * DTOモデルはテーブルのデータ構造に沿って作成します
 */

@Table(name = JpaConst.TABLE_EMP)
@NamedQueries({
        @NamedQuery(
                name = JpaConst.Q_EMP_GET_ALL, //==> ENTITY_EMP + ".getAll" ==> "employee" + ".getAll"
                query = JpaConst.Q_EMP_GET_ALL_DEF), //==> "SELECT e FROM Employee AS e ORDER BY e.id DESC"

        @NamedQuery(
                name = JpaConst.Q_EMP_COUNT, //==> ENTITY_EMP + ".count" ==> "employee" + ".count"
                query = JpaConst.Q_EMP_COUNT_DEF), //==> "SELECT COUNT(e) FROM Employee AS e"

        @NamedQuery(
                //指定した社員番号を保持する従業員の件数を取得する.指定された社員番号がすでにデータベースに存在しているかを調べる。
                name = JpaConst.Q_EMP_COUNT_RESISTERED_BY_CODE, //==>ENTITY_EMP + ".countRegisteredByCode" ==>employee +".countRegisteredByCode"
                query = JpaConst.Q_EMP_COUNT_RESISTERED_BY_CODE_DEF), //==>"SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;

        @NamedQuery(
                //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する.
                //従業員がログインするときに社員番号とパスワードが正しいかをチェックするためのものです。
                name = JpaConst.Q_EMP_GET_BY_CODE_AND_PASS, //==> ENTITY_EMP + ".getByCodeAndPass" ==> employee + ".getByCodeAndPass"
                query = JpaConst.Q_EMP_GET_BY_CODE_AND_PASS_DEF),//==>  "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE

        @NamedQuery(//論理削除を除く全ての従業員
                name = JpaConst.Q_EMP_GET_ALL_REM_DEL,
                query = JpaConst.Q_EMP_GET_ALL_REM_DEL_DEF),

        @NamedQuery(//論理削除を除く全ての従業員の件数を取得する
                name = JpaConst.Q_EMP_COUNT_REM_DEL,
                query = JpaConst.Q_EMP_COUNT_DEF_REM_DEL),

        //検索フォーム用。ただし、１～４はお試し実装。
        @NamedQuery( //検索結果を取得する
                name = JpaConst.Q_EMP_GET_SEARCH,
                query = JpaConst.Q_EMP_GET_SEARCH_DEF),
        @NamedQuery( //検索結果を取得する
                name = JpaConst.Q_EMP_GET_SEARCH_2,
                query = JpaConst.Q_EMP_GET_SEARCH_DEF_2),
        @NamedQuery( //検索結果を取得する
                name = JpaConst.Q_EMP_GET_SEARCH_3,
                query = JpaConst.Q_EMP_GET_SEARCH_DEF_3),
        @NamedQuery( //検索結果を取得する
                name = JpaConst.Q_EMP_GET_SEARCH_4,
                query = JpaConst.Q_EMP_GET_SEARCH_DEF_4),
        @NamedQuery( //検索結果を取得する。CASE文を使用
                name = JpaConst.Q_EMP_GET_SEARCH_FINAL,
                query = JpaConst.Q_EMP_GET_SEARCH_FINAL_DEF)

})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Employee {

    @Id
    @Column(name = JpaConst.EMP_COL_ID) //"id"
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自動発番
    private Integer id;

    /**
     * 社員番号
     */
    @Column(name = JpaConst.EMP_COL_CODE, nullable = false, unique = true) //空白・重複不可
    private String code;

    /**
     * 氏名
     */
    @Column(name = JpaConst.EMP_COL_NAME, nullable = false)
    private String name;

    /**
     * パスワード
     */
    @Column(name = JpaConst.EMP_COL_PASS, length = 64, nullable = false) //長さ指定
    private String password;

    /**
     * 管理者権限があるかどうか（一般：0、管理者：1）
     */
    @Column(name = JpaConst.EMP_COL_ADMIN_FLAG, nullable = false)
    private Integer adminFlag;

    /**
     *登録日時
     */
    @Column(name = JpaConst.EMP_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.EMP_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 削除された従業員かどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.EMP_COL_DELETE_FLAG, nullable = false)
    private Integer deleteFlag;


    /**
     * 従業員の役職（社員：0、部長：1、役員；2）
     */
    @Column(name = JpaConst.EMP_COL_POSITION_FLAG, nullable = true)
    private Integer positionFlag;

    /**
     * 従業員の画像のURL
     */
    @Column(name = JpaConst.EMP_COL_PROFILE_URL, nullable = true)
    private String profileUrl;


}
