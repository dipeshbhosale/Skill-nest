package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class CourseService {

    private static final String COLLECTION_NAME = "courses";
    private final Firestore firestore;

    public CourseService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Course createCourse(Course course) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        course.setId(docRef.getId());
        course.setCreatedAt(System.currentTimeMillis());
        course.setUpdatedAt(System.currentTimeMillis());
        docRef.set(course.toMap()).get();
        return course;
    }

    public List<Course> getAllCourses() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Course> courses = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            courses.add(Course.fromMap(doc.getData()));
        }
        return courses;
    }

    public Course getCourseById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return Course.fromMap(doc.getData());
        }
        return null;
    }

    public List<Course> getCoursesByTeacher(String teacherId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("teacherId", teacherId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Course> courses = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            courses.add(Course.fromMap(doc.getData()));
        }
        return courses;
    }

    public Course updateCourse(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        updates.put("updatedAt", System.currentTimeMillis());
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return Course.fromMap(updated.getData());
        }
        return null;
    }

    public void deleteCourse(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public void incrementEnrolledStudents(String courseId, int delta) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(courseId);
        docRef.update("enrolledStudents", FieldValue.increment(delta)).get();
    }

    public void incrementTotalClasses(String courseId, int delta) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(courseId);
        docRef.update("totalClasses", FieldValue.increment(delta)).get();
    }
}
