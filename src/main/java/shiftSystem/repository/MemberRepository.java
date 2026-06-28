package shiftSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shiftSystem.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{
    List<Member> findByActiveTrue();

}
