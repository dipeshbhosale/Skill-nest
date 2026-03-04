package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Reschedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RescheduleService {

    private static final Logger logger = LoggerFactory.getLogger(RescheduleService.class);
    private static final String COLLECTION_NAME = "reschedules";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, Reschedule> demoReschedules = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Reschedule createReschedule(Reschedule reschedule) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: creating reschedule");
            String id = "demo-reschedule-" + UUID.randomUUID().toString().substring(0, 8);
            reschedule.setId(id);
            reschedule.setCreatedAt(System.currentTimeMillis());
            reschedule.setStatus("pending");
            demoReschedules.put(id, reschedule);
            return reschedule;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        reschedule.setId(docRef.getId());
        reschedule.setCreatedAt(System.currentTimeMillis());
        reschedule.setStatus("pending");
        docRef.set(reschedule.toMap()).get();
        return reschedule;
    }

    public List<Reschedule> getReschedulesByClass(String classId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting reschedules for class {}", classId);
            return demoReschedules.values().stream()
                    .filter(r -> classId.equals(r.getClassId()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("classId", classId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Reschedule> reschedules = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            reschedules.add(Reschedule.fromMap(doc.getData()));
        }
        return reschedules;
    }

    public Reschedule getRescheduleById(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting reschedule {}", id);
            return demoReschedules.get(id);
        }

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return Reschedule.fromMap(doc.getData());
        }
        return null;
    }

    public Reschedule updateRescheduleStatus(String id, String status) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating reschedule status for {}", id);
            Reschedule r = demoReschedules.get(id);
            if (r != null) {
                r.setStatus(status);
                demoReschedules.put(id, r);
            }
            return r;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update("status", status).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return Reschedule.fromMap(updated.getData());
        }
        return null;
    }

    public List<Reschedule> getPendingReschedules() throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting pending reschedules");
            return demoReschedules.values().stream()
                    .filter(r -> "pending".equals(r.getStatus()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("status", "pending").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Reschedule> reschedules = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            reschedules.add(Reschedule.fromMap(doc.getData()));
        }
        return reschedules;
    }

    public void deleteReschedule(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: deleting reschedule {}", id);
            demoReschedules.remove(id);
            return;
        }
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}
