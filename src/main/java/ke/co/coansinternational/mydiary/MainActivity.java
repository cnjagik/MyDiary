package ke.co.coansinternational.mydiary;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ke.co.coansinternational.mydiary.data.FirebaseGetterAdapter;
import ke.co.coansinternational.mydiary.data.GetAllData;
import ke.co.coansinternational.mydiary.data.MyDairyPojo;
import ke.co.coansinternational.mydiary.data.NotesDB;
import ke.co.coansinternational.mydiary.data.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    ImageButton deleteme;
    NotesDB notesDB;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    FloatingActionButton floatingActionButton1;
    SQLiteDatabase mDatabase;
    DatabaseReference databaseReference;
    List<GetAllData> getAllData = new ArrayList<>();
    ArrayList<String> keylist = new ArrayList<>();
    List<MyDairyPojo> list = new ArrayList<>();
    FirebaseUser user;
    MyDairyPojo dataupload;
    RecyclerView.Adapter adapter;
    private Uri mCurrentNotesUri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1);
        linearLayout = findViewById(R.id.linearl);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNote.class));
            }
        });

        user = mAuth.getCurrentUser();
        getlocalDB();
    }

    private void getOnlineData() {
        if (user != null) {
            getSupportActionBar().setTitle("My Diary - Online");
            list.clear();
            keylist.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference("My Diary");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        dataupload = postSnapshot.getValue(MyDairyPojo.class);
                        keylist.add(postSnapshot.getRef().getKey());
                        list.add(dataupload);
                        if (list == null) {
                            linearLayout.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                    adapter = new FirebaseGetterAdapter(getApplicationContext(), list, new FirebaseGetterAdapter.CustomItemClickListenList() {
                        @Override
                        public void onItemClick(View v, final int position) {
                            deleteme = v.findViewById(R.id.deleteb);
                            deleteme.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                    databaseReference.child(keylist.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                            keylist.remove(position);
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void getlocalDB() {
        getSupportActionBar().setTitle("My Diary - Offline");
        getAllData.clear();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), getAllData);
        recyclerView.setAdapter(recyclerAdapter);
        notesDB = new NotesDB(getApplicationContext());
        mDatabase = notesDB.getWritableDatabase();
        final List<GetAllData> m = notesDB.getallUserData();
        notesDB.close();
        if (m.size() > 0) {
            for (int i = 0; i < m.size(); i++) {
                m.get(i).getNDate();
                getAllData.add(m.get(i));
                recyclerAdapter.notifyDataSetChanged();
            }
            if (getAllData != null) {
                linearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_one, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            case R.id.OnlineData:
                getAllData.clear();
                linearLayout.setVisibility(View.VISIBLE);
                getOnlineData();
                return true;
            case R.id.offlinedata:
                list.clear();
                linearLayout.setVisibility(View.VISIBLE);
                getlocalDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
