package hello.jdbcpractice.repository;

import hello.jdbcpractice.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * jdbc - dataSource 사용, jdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {
    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member (member_id, money) values (?, ?)";

        Connection conn = null; // Connection 객체 생성
        PreparedStatement pstmt = null; // PreparedStatement 객체 생성

        try {
            conn = getConnection(); // 커넥션 연결
            pstmt = conn.prepareStatement(sql); // SQL 전달 준비
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); // SQL을 DB로 전달

            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            // 사용한 리소스 정리
            pstmt.close();
            conn.close();
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection conn = null; // Connecton 객체 생성
        PreparedStatement pstmt = null; // PreparedStatement 객체 생성
        ResultSet rs = null; // ResultSet 객체 생성

        try {
            conn = getConnection(); // 커넥션 연결
            pstmt = conn.prepareStatement(sql); // SQL 전달 준비
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery(); // SQL을 DB로 전달하고 받은 응답 저장

            if (rs.next()) { // 커서를 옮기며 데이터가 있다면 true, 없다면 false 반환
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId:" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            // 사용한 리소스 정리
            rs.close();
            pstmt.close();
            conn.close();
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(conn);
    }

    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        log.info("get connection={}, class={}", conn, conn.getClass());
        return conn;
    }
}
