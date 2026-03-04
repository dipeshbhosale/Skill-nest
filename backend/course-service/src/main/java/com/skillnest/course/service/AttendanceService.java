package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Attendance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    private static final String COLLECTION_NAME = "attendance";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, Attendance> demoAttendance = new ConcurrentHashMap<>();

    // Initialize demo attendance
    {
        Attendance a1 = new Attendance();
        a1.setId("demo-attendance-001");
        a1.setClassId("demo-class-001");
        a1.setStudentId("demo-student-001");
        a1.setCourseId("demo-course-001");
        a1.setStatus("present");
        demoAttendance.put(a1.getId(), a1);
    }

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Attendance markAttendance(Attendance attendance) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: marking attendance");
            // Check if already marked for this class + student
            Optional<Attendance> existing = demoAttendance.values().stream()
                    .filter(a -> a.getClassId().equals(attendance.getClassId()) 
                            && a.getStudentId().equals(attendance.getStudentId()))
                    .findFirst();
            if (existing.isPresent()) {
                Attendance updated = existing.get();
                updated.setStatus(attendance.getStatus());
                demoAttendance.put(updated.getId(), updated);
                return updated;
            }
            String id = "demo-attendance-" + UUID.randomUUID().toString().substring(0, 8);
            attendance.setId(id);
            demoAttendance.put(id, attendance);
            return attendance;
        }

        // Check if already marked for this class + student
        ApiFuture<QuerySnapshot> check = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("classId", attendance.getClassId())
                .whereEqualTo("studentId", attendance.getStudentId())
                .get();

        if (!check.get().isEmpty()) {
            // Update existing attendance record
            QueryDocumentSnapshot existing = check.get().getDocuments().get(0);
            existing.getReference().update("status", attendance.getStatus()).get();
            Attendance updated = Attendance.fromMap(existing.getData());
            updated.setStatus(attendance.getStatus());
            return updated;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        attendance.setId(docRef.getId());
        docRef.set(attendance.toMap()).get();
        return attendance;
    }

    public List<Attendance> markBulkAttendance(List<Attendance> attendanceList) throws ExecutionException, InterruptedException {
        List<Attendance> results = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            results.add(markAttendance(attendance));
        }
        return results;
    }

    public List<Attendance> getAttendanceByClass(String classId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting attendance for class {}", classId);
            return demoAttendance.values().stream()
                    .filter(a -> classId.equals(a.getClassId()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("classId", classId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Attendance> attendanceList = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            attendanceList.add(Attendance.fromMap(doc.getData()));
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByStudent(String studentId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting attendance for student {}", studentId);
            return demoAttendance.values().stream()
                    .filter(a -> studentId.equals(a.getStudentId()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("studentId", studentId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Attendance> attendanceList = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            attendanceList.add(Attendance.fromMap(doc.getData()));
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByStudentAndCourse(String studentId, String courseId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting attendance for student {} in course {}", studentId, courseId);
            return demoAttendance.values().stream()
                    .filter(a -> studentId.equals(a.getStudentId()) && courseId.equals(a.getCourseId()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("courseId", courseId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Attendance> attendanceList = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            attendanceList.add(Attendance.fromMap(doc.getData()));
        }
        return attendanceList;
    }
}
