package com.example.cursed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginFragment extends Fragment implements AsyncValidate {
     View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

       AppDatabase db = Room.databaseBuilder(this.getContext(), AppDatabase.class, "UserBD").build();
        final EditText Login = view.findViewById(R.id.Login);
        final EditText Password = view.findViewById(R.id.Password);
       Button LoginButton = view.findViewById(R.id.LogButton);
       LoginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(Login.length()!= 0 && Password.length()!= 0) {
                   String md5Password = Password.getText().toString();
                   User user = new User(Login.getText().toString(), md5(md5Password));
                   BdValidTask bdvt = new BdValidTask(db, user);
                   bdvt.delegate = LoginFragment.this;
                   bdvt.execute();
                   Login.getText().clear();
                   Password.getText().clear();
               } else Toast.makeText(view.getContext(),"You must type something", Toast.LENGTH_SHORT).show();
           }
       });
       Button RegButton =view.findViewById(R.id.RegButton);
       RegButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(Login.length()!= 0 && Password.length()!= 0) {
                   String md5Password = Password.getText().toString();
                   User user = new User(Login.getText().toString(), md5(md5Password));
                   BdAddTask bdat = new BdAddTask(db, user, view);
                   bdat.execute();
                   Login.getText().clear();
                   Password.getText().clear();
               } else  Toast.makeText(view.getContext(),"You must type something", Toast.LENGTH_SHORT).show();
           }
       });
        return view;
    }


    @Override
    public void processFinish(boolean output) {
        if (output){
            FragmentManager fm = getFragmentManager();
         fm.beginTransaction().replace(R.id.fragment, new MainFragment(), null).disallowAddToBackStack().commit();
        } else {
            Toast.makeText(view.getContext(),"something wrong", Toast.LENGTH_SHORT).show();
        }

    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}