package com.googleapiintegration.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.googleapiintegration.dto.GoogleSheetDto;
import com.googleapiintegration.dto.GoogleSheetResponseDto;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

@Component
public class GoogleSheetConfig {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/path";

    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, SheetsScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = GoogleSheetConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Map<Object, Object> getDataFromGoogleSheet() throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "Class Data!A2:E";

        Sheets service = getSheetService();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        Map<Object, Object> storeDataFromGoogleSheet = new HashMap<>();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
//            System.out.println("Name, Major");
            for (List row : values) {
                storeDataFromGoogleSheet.put(row.get(0), row.get(4));
//                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
            return storeDataFromGoogleSheet;
        }
        return storeDataFromGoogleSheet;
    }

    private Sheets getSheetService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets sheetService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return sheetService;
    }

    private Drive getDriveService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return driveService;
    }

    public String createGoogleSheet(GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        Sheets sheetService = getSheetService();
        SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
        spreadsheetProperties.setTitle(googleSheetDto.getGoogleSheetName());

        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setTitle(googleSheetDto.getGoogleSheetName());

        Sheet sheet = new Sheet().setProperties(sheetProperties);
        Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties).setSheets(Collections.singletonList(sheet));

        return sheetService.spreadsheets().create(spreadsheet).execute().getSpreadsheetUrl();
    }

    public GoogleSheetResponseDto createGoogleSheetAndUpdate(GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        Sheets sheetService = getSheetService();
        SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
        spreadsheetProperties.setTitle(googleSheetDto.getGoogleSheetName());

        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setTitle(googleSheetDto.getGoogleSheetName());

        Sheet sheet = new Sheet().setProperties(sheetProperties);
        Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties).setSheets(Collections.singletonList(sheet));

        Spreadsheet createGoogleSheet = sheetService.spreadsheets().create(spreadsheet).execute();
        ValueRange valueRange = new ValueRange().setValues(googleSheetDto.getDataToBeUpdated());
        sheetService.spreadsheets().values().update(createGoogleSheet.getSpreadsheetId(), "A1", valueRange).setValueInputOption("RAW").execute();

        GoogleSheetResponseDto googleSheetResponseDto = new GoogleSheetResponseDto();
        googleSheetResponseDto.setSpreadSheetId(createGoogleSheet.getSpreadsheetId());
        googleSheetResponseDto.setSpreadSheetUrl(createGoogleSheet.getSpreadsheetUrl());
        return googleSheetResponseDto;
    }

    public GoogleSheetResponseDto createGoogleSheetAndUpdateAndPermissionToUser(GoogleSheetDto googleSheetDto) throws GeneralSecurityException, IOException {
        Sheets sheetService = getSheetService();
        SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
        spreadsheetProperties.setTitle(googleSheetDto.getGoogleSheetName());

        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setTitle(googleSheetDto.getGoogleSheetName());

        Sheet sheet = new Sheet().setProperties(sheetProperties);
        Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties).setSheets(Collections.singletonList(sheet));

        Spreadsheet createGoogleSheet = sheetService.spreadsheets().create(spreadsheet).execute();
        ValueRange valueRange = new ValueRange().setValues(googleSheetDto.getDataToBeUpdated());
        sheetService.spreadsheets().values().update(createGoogleSheet.getSpreadsheetId(), "A1", valueRange).setValueInputOption("RAW").execute();

        GoogleSheetResponseDto googleSheetResponseDto = new GoogleSheetResponseDto();
        googleSheetResponseDto.setSpreadSheetId(createGoogleSheet.getSpreadsheetId());
        googleSheetResponseDto.setSpreadSheetUrl(createGoogleSheet.getSpreadsheetUrl());

        Drive driveService = getDriveService();

        if (!googleSheetDto.getEmails().isEmpty() && googleSheetDto.getEmails() != null) {
            googleSheetDto.getEmails().forEach(emailaddress -> {
                Permission permission = new Permission().setType("user").setRole("writer").setEmailAddress(emailaddress);
                try {
                    driveService.permissions().create(createGoogleSheet.getSpreadsheetId(), permission).setSendNotificationEmail(true).setEmailMessage("Google sheet permission testing").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return googleSheetResponseDto;
    }

}
