package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.FirebaseConfig;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final Storage storage;
    private final FirebaseMessaging firebaseMessaging;


    private String uploadFile(File file, String fileName, String contentType) throws IOException {
        String blobName = "app/" + fileName;
        BlobId blobId = BlobId.of("muzimusic-c2598.appspot.com", blobName); // Replace with your bucket name

        Map<String, String> metadata = new HashMap<>();
        String token = UUID.randomUUID().toString();
        metadata.put("firebaseStorageDownloadTokens", token);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .setMetadata(metadata)
                .build();

        Blob blob = storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                blob.getBucket(),
                URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8),
                token
        );
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    public String upload(MultipartFile multipartFile, String contentType) {
        try {
            String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
            File file = convertToFile(multipartFile, originalFileName);
            String url = uploadFile(file, originalFileName, contentType);
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                System.err.println("Failed to delete the temporary file: " + file.getAbsolutePath());
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    public void sendNotification(String deviceToken, String title, String body) {
        if (deviceToken == null) {
            System.err.println("Invalid device token format: " + deviceToken);
            return;
        }

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending message to token: " + deviceToken);
            e.printStackTrace();
        }
    }

}