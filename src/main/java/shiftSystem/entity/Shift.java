package shiftSystem.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate startDate;
    private String wardName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ShiftItem> shiftList;

    public String getId(){ return this.id; }

    public LocalDate getStartDate(){return this.startDate;}
    public String getWardName(){ return this.wardName;}
    public List<ShiftItem> getShiftList(){ return this.shiftList;}

    public void setWardName(String wardName){ this.wardName = wardName;}



    public Shift(){}
    
    public Shift(LocalDate startDate,String wardName){
        this.startDate = startDate;
        this.wardName = wardName;
        this.shiftList = new ArrayList<>();

    }

    public void addShiftItem(ShiftItem shiftItem){
        this.shiftList.add(shiftItem);
    }

    
}
