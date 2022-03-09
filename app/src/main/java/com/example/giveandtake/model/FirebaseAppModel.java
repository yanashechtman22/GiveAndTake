package com.example.giveandtake.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class FirebaseAppModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    final private static String ADS_COLLECTION_NAME = "Ads";

    public void addNewAd(Ad newAd, AppModel.AddAdListener listener) {
        String id = db.collection(ADS_COLLECTION_NAME).document().getId();
        newAd.setId(id);
        Map<String, Object> json = newAd.toJson();
        db.collection(ADS_COLLECTION_NAME)
                .document(newAd.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void saveImage(Bitmap imageBitmap, String imageId, AppModel.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference adsImgRef = storageRef.child("ads_images/" + imageId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = adsImgRef.putBytes(data);
        uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            if(!task.isSuccessful()){
                throw task.getException();
            }
            return adsImgRef.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    listener.onComplete(downloadUri);
                }else{
                    listener.onComplete(null);
                }

            }
        });
    }
}
