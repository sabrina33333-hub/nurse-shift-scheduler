
package shiftSystem;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import shiftSystem.entity.Member;
import shiftSystem.entity.Shift;
import shiftSystem.entity.ShiftItem;
import shiftSystem.util.ShiftCodeResolver;

//空班表
public class ExcelExporter {
      
   public void exportEmptyShift(Shift shift, List<Member> members,List<LocalDate> holidays, String filePath)throws IOException{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("班表");
        int year = shift.getStartDate().getYear()-1911;
        int mm = shift.getStartDate().getMonthValue();
        String ward = shift.getWardName();

        Row titRow = sheet.createRow(0);
        Cell titleCell = titRow.createCell(0);  
        titleCell.setCellValue(ward+"班表 "+mm+"月"+year+"年");
        
        int daysInMonth = shift.getStartDate().lengthOfMonth();
        Row dateRow = sheet.createRow(1);
        Row dayRow = sheet.createRow(2);
        Row hRow = sheet.createRow(3);
        Cell cell1 = dateRow.createCell(0);
        cell1.setCellValue("姓名");
        for(int d =1; d<= daysInMonth;d++){
            sheet.setColumnWidth(d, 1024);
            
            Cell cell = dateRow.createCell(d);
            cell.setCellValue(d); 
            LocalDate date = shift.getStartDate().withDayOfMonth(d);
            date.getDayOfWeek();
            String dayStr = switch(date.getDayOfWeek()){
            case MONDAY -> "一";
            case TUESDAY -> "二";
            case WEDNESDAY -> "三";
            case THURSDAY -> "四";
            case FRIDAY -> "五";
            case SATURDAY -> "六";
            case SUNDAY -> "日";
                
            };
                Cell daycell = dayRow.createCell(d);
                daycell.setCellValue(dayStr);
                Cell hCell = hRow.createCell(d); 
            if(holidays.contains(date)){
                hCell.setCellValue("假");
                }else{
                hCell.setCellValue(" ");
                } 
        }

        for( int i = 0;i<members.size();i++){
            Row nameRow = sheet.createRow(4+i);
            Cell cellName = nameRow.createCell(0);
            String name =members.get(i).getName();
            cellName.setCellValue(name);
        }
        
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();

    
    }

    //產出實際班表
    public void exportShift(Shift shift, List<Member> members,List<ShiftItem> allShifts,List<LocalDate> holidays,  String filePath)throws IOException{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("班表");
        int year = shift.getStartDate().getYear()-1911;
        int mm = shift.getStartDate().getMonthValue();
        String ward = shift.getWardName();

        Row titRow = sheet.createRow(0);
        Cell titleCell = titRow.createCell(0);  
        titleCell.setCellValue(ward+"班表 "+mm+"月"+year+"年");
        
        int daysInMonth = shift.getStartDate().lengthOfMonth();
        Row dateRow = sheet.createRow(1);
        Row dayRow = sheet.createRow(2);
        Row hRow = sheet.createRow(3);
        Cell cell1 = dateRow.createCell(0);
        cell1.setCellValue("姓名/日期");
        

        for(int d =1; d<= daysInMonth;d++){
            sheet.setColumnWidth(d, 1024);
            Cell cell = dateRow.createCell(d);
            cell.setCellValue(d); 
            LocalDate date = shift.getStartDate().withDayOfMonth(d);
            date.getDayOfWeek();
            String dayStr = switch(date.getDayOfWeek()){
            case MONDAY -> "一";
            case TUESDAY -> "二";
            case WEDNESDAY -> "三";
            case THURSDAY -> "四";
            case FRIDAY -> "五";
            case SATURDAY -> "六";
            case SUNDAY -> "日";
                
            };
            
            Cell daycell = dayRow.createCell(d);
            daycell.setCellValue(dayStr);
            Cell hcell = hRow.createCell(d); 
            if(holidays.contains(date)){
                hcell.setCellValue("假");
                }else{
                hcell.setCellValue(" ");
                } 
        }

        for( int i = 0;i<members.size();i++){
            Row nameRow = sheet.createRow(4+i);
            Cell cellName = nameRow.createCell(0);
            String name =members.get(i).getName();
            cellName.setCellValue(name);


            for(int j =0; j < shift.getStartDate().lengthOfMonth();j++){
                String code = ShiftCodeResolver.getShiftCode(allShifts, j, members.get(i));
                nameRow.createCell(j+1).setCellValue(code);
            }
        }
        
        
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();

    
    }

    
}
