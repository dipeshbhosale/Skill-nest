package com.skillnest.assignment.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private Resource firebaseConfigPath;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseConfigPath.getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }
}
