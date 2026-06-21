package shiftSystem.service;

import org.springframework.stereotype.Service;

import shiftSystem.service.ShiftScheduler;  // 需要加這行

import shiftSystem.ExcelExporter;
import shiftSystem.Shift;
import shiftSystem.entity.Member;
import shiftSystem.repository.MemberRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import shiftSystem.dto.ScheduleResult;

@Service
public class ShiftService {

    private final MemberRepository memberRepository;

    public ShiftService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public ScheduleResult generateSchedule(int year, int month , String wardName)throws IOException {
       
        //建立shift物件
            Shift shift = new Shift(LocalDate.of(year,month, 1),wardName);

            // DB 讀 members
            ArrayList<Member> members = new ArrayList<>(memberRepository.findAll());

            //跑排班
            ShiftScheduler shiftScheduler = new ShiftScheduler(shift, members);
            shiftScheduler.makeShift();

            List<LocalDate> holidays = new ArrayList<>();
            holidays.add(LocalDate.of(2026,5,6));//國定假日設定
            String filePath = "/tmp/" + wardName + "_" + year + month + ".xlsx";
            ExcelExporter exporter = new ExcelExporter();

            

            exporter.exportShift(shift, members, shiftScheduler.getAllShifts(), holidays, filePath);
            System.out.println("班表已產生！");

            return new ScheduleResult(shift, members,shiftScheduler.getAllShifts());
            
    }
    
    

}
