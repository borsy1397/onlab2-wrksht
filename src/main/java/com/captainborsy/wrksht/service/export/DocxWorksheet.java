package com.captainborsy.wrksht.service.export;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocxWorksheet {

    private String name;
    private String productName;
    private String quantity;
    private String customer;
    private String orderNumber;
    private String deadline;
    private String status;
    private String createdAt;
    private String isDeleted;
    private String creator;

    private List<DocxTableRowWorkflow> docxTableRowWorkflows;
}
