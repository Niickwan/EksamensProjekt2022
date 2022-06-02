package com.jmmnt.UseCase.PDFGeneration;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.R;
import com.jmmnt.UseCase.OperateAssignment;
import org.apache.commons.io.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PDFGenerator {

    private Assignment assignment;
    private OperateAssignment opa = OperateAssignment.getInstance();
    private Table table;
    private Table noteTable;
    public PDFGenerator(Assignment assignment) {
        this.assignment = assignment;
    }

    public void createPDF(Context context) throws IOException {
        ArrayList<String> excel = opa.getExcelAsArrayList("sl.xls");

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "abcPDFff.pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        //Documents comes from IText.layout and is used to create tables, cells and other layout
        //related items in the pdf document. It takes a Pdf document as a parameter
        Document document = new Document(pdfDocument, PageSize.A4);
        //initialize notetable
        noteTable = new Table(getWidthMatchingPageSize(1,document)).useAllAvailableWidth();

        InputStream inputStreamArial = context.getAssets().open("font/arial.ttf");
        byte[] bytesArial = IOUtils.toByteArray(inputStreamArial);
        FontProgram createArial = FontProgramFactory.createFont(bytesArial);
        PdfFont fontArial = PdfFontFactory.createFont(createArial, PdfEncodings.IDENTITY_H,true);
        document.setFont(fontArial);

        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, new IEventHandler() {
            @Override
            public void handleEvent(Event event) {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfPage page = docEvent.getPage();
                int pageNumber = docEvent.getDocument().getPageNumber(page);
                PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);
                //CREATING BACKGROUND COLOR AFTER A NEW PAGE IS STARTED
                Rectangle rect = page.getPageSize();
                canvas.saveState().setFillColor(ColorConstants.YELLOW)
                        .rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight())
                        .fillStroke().restoreState();
                //CREATING HEADER AFTER A NEW PAGE IS STARTED
                new Canvas(canvas,pdfDocument, new Rectangle(document.getLeftMargin(), page.getPageSize().getHeight()-125, page.getPageSize().getWidth() - document.getLeftMargin()*2, 100))
                        .add(pDFHeader(context,pageNumber+"",pdfDocument, document))
                        .close();
            }
        });
        //SETS THE PAGE TO 100 SO THE HEADER DOES NOT OVERLAP START OF DOCUMENT
        document.setTopMargin(100);

        //ADDS ASSIGNMENT INFO TABLE AT THE TOP OF FIRST PAGE
        document.add(createAssignmentInfoTable(assignment,document));
        document.add(createParagraph("\n"));

        //ALGORITHM FOR READING EXCELSHEET
        insertExcelHeadlineAlgorithm(excel, document, context);

        //ALGORITHM FOR READING EXCELSHEET
        insertExcelInputHeadlineAlgorithm(excel, document,fontArial);

        //INSERTS DOCUMENT NOTE FROM EXCELSHEET INTO noteTable
        insertExcelDocumentNoteToNoteTable(excel);

        //METHOD FOR CREATING NOTE TABLE AT BOTTOM OF LAST PAGE
        document.add(noteTable);


        document.close();
        writer.close();
        pdfDocument.close();
    }

    public void insertExcelDocumentNoteToNoteTable(ArrayList<String> excel){
        if(!excel.get(excel.size()-1).equals("-1")){
            noteTable.addCell(createParagraph("General Bemærkning: \n" + excel.get(excel.size()-1))); //TODO string
        }
        else{
            noteTable.addCell(createParagraph("General Bemærkning: \n")); //TODO string
        }
    }

//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ INSERT HEADLINE TAG METHODS ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓v

    public void insertExcelHeadlineAlgorithm(ArrayList<String> excel, Document document, Context context){
        for (int j = 0; j < excel.size(); j++) {
            if (excel.get(j).equals("<Headline>")){

                String[] choices = new String[Integer.parseInt(excel.get(j+3))];

                for (int i = j; i < excel.size(); i++) {
                    if (excel.get(i).equals("<QuestionOptions>")) {
                        readExcelQuestionOptions(excel,i,j,choices,document);
                    }
                    else if (excel.get(i).equals("<Question>")){
                        readExcelQuestion(excel, document, context, choices, i);
                    }
                    else if (excel.get(i).equals("<HeadlineEnd>")){
                        j = i;
                        break;
                    }
                }
                document.add(createParagraph("\n"));
            }
        }
    }
    private void readExcelQuestion(ArrayList<String> excel, Document document, Context context, String[] choices, int i) {
        int answer = Integer.parseInt(excel.get(i +4));
        if (answer == -1){
            answer = 3;
        }
        readExcelNotesAndImages(excel, document, i);

        document.add(createMultipleChoiceTable(excel.get(i +1) + " " + excel.get(i +2), choices.length,
                answer,getColumnWidths(table), context));

    }

    private void readExcelNotesAndImages(ArrayList<String> excel, Document document, int i) {
        if (!excel.get(i +6).equals("-1") && !excel.get(i +8).equals("-1")){
            noteTable.addCell(createCell(excel.get(i +1) + ": " + excel.get(i +6), document,false).setBorderBottom(Border.NO_BORDER));
            int excelRowCounter = 8;
            int pictureCounter = 1;
            while(!excel.get(i + excelRowCounter).equals("<ImagesEnd>")) {

                noteTable.addCell(createCell("Billede " + pictureCounter + ": " +
                        excel.get(i + 8), document, true).setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));

                pictureCounter++;
                excelRowCounter++;

            }
        }
        else if (!excel.get(i +8).equals("-1")){
            int excelRowCounter = 8;
            int pictureCounter = 1;
            while(!excel.get(i + excelRowCounter).equals("<ImagesEnd>")) {
                noteTable.addCell(createCell(excel.get(i +1) + ":\nBillede " + pictureCounter + ": " + //TODO string
                        excel.get(i +8), document,true).setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER));
                pictureCounter++;
                excelRowCounter++;
            }
        }
    }

    public void readExcelQuestionOptions(ArrayList<String> excel, int i,int j, String[] choices, Document document){
        int excelRowCounter = 1;
        int choicesIndexCounter = 0;
        while (!excel.get(i + excelRowCounter).equals("<QuestionOptionsEnd>")) {
            choices[choicesIndexCounter] = excel.get(i + excelRowCounter);
            excelRowCounter++;
            choicesIndexCounter++;
        }
        table = createMultipleChoiceHeaderTable(excel.get(j+1), choices).useAllAvailableWidth();
        document.add(table);
    }
    public Table createMultipleChoiceHeaderTable(String header, String[] choices){
        float[] rows = new float[1+choices.length];
        //setting first row width to 350. this is the column where the headline will be placed.
        rows[0] = 400;
        //setting the rest of the rows width to 45. these are the rows where the name of the
        //choices will be placed
        for (int i = 1; i < rows.length; i++) {
            rows[i] = 35;
        }
        //creating a Table, inserting the column array as parameter in Table constructor.
        Table table = new Table(rows).useAllAvailableWidth();
        //adds a new cell with a paragraph taking the header string as parameter on row 1, column 1.
        //sets the paragraph to bold and removes the border around the cell.
        table.addCell(new Cell(0,1).add(createParagraph(header).setBold()).setBorder(Border.NO_BORDER));
        //adds new cells with the name of the choices from row 1 and up.
        for (int i = 0; i < choices.length; i++) {
            table.addCell(new Cell(1+i,1)
                    .add(createParagraph(choices[i]).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        }
        return table;
    }

    public Table createMultipleChoiceTable(String question, int numberOfChoices, int answer, float[] rows, Context context){
        Table table = new Table(rows).useAllAvailableWidth();
        table.addCell(new Cell(0,1).add(createParagraph(question)).setBorder(Border.NO_BORDER));
        for (int i = 0; i < numberOfChoices; i++) {
            if(i+1 == answer) {
                table.addCell(createCheckbox(true, context));
            }
            else{
                table.addCell(createCheckbox(false, context).setBorder(Border.NO_BORDER));
            }
        }
        return table;
    }

    //^^^^^^^^^^^^^^^^^^^^^^^INSERT HEADLINE TAG METHODS^^^^^^^^^^^^^^^^^^^^^^^^^

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓INSERT INPUT HEADLINE TAG METHODS ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public void insertExcelInputHeadlineAlgorithm(ArrayList<String> excel, Document document, PdfFont font) {

        for (int j = 0; j < excel.size(); j++) {
            if (excel.get(j).equals("<InputHeadline>")) {
                String[] numberOfQuestions = new String[Integer.parseInt(excel.get(j + 3))];
                String[] measuringUnits = new String[numberOfQuestions.length];
                Table table = new Table(getWidthMatchingPageSize(numberOfQuestions.length, document)).useAllAvailableWidth();
                table.addCell(new Cell(0, numberOfQuestions.length)
                        .add(createParagraph(excel.get(j + 1))).setBackgroundColor(ColorConstants.GRAY));

                for (int i = j; i < excel.size(); i++) {
                    if (excel.get(i).equals("<inputGroup>")) {
                        readExcelInputGroup(excel, document, numberOfQuestions, measuringUnits, table, i);

                    } else if (excel.get(i).equals("<InputUnderHeadline>")) {
                        readExcelInputUnderHeadline(excel, document, numberOfQuestions, table, i);

                    } else if (excel.get(i).equals("<inputAnswer>")) {
                        readExcelInputAnswer(excel, document, numberOfQuestions, measuringUnits, table, i);

                    } else if (excel.get(i).equals("<InputHeadlineEnd>")) {
                        j = i;
                        break;
                    }
                }
                document.add(table);
                document.add(createParagraph("\n"));

            } else if (excel.get(j).equals("<SingleInput>")) {
                readExcelSingleInput(excel, document, j);
            }

        }

    }

    private void readExcelSingleInput(ArrayList<String> excel, Document document, int j) {
        Table table = new Table(getWidthMatchingPageSize(1, document)).useAllAvailableWidth();
        if (excel.get(j + 2).equals("-1")){
            table.addCell(createCell(excel.get(j + 1), document, false));
        }
        else{
            table.addCell(createCell(excel.get(j + 1) + " " + excel.get(j + 2) + " " + excel.get(j + 3), document, false));
        }
        document.add(table);
        document.add(createParagraph("\n"));
    }

    private void readExcelInputAnswer(ArrayList<String> excel, Document document, String[] numberOfQuestions, String[] measuringUnits, Table table, int i) {
        for (int k = 0; k < numberOfQuestions.length; k++) {
            String measuringUnit = measuringUnits[k];
            String answer = excel.get(i + 1);

            if (answer.equals("-1")) {
                table.addCell(createCell("\n", document,false));
            } else {
                if (measuringUnit.equals("<>")) {
                    table.addCell(createCell(answer, document, false));
                } else {
                    table.addCell(createCell(answer + " " + measuringUnit, document, false));
                }
            }
        }
    }

    private void readExcelInputUnderHeadline(ArrayList<String> excel, Document document, String[] numberOfQuestions, Table table, int i) {
        int excelRowCounter = 0;
        int nextIndex = 0;
        while (excelRowCounter != numberOfQuestions.length) {
            int columnspan = Integer.parseInt(excel.get(i + 2 + nextIndex));
            String answer = excel.get(i + 1 + nextIndex);
            if (answer.equals("<>")) {
                table.addCell(createCell("", document, 1, columnspan, false));
                nextIndex += 2;
                excelRowCounter += columnspan;
            } else {
                table.addCell(createCell(excel.get(i + 1 + nextIndex), document, 1, columnspan, false));
                nextIndex += 2;
                excelRowCounter += columnspan;
            }
        }
    }

    private void readExcelInputGroup(ArrayList<String> excel, Document document, String[] numberOfQuestions, String[] measuringUnits, Table table, int i) {
        int counter = 0;
        for (int k = 0; k < numberOfQuestions.length; k++) {
            table.addCell(createCell(excel.get(i + 1 + counter), document, false));
            measuringUnits[k] = excel.get(i + 2 + counter);
            counter += 2;

        }
    }

    //^^^^^^^^^^^^^^^^^^^^^^^^INSERT INPUT HEADLINE TAG METHODS^^^^^^^^^^^^^^^^^^^^^^^^^


    public Paragraph createParagraph(String s){
        return new Paragraph(s);
    }

    public Paragraph createLinkParagraph(String s){
        Paragraph p = new Paragraph();
        Link link = new Link(s, PdfAction.createURI(s));
        p.add(link);
        return p;
    }

    public Cell createCell(String input, Document document, boolean linked){
        if(linked) {
            return new Cell().add(createLinkParagraph(input))
                    .setMaxWidth(document.getPageEffectiveArea(PageSize.A4).getWidth()
                            - (document.getRightMargin() + document.getLeftMargin()));
        }
        else{
            return new Cell().add(createLinkParagraph(input))
                    .setMaxWidth(document.getPageEffectiveArea(PageSize.A4).getWidth()
                            - (document.getRightMargin() + document.getLeftMargin()));
        }
    }

    public Cell createCell(String input,Document document, int rowspan, int columnspan, boolean linked){
        if(linked) {
            return new Cell(rowspan, columnspan).add(createLinkParagraph(input))
                    .setMaxWidth(document.getPageEffectiveArea(PageSize.A4).getWidth()
                            - (document.getRightMargin() + document.getLeftMargin()));
        }
        else{
            return new Cell(rowspan, columnspan).add(createLinkParagraph(input))
                    .setMaxWidth(document.getPageEffectiveArea(PageSize.A4).getWidth()
                            - (document.getRightMargin() + document.getLeftMargin()));
        }
    }

    //NOT GENERAL
    public Table pDFHeader(Context context, String pageNumber, PdfDocument pdfDocument, Document document){
        Drawable logo = context.getDrawable(R.drawable.zealand_company_logo_with_subheading);
        Image logoImage = createImage(logo);
        Table table = new Table(getWidthMatchingPageSize(3,document)).useAllAvailableWidth();

        table.addCell(new Cell(2,1).add(logoImage));
        table.addCell(new Cell(2,1).add(createParagraph("TJEKLISTE").setTextAlignment(TextAlignment.CENTER) //TODO string
                .setVerticalAlignment(VerticalAlignment.TOP).setFontColor(ColorConstants.ORANGE).setFontSize(25f)));
        table.addCell(new Cell().add(createParagraph("side: " + pageNumber))); //TODO string
        table.addCell(new Cell().add(createParagraph("Elinstallation"))); //TODO string
        return table;
    }

    public float[] getWidthMatchingPageSize(int divider, Document document){
        float width = (document.getPageEffectiveArea(PageSize.A4).getWidth() - document.getRightMargin()) / divider;
        float[] widths = new float[divider];
        Arrays.fill(widths, width);
        return widths;
    }

    //NOT GENERAL
    public Table createAssignmentInfoTable(Assignment assignment, Document document){
        Table table = new Table(getWidthMatchingPageSize(3,document)).useAllAvailableWidth();

        table.addCell(new Cell(1,3).add
                (createParagraph("Kundenavn: " + assignment.getCustomerName()))); //TODO string

        table.addCell(new Cell(1,3).add
                (createParagraph("Adresse: " + assignment.getAddress()))); //TODO string

        table.addCell(new Cell().add
                (createParagraph("Post nr.: " + assignment.getPostalCode()))); //TODO string

        table.addCell(new Cell().add
                (createParagraph("By: " + opa.getCityMatchingZipCode(assignment.getPostalCode())))); //TODO string

        table.addCell(new Cell().add
                (createParagraph("Ordrenummer: " + assignment.getOrderNumber()))); //TODO string

        return table;
    }

    public Image createImage(Drawable drawable){
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 175, 50, true);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        byte[] BitmapData = outStream.toByteArray();
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageData imageData = ImageDataFactory.create(BitmapData);
        return new Image(imageData);
    }


    public float[] getColumnWidths(Table table){
        float[] columnWidths = new float[table.getNumberOfColumns()];
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            columnWidths[i] = table.getColumnWidth(i).getValue();
        }
        return columnWidths;
    }

    public Cell createCheckbox(boolean onOrOff, Context context) {
        Table checkbox = new Table(1);
        checkbox.setHeight(13f);
        checkbox.setWidth(13f);
        if(onOrOff){
            checkbox.addCell(new Cell().add(createTick(context)).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            return new Cell().add(checkbox.setHorizontalAlignment(HorizontalAlignment.CENTER)).setBorder(Border.NO_BORDER);
        }
        checkbox.addCell(new Cell().setHorizontalAlignment(HorizontalAlignment.CENTER));
        return new Cell().add(checkbox.setHorizontalAlignment(HorizontalAlignment.CENTER)).setBorder(Border.NO_BORDER);
    }

    public Image createTick(Context context) {
        Drawable drawable = context.getDrawable(R.drawable.tick);

        Image tick = createImage(drawable);
        tick.setHeight(8f);
        tick.setWidth(8f);
        tick.setHorizontalAlignment(HorizontalAlignment.CENTER);

        return tick;
    }
}