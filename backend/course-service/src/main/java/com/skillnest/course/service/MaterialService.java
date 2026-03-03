package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Material;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class MaterialService {

    private static final String COLLECTION_NAME = "materials";
    private final Firestore firestore;

    public MaterialService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Material createMaterial(Material material) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        material.setId(docRef.getId());
        material.setUploadedAt(System.currentTimeMillis());
        docRef.set(material.toMap()).get();
        return material;
    }

    public List<Material> getMaterialsByCourse(String courseId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Material> materials = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            materials.add(Material.fromMap(doc.getData()));
        }
        return materials;
    }

    public Material getMaterialById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return Material.fromMap(doc.getData());
        }
        return null;
    }

    public Material updateMaterial(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return Material.fromMap(updated.getData());
        }
        return null;
    }

    public void deleteMaterial(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public void incrementDownloads(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update("downloads", FieldValue.increment(1)).get();
    }
}
