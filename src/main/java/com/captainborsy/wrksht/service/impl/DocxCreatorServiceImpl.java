package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.ExportException;
import com.captainborsy.wrksht.service.DocxCreatorService;
import com.captainborsy.wrksht.service.export.DocxTableRowWorkflow;
import com.captainborsy.wrksht.service.export.DocxWorksheet;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

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
        XWPFDocument document = new XWPFDocument();

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
            throw new ExportException("Error while exporting", WrkshtErrors.EXPORT_ERROR);
        }
    }


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
