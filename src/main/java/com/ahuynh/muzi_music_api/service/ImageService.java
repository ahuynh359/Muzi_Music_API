package com.ahuynh.muzi_music_api.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageService {

    private String uploadFile(File file, String fileName) throws IOException {
        fileName  = "app/" + fileName;
        BlobId blobId = BlobId.of("muzimusic-c2598.appspot.com", fileName); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("firebase.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return "";
    }

    private String uploadFileImage(File file, String fileName) throws IOException {
        fileName = "app/" + fileName;
        BlobId blobId = BlobId.of("muzimusic-c2598.appspot.com", fileName); // Replace with your bucket name

        // Create metadata with a token
        Map<String, String> metadata = new HashMap<>();
        String token = UUID.randomUUID().toString();
        metadata.put("firebaseStorageDownloadTokens", token);

        // Build BlobInfo with metadata
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .setMetadata(metadata)
                .build();

        // Load credentials and initialize the Storage service
        InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("firebase.json"); // change the file name with your one
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        // Upload the file to Firebase Storage
        Blob blob = storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // Generate the download URL
        String downloadUrl = String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                blob.getBucket(),
                URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8.toString()),
                token
        );

        return downloadUrl;
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = this.uploadFileImage(file, fileName);                                   // to get uploaded file link
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

}