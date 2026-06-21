package shiftSystem.dto;

import shiftSystem.Shift;
import shiftSystem.entity.Member;
import shiftSystem.entity.ShiftItem;
import java.util.List;

public class ScheduleResult {
    public final Shift shift;
    public final List<Member> members;
    public final List<ShiftItem> allShifts;

    public ScheduleResult(Shift shift, List<Member> members, List<ShiftItem> allShifts) {
        this.shift = shift;
        this.members = members;
        this.allShifts = allShifts;
    }
}
