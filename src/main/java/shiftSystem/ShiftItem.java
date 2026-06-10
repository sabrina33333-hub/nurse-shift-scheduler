package shiftSystem;

import java.time.LocalDate;
import java.util.ArrayList;
//三個班
public class ShiftItem {
    private LocalDate date;
    private ShiftType shiftType;
    private ArrayList<Member> itemNurse;

    public LocalDate getDate(){ return this.date;}
    public ShiftType getShiftType(){ return this.shiftType;}
    public ArrayList<Member> getNurse(){ return this.itemNurse;}

    public void addNurse(Member member){this.itemNurse.add(member);}
    public void removeNurse(Member member){this.itemNurse.remove(member);}



    public ShiftItem(LocalDate date,ShiftType shiftType){
        this.date = date;
        this.shiftType = shiftType;
        itemNurse = new ArrayList<>();
    }

    
}
