package shiftSystem.entity;
import java.time.LocalDate;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import shiftSystem.ShiftType;

@Entity
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String password;

    private int yearOfService;
    private boolean isSenior; //年資>=3年 為true
    private int annualLeaveBalance; //特休餘額
    private boolean active = true;
    
    @Transient
    private ArrayList<LocalDate>preferredAnnualLeave; //預排公休
    private double overtimeHours;  //加班時數累計
    
    @Transient
    private  ArrayList<LocalDate> preferredOvertimeLeave;
    @Enumerated(EnumType.STRING)
    private ShiftType dominantShift; //包班
    
    @Transient
    private ArrayList<Member> delegateList; //職務代理人清單


    @PostLoad
    private void init(){
        if(preferredAnnualLeave == null) preferredAnnualLeave = new ArrayList<>();
        if(preferredOvertimeLeave == null) preferredOvertimeLeave = new ArrayList<>();
        if(delegateList == null) delegateList = new ArrayList<>();
    }
    public String getName(){return this.name;}
    public String getId(){ return this.id;}
    public int getYearOfService(){ return this.yearOfService;}
    public boolean isSenior(){ return this.isSenior;}
    public double getOvertimeHours(){ return this.overtimeHours;}
    public ShiftType getDominantShift(){ return this.dominantShift;}
    public ArrayList<Member> getDelegateList(){ return this.delegateList;}
    public int getAnnualLeave(){ return annualLeaveBalance;}
    public ArrayList<LocalDate> getPreferredAL(){return preferredAnnualLeave;}
    public ArrayList<LocalDate> getPreferredOTL(){return preferredOvertimeLeave;}
    public boolean isActive(){ return this.active;}

    public void setName(String name){this.name = name;}
    public void setPassword(String password){ this.password = password;}
    public void setYearOfService(int yearOfService){ this.yearOfService = yearOfService;}
    public void setOvertimeHours(double overtimeHours){ this.overtimeHours = overtimeHours;}
    public void setDominantShift( ShiftType dominantShift){ this.dominantShift = dominantShift;}
    public void setAnnualLeave(int annualLeaveBalance){this.annualLeaveBalance = annualLeaveBalance;}
    public void setSenior(boolean isSenior){this.isSenior = isSenior;}
    public void setActive(boolean active){this.active = active;}
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }



    
    public Member(String id,String name,String password,int yearOfService){
        this.id = id;
        this.name = name;
        this.password = password;
        this.yearOfService = yearOfService;
        this.delegateList = new ArrayList<>();
         isSenior= yearOfService >=3;
        this.preferredAnnualLeave = new ArrayList<>();
        this.preferredOvertimeLeave = new ArrayList<>();
    }
    public Member(){
        this.delegateList = new ArrayList<>();
        this.preferredAnnualLeave = new ArrayList<>();
        this.preferredOvertimeLeave = new ArrayList<>();
    }

    
    public void addPreferredAnnualLeave(LocalDate date){ 
        this.preferredAnnualLeave.add(date);
        setAnnualLeave(annualLeaveBalance -1);
     }
    public void addPreferredOvertimeLeave(LocalDate date){
        this.preferredOvertimeLeave.add(date);
        setOvertimeHours(overtimeHours-8);
    }


    

    public void addDelegate(Member member){
        this.delegateList.add(member);
    }

    @Override
    public String toString(){
        return name;
    }
}
