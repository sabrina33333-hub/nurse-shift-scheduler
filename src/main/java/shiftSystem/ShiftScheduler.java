package shiftSystem;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ShiftScheduler {
    Shift shift;
    ArrayList<Member> nurse;
    List<ShiftItem> allShifts;

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
    

    
    //產出班別
    public void makeShift(){
        int total= nurse.size();
        
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
                    if(preN.getNurse().contains(member)){
                        N.addNurse(member);
                        signedMembers.add(member);
                        ncount ++;
                    }else if(preE.getNurse().contains(member)){
                        E.addNurse(member);
                        signedMembers.add(member);
                        ecount ++;
                    }else{
                        D.addNurse(member);
                        signedMembers.add(member);
                        dcount ++;
                        
                    }
                }
            }
            

            //白班
            
           
            for(int d =0;d <total&& dcount<3;d++){
                Member member = nurse2.get(d); 
             
                if(!signedMembers.contains(member)&&(preN == null || !preN.getNurse().contains(member))
                    &&( preE==null || !preE.getNurse().contains(member))&& (row4(member, i)<6 && member.isSenior() 
                &&!isPreferredOff(member, i))){
                    D.addNurse(member);
                    signedMembers.add(member);
                    dcount++;
                    break;
                }
            }
            
            for(int d =0;d <total&& dcount<3;d++){
                Member member = nurse2.get(d); 
             
                if(!signedMembers.contains(member)&&(preN == null || !preN.getNurse().contains(member))
                    &&( preE==null || !preE.getNurse().contains(member))&& row4(member, i)<6 &&!isPreferredOff(member, i)){
                    D.addNurse(member);
                    signedMembers.add(member);
                    dcount++;
                    
                }
            }
            if(!hasSenior(D.getNurse())){
                throw new IllegalStateException(i+"日，白班沒有資深人員！");
            }
            

            // 小夜班
            
            for(int e=0; e<total && ecount<2;e++){
                Member member = nurse2.get(e); 
                if(!signedMembers.contains(member)&& row4(member, i)<6 && member.isSenior() 
                    &&!isPreferredOff(member, i)){
                E.addNurse(member);
                signedMembers.add(member);
                ecount++;
                break;
                }
            }
            for(int e=0; e<total && ecount<2;e++){
                Member member = nurse2.get(e); 
                if(!signedMembers.contains(member)&& row4(member, i)<6 &&!isPreferredOff(member, i)){
                E.addNurse(member);
                signedMembers.add(member);
                ecount++;
                
                }
            }
            if(!hasSenior(E.getNurse())){
                throw new IllegalStateException(i+"日，小夜班沒有資深人員！");
            }

            //大夜班   R8 | 白班、小夜班不能接大夜班
            
            for(int n=0; n<total && ncount<1;n++){
                Member member = nurse2.get(n); 
                if(!signedMembers.contains(member)&& (preE==null||!preE.getNurse().contains(member) ) 
                    &&(preD == null || !preD.getNurse().contains(member))&& row4(member, i)<6 && member.isSenior() 
                    &&!isPreferredOff(member, i)){
                N.addNurse(member);
                signedMembers.add(member);
                ncount++;
                break;
                }
            } 
            for(int n=0; n<total && ncount<1;n++){
                Member member = nurse2.get(n); 
                if(!signedMembers.contains(member)&& (preE==null||!preE.getNurse().contains(member) ) 
                    &&(preD == null || !preD.getNurse().contains(member))&& row4(member, i)<6 
                    &&!isPreferredOff(member, i)){
                N.addNurse(member);
                signedMembers.add(member);
                ncount++;
                
                }
            }  
            if(!hasSenior(N.getNurse())){
                throw new IllegalStateException(i+"日，大夜班沒有資深人員！");
            }
            
            allShifts.add(D);
            allShifts.add(E);
            allShifts.add(N);
            
        }
        
    }

    private boolean isPreferredOff(Member member,int day){
        LocalDate date = shift.getStartDate().plusDays(day);
        return member.preferredAnnualLeave.contains(date)||
        member.preferredOvertimeLeave.contains(date);
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
