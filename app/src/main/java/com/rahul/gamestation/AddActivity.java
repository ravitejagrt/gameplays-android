package com.rahul.gamestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    private EditText titleText, ImageText, videoText;
    private Button submit;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        titleText = (EditText) findViewById(R.id.editName);
        ImageText = (EditText) findViewById(R.id.editImage);
        videoText = (EditText) findViewById(R.id.editVideo);

        submit = (Button) findViewById(R.id.submit_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference().push();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title,image,video;
                title = titleText.getText().toString();
                image = ImageText.getText().toString();
                video = videoText.getText().toString();


                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(getApplicationContext(), "Please enter title!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(image)) {
                    Toast.makeText(getApplicationContext(), "Please enter image...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(video)) {
                    Toast.makeText(getApplicationContext(), "Please enter video...", Toast.LENGTH_LONG).show();
                    return;
                }

                mDatabase.child("title").setValue(title);
                mDatabase.child("image").setValue(image);
                mDatabase.child("video").setValue(video);

                Toast.makeText(getApplicationContext(), "Game Added", Toast.LENGTH_LONG).show();

                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddActivity.this, MainActivity.class));
    }
}
