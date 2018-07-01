package ke.co.coansinternational.mydiary.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ke.co.coansinternational.mydiary.AddNote;
import ke.co.coansinternational.mydiary.R;

public class FirebaseGetterAdapter extends RecyclerView.Adapter<FirebaseGetterAdapter.Viewholder> {
    Context mcontext;
    List<MyDairyPojo> list;
    CustomItemClickListenList listener;

    public FirebaseGetterAdapter(Context mcontext, List<MyDairyPojo> list, CustomItemClickListenList listener) {
        this.mcontext = mcontext;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.detailslayout, parent, false);
        final Viewholder viewHolder = new Viewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        final MyDairyPojo myDairyPojo = list.get(position);
        holder.date.setText(myDairyPojo.getDairydate());
        holder.title.setText(myDairyPojo.getDiarytitle());
        holder.notes.setText(myDairyPojo.getDiarynote());
        holder.editme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, AddNote.class);
                i.putExtra("title", myDairyPojo.getDiarytitle());
                i.putExtra("details", myDairyPojo.getDiarynote());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CustomItemClickListenList {
        void onItemClick(View v, int position);
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView date, title, notes;
        ImageButton deleteme, editme;

        public Viewholder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.datetoday);
            title = itemView.findViewById(R.id.titledisplay);
            notes = itemView.findViewById(R.id.detailed);
            deleteme = itemView.findViewById(R.id.deleteb);
            editme = itemView.findViewById(R.id.editb);
        }
    }

}
