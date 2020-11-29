package com.captainborsy.wrksht.mapper;

import com.captainborsy.wrksht.api.model.WorksheetCreationDTO;
import com.captainborsy.wrksht.api.model.WorksheetDetailsDTO;
import com.captainborsy.wrksht.model.Worksheet;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class WorksheetMapper {

    private WorksheetMapper() {
    }

    public static WorksheetDetailsDTO mapWorksheetToWorksheetDetailsDTO(Worksheet worksheet) {
        return WorksheetDetailsDTO.builder()
                .id(worksheet.getId())
                .deleted(worksheet.isDeleted())
                .status(WorksheetDetailsDTO.StatusEnum.valueOf(worksheet.getStatus().toString()))
                .comment(worksheet.getComment())
                .name(worksheet.getName())
                .deadLine(worksheet.getDeadline().atOffset(ZoneOffset.UTC))
                .orderNumber(worksheet.getOrderNumber())
                .customer(worksheet.getCustomer())
                .quantity(worksheet.getQuantity())
                .productName(worksheet.getProductName())
                .creatorId(worksheet.getCreator().getId())
                .createdAt(worksheet.getCreatedAt().atOffset(ZoneOffset.UTC))
                .updatedAt(worksheet.getUpdatedAt().atOffset(ZoneOffset.UTC))
                .build();
    }

    public static List<WorksheetDetailsDTO> mapWorksheetListToWorksheetDetailsDTOList(List<Worksheet> worksheets) {
        return worksheets.stream().map(WorksheetMapper::mapWorksheetToWorksheetDetailsDTO).collect(Collectors.toList());
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
