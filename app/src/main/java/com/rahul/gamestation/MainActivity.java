package com.rahul.gamestation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    FirebaseRecyclerAdapter adapter;

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (user != null) {
            // User is signed in
            Toast.makeText(getApplicationContext(), "Sign in", Toast.LENGTH_SHORT).show();
            Log.e("signin", String.valueOf(user));

        } else {
            // No user is signed in
            Toast.makeText(getApplicationContext(), "not sign in", Toast.LENGTH_SHORT).show();
            Log.e("not signin", String.valueOf(user));

        }

        configureToolbar();

        mainRecyclerView();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        mainRecyclerView = findViewById(R.id.main_list);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        mainRecyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerView.setHasFixedSize(true);

        fetch();
    }

    private void fetch() {
//        Log.e("TAG","fetch");
        final Query query = mDatabaseReference;

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("title").getValue().toString(),
                                        snapshot.child("image").getValue().toString(),
                                        snapshot.child("video").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_item, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ViewHolder holder, final int position, final Model model) {

                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(model.getmImage())
                        .into(holder.list_image);

                holder.setTxtTitle(model.getmTitle());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent k = new Intent(MainActivity.this, VideoActivity.class);
                            k.putExtra("video", model.getmVideo());
                            startActivity(k);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        mainRecyclerView.setAdapter(adapter);
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_settings_black_24dp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_new:
                Intent k = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(k);
                return true;

            case R.id.add_new:
                if(user!=null){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to Create new entry")
                            .setMessage("Add your game to the list")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this, AddActivity.class));
                                    finish();
//                                    Toast.makeText(getApplicationContext(), "you are Sign Out", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    Toast.makeText(getApplicationContext(), "Still Sign in", Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("You Need to Sign In first")
                            .setMessage("do you want to sign in to add your game")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                    Toast.makeText(getApplicationContext(), "you are Sign Out", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    Toast.makeText(getApplicationContext(), "Still Sign in", Toast.LENGTH_LONG).show();
                }
                return true;

            case android.R.id.home:
                if(user!=null){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to SIGN OUT")
                            .setMessage("you will be redirected to login page")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                    Toast.makeText(getApplicationContext(), "you are Sign Out", Toast.LENGTH_LONG).show();
                                }
                                })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    Toast.makeText(getApplicationContext(), "Still Sign in", Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to SIGN IN")
                            .setMessage("you will be redirected to login page")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                    Toast.makeText(getApplicationContext(), "you are Sign Out", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    Toast.makeText(getApplicationContext(), "Still Sign in", Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();

    }
}
