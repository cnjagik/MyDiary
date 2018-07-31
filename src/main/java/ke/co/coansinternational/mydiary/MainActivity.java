package ke.co.coansinternational.mydiary;

import android.content.ContentUris;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ke.co.coansinternational.mydiary.data.FirebaseGetterAdapter;
import ke.co.coansinternational.mydiary.data.GetAllData;
import ke.co.coansinternational.mydiary.data.MyDairyPojo;
import ke.co.coansinternational.mydiary.data.NotesDB;
import ke.co.coansinternational.mydiary.data.RecyclerAdapter;

import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    ImageButton deleteme, editme;
    NotesDB notesDB;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    SQLiteDatabase mDatabase;
    DatabaseReference databaseReference;
    List<GetAllData> getAllData = new ArrayList<>();
    ArrayList<String> keylist = new ArrayList<>();
    List<MyDairyPojo> list = new ArrayList<>();
    FirebaseUser user;
    MyDairyPojo dataupload;
    RecyclerView.Adapter adapter;
    String dateget, titleget, notesget, Fdate, Fnote, FTitle;
    private Uri mCurrentNotesUri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayout = findViewById(R.id.linearl);
        android.support.design.widget.FloatingActionButton mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
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
            list.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference("My Diary").child(user.getUid());
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
                            editme = v.findViewById(R.id.editb);
                            deleteme.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Collections.reverse(keylist);
                                    String x = keylist.get(position);
                                    databaseReference.child(x).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MainActivity.this, "Online Note Deleted", Toast.LENGTH_SHORT).show();
                                                    getOnlineData();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            editme.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final MyDairyPojo getAllData1 = list.get(position);
                                    Fdate = getAllData1.getDairydate();
                                    FTitle = getAllData1.getDiarytitle();
                                    Fnote = getAllData1.getDiarynote();
                                    Collections.reverse(keylist);
                                    String k = keylist.get(position);
                                    Intent updateIn = new Intent(MainActivity.this, AddNote.class);
                                    updateIn.putExtra("Date", Fdate);
                                    updateIn.putExtra("Title", FTitle);
                                    updateIn.putExtra("Notes", Fnote);
                                    updateIn.putExtra("Key", k);
                                    startActivity(updateIn);
                                }
                            });
                        }
                    });

                    recyclerView.setAdapter(adapter);
                    Collections.reverse(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void getlocalDB() {
        getAllData.clear();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), getAllData, new RecyclerAdapter.CustomItemClickListenList() {
            @Override
            public void onItemClick(View v, final int position) {
                editme = v.findViewById(R.id.editb);
                editme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final GetAllData getAllData1 = getAllData.get(position);
                        long id = getAllData1.getId();
                        mCurrentNotesUri = ContentUris.withAppendedId(CONTENT_URI, id);
                        Intent updateIn = new Intent(MainActivity.this, AddNote.class);
                        dateget = getAllData1.getNDate();
                        titleget = getAllData1.getNTitle();
                        notesget = getAllData1.getNDetails();
                        updateIn.putExtra("Date", dateget);
                        updateIn.putExtra("Title", titleget);
                        updateIn.putExtra("Notes", notesget);
                        updateIn.setData(mCurrentNotesUri);
                        startActivity(updateIn);
                    }
                });
            }
        });

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
        recyclerView.setAdapter(recyclerAdapter);
        Collections.reverse(getAllData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_one, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
