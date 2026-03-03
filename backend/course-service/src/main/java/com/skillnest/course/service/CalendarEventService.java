package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.CalendarEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class CalendarEventService {

    private static final String COLLECTION_NAME = "calendar_events";
    private final Firestore firestore;

    public CalendarEventService(Firestore firestore) {
        this.firestore = firestore;
    }

    public CalendarEvent createEvent(CalendarEvent event) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        event.setId(docRef.getId());
        docRef.set(event.toMap()).get();
        return event;
    }

    public List<CalendarEvent> getEventsByUser(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<CalendarEvent> events = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            events.add(CalendarEvent.fromMap(doc.getData()));
        }
        return events;
    }

    public List<CalendarEvent> getEventsByCourse(String courseId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<CalendarEvent> events = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            events.add(CalendarEvent.fromMap(doc.getData()));
        }
        return events;
    }

    public CalendarEvent getEventById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return CalendarEvent.fromMap(doc.getData());
        }
        return null;
    }

    public CalendarEvent updateEvent(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return CalendarEvent.fromMap(updated.getData());
        }
        return null;
    }

    public void deleteEvent(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}
