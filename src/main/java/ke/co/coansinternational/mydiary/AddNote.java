package ke.co.coansinternational.mydiary;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import ke.co.coansinternational.mydiary.data.MyDairyPojo;

import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.COLUMN_DATE;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.COLUMN_NOTE;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.COLUMN_TITLE;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.CONTENT_URI;

public class AddNote extends AppCompatActivity {
    TextView datetoday;
    TextInputEditText myNote;
    EditText myTitle;
    Button saveb, discardb;
    DatabaseReference databaseReference;
    String Ndate, Nnotes, Ntitle, dateN;
    String myNotes, MyTitles;
    MyDairyPojo myDairyPojo;
    private Uri mCurrentNotesUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        datetoday = findViewById(R.id.datetoday);
        myNote = findViewById(R.id.myNotes);
        myTitle = findViewById(R.id.notesTitle);
        saveb = findViewById(R.id.saveb);
        discardb = findViewById(R.id.discardb);
        dateN = DateFormat.getDateInstance().format(new Date());
        datetoday.setText(dateN);
        MyTitles = getIntent().getStringExtra("title");
        myNotes = getIntent().getStringExtra("details");
        myTitle.setText(MyTitles);
        myNote.setText(myNotes);
        saveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveNote();
            }
        });
        discardb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNote.setText("");
                myTitle.setText("");
                startActivity(new Intent(AddNote.this, MainActivity.class));
            }
        });
    }

    public void SaveNote() {
        Ndate = datetoday.getText().toString().trim();
        Ntitle = myTitle.getText().toString().trim();
        Nnotes = myNote.getText().toString().trim();
        if (TextUtils.isEmpty(Ntitle)) {
            myTitle.setError("Required");
            return;
        } else if (TextUtils.isEmpty(Nnotes)) {
            myNote.setError("Required");
            return;
        }
        setDataToFirebase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, Ndate);
        values.put(COLUMN_NOTE, Nnotes);
        values.put(COLUMN_TITLE, Ntitle);
        if (mCurrentNotesUri == null) {
            // Insert a new incident into the provider, returning the content URI for the new incident.
            @SuppressWarnings("ConstantConditions") Uri newUri = getContentResolver().insert(CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error with saving data", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, "Data saved successfully ", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is existing data, so update the table with content URI: mCurrentNotesUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mcurrentNoteUri will already identify the correct row in the database that
            // we want to modify.
            @SuppressWarnings("ConstantConditions") int rowsAffected = getContentResolver().update(mCurrentNotesUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.failedUpdate),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.UpdateSuccess),
                        Toast.LENGTH_SHORT).show();
            }
        }
        myNote.setText("");
        myTitle.setText("");
        startActivity(new Intent(AddNote.this, MainActivity.class));
    }

    private void setDataToFirebase() {
        Ndate = datetoday.getText().toString().trim();
        Ntitle = myTitle.getText().toString().trim();
        Nnotes = myNote.getText().toString().trim();
        myDairyPojo = new MyDairyPojo(Ndate, Ntitle, Nnotes);
        myDairyPojo.setDairydate(Ndate);
        myDairyPojo.setDiarytitle(Ntitle);
        myDairyPojo.setDiarynote(Nnotes);
        databaseReference.child("My Diary").push().setValue(myDairyPojo);
    }
}
