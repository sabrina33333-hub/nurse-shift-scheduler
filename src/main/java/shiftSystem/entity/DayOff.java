package shiftSystem.entity;

import java.time.LocalDate;

import shiftSystem.DayOffStatus;

public class DayOff {
    private Member applicant;
    private LocalDate date;
    private Member delegate;
    private Member approver;
    private DayOffStatus status;

    public Member getApplicant(){return this.applicant;};
    public LocalDate getDate(){return this.date;};
    public Member getDelegate(){return this.delegate;};
    public Member getApprover(){ return this.approver;};
    public DayOffStatus getStatus(){ return this.status;};

    public void setDelegate(Member delegate){this.delegate = delegate;}
    public void setApprover(Member approver){this.approver = approver;}
    public void setStatus(DayOffStatus status){this.status = status;}

    public DayOff(Member applicant,LocalDate date,Member delegate){
        this.applicant= applicant;
        this.date = date;
        this.delegate = delegate;
        this.approver = null;
        this.status = DayOffStatus.WAITING;
    }
}
