package com.example.app_node_js;// TodoAdapter.java

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_node_js.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private JSONArray todos;
    private boolean finished;
    private OnTodoFinishedListener listener;
    public TodoAdapter(OnTodoFinishedListener listener) {
        this.todos = new JSONArray();
        this.listener = listener;
    }



    public interface OnTodoFinishedListener {
        void onTodoFinished(String todoId);
        void onTodoEdit(JSONObject todo);
    }
    public void setTodos(JSONArray todos,boolean finished) {
        this.todos = todos;
        this.finished = finished;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if(!finished){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.todo_item, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.todo_item_finished,parent,false);
        }


        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        try {
            JSONObject todo = todos.getJSONObject(position);
            String title = todo.getString("title");
            String id = todo.getString("_id");
            String description = todo.getString("desc");

            holder.titleTextView.setText(title);
            holder.descriptionTextView.setText(description);

            // Add a click listener to the ImageView to toggle the visibility of the LinearLayout
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentVisibility = holder.linearLayout.getVisibility();
                    if (currentVisibility == View.VISIBLE) {
                        holder.linearLayout.setVisibility(View.GONE);
                    } else {
                        holder.linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.toFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        listener.onTodoFinished(id);

//                    FinishedTodoDialog addTodoDialog = new FinishedTodoDialog(id);
//                    addTodoDialog.show(((AppCompatActivity)view.getContext()).getSupportFragmentManager(), "finished task");
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject todo = new JSONObject(); //You should create a new JSONObject and then set its values:
                    try {
                        todo.put("id",id);
                        todo.put("title",title);
                        todo.put("desc",description);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("to", String.valueOf(todo));
                    listener.onTodoEdit(todo);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return todos.length();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < todos.length()) {
            todos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public JSONObject getTodoAtPosition(int position){

        // the position is index of item that i want to retrieve;

          try {
              return todos.getJSONObject(position);
          } catch (JSONException e) {
              throw new RuntimeException(e);
          }

    }
    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView;
        LinearLayout linearLayout;
        ImageView toFinished;
        ImageView edit;
        ImageView delete;
        TodoViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.titleView);
            descriptionTextView = view.findViewById(R.id.descriptionView);
            imageView = view.findViewById(R.id.down);
            linearLayout = view.findViewById(R.id.layoutManagement);
            toFinished = view.findViewById(R.id.finishedTodo);
            edit = view.findViewById(R.id.editTodo);
            delete = view.findViewById(R.id.deleteTodo);

        }
    }
}
