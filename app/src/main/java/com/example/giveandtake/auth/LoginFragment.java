package com.example.giveandtake.auth;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.utils.EmailValidator;
import com.example.giveandtake.utils.InputValidator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    EditText email;
    EditText password;
    Button registerBtn;
    Button loginBtn;
    View view;
    boolean emailValid = false;
    boolean passwordValid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        email = view.findViewById(R.id.et_email_login);
        password = view.findViewById(R.id.et_password_login);
        registerBtn = view.findViewById(R.id.btn_to_register);
        loginBtn = view.findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(v-> {
            handleLogin();
        });

        email.addTextChangedListener(new InputValidator(email) {
            @Override
            public void validate(TextView textView, String text) {
                emailValid = validateEmail(text);
                checkInputValidation();
            }
        });

        password.addTextChangedListener(new InputValidator(password) {
            @Override
            public void validate(TextView textView, String text) {
                passwordValid = !text.isEmpty();
                checkInputValidation();
            }
        });

        registerBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(LoginFragmentDirections
                .actionLoginFragmentToRegisterFragment()));
        return view;
    }

    private void checkInputValidation() {
        loginBtn.setEnabled(passwordValid && emailValid);
    }

    private boolean validateEmail(String emailAddress) {
        return EmailValidator.validateEmail(emailAddress);
    }

    private void handleLogin() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        LoginAuthListener loginAuthListener = new LoginAuthListener();
        AuthenticationModel.instance.loginUser(emailText,passwordText, loginAuthListener);
    }

    public class LoginAuthListener implements AuthenticationModel.AuthListener{

        @Override
        public void onComplete(FirebaseUser user) {
            Snackbar.make(view, user.getDisplayName(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        @Override
        public void onFailure(String message) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}