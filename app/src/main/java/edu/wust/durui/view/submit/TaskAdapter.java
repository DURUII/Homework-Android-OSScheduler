package edu.wust.durui.view.submit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.wust.durui.R;
import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    //launcher
    public interface OnClickListener {
        void peep();

        void clear();

        void delete();
    }

    public static OnClickListener mOnClickListener;
    private List<Task> list;
    private int position;

    public TaskAdapter(OnClickListener mOnClickListener) {
        TaskAdapter.mOnClickListener = mOnClickListener;
    }

    // inner-class: placeholder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // one view-holder corresponds to one line/item-view
        CardView root;
        TextView id;
        TextView submittedTime;
        TextView requiredTime;
        TextView priority;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = (CardView) itemView.findViewById(R.id.item_root);
            id = (TextView) itemView.findViewById(R.id.id_text);
            submittedTime = (TextView) itemView.findViewById(R.id.submitted_time_text);
            requiredTime = (TextView) itemView.findViewById(R.id.required_time_text);
            priority = (TextView) itemView.findViewById(R.id.priority_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // instantiate a view via item.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        ViewHolder holder = new ViewHolder(view);
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                position = holder.getAdapterPosition();
                return false;
            }
        });
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                mOnClickListener.peep();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind date with the view specified by position
        Task task = list.get(position);
        holder.id.setText(task.getId() + "");
        holder.submittedTime.setText(Clock.generatePatternFromMinutes(task.getJobSubmitTime()));
        holder.requiredTime.setText(Clock.generatePatternFromMinutes(task.getEstimatedRequireTime()));
        holder.priority.setText(task.getPriority() + "");
    }

    public void update(List<Task> list) {
        if (this.list != null) {
            this.list.clear();
        }
        this.list = list;
        // last resort
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        // size of items
        return list.size();
    }

    public int getPosition() {
        return position;
    }
}
