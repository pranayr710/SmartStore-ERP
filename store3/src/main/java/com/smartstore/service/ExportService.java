package com.smartstore.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.smartstore.model.Order;
import com.smartstore.model.Product;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public void exportProductsExcel(HttpServletResponse response) throws Exception {
        List<Product> products = productService.getAllProducts();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Products");

        int rowIdx = 0;
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Brand");
        header.createCell(3).setCellValue("Category");
        header.createCell(4).setCellValue("Price");
        header.createCell(5).setCellValue("Stock");
        header.createCell(6).setCellValue("Battery");
        header.createCell(7).setCellValue("Camera");
        header.createCell(8).setCellValue("Storage");
        header.createCell(9).setCellValue("Rating");

        for (Product p : products) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
            r.createCell(1).setCellValue(p.getName() != null ? p.getName() : "");
            r.createCell(2).setCellValue(p.getBrand() != null ? p.getBrand() : "");
            r.createCell(3).setCellValue(p.getCategory() != null ? p.getCategory() : "");
            r.createCell(4).setCellValue(p.getPrice() != null ? p.getPrice() : 0.0);
            r.createCell(5).setCellValue(p.getStock() != null ? p.getStock() : 0);
            r.createCell(6).setCellValue(p.getBattery() != null ? p.getBattery() : "");
            r.createCell(7).setCellValue(p.getCamera() != null ? p.getCamera() : "");
            r.createCell(8).setCellValue(p.getStorage() != null ? p.getStorage() : "");
            r.createCell(9).setCellValue(p.getRating() != null ? p.getRating() : 0.0);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        wb.close();
        os.flush();
    }

    public void exportOrdersExcel(HttpServletResponse response) throws Exception {
        List<Order> orders = orderService.getAllOrders();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Orders");

        int rowIdx = 0;
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Customer");
        header.createCell(2).setCellValue("Product");
        header.createCell(3).setCellValue("Qty");
        header.createCell(4).setCellValue("Total");
        header.createCell(5).setCellValue("Date");

        for (Order o : orders) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(o.getId() != null ? o.getId() : 0);
            r.createCell(1).setCellValue(o.getCustomerId() != null ? o.getCustomerId() : 0);
            r.createCell(2).setCellValue(o.getProductId() != null ? o.getProductId() : 0);
            r.createCell(3).setCellValue(o.getQuantity() != null ? o.getQuantity() : 0);
            r.createCell(4).setCellValue(o.getTotal() != null ? o.getTotal() : 0.0);
            r.createCell(5).setCellValue(o.getDate() != null ? o.getDate().toString() : "");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        wb.close();
        os.flush();
    }

    public void exportProductsPdf(HttpServletResponse response) throws Exception {
        List<Product> products = productService.getAllProducts();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=products.pdf");

        Document doc = new Document();
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();
        doc.add(new Paragraph("Products"));

        PdfPTable table = new PdfPTable(5);
        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Brand");
        table.addCell("Price");
        table.addCell("Stock");

        for (Product p : products) {
            table.addCell(String.valueOf(p.getId() != null ? p.getId() : 0));
            table.addCell(p.getName() != null ? p.getName() : "");
            table.addCell(p.getBrand() != null ? p.getBrand() : "");
            table.addCell(String.valueOf(p.getPrice() != null ? p.getPrice() : 0.0));
            table.addCell(String.valueOf(p.getStock() != null ? p.getStock() : 0));
        }

        doc.add(table);
        doc.close();
    }

    public void exportOrdersPdf(HttpServletResponse response) throws Exception {
        List<Order> orders = orderService.getAllOrders();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=orders.pdf");

        Document doc = new Document();
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();
        doc.add(new Paragraph("Orders"));

        PdfPTable table = new PdfPTable(5);
        table.addCell("ID");
        table.addCell("Customer");
        table.addCell("Product");
        table.addCell("Qty");
        table.addCell("Total");

        for (Order o : orders) {
            table.addCell(String.valueOf(o.getId() != null ? o.getId() : 0));
            table.addCell(String.valueOf(o.getCustomerId() != null ? o.getCustomerId() : 0));
            table.addCell(String.valueOf(o.getProductId() != null ? o.getProductId() : 0));
            table.addCell(String.valueOf(o.getQuantity() != null ? o.getQuantity() : 0));
            table.addCell(String.valueOf(o.getTotal() != null ? o.getTotal() : 0.0));
        }

        doc.add(table);
        doc.close();
    }
}
