package com.openclassrooms.realestatemanager.repositories;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.local.immovables.Immo;
import com.openclassrooms.realestatemanager.models.database.dao.ImmoDao;
import com.openclassrooms.realestatemanager.models.local.immovables.Picture;
import com.openclassrooms.realestatemanager.utils.ImmoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class ImmoDataRepository {

    private final ImmoDao immoDao;
    private final Executor executor;

    // --- CONSTRUCTOR ---
    @Inject
    public ImmoDataRepository(ImmoDao immoDao, Executor executor) {
        this.immoDao = immoDao;
        this.executor = executor;
    }

    // --- GET ---
    public LiveData<List<Immo>> getImmosByAgent(int agentId){ return this.immoDao.getImmosByAgent(agentId); }

    public LiveData<List<Immo>> getAllImmos(){
        getAllImmosFromFirebase();
        return this.immoDao.getAllImmos();
    }

    public LiveData<Immo> getImmoById(int immoId){
        getImmoByIdFromFirebase(immoId);
        return this.immoDao.getImmoById(immoId);
    }

    // --- CREATE ---
    public void createImmo(Immo immo){
        createImmoInFirebase(immo);
        immoDao.insertImmo(immo);
    }

    // --- DELETE ---
    public void deleteImmo(int immoId){ immoDao.deleteImmo(immoId); }

    // --- UPDATE ---
    public void updateImmo(Immo immo){ immoDao.updateImmo(immo); }

    // --- REMOTE DATA GETTER ---
    private void getImmoByIdFromFirebase(int id){
        executor.execute(() -> {
            ImmoHelper.getImmoById(String.valueOf(id)).addOnCompleteListener(task -> {
                executor.execute(() -> {
                    if (task.getResult().exists()) {
                        Immo immo = task.getResult().toObject(Immo.class);
                        // check if he already exist in db
                        boolean immoExists = (immoDao.hasImmo(immo.getId()) != null);
                        // if not create it
                        if (!immoExists) {
                            immoDao.insertImmo(immo);
                        }
                    }
                });
            }).addOnFailureListener(this.onFailureListener());
        });
    }

    private void getAllImmosFromFirebase(){
        executor.execute(() -> {
            ImmoHelper.getAllImmos().addOnCompleteListener(task -> {
                executor.execute(() -> {
                    // for each immo return by firebase
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot immovable : task.getResult()) {
                            Immo immo = immovable.toObject(Immo.class);
                            // check if he already exist in db
                            boolean immoExists = (immoDao.hasImmo(immo.getId()) != null);
                            // if not create it
                            if (!immoExists) {
                                immoDao.insertImmo(immo);
                            }
                        }
                    }
                });
            }).addOnFailureListener(this.onFailureListener());
        });
    }

    // --- REMOTE DATA SETTER ---
    private void createImmoInFirebase(Immo immo){
        executor.execute(() -> {
            ImmoHelper.createImmo(immo).addOnCompleteListener(task -> {
                executor.execute(() -> {
                    // something notification related
                    Log.i("ImmoDataRepository", "ImmoAddOnFireBase");
                });
            }).addOnFailureListener(this.onFailureListener());
        });
    }



    private OnFailureListener onFailureListener(){
        return e -> e.printStackTrace();
    }

}
