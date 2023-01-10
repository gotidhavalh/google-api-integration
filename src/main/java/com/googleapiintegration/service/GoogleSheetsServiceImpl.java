package com.googleapiintegration.service;

import com.googleapiintegration.config.GoogleSheetConfig;
import com.googleapiintegration.dto.GoogleSheetDto;
import com.googleapiintegration.dto.GoogleSheetResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
public class GoogleSheetsServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsServiceImpl.class);

    @Autowired
    private GoogleSheetConfig googleSheetConfig;

    public Map<Object, Object> readDataFromGoogleSheet() throws GeneralSecurityException, IOException {
        return googleSheetConfig.getDataFromGoogleSheet();
    }

    public String createGoogleSheet(GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        return googleSheetConfig.createGoogleSheet(googleSheetDto);
    }

    public GoogleSheetResponseDto createGoogleSheetAndUpdate(GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        return googleSheetConfig.createGoogleSheetAndUpdate(googleSheetDto);
    }

    public GoogleSheetResponseDto createGoogleSheetAndUpdateAndPermissionToUser(GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        return googleSheetConfig.createGoogleSheetAndUpdateAndPermissionToUser(googleSheetDto);
    }

//    @Value("${spreadsheet.id}")
//    private String spreadsheetId;
//
//    @Autowired
//    private GoogleAuthorizationConfig googleAuthorizationConfig;
//
//    @Override
//    public void getSpreadsheetValues() throws IOException, GeneralSecurityException {
//        Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
//        Sheets.Spreadsheets.Values.BatchGet request =
//                sheetsService.spreadsheets().values().batchGet(spreadsheetId);
//        request.setRanges(getSpreadSheetRange());
//        request.setMajorDimension("ROWS");
//        BatchGetValuesResponse response = request.execute();
//        List<List<Object>> spreadSheetValues = response.getValueRanges().get(0).getValues();
//        List<Object> headers = spreadSheetValues.remove(0);
//        for ( List<Object> row : spreadSheetValues ) {
//            LOGGER.info("{}: {}, {}: {}, {}: {}, {}: {}",
//                    headers.get(0),row.get(0), headers.get(1),row.get(1),
//                    headers.get(2),row.get(2), headers.get(3),row.get(3));
//        }
//    }
//
//    private List<String> getSpreadSheetRange() throws IOException, GeneralSecurityException {
//        Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
//        Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(spreadsheetId);
//        Spreadsheet spreadsheet = request.execute();
//        Sheet sheet = spreadsheet.getSheets().get(0);
//        int row = sheet.getProperties().getGridProperties().getRowCount();
//        int col = sheet.getProperties().getGridProperties().getColumnCount();
//        return Collections.singletonList("R1C1:R".concat(String.valueOf(row))
//                .concat("C").concat(String.valueOf(col)));
//    }
}
