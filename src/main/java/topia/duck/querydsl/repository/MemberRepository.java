package topia.duck.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topia.duck.querydsl.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsername(String username);
}
