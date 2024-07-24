package com.example.miniproj;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiniprojApplicationTests {

	@Test
	void contextLoads() {
	}

}








import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Request {
    String userId;
    String requestType;
    Date timestamp;

    public Request(String userId, String requestType, Date timestamp) {
        this.userId = userId;
        this.requestType = requestType;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequestType() {
        return requestType;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

public class ExcelProcessor {

    public static void main(String[] args) {
        String excelFilePath = "path/to/your/excel/file.xlsx"; // Update the path to your file
        List<Request> requests = readExcelFile(excelFilePath);
        
        List<String> userIds = requests.stream()
            .collect(Collectors.groupingBy(Request::getUserId, Collectors.maxBy(Comparator.comparing(Request::getTimestamp))))
            .values().stream()
            .filter(optionalRequest -> optionalRequest.isPresent() && "disable".equalsIgnoreCase(optionalRequest.get().getRequestType()))
            .map(optionalRequest -> optionalRequest.get().getUserId())
            .collect(Collectors.toList());

        System.out.println("User IDs with latest request type 'disable': " + userIds);
    }

    private static List<Request> readExcelFile(String excelFilePath) {
        List<Request> requests = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Skip the header row
            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                Row row = iterator.next();
                String userId = row.getCell(0).getStringCellValue();
                String requestType = row.getCell(1).getStringCellValue();
                Date timestamp = dateFormat.parse(row.getCell(2).getStringCellValue());

                requests.add(new Request(userId, requestType, timestamp));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return requests;
    }
}
