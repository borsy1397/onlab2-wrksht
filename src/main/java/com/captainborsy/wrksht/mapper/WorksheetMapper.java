package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.api.model.WorksheetDetailsDTO;
import com.captainborsy.wrksht.model.Worksheet;

import java.util.List;

public class WorksheetMapper {

    private WorksheetMapper() {
    }

    public static WorksheetDetailsDTO mapWorksheetToWorksheetDetailsDTO(Worksheet worksheet) {
        return null;
    }

    public static List<WorksheetDetailsDTO> mapWorksheetListToWorksheetDetailsDTOList(List<Worksheet> worksheets) {
        return null;
    }

    public static Worksheet mapWorksheetCreationDTOtoWorksheet(WorksheetCreationDTO worksheetCreationDTO) {
        return Worksheet.builder()
                .comment(worksheetCreationDTO.getComment())
                .name(worksheetCreationDTO.getName())
                .deadline(worksheetCreationDTO.getDeadline().toInstant())
                .orderNumber(worksheetCreationDTO.getOrderNumber())
                .customer(worksheetCreationDTO.getCustomer())
                .quantity(worksheetCreationDTO.getQuantity())
                .productName(worksheetCreationDTO.getProductName())
                .build();
    }
}
