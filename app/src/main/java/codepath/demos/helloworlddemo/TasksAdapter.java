package codepath.demos.helloworlddemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter  extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position); // The class implementing this needs to know where the long press is done
    }

    List<String> tasks;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public TasksAdapter(List<String> tasks, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.tasks = tasks;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We'll create a new view and wrap it inside a viewholder
        //Use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        // Wrap inside
        return new ViewHolder(todoView);
    }

    // Binds data to a particular viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Grab the item at a position
        String task = tasks.get(position);
        // Bind item into a specified view holder
        holder.bind(task); // Bind writes inside view holder class
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    // Container for easy access to view

    class ViewHolder extends RecyclerView.ViewHolder{

        //Looking into simple_list_item_1
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // Update the view inside the ViewHolder with this data
        public void bind(String task) {
            tvItem.setText(task);
            tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Remove the item from the recycler view
                    // This is tougher than adding because we need to report into Adapter from main
                    // We're adding an interface here which will be implemented in main
                    longClickListener.onItemLongClicked(getAdapterPosition()); // Tell the listener which position
                    return true;
                }
            });
        };
    }
}
