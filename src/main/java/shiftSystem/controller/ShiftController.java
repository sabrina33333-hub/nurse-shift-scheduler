package shiftSystem.controller;
import java.io.IOException;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
            model.addAttribute("allShifts",result.allShifts);
            model.addAttribute("shift",result.shift);
            
        return "shift/result";
    }
    
    
}
