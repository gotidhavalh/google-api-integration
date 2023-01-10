package com.googleapiintegration.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleSheetDto {

    private String googleSheetName;

    private List<List<Object>> dataToBeUpdated;

    private List<String> emails;
}
