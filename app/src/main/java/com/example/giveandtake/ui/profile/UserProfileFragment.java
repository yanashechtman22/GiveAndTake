package com.example.giveandtake.ui.profile;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.giveandtake.R;
import com.example.giveandtake.auth.RegisterFragmentDirections;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.FireBaseUserModel;
import com.example.giveandtake.model.User;
import com.example.giveandtake.utils.EmailValidator;
import com.example.giveandtake.utils.InputValidator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class UserProfileFragment extends Fragment {

    TextView phone;
    TextView email;
    TextView name;
    Button registerBtn;
    CardView validationCard;
    ProgressBar progressBar;
    View view;
    FireBaseUserModel fireBaseUserModel = new FireBaseUserModel();

    //TODO: extract number to constants
    private final int MIN_PASS_LENGTH = 6;
    boolean nameNotEmpty = false;
    boolean emailValid = false;
    boolean passwordValid = false;
    User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //todo get the user email from the previous page
        // String userEmailFromPrev = UserProfileFragmentArgs.fromBundle(getArguments()).getStudentEmail();
        String userEmailFromPrev = "tt";
        //todo get user from db
        user = fireBaseUserModel.getUserByEmail(userEmailFromPrev);
        initializeUserData(user);

        return view;
    }

    private void initializeUserData(User user) {
        email = view.findViewById(R.id.user_profile_email);
        name = view.findViewById(R.id.user_profile_name);
        phone = view.findViewById(R.id.user_profile_phone);


        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        email.setText(user.getEmail());
    }
}