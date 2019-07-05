package com.example.tabbed_activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TabFragment1 extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerImageTextAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ContactRecyclerItem> mMyData;
    private View view;
    TextView tvData;
    private String getresult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDataset();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new RecyclerImageTextAdapter(mMyData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        스크롤바 추가부분
        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        fastScroller.setRecyclerView(mRecyclerView);

        tvData = (TextView) view.findViewById(R.id.textView);
        Button postbtn = (Button) view.findViewById(R.id.Postbtn);
        Button getbtn = (Button) view.findViewById(R.id.Getbtn);

        //버튼이 클릭되면 여기 리스너로 옴
        postbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new JSONTask().execute("http://143.248.38.76:4500/contacts/initialize");//AsyncTask 시작시킴
            }
        });

        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONGETTask().execute("http://143.248.38.76:4500/contacts");
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //연락처 추가 버튼
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }
        });
    }

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
            tvData.setText(result);
            getresult = result;
        }

    }


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
                JSONObject jsonOb = ArrListToJObj(mMyData, "test");

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
            tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부분
        }
    }

    public void initDataset() {
        mMyData = getContactList(); //폰에 저장된 연락처 리스트를 가져오는 함수(mMyData는 ArrayList<ContactRecyclerItem> type)
        ContactRecyclerItem contactItem;

        //연락처 icon을 drawable type으로 변환하는 부분. getContactList에서 진행해도 무관한 부분이다.
        for (int i = 0; i < mMyData.size(); i++) {
            contactItem = mMyData.get(i);
            Drawable drawable;
            Bitmap bm = loadContactPhoto(getActivity().getContentResolver(), contactItem.getPersonID(), contactItem.getIconID());
            if (bm == null)
                drawable = getResources().getDrawable(R.drawable.default_icon);
            else {
                drawable = new BitmapDrawable(getResources(), bm);
            }
            contactItem.setIcon(drawable);
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

    //ArrayList type을 JSONArray type으로 변환하는 함수.
    public JSONArray ArrListToJArr(ArrayList<ContactRecyclerItem> arrList) {
        JSONArray jArray = new JSONArray();
        try {

            for (int i = 0; i < arrList.size(); i++) {

                ContactRecyclerItem contactItem;
                contactItem = arrList.get(i);

                JSONObject sObj = new JSONObject();
                sObj.put("name", contactItem.getName());
                sObj.put("phonenumber", contactItem.getPhone());
                sObj.put("iconID", contactItem.getIconID());
                sObj.put("pID", contactItem.getPersonID());
//                sObj.put("iconDrawable", contactItem.getIcon());
                jArray.put(sObj);
            }

            System.out.println(jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jArray;
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
                sObj.put("iconID", contactItem.getPhone());
                sObj.put("pID", contactItem.getPersonID());
//                sObj.put("iconDrawable", contactItem.getIcon());
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
