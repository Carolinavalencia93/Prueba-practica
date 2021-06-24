package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.base.BaseApplication;
import co.com.ceiba.mobile.pruebadeingreso.dataAccess.SqliteRoutines;
import co.com.ceiba.mobile.pruebadeingreso.models.Users;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;
import co.com.ceiba.mobile.pruebadeingreso.rest.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainActivity extends Activity {

    private RecyclerView recyclerViewSearchResults;
    private RecyclerView_Adapter_view_users adapter;
    private JSONArray jsonArrayUsers = new JSONArray();
    private Users users;
    private EditText editTextSearch;
    private SqliteRoutines sqliteRoutines;
    private BaseApplication app;
    private Timer myTimer;
    private JSONArray jsonObjUser = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqliteRoutines = new SqliteRoutines(this);
        app = (BaseApplication) getApplicationContext();
        app.showProgressDialog("Por favor espere..", MainActivity.this);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setHasFixedSize(true);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(adapter);

        editTextSearch = findViewById(R.id.editTextSearch);

        editTextSearch.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }



                    @Override
                    public void afterTextChanged(final Editable s) {
                       String filtro = editTextSearch.getText().toString();

                       Log.v("Palabra", filtro);

                        adapter.getFilter().filter(filtro);
                        adapter.notifyDataSetChanged();



                    }
                }
        );

    }

    @Override
    protected void onStart() {
        jsonArrayUsers = new JSONArray();
        JSONArray jsonObjUser = sqliteRoutines.readUsers();
       if(jsonObjUser.length() == 0){
            loadUser();
        }else {
            loaderUsers();
        }

        super.onStart();


    }

    private void loaderUsers() {

        JSONArray jsonObjUser = sqliteRoutines.readUsers();

        for (int i = 0; i < jsonObjUser.length(); i++) {
            JSONObject jsonObjectUser = null;
            try {
                jsonObjectUser = jsonObjUser.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            jsonArrayUsers.put(jsonObjectUser);
            adapter = new RecyclerView_Adapter_view_users(MainActivity.this, jsonArrayUsers);
            recyclerViewSearchResults.setAdapter(adapter);
            app.hideProgressDialog();
        }

    }

    private void loadUser() {

        Endpoints endpoints = RetrofitClientInstance.getRetrofitInstance().create(Endpoints.class);

        Call<String> call = endpoints.users();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONArray jsonArrayResponse = new JSONArray(response.body());


                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        JSONObject jsonObjectUser = jsonArrayResponse.getJSONObject(i);
                        jsonArrayUsers.put(jsonObjectUser);

                        saveUsers(jsonObjectUser.getInt("id"),
                                  jsonObjectUser.getString("name"),
                                  jsonObjectUser.getString("email"),
                                  jsonObjectUser.getString("phone"));
                    }
                    adapter = new RecyclerView_Adapter_view_users(MainActivity.this, jsonArrayUsers);
                    recyclerViewSearchResults.setAdapter(adapter);
                    app.hideProgressDialog();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        R.string.internet,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUsers(int id, String name, String email, String phone) {
        users= new Users(id, name, email, phone);
        sqliteRoutines.saveUsers(users);
    }


    public class RecyclerView_Adapter_view_users extends RecyclerView.Adapter<RecyclerView_Adapter_view_users.ViewHolder> implements Filterable {
        private JSONArray tagsList = null;
        private JSONArray ArrayListUnits;
        private Context context;
		LayoutInflater layoutInflater;
        private  int LEFT_CELL = 0;
        private  int RIGHT_CELL = 1;

        public RecyclerView_Adapter_view_users(Context context, JSONArray modelList) {
            this.tagsList = modelList;
            this.ArrayListUnits = tagsList;
            this.context = context;
			layoutInflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name,phone,email;
            Button btn_view_post;

            public ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.name);
                phone = itemView.findViewById(R.id.phone);
                email = itemView.findViewById(R.id.email);
                btn_view_post = itemView.findViewById(R.id.btn_view_post);

            }
        }

        @Override
        public RecyclerView_Adapter_view_users.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if(RIGHT_CELL == viewType)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
            else
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(RecyclerView_Adapter_view_users.ViewHolder Vholder, int position) {
            try {
                if (tagsList.length() > 0){
                    JSONObject tag = tagsList.getJSONObject(position);
                    Vholder.name.setText(tag.getString("name"));
                    Vholder.phone.setText(tag.getString("phone"));
                    Vholder.email.setText(tag.getString("email"));

                    Vholder.btn_view_post.setOnClickListener(view -> {

                        for (int i = 0; i < tagsList.length(); i++) {
                            if (i == position) {
                                try {
                                    users = new Users(
                                            tagsList.getJSONObject(i).getInt("id"),
                                            tagsList.getJSONObject(i).getString("name"),
                                            tagsList.getJSONObject(i).getString("phone"),
                                            tagsList.getJSONObject(i).getString("email")
                                    );

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        Intent intent = new Intent(MainActivity.this, PostActivity.class);
                        intent.putExtra("user", (Serializable) users);

                        startActivity(intent);
                    });
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return tagsList.length();
        }

        @Override
        public int getItemViewType(int position) {


                if (tagsList.length() == 0){
                return LEFT_CELL;
            }else {
                    return RIGHT_CELL;
                }


        }

        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    try {
                        String charSequenceString = constraint.toString();
                        if (charSequenceString.isEmpty()) {
                            tagsList = ArrayListUnits;
                        } else {
                            JSONArray filteredList = new JSONArray();
                            for (int index = 0; index < ArrayListUnits.length(); index++) {
                                JSONObject tags = ArrayListUnits.getJSONObject(index);
                                String name = tags.getString("name");
                                Log.v("nombre", name);
                                if (name.toLowerCase().contains(charSequenceString.toLowerCase())) {
                                    filteredList.put(tags);
                                    tagsList = filteredList;
                                }else {
                                    if(filteredList.length() == 0){
                                        tagsList = filteredList;
                                    }


                                }

                               // tagsList = filteredList;
                            }

                        }
                        FilterResults results = new FilterResults();
                        results.values = tagsList;

                        return results;
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    tagsList = (JSONArray) results.values;

                        notifyDataSetChanged();


                }
            };
        }
    }



}
