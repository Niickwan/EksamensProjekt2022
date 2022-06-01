package com.jmmnt.UseCase;

import android.os.Environment;

import com.jmmnt.Entities.CircuitDetails;
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

    private OperateAssignment oA = OperateAssignment.getInstance();
    private GeneralUseCase gUc = GeneralUseCase.getInstance();

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
            ArrayList<String> skipHeadline = new ArrayList<>();
            StringBuilder headlineString;
            ArrayList<String> headlines = new ArrayList<>();
            WritableSheet sheet = workbook.createSheet("Ark1", 0);

            // Get headlines from excel
            for (int i = 0; i < excelTemplate.size(); i++) {
                if (excelTemplate.get(i).equalsIgnoreCase("<Headline>")) {
                    headlineString = new StringBuilder();
                    headlineString.append(excelTemplate.get(i));
                    headlineString.append("?");
                    int tempI = i;
                    while (!excelTemplate.get(tempI).equalsIgnoreCase("<QuestionOptionsEnd>")) {
                        tempI++;
                        headlineString.append(excelTemplate.get(tempI));
                        headlineString.append("?");
                    }
                    headlines.add(headlineString.toString());
                    i = tempI;
                }
//                if (excelTemplate.get(i).equalsIgnoreCase("<InputHeadline>")) {
//                    int tempI = i;
//                    headlineString = new StringBuilder();
//                    while (!excelTemplate.get(tempI).equalsIgnoreCase("<inputGroup>")) {
//                        headlineString.append(excelTemplate.get(tempI));
//                        headlineString.append("?");
//                        tempI++;
//                    }
//                    headlines.add(headlineString.toString());
//                    i = tempI;
//                }
//                if (excelTemplate.get(i).equalsIgnoreCase("<inputGroup>")) {
//                    int tempI = i;
//                    headlineString = new StringBuilder();
//                    while (!excelTemplate.get(tempI).equalsIgnoreCase("<inputAnswer>")) {
//                        headlineString.append(excelTemplate.get(tempI));
//                        headlineString.append("?");
//                        tempI++;
//                    }
//                    headlines.add(headlineString.toString());
//                    i = tempI;
//                }
            }
            headlines.forEach(System.out::println);
            double questionSection;
            int anwser;

            // Print Headlines
            for (int i = 0; i < headlines.size(); i++) {
                String[] splittedHeadline = gUc.splitStringBy(headlines.get(i), "?");
                for (int j = 0; j < splittedHeadline.length; j++) {
                    sheet.addCell(new Label(columnCount, rowCount, splittedHeadline[j]));
                    columnCount++;
                }
                if (splittedHeadline[0].equalsIgnoreCase("<InputHeadline>")) {
                    headlines.remove(i);
                    i = i-1;
                }
                columnCount = 0;
                rowCount++;

                questionSection = i+1;
                for (int j = 0; j < list.get(i).size(); j++) {
                    if (list.get(getList).get(j) instanceof Questions) {
                        anwser = ((Questions) list.get(getList).get(j)).getAnswer();
                        questionSection = questionSection + 0.1;
                        sheet.addCell(new Label(columnCount, rowCount, "<Question>"));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, String.valueOf(round(questionSection, 1))));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, ((Questions) list.get(getList).get(j)).getQuestion()));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, "<QuestionAnwsered>"));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, (String.valueOf(anwser))));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, "<Note>"));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, ((Questions) list.get(getList).get(j)).getComment()));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, "<Images>"));
                        columnCount++;
                        if (((Questions) list.get(getList).get(j)).getImages().size() > 0) {
                            for (int k = 0; k < ((Questions) list.get(getList).get(j)).getImages().size(); k++) {
                                sheet.addCell(new Label(columnCount, rowCount, ((Questions) list.get(getList).get(j)).getImages().get(k)));
                                columnCount++;
                            }
                        } else {
                            sheet.addCell(new Label(columnCount, rowCount, "-1"));
                            columnCount++;
                        }
                        sheet.addCell(new Label(columnCount, rowCount, "<ImagesEnd>"));
                        sheet.addCell(new Label(columnCount, rowCount, "<QuestionEnd>"));
                        columnCount = 0;
                        rowCount ++;
                    }
                    if (list.get(getList).get(j) instanceof CircuitDetails) {
                        sheet.addCell(new Label(columnCount, rowCount, "<inputAnswer>"));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(j)).getOb()));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(j)).getCharacteristics()));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(j)).getCrossSection()));
                        columnCount++;
                        sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(j)).getMaxOB()));
                        columnCount++;
                        int checkBox = ((CircuitDetails) list.get(getList).get(j)).getCheckbox();
                        if (checkBox == 1) {
                            sheet.addCell(new Label(columnCount, rowCount, "1"));
                            columnCount++;
                            sheet.addCell(new Label(columnCount, rowCount, "2"));
                            columnCount++;
                        } else {
                            sheet.addCell(new Label(columnCount, rowCount, "2"));
                            columnCount++;
                            sheet.addCell(new Label(columnCount, rowCount, "1"));
                            columnCount++;
                        }
                        sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(j)).getMegaOmega()));
                        columnCount++;
                    }
                    if (splittedHeadline[0].equalsIgnoreCase("<Headline>")) {
                        sheet.addCell(new Label(columnCount, rowCount, "<HeadlineEnd>"));
                    }
                }
                getList++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
