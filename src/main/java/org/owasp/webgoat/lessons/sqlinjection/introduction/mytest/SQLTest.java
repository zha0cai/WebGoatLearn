package org.owasp.webgoat.lessons.sqlinjection.introduction.mytest;

import jakarta.annotation.PostConstruct;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@AssignmentHints(
        value = {
                "SqlStringInjectionHint5-1",
                "SqlStringInjectionHint5-2",
                "SqlStringInjectionHint5-3",
                "SqlStringInjectionHint5-4"
        })

public class SQLTest extends AssignmentEndpoint {
    private final LessonDataSource dataSource;

    public SQLTest(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void createUser() {
        // HSQLDB does not support CREATE USER with IF NOT EXISTS so we need to do it in code (using
        // DROP first will throw error if user does not exists)
        try (Connection connection = dataSource.getConnection()) {
            try (var statement =
                         connection.prepareStatement("CREATE USER unauthorized_user PASSWORD test")) {
                statement.execute();
            }
        } catch (Exception e) {
            // user already exists continue
        }
    }

    // GRANT_RIGHTS 存在 UNAUTHORIZED_USER 的授权会返回 true
    private boolean checkSolution(Connection connection) {
        try {
            var stmt =
                    connection.prepareStatement(
                            "SELECT * FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES WHERE TABLE_NAME = ? AND GRANTEE ="
                                    + " ?");
            stmt.setString(1, "GRANT_RIGHTS");
            stmt.setString(2, "UNAUTHORIZED_USER");
            var resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            return false;
        }
    }

    @PostMapping("/SqlInjection/MyAttack")
    @ResponseBody
    public AttackResult completed() throws SQLException {
        // 授权
        //String query = "GRANT SELECT ON grant_rights TO unauthorized_user";
        // 取消授权
        String query = "REVOKE SELECT ON grant_rights FROM unauthorized_user RESTRICT";
        createUser();
        return injectableQuery(query);
    }

    protected AttackResult injectableQuery(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        statement.executeQuery(query);
        //statement.executeUpdate(query);

        if (checkSolution(connection)) { // 存在授权则返回 true
            return success(this).build();
        }
        return failed(this).output("Your query was: " + query).build();
    }
}
