package shiftSystem;

import java.util.List;

 import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/members")
public class MemberController {
    private  final  MemberRepository memberRepository;
    public MemberController(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members",members);
        return  "members/list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("member",new Member());
        
        return "members/add";
    }
    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/members";
    }
    
}
