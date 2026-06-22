package shiftSystem.dto;
import java.util.List;

import shiftSystem.entity.Member;

public class MemberSchedule{
    
    public final Member member;

    public final List<String> shift;

    public MemberSchedule(Member member, List<String> shift){
        this.member = member;
        this.shift = shift;
    }

}