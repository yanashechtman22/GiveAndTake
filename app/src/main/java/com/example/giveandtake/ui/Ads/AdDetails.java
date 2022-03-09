package com.example.giveandtake.ui.Ads;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.giveandtake.R;
import com.example.giveandtake.model.Ad;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;


public class NoteDetailsFragment extends Fragment {
    TextView titleTv;
    TextView idTv;
    TextView contentTv;
    Ad ad;
    Button editBtn;
    Button deleteBtn;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_details, container, false);

        UserInfo userInfo = AuthenticationModel.instance.getUserInfo();

        String noteId = NoteDetailsFragmentArgs.fromBundle(getArguments()).getNoteId();
        editBtn = view.findViewById(R.id.details_edit_btn);
        deleteBtn = view.findViewById(R.id.details_delete_btn);

        editBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);

        AppModel.instance.getNoteById(noteId, new AppModel.GetNoteById() {
            @Override
            public void onComplete(Note note) {
                titleTv.setText(note.getTitle());
                idTv.setText(note.getId());
                contentTv.setText(note.getContent());
                editBtn.setVisibility(note.getUserId().equals(userInfo.getUid())?
                        View.VISIBLE : View.INVISIBLE);
                deleteBtn.setVisibility(note.getUserId().equals(userInfo.getUid())?
                        View.VISIBLE : View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                if (note.getImageUrl() != null && note.getImageUrl().length() > 0) {
                    Picasso.get().load(note.getImageUrl()).into(imageView);
                }
            }
        });

        titleTv = view.findViewById(R.id.details_title_tv);
        idTv = view.findViewById(R.id.details_id_tv);
        contentTv = view.findViewById(R.id.details_content_tv);
        imageView = view.findViewById(R.id.details_image);


        editBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(NoteDetailsFragmentDirections
                    .actionNoteDetailsFragmentToFragmentEditNote(noteId));
        });

        deleteBtn.setOnClickListener((v) -> {
            progressBar.setVisibility(View.VISIBLE);
            Model.instance.getNoteById(noteId, new Model.GetNoteById() {
                @Override
                public void onComplete(Note noteFromDb) {
                    note = new Note(noteFromDb.getId(), noteFromDb.getTitle(), noteFromDb.getContent(),
                            new LatLng(noteFromDb.getLatitude(), noteFromDb.getLongitude()),
                            noteFromDb.getUserId(),
                            noteFromDb.getImageUrl(),
                            true);

                    Model.instance.editNote(note, () -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        Navigation.findNavController(view).navigate(NoteDetailsFragmentDirections
                                .actionNoteDetailsFragmentToMap());
                    });
                }
            });
        });
        return view;
    }
}