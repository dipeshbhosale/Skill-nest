package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Reschedule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class RescheduleService {

    private static final String COLLECTION_NAME = "reschedules";
    private final Firestore firestore;

    public RescheduleService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Reschedule createReschedule(Reschedule reschedule) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        reschedule.setId(docRef.getId());
        reschedule.setCreatedAt(System.currentTimeMillis());
        reschedule.setStatus("pending");
        docRef.set(reschedule.toMap()).get();
        return reschedule;
    }

    public List<Reschedule> getReschedulesByClass(String classId) throws ExecutionException, InterruptedException {
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
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return Reschedule.fromMap(doc.getData());
        }
        return null;
    }

    public Reschedule updateRescheduleStatus(String id, String status) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update("status", status).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return Reschedule.fromMap(updated.getData());
        }
        return null;
    }

    public List<Reschedule> getPendingReschedules() throws ExecutionException, InterruptedException {
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
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}
