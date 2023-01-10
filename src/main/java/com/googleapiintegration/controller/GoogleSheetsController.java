package com.googleapiintegration.controller;

import com.googleapiintegration.dto.GoogleSheetDto;
import com.googleapiintegration.dto.GoogleSheetResponseDto;
import com.googleapiintegration.service.GoogleSheetsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
//@RequestMapping(value = "/googlesheets")
public class GoogleSheetsController {

    @Autowired
    private GoogleSheetsServiceImpl googleSheetsServiceImpl;

    @GetMapping("/getDataFromGoogleSheet")
    public Map<Object, Object> readDataFromGoogleSheeet() throws IOException, GeneralSecurityException {
        return googleSheetsServiceImpl.readDataFromGoogleSheet();
    }

    @PostMapping("/createGoogleSheet")
    public String createGoogleSheet(@RequestBody GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        return googleSheetsServiceImpl.createGoogleSheet(googleSheetDto);
    }

    @PostMapping("/createGoogleSheetAndUpdate")
    public GoogleSheetResponseDto createGoogleSheetAndUpdate(@RequestBody GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        return googleSheetsServiceImpl.createGoogleSheetAndUpdate(googleSheetDto);
    }

    @PostMapping("/createGoogleSheetAndUpdateAndPermissionToUser")
    public GoogleSheetResponseDto createGoogleSheetAndUpdateAndPermissionToUser(@RequestBody GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        return googleSheetsServiceImpl.createGoogleSheetAndUpdateAndPermissionToUser(googleSheetDto);
    }
}
