// Elliot Coded
package com.example.bullseye_android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.StringUtils;

import org.jsoup.internal.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

// thanks to rreynoud on github (https://github.com/rreynoud/pdf-android) for the example
public class PDF {
    private final Context context;
    private Document document;
    private File pdfFile;
    private String tagLogErr = "pdf";
    private Paragraph currParagraph;
    private Font fTitle = new Font(Font.FontFamily.HELVETICA, 35, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
    private Font fName = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
    private Font fHeader = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
    private Font fSubHeader = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private String name;

    public PDF(Context context) {
        this.context = context;
    }

    public void openDocument(String name, int pages){

        try {
            this.name = name;
            createFile();
            document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageCount(pages);
        }catch (Exception e){
            Log.e(tagLogErr, e.toString());
        }

    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle){

        try {

            currParagraph = new Paragraph();
            addChild(new Paragraph(title, fTitle), Element.ALIGN_CENTER);
            addChild(new Paragraph(subTitle, fSubTitle), Element.ALIGN_CENTER);

            currParagraph.setSpacingAfter(20);
            document.add(currParagraph);

        }catch (Exception e){
            Log.e(tagLogErr, e.toString());
        }

    }

    public void addHeader(String text, int alignment) {
        currParagraph = new Paragraph(text, fHeader);
        currParagraph.setSpacingAfter(5);
        currParagraph.setSpacingBefore(40);
        currParagraph.setAlignment(alignment);
    }

    public void addSubheader(String text, int alignment) {

        if (currParagraph == null) {
            currParagraph = new Paragraph(text, fSubHeader);
            currParagraph.setAlignment(alignment);
        } else {
            Paragraph sub = new Paragraph(text, fSubHeader);
            sub.setAlignment(alignment);
            currParagraph.add(sub);
        }
    }

    public void createTable(List<String> headers, List<String[]> paras, int cols){

        try{
            PdfPTable table = new PdfPTable(cols);
            table.setWidthPercentage(100);

            for (int i = 0; i < paras.size(); i++) {
                Paragraph para = new Paragraph(headers.get(i) + "\n", fSubHeader);
                para.setAlignment(Element.ALIGN_CENTER);
                Paragraph header = new Paragraph(StringUtil.join(paras.get(i), "\n"), fText);
                header.setAlignment(Element.ALIGN_CENTER);
                para.add(header);
                PdfPCell cell1 = new PdfPCell(para);
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);
            }
            table.setSpacingBefore(10f);

            document.add(table);

//            PdfPTable pdfPTable = new PdfPTable(cols);
//            pdfPTable.setWidthPercentage(100);
//            PdfPCell pdfPCell;
//
//            int indexC;
//
//            pdfPTable.addCell("gamer");
//            for(int indexR=0; indexR<paras.size(); indexR++){
//                Paragraph para = new Paragraph(headers.get(indexR), fSubHeader);
//                String[] paragraphs = paras.get(indexR);
//                for(indexC=0; indexC<paragraphs.length; indexC++){
//                    Paragraph line = new Paragraph(paragraphs[indexC], fText);
//                    para.add(line);
//                }
//                pdfPCell = new PdfPCell(para);
//                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                pdfPCell.setFixedHeight(20);
//                pdfPCell.setFixedHeight(0);
//                pdfPTable.addCell(pdfPCell);
////                pdfPTable.addCell("GG");
//            }

//            document.add(pdfPTable);

        }catch (Exception e){
            Log.e(tagLogErr, e.toString());
        }


    }


    public void addParagraph(String text, int alignment){

        try{
            currParagraph = new Paragraph(text, fText);
            currParagraph.setSpacingAfter(5);
            currParagraph.setSpacingBefore(5);
            currParagraph.setAlignment(alignment);
            document.add(currParagraph);

        }catch (Exception e){
            Log.e(tagLogErr, e.toString());
        }

    }

    public void addParagraph(Paragraph paragraph) {
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addParagraph() {
        try {
            document.add(currParagraph);
            currParagraph = null;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addChild(Paragraph childParagraph, int alignment){
        childParagraph.setAlignment(alignment);
        currParagraph.add(childParagraph);
    }

    public void addChild(String text, int alignment){
        Paragraph childParagraph = new Paragraph(text, fText);
        childParagraph.setAlignment(alignment);
        currParagraph.add(childParagraph);
    }


    public void viewPDF(Activity activity){


        if(pdfFile != null && pdfFile.exists()){


            try {

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);

                if(file.exists()){
                    Uri uri = FileProvider.getUriForFile(context, "com.example.bullseye_android.fileprovider", file);

                    context.grantUriPermission(context.getApplicationContext().getPackageName(), uri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                    intent.setAction(Intent.ACTION_SEND);
                    activity.startActivity(Intent.createChooser(intent, null));
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i(context.getPackageName(), "can't open");
            }

        }
    }

    private void createFile() {

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

        if(!folder.exists())
            folder.mkdirs();


        if(folder.exists())
            pdfFile = new File(folder, name);

    }

}
