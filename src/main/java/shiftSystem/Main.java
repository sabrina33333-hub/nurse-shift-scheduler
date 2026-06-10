package shiftSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

  @SpringBootApplication
  public class Main {
      public static void main(String[] args) {
          SpringApplication.run(Main.class, args);
      }
  }

//public class Main {
    // public static void main(String[] args) throws IOException {
    //     Shift shift = new Shift(LocalDate.of(2026, 5, 1), "東區");
    //     ArrayList<Member> members = new ArrayList<>();
    //     members.add(new Member("001", "王小明", "pass123", 4));
    //     members.add(new Member("002", "李小華", "pass456", 1));
    //     members.add(new Member("003", "蔡小小", "pass406", 3));
    //     members.add(new Member("004", "陳乘乘", "pass8886", 4));
    //     members.add(new Member("005", "李尖尖", "pass9999", 3));
    //     members.add(new Member("006", "李婉殷", "pass2222", 6));
    //     members.add(new Member("007", "林小寶", "pass8888", 2));
    //     members.add(new Member("008", "張翩翩", "pass00008", 6));
    //     members.add(new Member("009", "杜甫", "pass8888", 5));
    //     members.add(new Member("010", "肖戰", "pass46788", 2));
        
    //     ShiftScheduler shiftScheduler = new ShiftScheduler(shift, members);
    //     members.get(0).addPreferredAnnualLeave(LocalDate.of(2026, 5, 6));
    //     shiftScheduler.makeShift();
        
    //     List<LocalDate> holidays = new ArrayList<>();
    //     holidays.add(LocalDate.of(2026,5,6));//國定假日設定
        
        

    //     ExcelExporter exporter = new ExcelExporter();
    //     exporter.exportEmptyShift(shift, members,holidays, "班表.xlsx");
     
    //     exporter.exportShift(shift, members,shiftScheduler.allShifts,holidays ,"自動班表.xlsx");
    //     System.out.println("班表已產生！");
    // }
//}
