package shiftSystem;

import java.util.ArrayList;
import java.time.LocalDate;
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

    // //public void generateShiftItems(){
    //     int m = startDate.lengthOfMonth();
    //     for(int n =1; n<=m ;n++){
            
    //         ShiftItem DAY = new ShiftItem(startDate.plusDays(n-1),ShiftType.DAY);
    //         ShiftItem EVENING = new ShiftItem(startDate.plusDays(n-1), ShiftType.EVENING);
    //         ShiftItem NIGHT = new ShiftItem(startDate.plusDays(n-1), ShiftType.NIGHT);
    //         addShiftItem(DAY);
    //         addShiftItem(EVENING);
    //         addShiftItem(NIGHT);
    //     }
     //   }
}
