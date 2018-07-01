package ke.co.coansinternational.mydiary.data;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ke.co.coansinternational.mydiary.AddNote;
import ke.co.coansinternational.mydiary.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public Context mContext;
    Uri mCurrentNotesUri;
    private List<GetAllData> getData;


    public RecyclerAdapter(Context mContext, List<GetAllData> getData) {
        this.mContext = mContext;
        this.getData = getData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.detailslayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final GetAllData getAllData1 = getData.get(position);
        holder.date.setText(getAllData1.getNDate());
        holder.title.setText(getAllData1.getNTitle());
        holder.notes.setText(getAllData1.getNDetails());

        holder.editme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, AddNote.class);
                i.putExtra("title", getAllData1.getNTitle());
                i.putExtra("details", getAllData1.getNDetails());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
        holder.deleteme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = getAllData1.getId();
                mCurrentNotesUri = ContentUris.withAppendedId(ContractClass.NotesEntry.CONTENT_URI, id);
                deleteEntry();
                getData.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    public void deleteEntry() {
        if (mCurrentNotesUri != null) {
            @SuppressWarnings("ConstantConditions") int rowsdeleted = mContext.getContentResolver().delete(mCurrentNotesUri, null, null);
            if (rowsdeleted == 0) {
                Toast.makeText(mContext, "Deletion failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return getData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, title, notes;
        ImageButton deleteme, editme;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.datetoday);
            title = itemView.findViewById(R.id.titledisplay);
            notes = itemView.findViewById(R.id.detailed);
            deleteme = itemView.findViewById(R.id.deleteb);
            editme = itemView.findViewById(R.id.editb);
        }
    }


}
