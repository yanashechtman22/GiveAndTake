package com.example.giveandtake.auth;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.utils.EmailValidator;
import com.example.giveandtake.utils.InputValidator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    EditText displayName;
    EditText email;
    EditText password;
    Button registerBtn;
    CardView validationCard;
    ProgressBar progressBar;
    View view;

    private final int MIN_PASS_LENGTH = 6;
    boolean nameNotEmpty = false;
    boolean emailValid = false;
    boolean passwordValid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        displayName = view.findViewById(R.id.et_name);
        email = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);
        registerBtn = view.findViewById(R.id.btn_register);
        validationCard = view.findViewById(R.id.card1);
        progressBar = view.findViewById(R.id.simpleProgressBar);

        displayName.addTextChangedListener(new InputValidator(displayName) {
            @Override
            public void validate(TextView textView, String text) {
                nameNotEmpty = text.length() > 0;
                checkInputValidation();
            }
        });

        email.addTextChangedListener(new InputValidator(email) {
            @Override
            public void validate(TextView textView, String text) {
                emailValid = validateEmail(text);
                checkInputValidation();
            }
        });

        password.addTextChangedListener(new InputValidator(password) {
            @SuppressLint("ResourceType")
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() >= MIN_PASS_LENGTH) {
                    validationCard.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                    passwordValid = true;
                } else {
                    validationCard.setCardBackgroundColor(Color.parseColor("#dcdcdc"));
                    passwordValid = false;
                }
                checkInputValidation();
            }
        });

        registerBtn.setOnClickListener(view1 -> handleRegisterNewUser());
        return view;
    }

    private boolean validateEmail(String emailAddress) {
        return EmailValidator.validateEmail(emailAddress);
    }

    private void checkInputValidation() {
        registerBtn.setEnabled(passwordValid && nameNotEmpty && emailValid);
    }

    private void handleRegisterNewUser() {
        progressBar.setVisibility(View.VISIBLE);
        String displayNameText = displayName.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        RegisterAuthListener registerListener = new RegisterAuthListener();
        AuthenticationModel.instance.registerNewUser(displayNameText, emailText, passwordText, registerListener);
    }

    public class RegisterAuthListener implements AuthenticationModel.AuthListener {

        @Override
        public void onComplete(FirebaseUser user) {
            String successMessage = String.format("User %s added successfully", user.getDisplayName());
            Snackbar.make(view, successMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Navigation.findNavController(view).navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            );
        }

        @Override
        public void onFailure(String message) {
            String failedMessage = "Failed to add user" + message;
            Snackbar.make(view, failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}