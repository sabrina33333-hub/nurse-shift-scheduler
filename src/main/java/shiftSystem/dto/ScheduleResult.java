package shiftSystem.dto;

import shiftSystem.Shift;
import shiftSystem.entity.Member;
import shiftSystem.entity.ShiftItem;
import java.util.List;

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
