package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialService.class);
    private static final String COLLECTION_NAME = "materials";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, Material> demoMaterials = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Material createMaterial(Material material) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: creating material");
            String id = "demo-material-" + UUID.randomUUID().toString().substring(0, 8);
            material.setId(id);
            material.setUploadedAt(System.currentTimeMillis());
            demoMaterials.put(id, material);
            return material;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        material.setId(docRef.getId());
        material.setUploadedAt(System.currentTimeMillis());
        docRef.set(material.toMap()).get();
        return material;
    }

    public List<Material> getMaterialsByCourse(String courseId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting materials for course {}", courseId);
            return demoMaterials.values().stream()
                    .filter(m -> courseId.equals(m.getCourseId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting material {}", id);
            return demoMaterials.get(id);
        }

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return Material.fromMap(doc.getData());
        }
        return null;
    }

    public Material updateMaterial(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating material {}", id);
            Material m = demoMaterials.get(id);
            if (m != null) {
                if (updates.containsKey("title")) m.setTitle((String) updates.get("title"));
                if (updates.containsKey("fileUrl")) m.setFileUrl((String) updates.get("fileUrl"));
                demoMaterials.put(id, m);
            }
            return m;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return Material.fromMap(updated.getData());
        }
        return null;
    }

    public void deleteMaterial(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: deleting material {}", id);
            demoMaterials.remove(id);
            return;
        }
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public void incrementDownloads(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: incrementing downloads for material {}", id);
            Material m = demoMaterials.get(id);
            if (m != null) {
                m.setDownloads(m.getDownloads() + 1);
                demoMaterials.put(id, m);
            }
            return;
        }
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update("downloads", FieldValue.increment(1)).get();
    }
}
