package shiftSystem.service;


import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



import org.springframework.stereotype.Service;

import shiftSystem.ExcelExporter;
import shiftSystem.dto.MemberSchedule;
import shiftSystem.dto.ScheduleResult;
import shiftSystem.entity.Member;
import shiftSystem.entity.Shift;
import shiftSystem.entity.ShiftItem;
import shiftSystem.repository.MemberRepository;
import shiftSystem.repository.ShiftRepository;
import shiftSystem.util.ShiftCodeResolver;
import java.io.ByteArrayOutputStream;


@Service
public class ShiftService {

    private final MemberRepository memberRepository;
    private final ShiftRepository shiftRepository;

    public ShiftService(MemberRepository memberRepository , ShiftRepository shiftRepository){
        this.memberRepository = memberRepository;
        this.shiftRepository = shiftRepository;
    }
    

    public ScheduleResult generateSchedule(int year, int month , String wardName)throws IOException {
       // DB 讀 members
        ArrayList<Member> members = new ArrayList<>(memberRepository.findByActiveTrue());

        
        
        for(int attemp =1 ;attemp <=1000; attemp++){
            try{
                //建立shift物件
                Shift shift = new Shift(LocalDate.of(year,month, 1),wardName); 
                //跑排班
                ShiftScheduler shiftScheduler = new ShiftScheduler(shift, members);
                shiftScheduler.makeShift();
                
                shiftRepository.save(shift);

                
                System.out.println("班表已產生！");

            

                return new ScheduleResult(shift, members,buildMemberSchedules(members,shiftScheduler.getAllShifts(),
                LocalDate.of(year, month, 1).lengthOfMonth()));
                }catch(IllegalStateException e){
                    System.out.println("班表已產生失敗！");
                }
        }
        throw new IllegalStateException ("失敗！ 在職<7 或 資深<4!");
        

            
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
        List<Member> members = memberRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ExcelExporter().exportShift(shift,  members,shift.getShiftList(), new ArrayList<>(),out);
        return out.toByteArray();
        
    }

    
    

}
