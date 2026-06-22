package shiftSystem.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import shiftSystem.ShiftType;



@Entity
public class ShiftItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @ManyToMany
    private List<Member> itemNurse;

    public ShiftItem(){}

    public LocalDate getDate(){ return this.date;}
    public ShiftType getShiftType(){ return this.shiftType;}
    public List<Member> getNurse(){ return this.itemNurse;}

    public void addNurse(Member member){this.itemNurse.add(member);}
    public void removeNurse(Member member){this.itemNurse.remove(member);}



    public ShiftItem(LocalDate date,ShiftType shiftType){
        this.date = date;
        this.shiftType = shiftType;
        itemNurse = new ArrayList<>();
    }

    
}
