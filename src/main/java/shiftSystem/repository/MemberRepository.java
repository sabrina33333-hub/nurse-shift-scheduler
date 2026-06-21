package shiftSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shiftSystem.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{
    
}
