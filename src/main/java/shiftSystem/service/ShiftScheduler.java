package shiftSystem.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shiftSystem.ShiftType;
import shiftSystem.entity.Member;
import shiftSystem.entity.Shift;
import shiftSystem.entity.ShiftItem;

class DayContext{
    List<Member> candidates; //nurse2
    ShiftItem preD,preE,preN;
    ArrayList<Member> signedMembers;
    int day;

    public DayContext(List<Member> candidates,ShiftItem preD,ShiftItem preE,ShiftItem preN,ArrayList<Member> signedMembers,int day) {
        this.candidates = candidates;
        this.preD = preD;
        this.preE = preE;
        this.preN = preN;
        this.signedMembers = signedMembers;
        this.day = day;
    }

    
}

class ShiftSlot{
    ShiftItem shiftItem; //D /E /N
    boolean checkPreD,checkPreE,checkPreN;
    int count;
    int maxCount;

    public ShiftSlot(ShiftItem shiftItem,boolean checkPreD,
        boolean checkPreE,boolean checkPreN, int count,int maxCount){
        
        this.shiftItem = shiftItem;
        this.checkPreD = checkPreD;
        this.checkPreE = checkPreE;
        this.checkPreN = checkPreN;
        this.count = count;
        this.maxCount = maxCount;
    }
}

public class ShiftScheduler {
    private Shift shift;
    private ArrayList<Member> nurse;
    private List<ShiftItem> allShifts;
    private Map<String, ShiftItem> assignedShift = new HashMap<>();
    
    private static final int DAY_COUNT = 3;
    private static final int EVENING_COUNT =2;
    private static final int NIGHT_COUNT = 1;

    public List<ShiftItem> getAllShifts(){ return  allShifts;}

    
 
    public ShiftScheduler(Shift shift,ArrayList<Member> members){
        this.shift = shift;
        this.nurse = members;
        allShifts = new ArrayList<>();
    }

   public static <T> List<T> getRandomOrderList(List<T> inputList) {
        List<T> newList = new ArrayList<>(inputList);

        Collections.shuffle(newList);

        return newList;
    }
    
    private  boolean  canAssign(Member member,DayContext dayContext,ShiftSlot slot){
             
            
            
            if(dayContext.signedMembers.contains(member)){return false;}
            if(slot.checkPreD && dayContext.preD != null && dayContext.preD.getNurse().contains(member)){return false;}
            if(slot.checkPreE && dayContext.preE != null && dayContext.preE.getNurse().contains(member)){return false;}
            if(slot.checkPreN && dayContext.preN != null && dayContext.preN.getNurse().contains(member)){return false;}
            if(isPreferredOff (member,dayContext.day)){return false;}
            if(row4(member, dayContext.day)>=6 ){return false;}
            return true;

        }

    private int fillShift(DayContext dayContext ,ShiftSlot slot){

             for(int d = 0;d < dayContext.candidates.size() && slot.count< slot.maxCount ;d++ ){
                Member member = dayContext.candidates.get(d);
                if(member.isSenior() && canAssign(member,dayContext,slot)){
                    slot.shiftItem.addNurse(member);
                    dayContext.signedMembers.add(member);
                    slot.count ++;
                    break;
                }

             }

             for(int d = 0;d < dayContext.candidates.size() && slot.count< slot.maxCount ;d++ ){
                Member member = dayContext.candidates.get(d);
                if(canAssign(member, dayContext, slot)){
                    slot.shiftItem.addNurse(member);
                    dayContext.signedMembers.add(member);
                    slot.count ++;

                }

             }
             return slot.count;
        }

    //產出班別
    public void makeShift(){
        

        for(int i =0;i< shift.getStartDate().lengthOfMonth();i++){
            ArrayList<Member> signedMembers = new ArrayList<>();
            ShiftItem D = new ShiftItem(shift.getStartDate().plusDays(i), ShiftType.DAY);
            ShiftItem E = new ShiftItem(shift.getStartDate().plusDays(i), ShiftType.EVENING);
            ShiftItem N = new ShiftItem(shift.getStartDate().plusDays(i), ShiftType.NIGHT);
            List<Member> nurse2= getRandomOrderList(nurse);//打亂的人員排序


            //Row1 不能「大夜接白班」
            //Row2不能「小夜接白班」
            ShiftItem preN = null;
            ShiftItem preE = null;
            ShiftItem preD = null;
            if(i>0){
                preN = allShifts.get((i-1)*3+2);//前一天大夜班
                preE = allShifts.get((i-1)*3+1);//前一天小夜班
                preD = allShifts.get((i-1)*3);//前一天白班

            }
            int ncount =0;
            int ecount =0;
            int dcount =0;
            
            //R3 OFF 前後不能夾 1 天班
            for(Member member:nurse2){
                if(i>=2 &&(!isOff(allShifts, i-1, member))&&(isOff(allShifts, i-2, member))){
                    if(preN != null && preN.getNurse().contains(member) && ncount < NIGHT_COUNT){
                        N.addNurse(member);
                        signedMembers.add(member);
                        ncount ++;
                    }else if(preE != null&& preE.getNurse().contains(member) && ecount < EVENING_COUNT){
                        E.addNurse(member);
                        signedMembers.add(member);
                        ecount ++;
                    }else if(dcount < DAY_COUNT){
                        D.addNurse(member);
                        signedMembers.add(member);
                        dcount ++;

                    }
                }
            }


            //白班

            DayContext dayContext =new DayContext(nurse2,preD,preE,preN,signedMembers,i);
            ShiftSlot dSlot = new ShiftSlot( D, false, true, true,dcount, DAY_COUNT);
                        
            fillShift(dayContext,dSlot);
            

            if(!hasSenior(D.getNurse())){
                throw new IllegalStateException(i+"日，白班沒有資深人員！");
            }


            // 小夜班
            
            ShiftSlot eSlot = new ShiftSlot( E, false, false, false,ecount, EVENING_COUNT);
            
            fillShift(dayContext,eSlot);

            if(!hasSenior(E.getNurse())){
                throw new IllegalStateException(i+"日，小夜班沒有資深人員！");
            }

            //大夜班   R8 | 白班、小夜班不能接大夜班
            
            ShiftSlot nSlot = new ShiftSlot( N, true, true, false,ncount, NIGHT_COUNT);
            
            fillShift(dayContext,nSlot);

            if(!hasSenior(N.getNurse())){
                throw new IllegalStateException(i+"日，大夜班沒有資深人員！");
            }

            allShifts.add(D);
            allShifts.add(E);
            allShifts.add(N);
            shift.addShiftItem(D);
            shift.addShiftItem(E);
            shift.addShiftItem(N);


        }

    }

   boolean solve(int day, ShiftType shiftType, int count) {

        if (day == shift.getStartDate().lengthOfMonth()) {
            return true;  // 全部排完了
        }
        
        
        int maxCount = switch(shiftType){
            case DAY ->3 ;
            case EVENING ->2 ;
            case NIGHT ->1;
            default ->1;
        };

        if (count == maxCount) {
        // 這個班次滿了，要換下一個目標（含是否有資深R5）
            if (shiftType == ShiftType.DAY && hasSenior(getShiftItem(day,ShiftType.DAY).getNurse())) {
                return solve(day, ShiftType.EVENING, 0);  // 換到「哪一天」的「哪個班別」，count 歸零
            } else if(shiftType == ShiftType.EVENING && hasSenior(getShiftItem(day,ShiftType.EVENING).getNurse())) {
                return solve( day, ShiftType.NIGHT, 0);  // 白班滿了換小夜、小夜滿了換大夜
            }else if( hasSenior(getShiftItem(day,ShiftType.NIGHT).getNurse())){
                return solve(day+1,ShiftType.DAY,0);
            }return false;
        }

        // 還沒滿，繼續在同一個 (day, shiftType) 裡找下一個人
        for (Member member : nurse) {
            if (isValid(member, day, shiftType)) {
                getShiftItem(day,shiftType).addNurse(member);
                if (solve(day, shiftType, count+1)) {
                    return true;
                }
                getShiftItem(day, shiftType).removeNurse(member);
            }
        }
        return false;
   }
   
   //是否能排白班
    boolean isValid(Member member, int day,ShiftType shiftType){
        //今天是否排過班
        if(getShiftItem(day,ShiftType.DAY).getNurse().contains(member)|| getShiftItem(day,ShiftType.EVENING).getNurse().contains(member)
            ||getShiftItem(day,ShiftType.NIGHT).getNurse().contains(member)){
        return false;
        
        //前一天大夜、小夜是否有人 Ｒ1\R2 
        // R4排班是否大於6天
        }else if(day >= 1 && shiftType == ShiftType.DAY && (getShiftItem(day-1,ShiftType.NIGHT).getNurse().contains(member)||
            getShiftItem(day-1, shiftType.EVENING).getNurse().contains(member))||(row4(member, day)>= 6)){ 
            
            return false;
        }
        

        return true;

    }
 
        ShiftItem getShiftItem(int day,ShiftType shiftType){
            String key = day+"_"+ shiftType;
            ShiftItem item = assignedShift.get(key);
            if(item == null){
                item = new ShiftItem(shift.getStartDate().plusDays(day),shiftType);
                assignedShift.put(key,item);
                allShifts.add(item);
                shift.addShiftItem(item);
            }
            return item;
}

    
    private boolean isPreferredOff(Member member,int day){
        LocalDate date = shift.getStartDate().plusDays(day);
        return member.getPreferredAL().contains(date)||
        member.getPreferredOTL().contains(date);
    }

    private boolean isOff(List<ShiftItem> allShifts, int day, Member member) {
        ShiftItem d = allShifts.get(day * 3);
        ShiftItem e = allShifts.get(day * 3 + 1);
        ShiftItem n = allShifts.get(day * 3 + 2);
        return !d.getNurse().contains(member)
            && !e.getNurse().contains(member)
            && !n.getNurse().contains(member);
    }


    //算連續上班天數 for R4 (連續上班<6 days)
    public int row4(Member member,int currentDay){
        int count=0;
        for(int i =currentDay-1;i >=0;i--){
            if(!isOff(allShifts, i, member)){
                count ++;
            }else{
                break;
            }
        }
        return count;
    }







    public boolean hasSenior(List<Member> members){
        for(Member m: members){
            if (m.isSenior()){
             return true;
            }
        }
        return false;

    }

    
    

}
