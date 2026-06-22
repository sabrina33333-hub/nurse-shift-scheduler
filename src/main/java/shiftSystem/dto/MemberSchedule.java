package shiftSystem.dto;
import shiftSystem.Shift;
import shiftSystem.entity.Member;


public class MemberSchedule{
    public final Member member;

    public final List<String> shift;

    public MemberSchedule(Member member, List<String> shift){
        this.member = member;
        this.shift = shift;
    }

}