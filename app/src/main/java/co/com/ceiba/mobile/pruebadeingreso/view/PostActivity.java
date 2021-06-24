package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.models.Users;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;
import co.com.ceiba.mobile.pruebadeingreso.rest.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends Activity {

    public TextView  name, email, phone;
    private int idUser;
    private Users user;
    private RecyclerView recyclerViewPostsResults;
    private RecyclerView.LayoutManager recyclerViewLayoutManagerPostsResults;
    private RecyclerView_Adapter_view_users_post adapterPostsResults;
    private JSONArray jsonArrayUsersPost = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        recyclerViewPostsResults = findViewById(R.id.recyclerViewPostsResults);
        recyclerViewPostsResults.setHasFixedSize(true);
        recyclerViewPostsResults.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        user = (Users)i.getSerializableExtra("user");
        idUser = user.getId();
        name.setText(user.getname());
        email.setText(user.getEmail());
        phone.setText(user.getphone());
    }

    @Override
    protected void onStart() {
        postsUser(idUser);
        super.onStart();
    }

    private void postsUser(int idUser) {
        Endpoints endpoints = RetrofitClientInstance.getRetrofitInstance().create(Endpoints.class);

        Call<String> call = endpoints.postUser(idUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONArray jsonArrayResponseUsersPost = new JSONArray(response.body());


                    for (int i = 0; i < jsonArrayResponseUsersPost.length(); i++) {
                        JSONObject jsonObjectUsersPost = jsonArrayResponseUsersPost.getJSONObject(i);
                        jsonArrayUsersPost.put(jsonObjectUsersPost);

                    }
                    adapterPostsResults = new RecyclerView_Adapter_view_users_post(PostActivity.this, jsonArrayUsersPost);
                    recyclerViewPostsResults.setAdapter(adapterPostsResults);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PostActivity.this,
                        R.string.internet,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

        public class RecyclerView_Adapter_view_users_post extends RecyclerView.Adapter<RecyclerView_Adapter_view_users_post.ViewHolder> {
        private JSONArray userList;
        private JSONArray ArrayListUnits;
        private Context context;
        LayoutInflater layoutInflater;

        public RecyclerView_Adapter_view_users_post(Context context, JSONArray modelList) {
            this.userList = modelList;
            this.ArrayListUnits = userList;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title,body;

            public ViewHolder(View v) {
                super(v);
                title = v.findViewById(R.id.title);
                body = itemView.findViewById(R.id.body);

            }
        }

        @Override
        public RecyclerView_Adapter_view_users_post.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(context).inflate(R.layout.post_list_item, parent, false);
            RecyclerView_Adapter_view_users_post.ViewHolder viewHolder1 = new RecyclerView_Adapter_view_users_post.ViewHolder(view1);
            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(RecyclerView_Adapter_view_users_post.ViewHolder Vholder, int position) {
            try {
                JSONObject tag = userList.getJSONObject(position);
                Vholder.title.setText(tag.getString("title"));
                Vholder.body.setText(tag.getString("body"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return userList.length();
        }

    }

}
