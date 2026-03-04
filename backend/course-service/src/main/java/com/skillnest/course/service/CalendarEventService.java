package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.CalendarEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CalendarEventService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarEventService.class);
    private static final String COLLECTION_NAME = "calendar_events";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, CalendarEvent> demoEvents = new ConcurrentHashMap<>();

    // Initialize demo events
    {
        CalendarEvent e1 = new CalendarEvent();
        e1.setId("demo-event-001");
        e1.setUserId("demo-teacher-001");
        e1.setCourseId("demo-course-001");
        e1.setTitle("Java Class - Introduction to Variables");
        e1.setDate("2026-03-05");
        e1.setTime("10:00");
        e1.setType("class");
        e1.setColor("#4F46E5");
        demoEvents.put(e1.getId(), e1);
    }

    private boolean isDemoMode() {
        return firestore == null;
    }

    public CalendarEvent createEvent(CalendarEvent event) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: creating calendar event");
            String id = "demo-event-" + UUID.randomUUID().toString().substring(0, 8);
            event.setId(id);
            demoEvents.put(id, event);
            return event;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        event.setId(docRef.getId());
        docRef.set(event.toMap()).get();
        return event;
    }

    public List<CalendarEvent> getEventsByUser(String userId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting events for user {}", userId);
            return demoEvents.values().stream()
                    .filter(e -> userId.equals(e.getUserId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting events for course {}", courseId);
            return demoEvents.values().stream()
                    .filter(e -> courseId.equals(e.getCourseId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting event {}", id);
            return demoEvents.get(id);
        }

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return CalendarEvent.fromMap(doc.getData());
        }
        return null;
    }

    public CalendarEvent updateEvent(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating event {}", id);
            CalendarEvent e = demoEvents.get(id);
            if (e != null) {
                if (updates.containsKey("title")) e.setTitle((String) updates.get("title"));
                if (updates.containsKey("date")) e.setDate((String) updates.get("date"));
                if (updates.containsKey("time")) e.setTime((String) updates.get("time"));
                demoEvents.put(id, e);
            }
            return e;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return CalendarEvent.fromMap(updated.getData());
        }
        return null;
    }

    public void deleteEvent(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: deleting event {}", id);
            demoEvents.remove(id);
            return;
        }
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}
