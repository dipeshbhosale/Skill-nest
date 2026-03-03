package com.skillnest.assignment.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.assignment.model.Earning;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class EarningService {

    private static final String COLLECTION_NAME = "earnings";

    private final Firestore firestore;

    public EarningService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Earning createEarning(Earning earning) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        earning.setId(id);
        if (earning.getStatus() == null || earning.getStatus().isEmpty()) {
            earning.setStatus("pending");
        }
        earning.setCreatedAt(Instant.now().toString());

        firestore.collection(COLLECTION_NAME).document(id).set(earning.toMap()).get();
        return earning;
    }

    public List<Earning> getEarningsByTeacher(String teacherId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("teacherId", teacherId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Earning> earnings = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            earnings.add(Earning.fromMap(document.getData()));
        }
        return earnings;
    }

    public Earning getEarningById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return Earning.fromMap(document.getData());
        }
        return null;
    }

    public double getTotalEarnings(String teacherId) throws ExecutionException, InterruptedException {
        List<Earning> earnings = getEarningsByTeacher(teacherId);
        double total = 0.0;
        for (Earning earning : earnings) {
            total += earning.getAmount();
        }
        return total;
    }

    public List<Earning> getEarningsByMonth(String teacherId, String yearMonth) throws ExecutionException, InterruptedException {
        List<Earning> allEarnings = getEarningsByTeacher(teacherId);
        List<Earning> filtered = new ArrayList<>();
        for (Earning earning : allEarnings) {
            if (earning.getDate() != null && earning.getDate().startsWith(yearMonth)) {
                filtered.add(earning);
            }
        }
        return filtered;
    }

    public Earning updateEarningStatus(String id, String status) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            return null;
        }

        Earning earning = Earning.fromMap(document.getData());
        earning.setStatus(status);

        firestore.collection(COLLECTION_NAME).document(id).set(earning.toMap()).get();
        return earning;
    }
}
