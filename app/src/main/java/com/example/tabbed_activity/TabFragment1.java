package com.example.tabbed_activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


public class TabFragment1 extends Fragment{//} implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private RecyclerImageTextAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ContactRecyclerItem> mMyData = new ArrayList<>();
    private View view;
    private Boolean isFABOpen = Boolean.FALSE;
    private FloatingActionButton refreshFab, addContactFab, postFab, getFab, resetFab;
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;

//    SwipeRefreshLayout swipeLayout;
    private void GetContactFromDB() throws ExecutionException, InterruptedException {
        new JSONGETTask().execute("http://143.248.38.76:4500/contacts").get();
    }
    private void PostContactToDB() throws ExecutionException, InterruptedException {
        new JSONTask().execute("http://143.248.38.76:4500/contacts/initialize").get();
    }
    private void ResetConatctAtDB() throws ExecutionException, InterruptedException {
        new JSONResetTask().execute("http://143.248.38.76:4500/contacts/reset").get();
    }
    private void DeleteContactAtDB(String phonenum) throws ExecutionException, InterruptedException{
        new JSONDeleteTask().execute("http://143.248.38.76:4500/contacts/phonenumber/"+phonenum).get();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Shared preferences
//        pref = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
//        editor = pref.edit();

        new JSONGETTask().execute("http://143.248.38.76:4500/contacts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, container, false);

//        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
//        swipeLayout.setOnRefreshListener(this);
//        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
//                getResources().getColor(android.R.color.holo_red_dark),
//                getResources().getColor(android.R.color.holo_blue_dark),
//                getResources().getColor(android.R.color.holo_orange_dark));

//        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(view);

        return view;
    }

//    @Override
//    public void onRefresh() {
//        try {
//            String get_result = new JSONGETTask().execute("http://143.248.38.76:4500/contacts").get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        mAdapter = new RecyclerImageTextAdapter(mMyData);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
////        스크롤바 추가부분
//        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
//        fastScroller.setRecyclerView(mRecyclerView);
//    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
//        initDataset();

//        mRecyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.scrollToPosition(0);
//
//        mAdapter = new RecyclerImageTextAdapter(mMyData);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
////        스크롤바 추가부분
//        if (mMyData != null) {
        mAdapter = new RecyclerImageTextAdapter(mMyData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListner(new RecyclerImageTextAdapter.onItemClickListner() {
            @Override
            public void onClick(String str) throws ExecutionException, InterruptedException {
                DeleteContactAtDB(str);
                Toast.makeText(getContext(), "delelted!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                GetContactFromDB();
            }
        });
        mAdapter.notifyDataSetChanged();
//        스크롤바 추가부분
        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        fastScroller.setRecyclerView(mRecyclerView);
//        }
//        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
//        fastScroller.setRecyclerView(mRecyclerView);

//        Button postbtn = (Button) view.findViewById(R.id.Postbtn);
//        Button getbtn = (Button) view.findViewById(R.id.getbtn);
//        Button resetbtn = view.findViewById(R.id.Resetbtn);
//
//        //버튼이 클릭되면 여기 리스너로 옴
//        postbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new JSONTask().execute("http://143.248.38.76:4500/contacts/initialize");//AsyncTask 시작시킴
//            }
//        });
//
//        getbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new JSONGETTask().execute("http://143.248.38.76:4500/contacts");
//            }
//        });
//
//        resetbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new JSONResetTask().execute("http://143.248.38.76:4500/contacts/reset");
////                Log.d("ERRORCATCHING", "ResetDone");
////                new JSONTask().execute("http://143.248.38.76:4500/contacts/initialize");
////                Log.d("ERRORCATCHING", "Init Done");
////                new JSONGETTask().execute("http://143.248.38.76:4500/contacts");
////                Log.d("ERRORCATCHING", "GetDone");
//            }
//        });

        FloatingActionButton fab_menu = view.findViewById(R.id.fab_menu);
        refreshFab = view.findViewById(R.id.fab_sync);
        addContactFab = view.findViewById(R.id.fab_addcontact);
        postFab = view.findViewById(R.id.fab_post);
        getFab = view.findViewById(R.id.fab_get);
        resetFab = view.findViewById(R.id.fab_reset);
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //연락처 추가 버튼
                if(!isFABOpen){
                    showFABMenu();
                }
                else{
                    closeFABMenu();
                }
            }
        });
//        FloatingActionButton fab_menu = view.findViewById(R.id.fab_menu);
//        refreshFab = view.findViewById(R.id.fab_sync);
//        addContactFab = view.findViewById(R.id.fab_addcontact);
//        fab_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { //연락처 추가 버튼
////                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
////                startActivity(intent);
//                showFABMenu();
//            }
//        });

    }
    private void showFABMenu(){
        isFABOpen=true;
        addContactFab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        addContactFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){//연락처 추가
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
//                refresh();
//                mAdapter = new RecyclerImageTextAdapter(mMyData);
//                mRecyclerView.setAdapter(mAdapter);
            }
        });
        refreshFab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PostContactToDB();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    GetContactFromDB();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                refresh();
//                Toast.makeText(getContext(), "하위", Toast.LENGTH_LONG).show();

            }
        });
        postFab.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        postFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    PostContactToDB();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        getFab.animate().translationY(-getResources().getDimension(R.dimen.standard_205));
        getFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    GetContactFromDB();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "mAdapter", Toast.LENGTH_LONG);
                Log.d("TOAST", "TODODODODOODODO");
            }
        });
        resetFab.animate().translationY(-getResources().getDimension(R.dimen.standard_255));
        resetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ResetConatctAtDB();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
//        fab2.animate().
//        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void refresh(){
        Log.d("ERRORCHECKING" ,"REFRESH");
        new JSONTask().execute("http://143.248.38.76:4500/contacts/initialize");//AsyncTask 시작시킴
//        new JSONGETTask().execute("http://143.248.38.76:4500/contacts");
    }


    private void closeFABMenu(){
        isFABOpen=false;
        refreshFab.animate().translationY(0);
        addContactFab.animate().translationY(0);
        postFab.animate().translationY(0);
        getFab.animate().translationY(0);
        resetFab.animate().translationY(0);
//        fab3.animate().translationY(0);
    }

    public void hohoho(String str) throws JSONException {
        JSONArray mArray = new JSONArray();
        try {
            mArray = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ERROR", String.valueOf(e));

            Log.d("ERROR", "HOHOHOexception");
            Log.d("ERROR", str);
        }
//        Log.d("CHECKCHECK", mArray.toString());
//        JSONObject obj;
//        for (int i = 0; i < mArray.length(); i ++){
//            obj = mArray.getJSONObject(i);
//        }
        ArrayList<ContactRecyclerItem> listdata = new ArrayList<ContactRecyclerItem>();
        for (int i = 0; i < mArray.length(); i++) {
            ContactRecyclerItem ct = new ContactRecyclerItem();
            ct.setName(mArray.getJSONObject(i).get("name").toString());
            ct.setPhone(mArray.getJSONObject(i).get("phonenumber").toString());
            ct.setIconID(Long.valueOf(mArray.getJSONObject(i).get("iconID").toString()));
            ct.setPersonID(Long.valueOf(mArray.getJSONObject(i).get("pID").toString()));
            Log.d("CHECKCHECK", mArray.getJSONObject(i).get("ImageStr").toString());

            if (mArray.getJSONObject(i).get("ImageStr").toString() == "null") {
                Log.d("CHECK", "imageStr == null ::::: " + mArray.getJSONObject(i).get("name").toString());
                Drawable drawable = getContext().getDrawable(R.drawable.default_icon);
                //if bm==null default_icon to bm4
                ct.setIcon(drawable);
                ct.setImageStr(null);
            } else {
                ct.setImageStr(mArray.getJSONObject(i).get("ImageStr").toString());
                ct.setIcon(null);
            }

            listdata.add(ct);
        }
        mMyData = listdata;
    }

    //Get
    public class JSONGETTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    try {
                        hohoho(buffer.toString());
                    } catch (JSONException e) {
                        Log.e("ERROR", "HOHOHO ERROR");
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getContext(), "Load Successful", Toast.LENGTH_SHORT).show();

        }
    }

    //Post
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.


                ContactRecyclerItem contactItem;
                for (int i = 0; i < mMyData.size(); i++) {
                    contactItem = mMyData.get(i);
                    Bitmap bm = loadContactPhoto(getActivity().getContentResolver(), contactItem.getPersonID(), contactItem.getIconID());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
                    bm.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
                    byte[] byteArray = stream.toByteArray() ;
                    String s = new String(byteArray, StandardCharsets.US_ASCII);
                    contactItem.setImageStr(s);
                    }


                JSONArray jsonArray;// = new JSONArray();
                JSONObject jsonOb = ArrListToJObj(initDataset(), "test");
                Log.d("CHECK", String.valueOf(jsonOb));
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.accumulate("todoid", 14);
//                jsonObject.accumulate("content", "yun");
//                jsonObject.accumulate("completed", "false");
//                for (int i=0; i<jsonOb.length(); i++){
//                    JSONArray
                jsonArray = (JSONArray) jsonOb.get("ContactData");
//                    jsonArray.put(ho);
//                }
                Log.d("CONTACT", jsonArray.toString());

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
//                    writer.write(jsonArray.toString());
                    writer.write(jsonArray.toString());
                    Log.d("BUFFER", jsonArray.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부분
            Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
//            str = result;
        }

    }

    //Reset
    public class JSONResetTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("DELETE");
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    Log.d("ERRORRCHECKRESET", buffer.toString());
                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
//                    try {
//                        //버퍼를 닫아준다.
//                        if (reader != null) {
//                            reader.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getContext(), "Reset Successful", Toast.LENGTH_SHORT).show();
        }
    }

    //Delete
    public class JSONDeleteTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("DELETE");
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    Log.d("DELETE PHONENUM", buffer.toString());
                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
//                    try {
//                        //버퍼를 닫아준다.
//                        if (reader != null) {
//                            reader.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ContactRecyclerItem> initDataset() {
        ArrayList<ContactRecyclerItem> mmMyData = getContactList(); //폰에 저장된 연락처 리스트를 가져오는 함수(mMyData는 ArrayList<ContactRecyclerItem> type)
        ContactRecyclerItem contactItem;

        //연락처 icon을 drawable type으로 변환하는 부분. getContactList에서 진행해도 무관한 부분이다.
        for (int i = 0; i < mmMyData.size(); i++) {
            contactItem = mmMyData.get(i);


            Drawable drawable;
            Bitmap bm = loadContactPhoto(getActivity().getContentResolver(), contactItem.getPersonID(), contactItem.getIconID());
            if (bm == null) {
                drawable = null;
                contactItem.setIcon(drawable);
                contactItem.setImageStr(null);
            } else {
                drawable = new BitmapDrawable(getResources(), bm);
                contactItem.setIcon(drawable);

                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                String str = org.bson.internal.Base64.encode(bitmapdata);
                contactItem.setImageStr(str);
            }

//            Drawable temp = contactItem.getIcon();
//            if(temp == null){
//                contactItem.setImageStr(null);
//            } else {
//                Bitmap bitmap = ((BitmapDrawable) temp).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] bitmapdata = stream.toByteArray();
//                String str = org.bson.internal.Base64.encode(bitmapdata);
//                contactItem.setImageStr(str);
//            }

//            Drawable drawable;
//            Bitmap bm = loadContactPhoto(getActivity().getContentResolver(), contactItem.getPersonID(), contactItem.getIconID());
//            if (bm == null) {
//                drawable = getContext().getDrawable(R.drawable.default_icon);
//                //if bm==null default_icon to bm4
//                bm = DrawableToBitmap(drawable);
//            } else {
//                drawable = new BitmapDrawable(getResources(), bm);
//            }
//            contactItem.setIcon(drawable);
//            if(bm == null){
//                contactItem.setImageStr(null);
//            }
//            else {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                String str = org.bson.internal.Base64.encode(byteArray);
//                contactItem.setImageStr(str);
//            }
        }
        return mmMyData;
    }

    private Bitmap DrawableToBitmap(Drawable drawable) {
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }

    public ArrayList<ContactRecyclerItem> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, sortOrder);
        ArrayList<ContactRecyclerItem> contactItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            //저장된 연락처를 하나씩 가져와 각 정보들을 ContactRecyclerItem type으로 변환, contactItems에 추가한다.
            do {
                long photo_id = cursor.getLong(2);
                long person_id = cursor.getLong(3);
                ContactRecyclerItem contactItem = new ContactRecyclerItem();
                contactItem.setName(cursor.getString(1));
                contactItem.setPhone(cursor.getString(0));
                contactItem.setIconID(photo_id);
                contactItem.setPersonID(person_id);

                contactItems.add(contactItem);
            } while (cursor.moveToNext());
        }
        return contactItems;
    }

    //연락처 사진 load시에 사용.
    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input != null)
            return resizingBitmap(BitmapFactory.decodeStream(input));

        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        if (photoBytes != null)
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));

        return null;
    }

    //loadContactPhoto에서 사용.
    public Bitmap resizingBitmap(Bitmap oBitmap) {
        if (oBitmap == null)
            return null;
        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 200;
        Bitmap rBitmap = null;
        if (width > resizing_size) {
            float mWidth = (float) (width / 100);
            float fScale = (float) (resizing_size / mWidth);
            width *= (fScale / 100);
            height *= (fScale / 100);
        } else if (height > resizing_size) {
            float mHeight = (float) (height / 100);
            float fScale = (float) (resizing_size / mHeight);
            width *= (fScale / 100);
            height *= (fScale / 100);
        }

        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int) width, (int) height, true);
        return rBitmap;
    }

    //ArrayList type을 JSONObject type으로 변환하는 함수.
    public JSONObject ArrListToJObj(ArrayList<ContactRecyclerItem> arrList, String name) {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();
            for (int i = 0; i < arrList.size(); i++) {

                ContactRecyclerItem contactItem;
                contactItem = arrList.get(i);

                JSONObject sObj = new JSONObject();
                sObj.put("name", contactItem.getName());
                sObj.put("phonenumber", contactItem.getPhone());
                sObj.put("iconID", contactItem.getIconID());
                sObj.put("pID", contactItem.getPersonID());
                sObj.put("ImageStr", contactItem.getImageStr());
                sObj.put("iconDrawable", contactItem.getIcon());
                jArray.put(sObj);
            }
            obj.put("filename", name);
            obj.put("ContactData", jArray);

            System.out.println(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}


//    //ArrayList type을 JSONArray type으로 변환하는 함수.
//    public JSONArray ArrListToJArr(ArrayList<ContactRecyclerItem> arrList) {
//        JSONArray jArray = new JSONArray();
//        try {
//
//            for (int i = 0; i < arrList.size(); i++) {
//
//                ContactRecyclerItem contactItem;
//                contactItem = arrList.get(i);
//
//                JSONObject sObj = new JSONObject();
//                sObj.put("name", contactItem.getName());
//                sObj.put("phonenumber", contactItem.getPhone());
//                sObj.put("iconID", contactItem.getIconID());
//                sObj.put("pID", contactItem.getPersonID());
//
//                jArray.put(sObj);
//            }
//
//            System.out.println(jArray.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return jArray;
//    }