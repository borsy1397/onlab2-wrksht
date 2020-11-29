package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.service.DocxCreatorService;
import com.captainborsy.wrksht.service.export.DocxTableRowWorkflow;
import com.captainborsy.wrksht.service.export.DocxWorksheet;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


@Service
public class DocxCreatorServiceImpl implements DocxCreatorService {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @Override
    public void export(DocxWorksheet docxWorksheet, ByteArrayOutputStream os) {

        /*POIFSFileSystem fs = null;
        try {
            fs = new POIFSFileSystem(new FileInputStream(filePath));
            HWPFDocument doc = new HWPFDocument(fs);
            doc = replaceText(doc, "$deadline", "MyValue1");

            saveWord(filePath, doc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        XWPFDocument document = new XWPFDocument();

        //XWPFStyles styles = document.createStyles();
        //addCustomHeadingStyle(styles, heading1, 1, 36, "000000");

        addParagraph(document, "Munkalap név: ", docxWorksheet.getName(), false);
        addParagraph(document, "Termék neve: ", docxWorksheet.getProductName(), false);
        addParagraph(document, "Mennyiség: ", docxWorksheet.getQuantity(), false);
        addParagraph(document, "Vásárló: ", docxWorksheet.getCustomer(), false);
        addParagraph(document, "Rendelési szám: ", docxWorksheet.getOrderNumber(), false);
        addParagraph(document, "Határidő: ", docxWorksheet.getDeadline(), false);
        addParagraph(document, "Létrehozás ideje: ", docxWorksheet.getCreatedAt(), false);
        addParagraph(document, "Létrehozó: ", docxWorksheet.getCreator(), false);
        addParagraph(document, docxWorksheet.getIsDeleted(), "", false);

        addNewTable("Munkafolyamatok", document, docxWorksheet.getDocxTableRowWorkflows(), false);


        try {
            document.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addCustomHeadingStyle(XWPFStyles styles, String strStyleId, int headingLevel, int pointSize, String hexColor) {
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        ctStyle.setQFormat(onoffnull);

        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
        size.setVal(new BigInteger(String.valueOf(pointSize)));
        CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
        size2.setVal(new BigInteger("24"));

        CTFonts fonts = CTFonts.Factory.newInstance();
        fonts.setAscii("Loma");

        CTRPr rpr = CTRPr.Factory.newInstance();
        rpr.setRFonts(fonts);
        rpr.setSz(size);
        rpr.setSzCs(size2);

        CTColor color = CTColor.Factory.newInstance();
        color.setVal(hexToBytes(hexColor));
        rpr.setColor(color);
        style.getCTStyle().setRPr(rpr);

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
    }

    private byte[] hexToBytes(String hexString) {
        HexBinaryAdapter adapter = new HexBinaryAdapter();
        return adapter.unmarshal(hexString);
    }


/*
    private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range r1 = doc.getRange();

        for (int i = 0; i < r1.numSections(); ++i) {
            Section s = r1.getSection(i);
            for (int x = 0; x < s.numParagraphs(); x++) {
                Paragraph p = s.getParagraph(x);
                for (int z = 0; z < p.numCharacterRuns(); z++) {
                    CharacterRun run = p.getCharacterRun(z);
                    String text = run.text();
                    if (text.contains(findText)) {
                        run.replaceText(findText, replaceText);
                    }
                }
            }
        }
        return doc;
    }

    private static void saveWord(String filePath, HWPFDocument doc) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            doc.write(out);
        } finally {
            out.close();
        }
    }
*/


    public void addParagraph(XWPFDocument document, String title, String details, boolean pageBreak) {
        XWPFParagraph newParagraph = document.createParagraph();
        newParagraph.setPageBreak(pageBreak);
        XWPFRun runParaTitle = newParagraph.createRun();
        runParaTitle.setColor("000033");
        runParaTitle.setBold(false);
        runParaTitle.setFontFamily("Courier");
        runParaTitle.setFontSize(16);
        runParaTitle.setText(title);

        if (details != null) {
            XWPFRun runParaDetails = newParagraph.createRun();
            runParaDetails.setColor("000099");
            runParaDetails.setBold(true);
            runParaDetails.setFontFamily("Courier");
            runParaDetails.setFontSize(20);
            runParaDetails.setText(details);
        } else {
            newParagraph.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    public void addNewTable(String title, XWPFDocument document, List<DocxTableRowWorkflow> rows, boolean pageBreak) {
        if (!rows.isEmpty()) {
            addParagraph(document, title, null, pageBreak);
        }
        createTable(document, rows);
    }

    public void createTable(XWPFDocument document, List<DocxTableRowWorkflow> docxTableRowWorkflowList) {
        if (!docxTableRowWorkflowList.isEmpty()) {
            XWPFTable table = document.createTable();
            table.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(9000));
            addTableRows(table, docxTableRowWorkflowList);
        }
    }


    private void addTableRows(XWPFTable table, List<DocxTableRowWorkflow> docxTableRowWorkflowList) {
        for (int i = 0; i < docxTableRowWorkflowList.size(); i++) {
            if (i == 0) {
                addFirstRowToTable(table, docxTableRowWorkflowList.get(i).getValue());
            } else {
                addRowToTable(table, docxTableRowWorkflowList.get(i).getValue());
            }
        }
    }

    private void addFirstRowToTable(XWPFTable table, List<Object> valueText) {
        XWPFTableRow row = table.getRow(0);
        for (int i = 0; i < valueText.size(); i++) {
            if (i == 0) {
                setCellAlign(row.getCell(0));
                row.getCell(0).setText(Objects.toString(valueText.get(i), ""));
            } else {
                XWPFTableCell valueCell = row.createCell();
                setCellAlign(valueCell);
                valueCell.setText(Objects.toString(valueText.get(i), ""));
            }
        }
    }

    private void setCellAlign(XWPFTableCell cell) {
        XWPFParagraph tempParagraph = cell.getParagraphs().get(0);
        tempParagraph.setAlignment(ParagraphAlignment.CENTER);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    private void addRowToTable(XWPFTable table, List<Object> valueText) {
        XWPFTableRow row = table.createRow();
        for (int i = 0; i < valueText.size(); i++) {
            XWPFTableCell valueCell = row.getCell(i);
            setCellAlign(valueCell);
            valueCell.setText(Objects.toString(valueText.get(i), ""));
        }
    }
}
