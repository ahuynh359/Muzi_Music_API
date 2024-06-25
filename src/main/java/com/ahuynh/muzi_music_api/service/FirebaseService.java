package com.ahuynh.muzi_music_api.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
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
import java.util.Objects;
import java.util.UUID;

@Service
public class FirebaseService {

    private String uploadFileImage(File file, String fileName, String type) throws IOException {
        fileName = "app/" + fileName;
        BlobId blobId = BlobId.of("muzimusic-c2598.appspot.com", fileName); // Replace with your bucket name

        // Create metadata with a token
        Map<String, String> metadata = new HashMap<>();
        String token = UUID.randomUUID().toString();
        metadata.put("firebaseStorageDownloadTokens", token);

        // Build BlobInfo with metadata
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(type)
                .setMetadata(metadata)
                .build();

        // Load credentials and initialize the Storage service
        InputStream inputStream = FirebaseService.class.getClassLoader().getResourceAsStream("firebase.json"); // change the file name with your one
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
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String upload(MultipartFile multipartFile, String type) {
        try {
            String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename()); // Get the original file name
            File file = this.convertToFile(multipartFile, originalFileName);                      // Convert multipartFile to File
            String URL = this.uploadFileImage(file, originalFileName, type);                      // Get uploaded file link
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }
}