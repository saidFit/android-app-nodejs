package com.example.app_node_js;

import org.json.JSONArray;
import org.json.JSONObject;

public interface TodosCallback {
    void onTodosReceived(JSONArray todos);
    void onTodoReceived(JSONObject todo);
    void onError(String message);
}
