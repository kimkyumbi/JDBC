package hello.jdbcpractice.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbcpractice.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    // Connection 생성 로직
    public static Connection getConnection() {
        try {
            /**
             * DriverManager의 getConnection 메서드에
             * ConnectionConst에서 H2 데이터베이스의 URL, USER, PASSWORD 정보를 넣어서 직접 커넥션 생성
             */
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            log.info("get connectin={} class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
