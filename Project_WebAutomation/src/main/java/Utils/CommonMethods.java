package Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CommonMethods {

    public List<Object[]> getTestData(String filePath) throws IOException {
        List<Object[]> dataList = new ArrayList<>();
        FileInputStream file = new FileInputStream(filePath);
        Workbook wb = new XSSFWorkbook(file);
        Sheet sheet = wb.getSheetAt(0);

        Iterator<Row> row =sheet.iterator();
        row.next();

        while(row.hasNext()) {
            Row r = row.next();
            String firstName = r.getCell(0).getStringCellValue();
            String lastName = r.getCell(1).getStringCellValue();
            String emailId = r.getCell(2).getStringCellValue();
            String gender = r.getCell(3).getStringCellValue();
            String mobileNumber = r.getCell(4).getStringCellValue();
            String monthDOB = r.getCell(5).getStringCellValue();
            String yearDOB = r.getCell(6).getStringCellValue();
            String dayDOB = r.getCell(7).getStringCellValue();
            String subjectCell = r.getCell(8).getStringCellValue();
            List<String> subjects = Arrays.asList(subjectCell.split(", "));
            String hobbiesCell = r.getCell(9).getStringCellValue();
            List<String> hobbies = Arrays.asList(hobbiesCell.split(",\\s*"));
            String address = r.getCell(10).getStringCellValue();
            String state = r.getCell(11).getStringCellValue();
            String city = r.getCell(12).getStringCellValue();

            dataList.add(new Object[] {firstName, lastName, emailId, gender, mobileNumber,monthDOB, yearDOB, dayDOB,
                    subjects, hobbies, address, state, city});
        }
        wb.close();
        file.close();

        return dataList;
    }
}