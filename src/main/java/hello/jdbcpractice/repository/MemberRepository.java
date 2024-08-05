package hello.jdbcpractice.repository;

import hello.jdbcpractice.domain.member.Member;

public interface MemberRepository {
    Member save(Member member);
    Member findById(String memberId);
    void update(String memberId, int money);
    void delete(String memberId);
}
