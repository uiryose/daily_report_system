package filters;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String contextPath = ((HttpServletRequest) request).getContextPath();   //どういう構文・・・？
        String servletPath = ((HttpServletRequest) request).getServletPath();

        if (servletPath.matches("/css.*")) {
            // CSSフォルダ内は認証処理から除外する
            chain.doFilter(request, response);

        } else {
            HttpSession session = ((HttpServletRequest) request).getSession();

            //クエリパラメータからactionとcommandを取得
            String action = request.getParameter(ForwardConst.ACT.getValue());  //"action"に対応するクエリ
            String command = request.getParameter(ForwardConst.CMD.getValue()); //"command"に対応するクエリ

            //セッションからログインしている従業員の情報を取得
            EmployeeView ev = (EmployeeView) session.getAttribute(AttributeConst.LOGIN_EMP.getValue()); //"login_employee"のセッションスコープ
                //フィルターでEmployeeView evを作って、様々なところで競合？する可能性はないのか？

            if (ev == null) {
                //未ログイン

                    //URLが認証Authでもなくログイン画面でもない、または、？？の時・・・どういうif文？
                if (!(ForwardConst.ACT_AUTH.getValue().equals(action)               //文字列"Auth"とクエリで取得したactionの内容と一致するか
                        && (ForwardConst.CMD_SHOW_LOGIN.getValue().equals(command)  //文字列"showLogin"とクエリで取得したcommandの内容と一致するか
                                || ForwardConst.CMD_LOGIN.getValue().equals(command)))) { //文字列"login"とクエリで取得したcommandの内容と一致するか


                    //ログインページの表示またはログイン実行以外はログインページにリダイレクト？
                    ((HttpServletResponse) response).sendRedirect(
                            contextPath                                          //contextPathは上で定義しているが、ここからわからない。
                                    + "?action=" + ForwardConst.ACT_AUTH.getValue()            //action=Auth&command=showLoginのリンクにリダイレクトする
                                    + "&command=" + ForwardConst.CMD_SHOW_LOGIN.getValue());
                    /*
                    　今までのリダイレクトの構文はこんな感じ。request.getContextPath() + "/index"で何が起きている？
                    　protected void doPost(HttpServletRequest request, HttpServletResponse response) {
                    　response.sendRedirect(request.getContextPath() + "/index");  }
                    */

                    return;     //なにをreturnする？
                }
            } else {
                //ログイン済・・・実行できる認証系の機能はログアウトだけ

                if (ForwardConst.ACT_AUTH.getValue().equals(action)) {
                    //URLが認証系Actionを行おうとしている場合

                    if (ForwardConst.CMD_SHOW_LOGIN.getValue().equals(command)) {  // ログイン中にCMD_SHOW_LOGIN==>"showLogin"の場合。トップ画面にリダイレクト
                        //ログインページの表示はトップ画面にリダイレクト
                        ((HttpServletResponse) response).sendRedirect(
                                contextPath
                                        + "?action=" + ForwardConst.ACT_TOP.getValue()
                                        + "&command=" + ForwardConst.CMD_INDEX.getValue());
                        return;   //なにをreturnする？

                    } else if (ForwardConst.CMD_LOGOUT.getValue().equals(command)) {
                        //ログアウトの実施は許可

                    } else {
                        //上記以外の認証系Actionはエラー画面

                        String forward = String.format("/WEB-INF/views/%s.jsp", "error/unknown");
                        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
                        dispatcher.forward(request, response);

                        return;

                    }
                }
            }

            //次のフィルタまたはサーブレットを呼び出し
            chain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
