package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェイスに定義した変数は public static final 修飾子がついているとみなされる
 */
public interface JpaConst {

    //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "daily_report_system";

    //データ取得件数の最大値
    int ROW_PER_PAGE = 15; //1ページに表示するレコードの数

    //従業員テーブル
    String TABLE_EMP = "employees"; //テーブル名
    //従業員テーブルカラム
    String EMP_COL_ID = "id"; //id
    String EMP_COL_CODE = "code"; //社員番号
    String EMP_COL_NAME = "name"; //氏名
    String EMP_COL_PASS = "password"; //パスワード
    String EMP_COL_ADMIN_FLAG = "admin_flag"; //管理者権限
    String EMP_COL_CREATED_AT = "created_at"; //登録日時
    String EMP_COL_UPDATED_AT = "updated_at"; //更新日時
    String EMP_COL_DELETE_FLAG = "delete_flag"; //削除フラグ
    String EMP_COL_POSITION_FLAG = "position_flag";//役職
    String EMP_COL_PROFILE_URL = "profile_url";//画像URL

    int ROLE_ADMIN = 1; //管理者権限ON(管理者)
    int ROLE_GENERAL = 0; //管理者権限OFF(一般)
    int EMP_DEL_TRUE = 1; //削除フラグON(削除済み)
    int EMP_DEL_FALSE = 0; //削除フラグOFF(現役)

    int POSITION_TOP = 2; //役員
    int POSITION_MID = 1; //部長
    int POSITION_LOW = 0; //社員


    //日報テーブル
    String TABLE_REP = "reports"; //テーブル名
    //日報テーブルカラム
    String REP_COL_ID = "id"; //id
    String REP_COL_EMP = "employee_id"; //日報を作成した従業員のid
    String REP_COL_REP_DATE = "report_date"; //いつの日報かを示す日付
    String REP_COL_TITLE = "title"; //日報のタイトル
    String REP_COL_CONTENT = "content"; //日報の内容
    String REP_COL_CREATED_AT = "created_at"; //登録日時
    String REP_COL_UPDATED_AT = "updated_at"; //更新日時

    //フォローテーブル
    String TABLE_FOL = "follows"; //テーブル名
    //フォローテーブルカラム
    String FOL_COL_ID = "id"; //id
    String FOL_COL_MY_ID = "my_id"; //自分の従業員id
    String FOL_COL_FOL_ID = "follow_id"; //フォロー先の従業員のid
    String FOL_COL_CREATED_AT = "created_at"; //登録日時

    //コメントテーブル
    String TABLE_COM = "comments"; //テーブル名
    //コメントテーブルカラム
    String COM_COL_ID = "id"; //id
    String COM_COL_REP = "rep_id"; //日報を作成した従業員のid
    String COM_COL_EMP = "emp_id"; //日報を作成した従業員のid
    String COM_COL_CONTENT = "content"; //日報の内容
    String COM_COL_CREATED_AT = "created_at"; //登録日時
    String COM_COL_UPDATED_AT = "updated_at"; //更新日時
    String COM_COL_EDIT_FLAG = "edit_flag"; //編集フラグ
    String COM_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int COM_DEL_TRUE = 1; //削除フラグON(削除済み)
    int COM_DEL_FALSE = 0; //削除フラグOFF(現役)

    //Entity名
    String ENTITY_EMP = "employee"; //従業員
    String ENTITY_REP = "report"; //日報
    String ENTITY_FOL = "follow"; //フォロー
    String ENTITY_COM = "comment"; //コメント

    //JPQL内パラメータ
    String JPQL_PARM_NAME = "name"; //氏名
    String JPQL_PARM_ID = "id"; //id
    String JPQL_PARM_ID_FOL = "follow_id"; //フォロー先の従業員ID
    String JPQL_PARM_CODE = "code"; //社員番号
    String JPQL_PARM_PASSWORD = "password"; //パスワード
    String JPQL_PARM_EMPLOYEE = "employee"; //従業員
    String JPQL_PARM_ADMIN_FLAG = "admin_flag"; //管理者権限
    String JPQL_PARM_DELET_FLG = "delete_flag"; //削除フラグ
    String JPQL_PARM_FOLLOW = "follow"; //フォロー
    String JPQL_PARM_PROFILE_URL ="profile_url";

    int JPQL_DEL_TRUE = 1; //削除フラグON(削除済み)
    int JPQL_DEL_FALSE = 0; //削除フラグOFF(現役)


    //NamedQueryの nameとquery
    //全ての従業員をidの降順に取得する
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; //name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
    //全ての従業員の件数を取得する
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //指定した社員番号を保持する従業員の件数を取得する
    String Q_EMP_COUNT_RESISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    String Q_EMP_COUNT_RESISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
    //全ての日報をidの降順に取得する
    String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
    String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r ORDER BY r.id DESC";
    //全ての日報の件数を取得する
    String Q_REP_COUNT = ENTITY_REP + ".count";
    String Q_REP_COUNT_DEF = "SELECT COUNT(r) FROM Report AS r";
    //指定した従業員が作成した日報を全件idの降順で取得する
    String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
    String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY r.id DESC";
    //指定した従業員が作成した日報の件数を取得する
    String Q_REP_COUNT_ALL_MINE = ENTITY_REP + ".countAllMine";
    String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE;

    //論理削除を除く全ての従業員
    String Q_EMP_GET_ALL_REM_DEL = ENTITY_EMP + ".getAllRemDel"; //name
    String Q_EMP_GET_ALL_REM_DEL_DEF = "SELECT e FROM Employee AS e WHERE " + JPQL_PARM_DELET_FLG + " = "+EMP_DEL_FALSE+"  ORDER BY e.id DESC"; //query

    //論理削除を除く全ての従業員の件数を取得する
    String Q_EMP_COUNT_REM_DEL = ENTITY_EMP + ".countRemDel";
    String Q_EMP_COUNT_DEF_REM_DEL = "SELECT COUNT(e) FROM Employee AS e WHERE " +  JPQL_PARM_DELET_FLG + "="+ EMP_DEL_FALSE ;

    //自分のidとフォロー先のidが一致するレコードを取得する
    String Q_FOL_GET_ONE = ENTITY_FOL + ".getOne";
    String Q_FOL_GET_ONE_DEF = "SELECT f FROM Follow AS f WHERE f.myEmployee.id = :" +  JPQL_PARM_ID +" AND f.followEmployee.id = :" + JPQL_PARM_ID_FOL;

    //指定した従業員のフォロー先を全件idの降順で取得する
    String Q_FOL_GET_ALL_MINE = ENTITY_FOL + ".getAllMine";
    String Q_FOL_GET_ALL_MINE_DEF = "SELECT f FROM Follow AS f WHERE f.myEmployee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY f.id DESC";

    //指定したレポートのidでコメント情報を取得する
    String Q_COM_GET_ALL_MINE = ENTITY_COM + "getAllMine";
    String Q_COM_GET_ALL_MINE_DEF = "SELECT c FROM Comment AS c WHERE c.report.id = :" + JPQL_PARM_ID;



    //従業員検索（３項目入力した場合）
    String Q_EMP_GET_SEARCH = ENTITY_EMP + ".getSearch";
    String Q_EMP_GET_SEARCH_DEF = "SELECT e FROM Employee AS e WHERE e.code LIKE :" + JPQL_PARM_CODE + " AND e.name LIKE :" + JPQL_PARM_NAME + " AND e.adminFlag = :" + JPQL_PARM_ADMIN_FLAG;

    //従業員検索（codeとnameを入力した場合）
    String Q_EMP_GET_SEARCH_2 = ENTITY_EMP + ".getSearch2";
    String Q_EMP_GET_SEARCH_DEF_2 = "SELECT e FROM Employee AS e WHERE e.code LIKE :" + JPQL_PARM_CODE + " AND e.name LIKE :" + JPQL_PARM_NAME ;

    //従業員検索（codeとadminFlagを入力した場合）
    String Q_EMP_GET_SEARCH_3 = ENTITY_EMP + ".getSearch3";
    String Q_EMP_GET_SEARCH_DEF_3 = "SELECT e FROM Employee AS e WHERE e.code LIKE :" + JPQL_PARM_CODE + " AND e.adminFlag = :" + JPQL_PARM_ADMIN_FLAG ;

    //従業員検索（nameとadminFlagを入力した場合）
    String Q_EMP_GET_SEARCH_4 = ENTITY_EMP + ".getSearch4";
    String Q_EMP_GET_SEARCH_DEF_4 = "SELECT e FROM Employee AS e WHERE e.name LIKE :" + JPQL_PARM_NAME + " AND e.adminFlag = :" + JPQL_PARM_ADMIN_FLAG ;


    String Q_EMP_GET_SEARCH_FINAL = ENTITY_EMP + ".getSearchFINAL";
    String Q_EMP_GET_SEARCH_FINAL_DEF = "SELECT e FROM Employee AS e WHERE e.code LIKE CASE WHEN :" + JPQL_PARM_CODE + " = '' THEN e.code  ELSE  :" + JPQL_PARM_CODE + " END" +
                                            " AND e.name LIKE CASE WHEN :" + JPQL_PARM_NAME + " = '' THEN e.name  ELSE  :" + JPQL_PARM_NAME + " END"+
                                            " AND e.adminFlag = CASE WHEN :" + JPQL_PARM_ADMIN_FLAG + " = 9 THEN e.adminFlag  ELSE  :" + JPQL_PARM_ADMIN_FLAG + " END";

}
