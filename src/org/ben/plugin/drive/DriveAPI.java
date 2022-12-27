/**
 * a note:
 * the pp plugin is free software and comes with no warranty whatsoever. My claims of functionality
 * are purely a figment of your imagination. All rights belong to their respective owners.
 * 
 * Ha. there now you can't sue me when it doesn't work :)
 *
 * @author Ben Staehle
 * @date 8/13/22
 */

 package org.ben.plugin.drive;

 import com.google.api.client.googleapis.json.GoogleJsonResponseException;
 import com.google.api.client.http.FileContent;
 import com.google.api.client.http.javanet.NetHttpTransport;
 import com.google.api.client.json.gson.GsonFactory;
 import com.google.api.services.drive.Drive;
 import com.google.api.client.json.JsonFactory;
 import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
 import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
 import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
 import com.google.api.services.drive.DriveScopes;
 import com.google.api.services.drive.model.File;
 import com.google.api.client.auth.oauth2.Credential;
 import java.io.IOException;
 import java.io.FileNotFoundException;
 import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
 import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
 import com.google.api.client.util.store.FileDataStoreFactory;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.util.List;
 import java.util.ArrayList;
 import java.util.Arrays;
 import com.google.api.services.drive.Drive.Files;
 import com.google.api.services.drive.model.File;
 import com.google.api.services.drive.model.FileList;
 
 public class DriveAPI {
      private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
      private static final String CREDENTIALS_FILE_PATH = "/credentials/credentials.json";
      private static final String TOKENS_DIRECTORY_PATH = "tokens";
      private static final List<String> SCOPES = new ArrayList<String>(Arrays.asList(DriveScopes.DRIVE_FILE));
      private static final String APPLICATION_NAME = "pp_plugin";

      private static Drive service;

      private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
         // Load client secrets.
         InputStream in = DriveAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
         if (in == null) {
         throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
         }
         GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
   
         // Build flow and trigger user authorization request.
         GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
         LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
         Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
         //returns an authorized Credential object.
         return credential;
      }

      public static void initDrive() throws Exception {
         // Load pre-authorized user credentials from the environment.
         // guides on implementing OAuth2 for your application.

         // Build a new authorized API client service.
         final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
         service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME) 
            .build();
      }

      public static String getFileId() throws Exception {
         List<File> fileList = new ArrayList<>();
         Files.List request = service.files().list();

         do {
            try {
               FileList files = request.execute();

               fileList.addAll(files.getFiles());
               request.setPageToken(files.getNextPageToken());
            } catch(Exception e) {
               e.printStackTrace();
               request.setPageToken(null);
            }
         } while(request.getPageToken() != null && request.getPageToken().length() > 0);

         for(File f : fileList) {
            if(f.getName().equals("pp_worldsave.zip")) {
               return f.getId();
            }
         }
         return null;
      }

      public static void deleteBasic() throws Exception{
         initDrive();
         String id = getFileId();

         //delete the previous save file
         try {
            if(id != null) {
               service.files().delete(id).execute();
            } 
         } catch(IOException e) {
            e.printStackTrace();
         }
      }

      /**
      * Upload new file.
      *
      * @return Inserted file metadata if successful, {@code null} otherwise.
      * @throws IOException if service account credentials file not found.
      */
      public static String uploadBasic() throws Exception {
         initDrive();

         // Upload file photo.jpg on drive.
         File fileMetadata = new File();
         fileMetadata.setName("pp_worldsave.zip");
         // File's content.
         java.io.File filePath = new java.io.File("pp_worldsave.zip");
         // Specify media type and file-path for file.
         FileContent mediaContent = new FileContent("zip_archive/zip", filePath);
         try {
            File file = service.files().create(fileMetadata, mediaContent)
                  .setFields("id")
                  .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
         } catch (GoogleJsonResponseException e) {
            System.out.println("Unable to upload file: " + e.getDetails());
            throw e;
         }
      }
 }