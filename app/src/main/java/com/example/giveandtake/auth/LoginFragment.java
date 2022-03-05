package com.example.giveandtake.auth;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AuthenticationModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button registerBtn;
    Button loginBtn;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        email = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);
        registerBtn = view.findViewById(R.id.btn_to_register);
        loginBtn = view.findViewById(R.id.btn_login);

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        loginBtn.setOnClickListener(v-> {
            handleLogin(emailText,passwordText);
        });

        registerBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(LoginFragmentDirections
                .actionLoginFragmentToRegisterFragment()));
        return view;
    }

    private void handleLogin(String emailText, String passwordText) {
        LoginListener loginListener = new LoginListener();
        AuthenticationModel.instance.loginUser(emailText,passwordText, loginListener);
    }

    public class LoginListener implements AuthenticationModel.LoginListener{

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