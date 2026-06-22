package shiftSystem.controller;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import shiftSystem.entity.Shift;
import shiftSystem.ExcelExporter;
import shiftSystem.entity.Member;
import shiftSystem.repository.ShiftRepository;
import shiftSystem.repository.MemberRepository;



import shiftSystem.service.ShiftService;

import shiftSystem.dto.ScheduleResult;

@Controller
@RequestMapping("/shift")
public class ShiftController {
    
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService){
        this.shiftService = shiftService;
    }
    @GetMapping
    public String showForm(Model model){

        return"shift/form";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam int year,@RequestParam int month, 
        @RequestParam String wardName,Model model) throws IOException{
        
            ScheduleResult result = shiftService.generateSchedule(year,month,wardName);
            //存 DB
            model.addAttribute("members", result.members);
            model.addAttribute("memberSchedules",result.memberSchedules);
            model.addAttribute("shift",result.shift);
            
        return "shift/result";
    }
    
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String id)throws IOException {
        Shift shift = shiftRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("找不到班表！"));
        List<Member> members = memberRepository.findAll();
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2026, 5, 6));
        
        String filePath = "/tmp/download_" + id + ".xlsx";
        new ExcelExporter().exportShift(shift, members, shift.getShiftList(), holidays, filePath);
        
        byte[] fileContent = Files.readAllBytes(Path.of(filePath));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"schedule.xlsx\"")
            .contentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(fileContent);
    }
    
    
}
