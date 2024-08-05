package hello.jdbcpractice.repository;

import hello.jdbcpractice.connection.DBConnectionUtil;
import hello.jdbcpractice.domain.member.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 순수 JDBC로만 구성된 로직들
 */
@Slf4j
public class MemberRepositoryV0 {

    /**
     * 주어진 회원 정보를 데이터베이스에 저장하는 메서드입니다.
     * 데이터베이스에 접근 중 발생할 수 있는 SQLException을 던집니다.
     *
     * @param member 저장할 회원 객체, 회원의 ID와 금액을 포함합니다.
     * @return 저장된 회원 객체를 반환합니다.
     * @throws SQLException 데이터베이스 작업 중 발생할 수 있는 SQL 예외
     */
    public Member save(Member member) throws SQLException {
        // SQL문 작성: member 테이블에 member_id와 money 값을 삽입하는 쿼리
        String sql = "insert into member (member_id, money) values (?, ?)";

        // 데이터베이스 연결 객체를 초기화
        Connection conn = null;
        // SQL문을 실행할 PreparedStatement 객체를 초기화
        PreparedStatement pstmt = null;

        try {
            // 커넥션 연결
            conn = getConnection();
            // SQL문 전달
            pstmt = conn.prepareStatement(sql);
            // 첫 번째 매개변수에 member 객체에서 가져온 memberId 값을 설정 (String 타입)
            pstmt.setString(1, member.getMemberId());
            // 두 번째 매개변수에 member 객체에서 가져온 money 값을 설정 (int 타입)
            pstmt.setInt(2, member.getMoney());
            // insert, update, delete 쿼리에서 영향을 받은 행의 개수를 반환
            pstmt.executeUpdate();
            // member 객체를 반환
            return member;
        } catch (SQLException e) { // SQL 예외 발생 시 처리
            // 오류 로그를 기록하고 예외 메시지를 출력
            log.error("db error", e);
            // 예외를 호출자에게 다시 던짐
            throw e;
        } finally {
            // 사용한 리소스(Connection, PreparedStatement)를 해제하여 메모리 누수 방지
            close(conn, pstmt, null);
        }
    }


    /**
     * 회원 id로 회원을 검색하는 메서드
     * 데이터베이스에 접근 중 발생할 수 있는 SQLException을 던집니다.
     *
     * @param memberId 검색하기 위한 memberId
     * @return 저장된 회원 객체 반환
     * @throws SQLException 데이터베이스 작업 중 발생할 수 있는 SQL 예외
     */
    public Member findById(String memberId) throws SQLException {
        // SQL문 작성
        String sql = "select * from member where member_id = ?";

        // 커넥션 객체 생성
        Connection conn = null;
        // PreparedStatement 객체 생성
        PreparedStatement pstmt = null;
        // ResultSet 객체 생성
        ResultSet rs = null;

        try {
            // 커넥션 생성
            conn = getConnection();
            // SQL문을 파라미터로 받는 커넥션의 prepareStatement 메서드 호출
            pstmt = conn.prepareStatement(sql);
            // setString 메서드로 1번 파라미터의 값에 받은 memberId를 넣어줌(memberId = String)
            pstmt.setString(1, memberId);

            // 쿼리 실행한 값을 rs에 넣음
            rs = pstmt.executeQuery();

            // SQL문으로 얻은 데이터에서 행이 있다면 true 반환, 없으면 false 반환
            if (rs.next()) {
                // member 객체 생성
                Member member = new Member();
                // 실행결과에서 컬럼 이름이 member_id인 컬럼을 String으로 가져와 member의 MemberId에 설정
                member.setMemberId(rs.getString("member_id"));
                // 실행결과에서 컬럼 이름이 money인 컬럼을 Int으로 가져와 member의 Money에 설정
                member.setMoney(rs.getInt("money"));
                // member 반환
                return member;
            } else {
                /**
                 * rs.next()가 false일 경우
                 * NoSuchElementException memberId와 함께 던지기
                 */
                throw new NoSuchElementException("member not found memberId:" + memberId);
            }
        } catch (SQLException e) { // SQL 예외 발생 시 처리
            // 오류 로그를 기록하고 예외 메시지를 출력
            log.error("db error", e);
            // 예외를 호출자에게 다시 던짐
            throw e;
        } finally {
            // 사용한 리소스(Connection, PreparedStatement, rs)를 해제하여 메모리 누수 방지
            close(conn, pstmt, rs);
        }
    }

    /**
     * 회원의 money를 업데이트하는 메서드
     * 데이터베이스에 접근 중 발생할 수 있는 SQLException을 던집니다.
     *
     * @param memberId money를 업데이트할 회원의 Id
     * @param money 업데이트할 money
     * @throws SQLException 데이터베이스 작업 중 발생할 수 있는 SQL 예외
     */
    public void update(String memberId, int money) throws SQLException {
        // SQL문: 검색 조건인 member_id로 회원을 검색해서 회원의 money를 재설정하는 쿼리
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
        if (rs != null) {
            try {
                rs.close(); // Exception
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close(); // Exception
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }
    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
