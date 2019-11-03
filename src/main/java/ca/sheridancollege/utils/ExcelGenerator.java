package ca.sheridancollege.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import ca.sheridancollege.beans.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelGenerator {

    public static ByteArrayInputStream reportExcel(List<Rental> rentals,
                                                   List<Bike> bikes,
                                                   List<LockItem> locks,
                                                   List<Basket> baskets,
                                                   List<Customer> customers) throws IOException {

        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ){
            //CreationHelper createHelper = workbook.getCreationHelper();

            writeRentalsSheet(workbook, rentals);
            writeInventorySheet(workbook, bikes, locks, baskets);
            writeCustomersSheet(workbook, customers);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private static void writeRentalsSheet(Workbook workbook,
                                          List<Rental> rentals) {
        String[] COLUMNs = {"Id", "Comment", "Due Date", "Returned Date",
                "Sign Out Date", "Sheridan ID"};

        Sheet sheet = workbook.createSheet("Rental");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        // CellStyle for Age
//            CellStyle ageCellStyle = workbook.createCellStyle();
//            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

        int rowIdx = 1;
        for (Rental rental : rentals) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(Objects.toString(rental.getId(),
                    ""));
            row.createCell(1).setCellValue(Objects.toString(rental.getComment()));
            row.createCell(2).setCellValue(Objects.toString(dateFormatter(rental.getDueDate())));

            row.createCell(3).setCellValue(dateFormatter(rental.getReturnedDate()));
            row.createCell(4).setCellValue(dateFormatter(rental.getSignOutDate()));


            row.createCell(5).setCellValue(Objects.toString(rental.getCustomer().getSheridanId()));
//
//                Cell ageCell = row.createCell(3);
//                ageCell.setCellValue(rental.getAge());
//                ageCell.setCellStyle(ageCellStyle);
        }

    }

    private static void writeInventorySheet(Workbook workbook,
                                            List<Bike> bikes,
                                            List<LockItem> locks,
                                            List<Basket> baskets) {
        String[] COLUMNs = {"Id", "Item Name", "Notes", "State", "Image Path (Bike)",
                "Manufacturer (Bike)", "Model (Bike)", "Product Code (Bike)"
                , "Serial Number (Bike)", "Key Number (Key)"};

        Sheet sheet = workbook.createSheet("Inventory");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        for (Bike bike : bikes) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(bike.getId());
            row.createCell(1).setCellValue(Objects.toString(bike.getName()));
            row.createCell(2).setCellValue(Objects.toString(bike.getNotes()));
            row.createCell(3).setCellValue(Objects.toString(bike.getState().toString()));

            row.createCell(4).setCellValue(Objects.toString(bike.getImgPath()));
            row.createCell(5).setCellValue(Objects.toString(bike.getManufacturer()));
            row.createCell(6).setCellValue(Objects.toString(bike.getModel()));
            row.createCell(7).setCellValue(Objects.toString(bike.getProductCode()));
            row.createCell(8).setCellValue(Objects.toString(bike.getSerialNumber()));

        }

        for (LockItem lock: locks) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(lock.getId());
            row.createCell(1).setCellValue(Objects.toString(lock.getName()));
            row.createCell(2).setCellValue(Objects.toString(lock.getNotes()));
            row.createCell(3).setCellValue(Objects.toString(lock.getState().toString()));

            row.createCell(9).setCellValue(Objects.toString(lock.getKeyNum()));
        }

        for (Basket basket: baskets) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(basket.getId());
            row.createCell(1).setCellValue(Objects.toString(basket.getName()));
            row.createCell(2).setCellValue(Objects.toString(basket.getNotes()));
            row.createCell(3).setCellValue(basket.getState().toString());
        }

    }

    private static void writeCustomersSheet(Workbook workbook,
                                          List<Customer> customers) {
        String[] COLUMNs = {"Id", "First Name", "Last Name", "Address",
                "Signed Up On",
                "Emergency Contact (First Name)",
                "Emergency Contact (Last Name)", "Emergency Contact Phone",
                "End of Program", "Blacklisted",
                "Last Waiver Signed", "Waiver Expiration Date",
                "Notes", "Personal Email", "Sheridan Email",  "Phone",
                "Type", "Email Subscribed"};

        Sheet sheet = workbook.createSheet("Customer");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        for (Customer customer : customers) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(customer.getSheridanId());
            row.createCell(1).setCellValue(Objects.toString(customer.getFirstName()));
            row.createCell(2).setCellValue(Objects.toString(customer.getLastName()));
            row.createCell(3).setCellValue(Objects.toString(customer.getAddress()));
            row.createCell(4).setCellValue(dateFormatter(customer.getCreatedOn()));
            row.createCell(5).setCellValue(Objects.toString(customer.getEmergencyContactFirstName()));
            row.createCell(6).setCellValue(Objects.toString(customer.getEmergencyContactLastName()));
            row.createCell(7).setCellValue(Objects.toString(customer.getEmergencyContactPhone()));
            row.createCell(8).setCellValue(dateFormatter(customer.getEndOfProgram()));
            row.createCell(9).setCellValue(Objects.toString(customer.isBlackListed()));
            row.createCell(10).setCellValue(dateFormatter(customer.getLastWaiverSignedAt()));
            row.createCell(11).setCellValue(dateFormatter(customer.getWaiverExpirationDate()));
            row.createCell(12).setCellValue(Objects.toString(customer.getNotes()));
            row.createCell(13).setCellValue(Objects.toString(customer.getPersonalEmail()));
            row.createCell(14).setCellValue(Objects.toString(customer.getSheridanEmail()));
            row.createCell(15).setCellValue(Objects.toString(customer.getPhone()));
            row.createCell(16).setCellValue(Objects.toString(customer.getType().toString()));
            row.createCell(17).setCellValue(customer.isWillRecvEmail());
        }

    }

    public static String dateFormatter(LocalDate date) {

        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy" +
                    "-MM-dd");
            String formattedString = date.format(formatter);

            return formattedString;
        }
        return "";
    }
}
