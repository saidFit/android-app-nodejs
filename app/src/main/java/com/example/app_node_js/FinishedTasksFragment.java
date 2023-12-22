package com.example.app_node_js;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

public class FinishedTasksFragment extends Fragment implements TodoAdapter.OnTodoFinishedListener {
    RecyclerView recyclerView;
    TodoAdapter todoAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished_tasks, container, false);
        recyclerView = view.findViewById(R.id.todoRecyclerViewFinished);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        todoAdapter = new TodoAdapter((TodoAdapter.OnTodoFinishedListener) FinishedTasksFragment.this); // Create your custom adapter

        retrieveAndDisplayTodos();

        // Inflate the layout for this fragment

        recyclerView.setAdapter(todoAdapter);

        return view;
    }



    void retrieveAndDisplayTodos() {
        // Define your GET_TODOS_URL
        final String GET_TODOS_URL = "https://node-app-test-vm47.onrender.com/todo/todosFinished";

        // Call the method to retrieve todos with authorization
        Requests.getTodosWithAuthorization(requireContext(), GET_TODOS_URL, new TodosCallback() {
            @Override
            public void onTodosReceived(JSONArray todos) {
                // Parse and display the todos
                todoAdapter.setTodos(todos,true);
                Log.d("todos", String.valueOf(todos));
            }

            @Override
            public void onTodoReceived(JSONObject todo) {

            }

            @Override
            public void onError(String message) {
                // Handle the error
            }
        });
    }

    @Override
    public void onTodoFinished(String todoId) {
          return;
    }

    @Override
    public void onTodoEdit(JSONObject todo) {
        return;
    }
}