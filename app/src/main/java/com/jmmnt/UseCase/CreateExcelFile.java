package com.jmmnt.UseCase;

import android.os.Environment;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.Entities.TestingRCD;
import com.jmmnt.Entities.TransitionResistance;
import com.jmmnt.UseCase.Adapters.AdapterFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class CreateExcelFile {

    /**
     * This class is creating an excel document by getting objects from the UI controller.
     */

    private OperateAssignment oA = OperateAssignment.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    AdapterFactory apFac = new AdapterFactory();
    WritableSheet sheet;
    private WritableWorkbook workbook;
    // private WritableSheet sheet;
    private int rowCount = 0;
    private int columnCount = 0;
    int getList = 0;
    double questionSection = 1;
    String note;

    // Keep track of ExcelTemplate
    private int startPosition = 0;
    private int endPosition = 0;
    ArrayList<String> excelTemplate;

    public void createExcelSheet(String excelFileName, ArrayList<List<Object>> list, String documentNote) {
        excelTemplate = oA.getExcelAsArrayList("current_assignment.xls");
        note = documentNote;
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
            sheet = workbook.createSheet("Ark1", 0);
            for (int i = 0; i < excelTemplate.size(); i++) {
                if (isHeadlineOrStartTag() == 1) {
                    if (isEndTag()) {
                        // HEADLINE
                        for (int j = startPosition; j <= endPosition; j++) {
                            sheet.addCell(new Label(columnCount, rowCount, excelTemplate.get(j)));
                            columnCount++;
                        }
                        columnCount = 0;
                        rowCount++;
                        if (list.size() >getList && list.get(getList).size() > 0) {
                            if (list.get(getList).get(0) instanceof Questions) {
                                writeQuestions(list, sheet);
                                sheet.addCell(new Label(columnCount, rowCount, "<HeadlineEnd>"));
                            } else if (list.get(getList).get(0) instanceof CircuitDetails) {
                                writeCircuitDetails(list, sheet);
                                sheet.addCell(new Label(columnCount, rowCount, "<InputHeadlineEnd>"));
                            } else if (list.get(getList).get(0) instanceof ShortCircuitCurrentAndVoltageDrop) {
                                writeShortCircuitCurrent(list, sheet);

                                for (int j = 0; j < 2; j++) {
                                    startPosition++;
                                    endPosition++;
                                    if (isHeadlineOrStartTag() == 1 || isHeadlineOrStartTag() == 2) {
                                        if (isEndTag()) {
                                            // HEADLINE
                                            for (int k = startPosition; k <= endPosition; k++) {
                                                sheet.addCell(new Label(columnCount, rowCount, excelTemplate.get(k)));
                                                columnCount++;
                                            }
                                            columnCount = 0;
                                            rowCount++;
                                        }
                                    }
                                }
                                writeVoltageDrop(list, sheet);
                                sheet.addCell(new Label(columnCount, rowCount, "<InputHeadlineEnd>"));
                            } else if (list.get(getList).get(0) instanceof TestingRCD) {
                                writeTestingRCD(list, sheet);
                                sheet.addCell(new Label(columnCount, rowCount, "<InputHeadlineEnd>"));
                            }
                        }
                        columnCount = 0;
                        rowCount++;
                        startPosition++;
                        endPosition++;
                    }
                } else if (isHeadlineOrStartTag() == 2) {
                    if (isEndTag()) {
                        // START TAG
                        for (int j = startPosition; j <= endPosition; j++) {
                            sheet.addCell(new Label(columnCount, rowCount, excelTemplate.get(j)));
                            columnCount++;
                        }
                        columnCount = 0;
                        rowCount++;
                        startPosition++;
                        endPosition++;
                    }
                } else if (isHeadlineOrStartTag() == 3) {
                    if (isEndTag()) {
                        if (list.get(getList).get(0) instanceof TransitionResistance) {
                            writeTransitionResistance(list, sheet);
                            columnCount = 0;
                            startPosition++;
                            endPosition++;
                        }
                    }
                }
            }
            sheet.addCell(new Label(columnCount, rowCount, "<Document Note>"));
            columnCount++;
            sheet.addCell(new Label(columnCount, rowCount, note));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTestingRCD(ArrayList<List<Object>> list, WritableSheet sheet) {
        try {
            for (int i = 0; i < list.get(getList).size(); i++) {
                int ok = ((TestingRCD) list.get(getList).get(i)).getCheckboxOK();
                String okStr = String.valueOf(ok);
                sheet.addCell(new Label(columnCount, rowCount, "<inputAnswer>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getGroupName()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getFirstResult()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getSecondResult()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getThirdResult()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getFourthResult()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getFifthResult()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((TestingRCD) list.get(getList).get(i)).getSixthResult()));
                columnCount++;

                if (okStr.equals("1")) sheet.addCell(new Label(columnCount, rowCount, "ok"));
                else sheet.addCell(new Label(columnCount, rowCount, "-1"));

            //    if (okStr.equals("1")){
              //      sheet.addCell(new Label(columnCount, rowCount, "ok")); //TODO se her
                //}


                columnCount = 0;
                rowCount++;
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }
        getList++;
    }

    private void writeVoltageDrop(ArrayList<List<Object>> list, WritableSheet sheet) {
        try {
            for (int i = 0; i < list.get(getList).size(); i++) {
                sheet.addCell(new Label(columnCount, rowCount, "<inputAnswer>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount,
                        ((ShortCircuitCurrentAndVoltageDrop) list.get(getList).get(i)).getVoltageDropGroupName()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount,
                        ((ShortCircuitCurrentAndVoltageDrop) list.get(getList).get(i)).getVoltageDropDeltaVoltage()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount,
                        ((ShortCircuitCurrentAndVoltageDrop) list.get(getList).get(i)).getVoltageDropMeasuredOnLocation()));
                columnCount = 0;
                rowCount++;
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }
        getList++;
    }

    private void writeShortCircuitCurrent(ArrayList<List<Object>> list, WritableSheet sheet) {
        try {
            for (int i = 0; i < list.get(getList).size(); i++) {
                sheet.addCell(new Label(columnCount, rowCount, "<inputAnswer>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount,
                        ((ShortCircuitCurrentAndVoltageDrop) list.get(getList).get(i)).getShortCircuitGroupName()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount,
                        ((ShortCircuitCurrentAndVoltageDrop) list.get(getList).get(i)).getShortCircuitLk()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount,
                        ((ShortCircuitCurrentAndVoltageDrop) list.get(getList).get(i)).getShortCircuitMeasuredOnLocation()));
                columnCount = 0;
                rowCount++;
            }
            sheet.addCell(new Label(columnCount, rowCount, "<InputHeadlineEnd>"));
            rowCount++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private void writeTransitionResistance(ArrayList<List<Object>> list, WritableSheet sheet) {
        try {
            for (int i = 0; i < list.get(getList).size(); i++) {
                double resist = ((TransitionResistance) list.get(getList).get(i)).getTransitionResistance();
                String res = String.valueOf(resist);
                sheet.addCell(new Label(columnCount, rowCount, "<SingleInput>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "Overgangsmodstand for jordingsleder og jordelektrode R:"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, res));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "Ω"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "<SingleInputEnd>"));
                columnCount = 0;
                rowCount++;
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }
        getList++;
    }

    private void writeCircuitDetails(ArrayList<List<Object>> list, WritableSheet sheet) {
        try {
            for (int i = 0; i < list.get(getList).size(); i++) {
                sheet.addCell(new Label(columnCount, rowCount, "<inputAnswer>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getGroupName()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getOb()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getCharacteristics()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getCrossSection()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getMaxOB()));
                columnCount++;
                if (((CircuitDetails) list.get(getList).get(i)).getCheckbox() == 1) {
                    sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getOmega()));
                    columnCount++;
                    sheet.addCell(new Label(columnCount, rowCount, "-1"));
                    columnCount++;
                } else if  (((CircuitDetails) list.get(getList).get(i)).getCheckbox() == 2){
                    sheet.addCell(new Label(columnCount, rowCount, "-1"));
                    columnCount++;
                    sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getOmega()));
                    columnCount++;
                }  else {
                    sheet.addCell(new Label(columnCount, rowCount, "-1"));
                    columnCount++;
                    sheet.addCell(new Label(columnCount, rowCount, "-1"));
                    columnCount++;
                }
                sheet.addCell(new Label(columnCount, rowCount, ((CircuitDetails) list.get(getList).get(i)).getMilliOmega()));
                columnCount = 0;
                rowCount++;
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }
        getList++;
    }

    private void writeQuestions(ArrayList<List<Object>> list, WritableSheet sheet) {
        int answer;
        try {
            for (int i = 0; i < list.get(getList).size(); i++) {
                answer = ((Questions) list.get(getList).get(i)).getAnswer();
                questionSection = questionSection + 0.1;
                sheet.addCell(new Label(columnCount, rowCount, "<Question>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, String.valueOf(gUC.round(questionSection, 1))));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((Questions) list.get(getList).get(i)).getQuestion()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "<QuestionAnwsered>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, (String.valueOf(answer))));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "<Note>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, ((Questions) list.get(getList).get(i)).getComment()));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "<Images>"));
                columnCount++;
              if (((Questions) list.get(getList).get(i)).getImages().size() > 0) {
                   for (int k = 0; k < ((Questions) list.get(getList).get(i)).getImages().size(); k++) {
                       sheet.addCell(new Label(columnCount, rowCount,
                                ((Questions) list.get(getList).get(i)).getImages().get(k)));
                            columnCount++;
                        }
                    } else {
                    sheet.addCell(new Label(columnCount, rowCount, "-1"));
                    columnCount++;
                }
                ((Questions) list.get(getList).get(i)).getImages().forEach(System.out::println);
                sheet.addCell(new Label(columnCount, rowCount, "<ImagesEnd>"));
                columnCount++;
                sheet.addCell(new Label(columnCount, rowCount, "<QuestionEnd>"));
                columnCount = 0;
                System.out.println(getList + "--------------------------------" + rowCount);
                rowCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getList++;
        questionSection = gUC.round(questionSection, 0);
        questionSection++;
    }

    private int isHeadlineOrStartTag() {
        // 1 = Headline - 2 = StartTag
        for (int i = startPosition; i < excelTemplate.size(); i++) {
            if (excelTemplate.get(i).equalsIgnoreCase("<Headline>")) {
                startPosition = i;
                return 1;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<SingleInput>")) {
                startPosition = i;
                return 3;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<inputGroup>")) {
                startPosition = i;
                return 1;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<Document Note>")) {
                startPosition = i;
                return 1;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<InputHeadline>")) {
                startPosition = i;
                return 2;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<InputUnderHeadline>")) {
                startPosition = i;
                return 2;
            }
        }
        return -1;
    }

    private boolean isEndTag() {
        for (int i = endPosition; i < excelTemplate.size(); i++) {
            if (excelTemplate.get(i).equalsIgnoreCase("<QuestionOptionsEnd>")) {
                endPosition = i;
                return true;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<SingleInputEnd>")) {
                endPosition = i;
                return true;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<InputUnderHeadlineEnd>")) {
                endPosition = i;
                return true;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<inputGroupEnd>")) {
                endPosition = i;
                return true;
            }
            else if (excelTemplate.get(i).equalsIgnoreCase("<End>")) {
                endPosition = i;
                return true;
            }
        }
        return false;
    }

}
