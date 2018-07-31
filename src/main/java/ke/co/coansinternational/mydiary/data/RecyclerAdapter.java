package ke.co.coansinternational.mydiary.data;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
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

import ke.co.coansinternational.mydiary.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private Uri mCurrentNotesUri;
    private List<GetAllData> getData;
    private CustomItemClickListenList listener;

    public RecyclerAdapter(Context mContext, List<GetAllData> getData, CustomItemClickListenList listener) {
        this.mContext = mContext;
        this.getData = getData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layouttry, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        final TextView notes;
        notes = view.findViewById(R.id.detailed);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notes.getVisibility() == View.GONE) {
                    notes.setVisibility(View.VISIBLE);
                } else {
                    notes.setVisibility(View.GONE);
                }
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final GetAllData getAllData1 = getData.get(position);
        holder.date.setText(getAllData1.getNDate());
        holder.title.setText(getAllData1.getNTitle());
        holder.notes.setText(getAllData1.getNDetails());
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
        holder.notes.setVisibility(View.GONE);
        holder.onoff.setText(R.string.offline);
    }

    private void deleteEntry() {
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

    public interface CustomItemClickListenList {
        void onItemClick(View v, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, title, notes, onoff;
        ImageButton deleteme, editme;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.datetoday);
            title = itemView.findViewById(R.id.titledisplay);
            notes = itemView.findViewById(R.id.detailed);
            deleteme = itemView.findViewById(R.id.deleteb);
            editme = itemView.findViewById(R.id.editb);
            onoff = itemView.findViewById(R.id.onlineoffline);
        }
    }


}
