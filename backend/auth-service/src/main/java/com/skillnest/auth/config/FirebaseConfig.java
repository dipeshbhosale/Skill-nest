package com.skillnest.auth.config;

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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.config-path:#{null}}")
    private Resource serviceAccountResource;

    @Value("${firebase.database-url:}")
    private String databaseUrl;

    @Value("${firebase.credentials-base64:}")
    private String credentialsBase64;

    private boolean firebaseInitialized = false;

    @PostConstruct
    public void initialize() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                firebaseInitialized = true;
                return;
            }

            InputStream credentialsStream = resolveCredentials();
            if (credentialsStream == null) {
                logger.warn("Firebase service account not found. Running in DEMO mode.");
                return;
            }

            FirebaseOptions.Builder builder = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream));
            if (databaseUrl != null && !databaseUrl.isEmpty()) {
                builder.setDatabaseUrl(databaseUrl);
            }
            FirebaseApp.initializeApp(builder.build());
            firebaseInitialized = true;
            logger.info("Firebase initialized successfully for project: skill-nest-dccb0");
        } catch (Exception e) {
            logger.warn("Firebase initialization failed: {}. Running in DEMO mode.", e.getMessage());
        }
    }

    /**
     * Resolve credentials with priority:
     * 1. FIREBASE_CREDENTIALS_BASE64 env var (Base64-encoded JSON — safe for CI/CD)
     * 2. firebase.credentials-base64 application property
     * 3. firebase.config-path file resource (classpath JSON file)
     */
    private InputStream resolveCredentials() {
        // Priority 1: Environment variable (Base64-encoded service account JSON)
        String envCredentials = System.getenv("FIREBASE_CREDENTIALS_BASE64");
        if (envCredentials != null && !envCredentials.isBlank()) {
            logger.info("Loading Firebase credentials from FIREBASE_CREDENTIALS_BASE64 env var");
            return new ByteArrayInputStream(Base64.getDecoder().decode(envCredentials));
        }

        // Priority 2: Application property (Base64-encoded)
        if (credentialsBase64 != null && !credentialsBase64.isBlank()) {
            logger.info("Loading Firebase credentials from firebase.credentials-base64 property");
            return new ByteArrayInputStream(Base64.getDecoder().decode(credentialsBase64));
        }

        // Priority 3: Classpath JSON file
        try {
            if (serviceAccountResource != null && serviceAccountResource.exists()) {
                logger.info("Loading Firebase credentials from classpath file");
                return serviceAccountResource.getInputStream();
            }
        } catch (Exception e) {
            logger.warn("Failed to read service account file: {}", e.getMessage());
        }

        return null;
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
