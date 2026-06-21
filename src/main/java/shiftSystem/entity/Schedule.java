package shiftSystem.entity;

import java.time.LocalDate;
import java.util.ArrayList;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;       
import jakarta.persistence.CascadeType; 
import java.util.List;                 


@Entity
public class Schedule {
  
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private  LocalDate startDate;
    private  String wardName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ShiftItem> shiftItems = new ArrayList<>();

    public Schedule(){}

}
