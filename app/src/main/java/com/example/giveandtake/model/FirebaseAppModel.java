package com.example.giveandtake.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseAppModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    final private static String POSTS_COLLECTION_NAME = "Posts";

    public void getAllPosts(Long lastUpdateDate, AppModel.GetPostsListener listener) {
        db.collection(POSTS_COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                     List<Post> list = new LinkedList<>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Map<String, Object> postData = doc.getData();
                            postData.put("id", doc.getId());
                            Post post = Post.fromJson(postData);
                            if (post != null){
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addNewPost(Post newPost, AppModel.CrudPostListener listener) {
        String id = db.collection(POSTS_COLLECTION_NAME).document().getId();
        newPost.setId(id);
        Map<String, Object> json = newPost.toJson();
        db.collection(POSTS_COLLECTION_NAME)
                .document(newPost.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete(true))
                .addOnFailureListener(e -> listener.onComplete(false));
    }

    public void updatePost(Post post, AppModel.CrudPostListener listener) {
        Map<String, Object> json = post.toJson();
        db.collection(POSTS_COLLECTION_NAME)
                .document(post.getId())
                .update(json)
                .addOnSuccessListener(unused -> listener.onComplete(true))
                .addOnFailureListener(e -> listener.onComplete(false));
    }

    public void saveImage(Bitmap imageBitmap, String imageId, AppModel.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference adsImgRef = storageRef.child("posts_images/" + imageId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = adsImgRef.putBytes(data);
        uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            if(!task.isSuccessful()){
                throw task.getException();
            }
            return adsImgRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Uri downloadUri = task.getResult();
                listener.onComplete(downloadUri.toString());
            }else{
                listener.onComplete(null);
            }

        });
    }

    public void getPostById(String postId, AppModel.GetPostByIdListener listener) {
        db.collection(POSTS_COLLECTION_NAME)
                .document(postId)
                .get()
                .addOnCompleteListener(task -> {
                    Post post = null;
                    if (task.isSuccessful() && task.getResult()!= null){
                        Map<String, Object> data = task.getResult().getData();
                        post = Post.fromJson(data);
                    }
                    listener.onComplete(post);
                });

    }

    public void deletePostById(String postId, AppModel.CrudPostListener listener) {
        db.collection(POSTS_COLLECTION_NAME)
                .document(postId)
                .update("isDeleted",true)
                .addOnSuccessListener(unused -> listener.onComplete(true))
                .addOnFailureListener(e -> listener.onComplete(false));
    }

    public void getNoteByUserId(String userId, AppModel.GetPostByIdListener listener) {
        db.collection(POSTS_COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    Post post = null;
                    if (task.isSuccessful() && task.getResult()!= null){
                        Map<String, Object> data = task.getResult().getData();
                        post = Post.fromJson(data);
                    }
                    listener.onComplete(post);
                });

    }
}
