package shiftSystem.dto;

import java.util.List;

import shiftSystem.entity.Member;
import shiftSystem.entity.Shift;

public class ScheduleResult {
    public final Shift shift;
    public final List<Member> members;
    public final List<MemberSchedule> memberSchedules;

    public ScheduleResult(Shift shift, List<Member> members, List<MemberSchedule> memberSchedules) {
        this.shift = shift;
        this.members = members;
        this.memberSchedules = memberSchedules;
    }
}
