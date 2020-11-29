package com.captainborsy.wrksht.service;

import com.captainborsy.wrksht.service.export.DocxWorksheet;

import java.io.ByteArrayOutputStream;

public interface DocxCreatorService {

    void export(DocxWorksheet docxWorksheet, ByteArrayOutputStream os);
}
