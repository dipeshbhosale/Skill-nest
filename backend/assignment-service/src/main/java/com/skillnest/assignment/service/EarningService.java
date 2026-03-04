package com.skillnest.assignment.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.assignment.model.Earning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class EarningService {

    private static final Logger logger = LoggerFactory.getLogger(EarningService.class);
    private static final String COLLECTION_NAME = "earnings";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, Earning> demoEarnings = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Earning createEarning(Earning earning) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        earning.setId(id);
        if (earning.getStatus() == null || earning.getStatus().isEmpty()) {
            earning.setStatus("pending");
        }
        earning.setCreatedAt(Instant.now().toString());

        if (isDemoMode()) {
            logger.info("Demo mode: creating earning");
            demoEarnings.put(id, earning);
            return earning;
        }

        firestore.collection(COLLECTION_NAME).document(id).set(earning.toMap()).get();
        return earning;
    }

    public List<Earning> getEarningsByTeacher(String teacherId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting earnings for teacher {}", teacherId);
            return demoEarnings.values().stream()
                    .filter(e -> teacherId.equals(e.getTeacherId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting earning {}", id);
            return demoEarnings.get(id);
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: updating earning status for {}", id);
            Earning earning = demoEarnings.get(id);
            if (earning != null) {
                earning.setStatus(status);
                demoEarnings.put(id, earning);
            }
            return earning;
        }

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
