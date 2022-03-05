package com.example.giveandtake.auth;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.giveandtake.R;


public class LoginFragment extends Fragment {

    Button registerBtn;
    View view;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        registerBtn = view.findViewById(R.id.btn_to_register);
        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("pressed button");
                        Navigation.findNavController(view).navigate(LoginFragmentDirections
                                .actionLoginFragmentToRegisterFragment());
                    }
        });
        return view;
    }

}