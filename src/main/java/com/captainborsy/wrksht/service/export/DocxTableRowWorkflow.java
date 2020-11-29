package com.captainborsy.wrksht.service.export;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class DocxTableRowWorkflow {
    private List<Object> value;

    public DocxTableRowWorkflow(Object... value){
        this.value = Arrays.asList(value);
    }
}
