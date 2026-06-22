pakege shiftSystem.util;
import shiftSystem.entity.Member;
import shiftSystem.entity.ShiftItem;

public class ShiftCodeResolver{

    public static  String getShiftCode(List<ShiftItem> allShifts, int day, Member member) {
        ShiftItem d = allShifts.get(day*3);
        ShiftItem e = allShifts.get(day*3+1);
        ShiftItem n = allShifts.get(day*3+2);
        if(d.getNurse().contains(member)){return "D" ;
        }else if(e.getNurse().contains(member)){return "E" ;
        }else if(n.getNurse().contains(member)){return "N" ;
        }else{return "OFF"}
    }
}
