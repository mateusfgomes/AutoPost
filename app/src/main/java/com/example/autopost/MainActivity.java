package com.example.autopost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Random rand = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] frase = {"", ""};


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference frases = database.getReference("frases");
        final DatabaseReference autores = database.getReference("autores");


        initPython();


        Button post = findViewById(R.id.button);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frases.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            frase[0] = ds.getValue(String.class);
                            ds.getRef().removeValue();
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("DEU RUIM", "Failed to read value.", error.toException());
                    }
                });
                autores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            frase[1] = ds.getValue(String.class);
                            postTweet(frase[0], frase[1]);
                            ds.getRef().removeValue();
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("DEU RUIM", "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }

    private void postTweet(String frase, String autor){
        String tweet = '"' + frase + '"' + ' ' + '-' + ' ' + autor;
        Python ex = Python.getInstance();
        PyObject file = ex.getModule("post");
        file.callAttr("publicar", tweet);
    }

    private void initPython(){
        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
    }
}