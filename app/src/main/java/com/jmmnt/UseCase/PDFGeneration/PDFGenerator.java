package com.jmmnt.UseCase.PDFGeneration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.R;
import com.jmmnt.UseCase.OperateAssignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PDFGenerator {
    Assignment assignment;
    OperateAssignment opa = new OperateAssignment();
    Table table = null;

    public PDFGenerator(Assignment assignment) {
        this.assignment = assignment;
    }

    public void createPDF(Context context) throws FileNotFoundException {
        OperateAssignment oPA = new OperateAssignment();
        ArrayList<String> excel = oPA.getExcelAsArrayList("lllll.xls");

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "abcPDFff.pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        //Documents comes from IText.layout and is used to create tables, cells and other layout
        //related items in the pdf document. It takes a Pdf document as a parameter
        Document document = new Document(pdfDocument, PageSize.A4);
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
        document.add(new Paragraph("\n"));

        //ALGORITHM FOR READING EXCELSHEET
        insertExcelHeadlineAlgorithm(excel, document, context);

        document.close();
    }

    public void insertExcelHeadlineAlgorithm(ArrayList<String> excel, Document document, Context context){
        for (int j = 0; j < excel.size(); j++) {
            if (excel.get(j).equals("<Headline>")){

                String[] choices = new String[Integer.parseInt(excel.get(j+3))];

                for (int i = j; i < excel.size(); i++) {
                    if (excel.get(i).equals("<QuestionOptions>")) {
                        int excelRowCounter = 1;
                        int choicesIndexCounter = 0;
                        while (!excel.get(i + excelRowCounter).equals("<QuestionOptionsEnd>")) {
                            choices[choicesIndexCounter] = excel.get(i + excelRowCounter);
                            excelRowCounter++;
                            choicesIndexCounter++;
                        }
                        table = createMultipleChoiceHeaderTable(excel.get(j+1), choices);
                        document.add(table);

                    }
                    else if (excel.get(i).equals("<Question>")){
                        //TODO gør at -1 bliver sat til ikke relevant
                        document.add(createMultipleChoiceTable(excel.get(i+1), choices.length,
                                Integer.parseInt(excel.get(i+3)),getColumnWidths(table), context));
                    }
                    else if (excel.get(i).equals("<HeadlineEnd>")){
                        j = i;
                        break;
                    }
                }
                document.add(new Paragraph("\n"));
            }
        }
    }

    //NOT GENERAL
    public Table pDFHeader(Context context, String pageNumber, PdfDocument pdfDocument, Document document){
        Drawable logo = context.getDrawable(R.drawable.zealand_company_logo_with_subheading);
        Image logoImage = createImage(logo);
        Table table = new Table(getWidthMatchingPageSize(3,document));

        table.addCell(new Cell(2,1).add(logoImage));
        table.addCell(new Cell(2,1).add(new Paragraph("TJEKLISTE").setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP).setFontColor(ColorConstants.ORANGE).setFontSize(25f)));
        table.addCell(new Cell().add(new Paragraph("side: " + pageNumber)));
        table.addCell(new Cell().add(new Paragraph("Elinstallation")));

        return table;
    }

    public float[] getWidthMatchingPageSize(int divider, Document document){
        float width = (document.getPageEffectiveArea(PageSize.A4).getWidth() - document.getLeftMargin()*2) / divider;
        float[] widths = new float[divider];
        Arrays.fill(widths, width);
        return widths;
    }

    //NOT GENERAL
    public Table createAssignmentInfoTable(Assignment assignment, Document document){
        Table table = new Table(getWidthMatchingPageSize(3,document));

        table.addCell(new Cell(1,3).add
                (new Paragraph("Kundenavn: " + assignment.getCustomerName())));

        table.addCell(new Cell(1,3).add
                (new Paragraph("Adresse: " + assignment.getAddress())));

        table.addCell(new Cell().add
                (new Paragraph("Post nr.: " + assignment.getPostalCode())));

        table.addCell(new Cell().add
                (new Paragraph("By: " + opa.getCityMatchingZipCode("https://api.dataforsyningen.dk/postnumre/"
                        ,assignment.getPostalCode()))));

        table.addCell(new Cell().add
                (new Paragraph("Ordrenummer: " + assignment.getOrderNumber())));

        return table;
    }




    // public float[] createMultipleChoiceRows(int choiceAmount){
   //
   // }
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
        Table table = new Table(rows);
        //adds a new cell with a paragraph taking the header string as parameter on row 1, column 1.
        //sets the paragraph to bold and removes the border around the cell.
        table.addCell(new Cell(0,1).add(new Paragraph(header).setBold()).setBorder(Border.NO_BORDER));
        //adds new cells with the name of the choices from row 1 and up.
        for (int i = 0; i < choices.length; i++) {
            table.addCell(new Cell(1+i,1)
                    .add(new Paragraph(choices[i]).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        }
        return table;
    }

    public Table createMultipleChoiceTable(String question, int numberOfChoices, int answer, float[] rows, Context context){
        Table table = new Table(rows);
        table.addCell(new Cell(0,1).add(new Paragraph(question)).setBorder(Border.NO_BORDER));
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
