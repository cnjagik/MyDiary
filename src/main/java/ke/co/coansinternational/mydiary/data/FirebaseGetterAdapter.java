package ke.co.coansinternational.mydiary.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ke.co.coansinternational.mydiary.R;

public class FirebaseGetterAdapter extends RecyclerView.Adapter<FirebaseGetterAdapter.Viewholder> {
    private Context mcontext;
    private List<MyDairyPojo> list;
    private CustomItemClickListenList listener;

    public FirebaseGetterAdapter(Context mcontext, List<MyDairyPojo> list, CustomItemClickListenList listener) {
        this.mcontext = mcontext;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.layouttry, parent, false);
        final Viewholder viewHolder = new Viewholder(view);
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
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        final MyDairyPojo myDairyPojo = list.get(position);
        holder.date.setText(myDairyPojo.getDairydate());
        holder.title.setText(myDairyPojo.getDiarytitle());
        holder.notes.setText(myDairyPojo.getDiarynote());
        holder.notes.setVisibility(View.GONE);
        holder.onoff.setText(R.string.online);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CustomItemClickListenList {
        void onItemClick(View v, int position);
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView date, title, notes, onoff;
        ImageButton deleteme, editme;

        Viewholder(View itemView) {
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
