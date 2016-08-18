package com.shituocheng.calcalculateapplication.com.test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private int pages;
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    public static final String ARGS_PAGE = "args_page";
    private RecyclerView recyclerView;

    public static BlankFragment newInstance(int page){
        // Required empty public constructor
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE,page);
        BlankFragment blankFragment = new BlankFragment();
        blankFragment.setArguments(args);
        return blankFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pages = getArguments().getInt(ARGS_PAGE);
        title.add("animated");
        title.add("attachments");
        title.add("debuts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blank, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        fetchData();
        return v;
    }

    public void fetchData(){
        new Thread(new Runnable() {
            HttpURLConnection connection;
            InputStream inputStream;
            @Override
            public void run() {
                String api = "https://api.dribbble.com/v1/"+"shots"+"?"+"list"+"="+title.get(pages -1 )+"&"+ "access_token=" + "aef92385e190422a5f27496da51e9e95f47a18391b002bf6b1473e9b601e6216";
                try {
                    connection = (HttpURLConnection)new URL(api).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        list.add(title);
                    }

                    if (getActivity() == null){
                        return;
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Adapter adapter = new Adapter(list);
                                recyclerView.setAdapter(adapter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

        private ArrayList<String> itemTitle = new ArrayList<>();

        public Adapter(ArrayList<String> itemTitle) {
            this.itemTitle = itemTitle;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,null);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            String title = itemTitle.get(position);
            holder.textView.setText(title);
        }

        @Override
        public int getItemCount() {
            return itemTitle.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView)itemView.findViewById(R.id.textView);
            }
        }


    }

}
