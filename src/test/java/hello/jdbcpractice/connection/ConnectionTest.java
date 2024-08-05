package hello.jdbcpractice.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbcpractice.connection.ConnectionConst.*;

@Slf4j
class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection conn1 = DriverManager.getConnection(URL, USER, PASSWORD);
        Connection conn2 = DriverManager.getConnection(URL, USER, PASSWORD);
        log.info("connection={} class={}", conn1, conn1.getClass());
        log.info("connection={} class={}", conn2, conn2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        DataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USER);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(30);
        hikariDataSource.setPoolName("pull");

        useDataSource(hikariDataSource);
        Thread.sleep(1000);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();
        log.info("connection={} class={}", conn1, conn1.getClass());
        log.info("connection={} class={}", conn2, conn2.getClass());
    }
}
