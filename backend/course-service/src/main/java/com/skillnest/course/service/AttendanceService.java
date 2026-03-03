package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Attendance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AttendanceService {

    private static final String COLLECTION_NAME = "attendance";
    private final Firestore firestore;

    public AttendanceService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Attendance markAttendance(Attendance attendance) throws ExecutionException, InterruptedException {
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
