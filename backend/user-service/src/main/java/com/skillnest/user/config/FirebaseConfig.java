package com.skillnest.user.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.config-path:#{null}}")
    private Resource serviceAccountResource;

    @Value("${firebase.database-url:}")
    private String databaseUrl;

    private boolean firebaseInitialized = false;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty() && serviceAccountResource != null && serviceAccountResource.exists()) {
                FirebaseOptions.Builder builder = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountResource.getInputStream()));
                if (databaseUrl != null && !databaseUrl.isEmpty()) {
                    builder.setDatabaseUrl(databaseUrl);
                }
                FirebaseApp.initializeApp(builder.build());
                firebaseInitialized = true;
                logger.info("Firebase initialized successfully");
            } else if (!FirebaseApp.getApps().isEmpty()) {
                firebaseInitialized = true;
            } else {
                logger.warn("Firebase service account not found. Running in DEMO mode.");
            }
        } catch (Exception e) {
            logger.warn("Firebase initialization failed: {}. Running in DEMO mode.", e.getMessage());
        }
    }

    @Bean
    public Firestore firestore() {
        if (firebaseInitialized) {
            return FirestoreClient.getFirestore();
        }
        logger.warn("Firestore not available - running in demo mode");
        return null;
    }

    public boolean isFirebaseInitialized() {
        return firebaseInitialized;
    }
}
