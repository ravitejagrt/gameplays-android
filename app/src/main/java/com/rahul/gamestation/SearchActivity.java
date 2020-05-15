package com.rahul.gamestation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    RecyclerView searchRecyclerView;
    FirebaseRecyclerAdapter adapter;

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mainRecyclerView();
        fetch("");

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchTxt = mSearchField.getText().toString();
                if(searchTxt.equals("")){
                    fetch("333");
                }else {
                    fetch(searchTxt);
                }
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTxt = mSearchField.getText().toString();
                fetch(searchTxt);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;
        public TextView txtTitle;
        public ImageView list_image;

        public ViewHolder(View itemView) {

            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.list_title);
            list_image = itemView.findViewById(R.id.list_image);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }

    }

    private void mainRecyclerView() {
        searchRecyclerView = findViewById(R.id.result_list);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerView.setHasFixedSize(true);
    }

    private void fetch(String searchText) {
        Query query = mDatabaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "uf8ff");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {

                                return new Model (snapshot.child("title").getValue().toString(),
                                snapshot.child("image").getValue().toString(),
                                        snapshot.child("video").getValue().toString());

                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ViewHolder holder, final int position, final Model model) {
                holder.setTxtTitle(model.getmTitle());

                Glide.with(SearchActivity.this)
                        .asBitmap()
                        .load(model.getmImage())
                        .into(holder.list_image);

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent k = new Intent(SearchActivity.this, VideoActivity.class);
                            k.putExtra("video", model.getmVideo());
                            startActivity(k);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        adapter.startListening();
        searchRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
        }
    }

}