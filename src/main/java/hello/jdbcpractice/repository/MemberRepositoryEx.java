package hello.jdbcpractice.repository;

import hello.jdbcpractice.domain.member.Member;

import java.sql.SQLException;

// SQLException에 의존하는 Repository
public interface MemberRepositoryEx {
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
