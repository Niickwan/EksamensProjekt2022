package com.jmmnt.UseCase;

import android.os.Environment;

import com.jmmnt.Entities.Questions;
import com.jmmnt.UseCase.Adapters.AdapterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CreateExcelFile {
    private OperateAssignment oA = new OperateAssignment();
    AdapterFactory apFac = new AdapterFactory();
    private WritableWorkbook workbook;

    public void createExcelSheet(String excelFileName, ArrayList<List<Object>> list) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File inputWorkbook = new File(path, excelFileName);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setEncoding("Cp1252");
        try {
            workbook = Workbook.createWorkbook(inputWorkbook, wbSettings);
            createFirstSheet(list);
//            createSecondSheet();
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFirstSheet(ArrayList<List<Object>> list) {
        try {
            ArrayList<String> excelTemplate = oA.getExcelAsArrayList("TjeklisteTemplate.xls");

            int rowCount = 0;
            int columnCount = 0;
            int getList = 0;
            WritableSheet sheet = workbook.createSheet("Ark1", 0);
            for (int i = 0; i < excelTemplate.size(); i++) {
                if (excelTemplate.get(i).equalsIgnoreCase("<Headline>")) {
                    int tempI = i;
                    while (!excelTemplate.get(tempI).equalsIgnoreCase("<Question>")) {
                        sheet.addCell(new Label(rowCount, columnCount, excelTemplate.get(tempI)));
                        rowCount++;
                        tempI++;
                    }
                    columnCount++;
                    rowCount = 0;
                    while (!excelTemplate.get(tempI).equalsIgnoreCase("<HeadlineEnd>")) {
//                        if (excelTemplate.get(tempI).equalsIgnoreCase("<inputGroup>")) return; // TODO FEJLSIKRING
                        for (int j = 0; j < list.get(getList).size(); j++) {
                            System.out.println(list.get(getList).get(j));
                            sheet.addCell(new Label(rowCount, columnCount, excelTemplate.get(tempI)));
                            rowCount++;
                            tempI++;
                        }

                        if (excelTemplate.get(tempI).equalsIgnoreCase("<Question>")) {
                            columnCount++;
                            rowCount = 0;
                        }
                    }
                    i = tempI;
                    columnCount++;
                    rowCount = 0;
                }
//                for (int j = 0; j < list.size(); j++) {
//                    sheet.addCell(new Label(rowCount, columnCount, ));
//                    for (int k = 0; k < list.get(i).size(); k++) {
//
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            List<Bean> listdata = new ArrayList();
//
//            listdata.add(new Bean("mr","firstName1","middleName1","lastName1"));
//            listdata.add(new Bean("mr","firstName1","middleName1","lastName1"));
//            listdata.add(new Bean("mr","firstName1","middleName1","lastName1"));
//            //Excel sheet name. 0 (number)represents first sheet
//            WritableSheet sheet = workbook.createSheet("Ark1", 0);
//            // column and row title
//            sheet.addCell(new Label(0, 0, "NameInitial"));
//            sheet.addCell(new Label(1, 0, "firstName"));
//            sheet.addCell(new Label(2, 0, "middleName"));
//            sheet.addCell(new Label(3, 0, "lastName"));
//
//            for (int i = 0; i < listdata.size(); i++) {
//                sheet.addCell(new Label(0, i + 1, listdata.get(i).getInitial()));
//                sheet.addCell(new Label(1, i + 1, listdata.get(i).getFirstName()));
//                sheet.addCell(new Label(2, i + 1, listdata.get(i).getMiddleName()));
//                sheet.addCell(new Label(3, i + 1, listdata.get(i).getLastName()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
