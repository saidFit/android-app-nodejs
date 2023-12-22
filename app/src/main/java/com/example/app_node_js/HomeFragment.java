package com.example.app_node_js;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HomeFragment extends Fragment implements
        AddTodoDialogFragment.OnTodoAddedListener,
        FinishedTodoDialogFragment.OnTodoAddedListener,
        TodoAdapter.OnTodoFinishedListener
{

    RecyclerView recyclerView;
    TodoAdapter todoAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.todoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todoAdapter = new TodoAdapter((TodoAdapter.OnTodoFinishedListener) HomeFragment.this); // Create your custom adapter

        retrieveAndDisplayTodos();

        
        View fab = view.findViewById(R.id.fabButton1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTodoDialogFragment addTodoDialog = new AddTodoDialogFragment(false);
                addTodoDialog.show(getFragmentManager(), "add_todo_dialog1");
                addTodoDialog.setOnTodoAddedListener(HomeFragment.this); // Set the listener to this fragment
            }
        });


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe event to delete the item
                int position = viewHolder.getAdapterPosition();
                String DELETE_TODO_URL=null;

                // Ensure the position is valid
                if (position != RecyclerView.NO_POSITION) {
                    // Remove the item from your data source
                    JSONObject todo = todoAdapter.getTodoAtPosition(position);
                    // Extract the properties you need;
                    String id;
                    try {
                        id = todo.getString("_id");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                     DELETE_TODO_URL = "https://node-app-test-vm47.onrender.com/todo/deleteTodo/"+id;

//                    todoAdapter.removeItem(position);
//                    // Notify the adapter about the item removal
//                    todoAdapter.notifyItemRemoved(position);

                    // Optionally, make a request to delete the item on the server.
                    // Replace TODO: with your code to send a delete request.
                    // Example:
                    // String todoId = todoAdapter.getTodoId(position);
                    // sendDeleteRequestToServer(todoId);
                }

                if(DELETE_TODO_URL != null){
                    Requests.deleteTodo(requireContext(), DELETE_TODO_URL, new TodosCallback() {
                        @Override
                        public void onTodosReceived(JSONArray todos) {
                            retrieveAndDisplayTodos();
                        }

                        @Override
                        public void onTodoReceived(JSONObject todo) {

                        }

                        @Override
                        public void onError(String message) {

                        }
                    });
                }



                switch (direction){
                    case ItemTouchHelper.RIGHT:
                        break;
                    case ItemTouchHelper.LEFT:
                        break;
                }

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                // Hide the "Delete" button when the swipe is cleared
                TodoAdapter.TodoViewHolder todoViewHolder = (TodoAdapter.TodoViewHolder) viewHolder;

            }

            @Override

            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){


                float threshold = (float) (recyclerView.getWidth() * 1.20); // Example: 25% of the item's width

                if (dX < -threshold) {
                    // The item is moved enough to the left for removal
                    // Provide visual cues if needed, like changing the background color
                    // viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_warning));

                    if (isCurrentlyActive) {
                        // The user has released the item, and it's active (swipe complete)
                        int position = viewHolder.getAdapterPosition();

                        // Ensure the position is valid
                        if (position != RecyclerView.NO_POSITION) {
                            // Remove the item from your data source
                            todoAdapter.removeItem(position);

                            // Notify the adapter about the item removal
                            todoAdapter.notifyItemRemoved(position);

                            // Optionally, make a request to delete the item on the server
                            // Replace TODO: with your code to send a delete request
                        }
                    }
                }

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_danger))
                        .addActionIcon(R.drawable.delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

// Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



        recyclerView.setAdapter(todoAdapter);
        return view;
    }



    void retrieveAndDisplayTodos() {
        // Define your GET_TODOS_URL
        final String GET_TODOS_URL = "https://node-app-test-vm47.onrender.com/todo/allTodos";

        // Call the method to retrieve todos with authorization
        Requests.getTodosWithAuthorization(requireContext(), GET_TODOS_URL, new TodosCallback() {
            @Override
            public void onTodosReceived(JSONArray todos) {
                // Parse and display the todos
                todoAdapter.setTodos(todos,false);
                Log.d("todos", String.valueOf(todos));
            }

            @Override
            public void onTodoReceived(JSONObject todo) {
                return;
            }

            @Override
            public void onError(String message) {
                // Handle the error
            }
        });
    }

    public void onTodoAdded() {
        // Refresh the RecyclerView by calling the method to retrieve and display todos
        retrieveAndDisplayTodos();
    }

    @Override
    public void onTodoFinished(String todoId) {
        FinishedTodoDialogFragment finishedTodoDialog = new FinishedTodoDialogFragment(todoId);
        finishedTodoDialog.show(getChildFragmentManager(),"finished_dialog");
        finishedTodoDialog.setOnTodoAddedListener(HomeFragment.this);
    }

    @Override
    public void onTodoEdit(JSONObject todo) {
        AddTodoDialogFragment addTodoDialogFragment = new AddTodoDialogFragment(true,todo);
        addTodoDialogFragment.show(getChildFragmentManager(),"edit_todo");
        addTodoDialogFragment.setOnTodoAddedListener(HomeFragment.this);

    }
}
