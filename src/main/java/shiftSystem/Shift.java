package shiftSystem;

import java.time.LocalDate;
import java.util.ArrayList;

import shiftSystem.entity.ShiftItem;
//空班表（預班表）
public class Shift {
    private LocalDate startDate;
    private String wardName;
    private ArrayList<ShiftItem> shiftList;

    public LocalDate getStartDate(){return this.startDate;}
    public String getWardName(){ return this.wardName;}
    public ArrayList<ShiftItem> getShiftList(){ return this.shiftList;}

    public void setWardName(String wardName){ this.wardName = wardName;}



    public Shift(LocalDate startDate,String wardName){
        this.startDate = startDate;
        this.wardName = wardName;
        this.shiftList = new ArrayList<>();

    }

    public void addShiftItem(ShiftItem shiftItem){
        this.shiftList.add(shiftItem);
    }

    
}
