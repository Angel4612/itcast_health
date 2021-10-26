package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class POITest {
    // 使用POI读取excel中的数据
    @Test
    public void test01() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("e:\\poi.xlsx")));

        XSSFSheet sheet = excel.getSheetAt(0);

        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }

        excel.close();

    }

    @Test
    public void test02() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("e:\\poi.xlsx")));

        XSSFSheet sheet = excel.getSheetAt(0);

        // 获取最后一行的行号(从0开始)
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            // 获取最后一个单元格
            short lastCellNum = row.getLastCellNum();
            for (short j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }

        excel.close();

    }

    @Test
    public void test03() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("e:\\poi.xlsx")));

        XSSFSheet sheet = excel.createSheet("传智播客");

        // 创建航, 0表示第一行
        XSSFRow row = sheet.createRow(0);
        // 创建单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("10");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小王");
        row2.createCell(2).setCellValue("20");


        //通过输出流将workbook对象下载到磁盘
        FileOutputStream out = new FileOutputStream("D:\\itcast.xlsx");
        excel.write(out);
        out.flush();
        out.close();

        excel.close();

    }
}
