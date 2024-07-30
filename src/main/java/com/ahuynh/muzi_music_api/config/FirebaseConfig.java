package com.ahuynh.muzi_music_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public Storage initializeFirebaseStorage() throws IOException {
        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream("firebase.json");
        if (serviceAccount == null) {
            throw new IOException("Firebase service account file not found.");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }



    @Bean
    public FirebaseMessaging initializeFirebaseMessaging() throws IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("firebase.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(credentials).build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);
        return FirebaseMessaging.getInstance(app);
    }
}