package shiftSystem.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import shiftSystem.ExcelExporter;
import shiftSystem.ShiftType;
import shiftSystem.dto.MemberSchedule;
import shiftSystem.dto.ScheduleResult;
import shiftSystem.entity.Member;
import shiftSystem.entity.Shift;
import shiftSystem.entity.ShiftItem;
import shiftSystem.repository.MemberRepository;
import shiftSystem.repository.ShiftRepository;
import shiftSystem.util.ShiftCodeResolver;



@Service
public class ShiftService {

    private final MemberRepository memberRepository;
    private final ShiftRepository shiftRepository;

    public ShiftService(MemberRepository memberRepository , ShiftRepository shiftRepository){
        this.memberRepository = memberRepository;
        this.shiftRepository = shiftRepository;
    }
    
    public int countSenior(List<Member> members){
            int count =0;
            for(Member m: members){
                
                if (m.isSenior()){
                
                count ++;
                }
            }return count;
    }

    public ScheduleResult generateSchedule(int year, int month , String wardName)throws IOException {
       // DB 讀 members
        ArrayList<Member> members = new ArrayList<>(memberRepository.findByActiveTrue());
        if(members.size()<7){
            throw new IllegalStateException ("人力不足7人! 共"+members.size()+"人！");
        }else if(countSenior(members)<4){
            throw new IllegalStateException ("資深人力不足4人,僅"+countSenior(members)+"人！");
        };
    
     

    
        
        for(int attemp =1 ;attemp <=1000; attemp++){
            try{
                //建立shift物件
                Shift shift = new Shift(LocalDate.of(year,month, 1),wardName); 
                //跑排班
                ShiftScheduler shiftScheduler = new ShiftScheduler(shift, members);
                boolean success = shiftScheduler.solve(0, ShiftType.DAY, 0);
                if(!success){
                    throw new IllegalStateException("排不出合法班表！");
                }
                
                shiftRepository.save(shift);

                
                System.out.println("班表已產生！");

            

                return new ScheduleResult(shift, members,buildMemberSchedules(members,shiftScheduler.getAllShifts(),
                LocalDate.of(year, month, 1).lengthOfMonth()));
                 }catch(IllegalStateException e){
                    System.out.println("第"+ attemp +"次班表產生失敗！");
                }
        }
        throw new IllegalStateException ("沒有好的組合班表！");
        

            
    }
    
    private List<MemberSchedule> buildMemberSchedules(List<Member> members, 
        List<ShiftItem> allShifts, int daysInMonth) {
        List<MemberSchedule> result = new ArrayList<>();
        for (Member member : members) {
            List<String> shifts = new ArrayList<>();
            for (int day = 0; day < daysInMonth; day++) {
                shifts.add(ShiftCodeResolver.getShiftCode(allShifts, day, member));
            }
            result.add(new MemberSchedule(member, shifts));
        }
        return result;
    }

    public byte[] downloadSchedule(String id) throws IOException{
        Shift shift=  shiftRepository.findById(id).orElseThrow();
        List<Member> members = memberRepository.findByActiveTrue();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ExcelExporter().exportShift(shift,  members,shift.getShiftList(), new ArrayList<>(),out);
        return out.toByteArray();
        
    }

    
    

}
