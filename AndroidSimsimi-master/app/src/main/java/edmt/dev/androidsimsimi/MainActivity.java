package edmt.dev.androidsimsimi;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.library.bubbleview.BubbleTextView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edmt.dev.androidsimsimi.Adapter.CustomAdapter;
import edmt.dev.androidsimsimi.Adapter.CustomAdapter2;
import edmt.dev.androidsimsimi.Helper.HttpDataHandler;
import edmt.dev.androidsimsimi.Models.ChatModel;
import edmt.dev.androidsimsimi.Models.SimsimiModel;

import com.google.gson.JsonArray;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    ArrayList<QandR> list_chat = new ArrayList<>();
    FloatingActionButton btn_send_message;
    String str="";
    public String respon = "ho";
    String text="";

    CustomAdapter adapter;
    CustomAdapter2 adapter2;
    QandR qandr2=null;
    JSONObject jsoo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_of_message);
        editText = (EditText) findViewById(R.id.user_message);
        btn_send_message = (FloatingActionButton) findViewById(R.id.fab);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new JSONTask().execute("http://143.248.38.253:4500/simsims");//AsyncTask 시작시킴
//                new JSONGETTask().execute("http://143.248.38.76:4500/simsims");
//                String text = editText.getText().toString();
////                ChatModel model = new ChatModel(text,true); // user send message
//                JSONArray mArray = new JSONArray();
//                try {
//                    mArray = new JSONArray(str);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("ERROR", String.valueOf(e));
//
//                    Log.d("ERROR", "HOHOHOexception");
//                    Log.d("ERROR", str);
//                }
////        JSONObject obj;
////        for (int i = 0; i < mArray.length(); i ++){
////            obj = mArray.getJSONObject(i);
////        }
//                QandR qandr = null;
//                respon = "";
//                ArrayList<QandR> listdata = new ArrayList<QandR>();
//                boolean indata = false;
//                for (int i = 0; i < mArray.length(); i++) {
////                    ContactRecyclerItem ct = new ContactRecyclerItem();
////                    ct.setName(mArray.getJSONObject(i).get("name").toString());
////                    ct.setPhone(mArray.getJSONObject(i).get("phonenumber").toString());
////                    ct.setIconID(Long.valueOf(mArray.getJSONObject(i).get("iconID").toString()));
////                    ct.setPersonID(Long.valueOf(mArray.getJSONObject(i).get("pID").toString()));
////                    ct.setImageStr(mArray.getJSONObject(i).get("ImageStr").toString());
//                    try {
//                        if (text == mArray.getJSONObject(i).get("ques").toString()) {
//                            respon = mArray.getJSONObject(i).get("res").toString();
////                            리스폰 출력해주기
//                            qandr.setResponse(mArray.getJSONObject(i).get("ques").toString());
//                            qandr.setQuestion(mArray.getJSONObject(i).get("res").toString());
//                            qandr.setSend(true);
//                            mArray.put(qandr);
//
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (indata == false) {
////                    버튼클릭해서 응답작성 해주게 하는 거 만들기
////                    응답작성한것 add해주기
////                    JSONTask().execute("http://143.248.38.76:4500/contacts/initialize");//AsyncTask 시작시킴
//                }
//                ArrayList<QandR> sihyeon = new ArrayList<QandR>();
//                if (mArray != null) {
//                    for (int i=0;i<mArray.length();i++){
//                        QandR qandr2=null;
//                        try {
//                            qandr2.setResponse(mArray.getJSONObject(i).get("ques").toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            qandr2.setQuestion(mArray.getJSONObject(i).get("res").toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            qandr2.setSend((Boolean) mArray.getJSONObject(i).get("send"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        listdata.add(qandr2);
//                    }
//                    list_chat=listdata;
//                }
//
////                new SimsimiAPI().execute(list_chat);
//                //remove user message
//                editText.setText("");






                new JSONGETTask().execute("http://143.248.38.253:4500/simsims");
            }

        });
    }

    public void onClickExecute() throws JSONException, ExecutionException, InterruptedException {
        ArrayList<ChatModel> sisi=new ArrayList<>();
        Log.d("DEBUG", "onClickExecute");
        text = editText.getText().toString();
        ChatModel momo = new ChatModel(text,true);
        sisi.add(momo);// user send message
        JSONArray mArray = new JSONArray();
        try {
            mArray = new JSONArray(str);
            Log.d("DEBUG", "str: " + str);
            Log.d("DEBUG", mArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ERROR", String.valueOf(e));
        }
//        JSONObject obj;
//        for (int i = 0; i < mArray.length(); i ++){
//            obj = mArray.getJSONObject(i);
//        }
        QandR qandr = new QandR();
        respon = "";
        ArrayList<QandR> listdata = new ArrayList<QandR>();
        boolean indata = false;
        Log.d("DEBUG", "mArray length : " + mArray.length() + "////" + text);
        for (int i = 0; i < mArray.length(); i++) {
//                    ContactRecyclerItem ct = new ContactRecyclerItem();
//                    ct.setName(mArray.getJSONObject(i).get("name").toString());
//                    ct.setPhone(mArray.getJSONObject(i).get("phonenumber").toString());
//                    ct.setIconID(Long.valueOf(mArray.getJSONObject(i).get("iconID").toString()));
//                    ct.setPersonID(Long.valueOf(mArray.getJSONObject(i).get("pID").toString()));
//                    ct.setImageStr(mArray.getJSONObject(i).get("ImageStr").toString());
            Log.d("hohohohoho", str);


            Log.d("hohohohoho", "hi");
//                Log.d("DEBUG", mArray.getJSONObject(i).get("ques").toString());
//                Log.d("DEBUG", );
            if (text.contains((CharSequence) mArray.getJSONObject(i).get("ques"))) {//text == mArray.getJSONObject(i).get("ques").toString()) {
                Log.d("DEBUG", "hihihihihihihihihii");
                respon = mArray.getJSONObject(i).get("res").toString();
//                            리스폰 출력해주기
                ChatModel model = new ChatModel(respon, false);
                sisi.add(model);// user send message
//                qandr.setResponse(mArray.getJSONObject(i).get("ques").toString());
//                qandr.setQuestion(mArray.getJSONObject(i).get("res").toString());
//                qandr.setSend(true);
//                mArray.put(qandr);
                indata = true;
                Log.d("DEBUG", "mArray : " + mArray.toString());
            }
        }

        if (indata == false) {
//                    버튼클릭해서 응답작성 해주게 하는 거 만들기
//                    응답작성한것 add해주기
//                    JSONTask().execute("http://143.248.38.76:4500/contacts/initialize");//AsyncTask 시작시킴

            final EditText editte = new EditText(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("AlertDialog Title");
            builder.setMessage("AlertDialog Content");
            builder.setView(editte);
            builder.setPositiveButton("입력",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),editte.getText().toString() ,Toast.LENGTH_LONG).show();
                            JSONObject jso=new JSONObject();
                            String textoo=editte.getText().toString();
                            try {
                                jso.put("ques", text);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                jso.put("res", textoo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                jso.put("send", false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsoo=jso;
//            new JSONTask().execute("http://143.248.38.253:4500/simsims");//AsyncTask 시작시킴
                            try {
                                new JSONTask().execute("http://143.248.38.253:4500/simsims").get();//AsyncTask 시작시킴
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();

        }

//        ArrayList<QandR> sihyeon = new ArrayList<QandR>();
//        if (mArray != null) {
//            for (int i=0;i<mArray.length();i++){
//                QandR qandr2=new QandR();
//                try {
//                    qandr2.setQuestion(mArray.getJSONObject(i).get("ques").toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    qandr2.setResponse(mArray.getJSONObject(i).get("res").toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    qandr2.setSend((Boolean) mArray.getJSONObject(i).get("send"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                sihyeon.add(qandr2);
//            }
//            list_chat=sihyeon;
//        }

//                new SimsimiAPI().execute(list_chat);
        //remove user message
        editText.setText("");


//        adapter2 = new CustomAdapter2(list_chat,getApplicationContext());
//        listView.setAdapter(adapter2);
        adapter = new CustomAdapter(sisi,getApplicationContext());
        listView.setAdapter(adapter);
//        onClickExecute();
    }
//    protected String doInBackground(List<ChatModel>… params) {
//        String url = String.format("http://sandbox.api.simsimi.com/request.p?key=%s&lc=en&ft=1.0&text=%s",getString(R.string.simsimi_api),text);
//        models = params[0];
//        HttpDataHandler httpDataHandler = new HttpDataHandler();
//        stream = httpDataHandler.GetHTTPData(url);
//        return stream;
//    }

//    private class SimsimiAPI extends AsyncTask<List<QandR>,Void,String> {
//        String stream = null;
//        List<QandR> models;
//        String text = editText.getText().toString();
//
////        @Override
////        protected String doInBackground(List<ChatModel>... params) {
////
////            HttpResponse<String> response = Unirest.post("https://wsapi.simsimi.com/190410/talk/")
////                    .header("Content-Type", "application/json")
////                    .header("x-api-key", "PASTE_YOUR_PROJECT_KEY_HERE")
////                    .body("{\n\t\"utext\": \"hello there\", \n\t\"lang\": \"en\" \n}")
////                    .asString();
////            return response;
////        }
//        @Override
//        protected String doInBackground(List<QandR>... params) {
//            String url="";
//            try {
//                url=String.valueOf((Unirest.post("https://wsapi.simsimi.com/190410/talk/")
//                        .header("Content-Type", "application/json")
//                        .header("x-api-key", "PASTE_YOUR_PROJECT_KEY_HERE")
//                        .body("{\n\t\"utext\": \"hello there\", \n\t\"lang\": \"en\" \n}")
//                        .asString()));
//            } catch (UnirestException e) {
//                e.printStackTrace();
//            }
//            HttpDataHandler httpDataHandler = new HttpDataHandler();
//            stream = httpDataHandler.GetHTTPData(url);
//            return stream;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
////           Gson gson = new Gson();
////            SimsimiModel response = gson.fromJson(s,SimsimiModel.class);
////
////            ChatModel chatModel = new ChatModel(response.getResponse(),false); // get response from simsimi
////            models.add(chatModel);
////            CustomAdapter adapter = new CustomAdapter(models,getApplicationContext());
////            listView.setAdapter(adapter);
//        }
//    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

//                JSONArray jsonArray;// = new JSONArray();
//                JSONArray jsonAr = ArrListToJArr(list_chat);
//                Log.d("ERROR", String.valueOf(jsonAr));
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.accumulate("todoid", 14);
//                jsonObject.accumulate("content", "yun");
//                jsonObject.accumulate("completed", "false");
//                for (int i=0; i<jsonOb.length(); i++){
//                    JSONArray
//                jsonArray = (JSONArray) jsonOb.get("ContactData");
////                    jsonArray.put(ho);
////                }
//                Log.d("CONTACT", jsonArray.toString());

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
                    writer.write(jsoo.toString());
                    Log.d("BUFFER", jsoo.toString());
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
//            new JSONGETTask().execute("http://143.248.38.253:4500/simsims");
            Log.d("dabc","hi");
        }
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

                        str = buffer.toString();

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
//            tvData.setText(result);
                try {
                    hohoho();
                } catch (JSONException e) {
                    Log.e("ERROR", "HOHOHO ERROR");
                }

                try {
                    onClickExecute();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//                CustomAdapter adapter = new CustomAdapter(list_chat);
//                listView.setAdapter(adapter);
//                onClickExecute();
            }
        }


    public void hohoho() throws JSONException {
        JSONArray mArray = new JSONArray();
        try {
            mArray = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ERROR", String.valueOf(e));

            Log.d("ERROR", "HOHOHOexception");
            Log.d("ERROR", str);
        }

        ArrayList<QandR> listdata = new ArrayList<QandR>();
        for (int i = 0; i < mArray.length(); i++) {
            QandR qandr = new QandR();
            qandr.setQuestion(mArray.getJSONObject(i).get("ques").toString());
            qandr.setResponse(mArray.getJSONObject(i).get("res").toString());
            qandr.setSend(false);

            listdata.add(qandr);
        }
        list_chat = listdata;

    }


    public JSONArray ArrListToJArr(ArrayList<QandR> arrList) {
        JSONArray jArray = new JSONArray();
        try {

            for (int i = 0; i < arrList.size(); i++) {

                QandR contactItem;
                contactItem = arrList.get(i);

                JSONObject sObj = new JSONObject();
                sObj.put("ques", contactItem.getQuestionStr());
                sObj.put("res", contactItem.getResponseStr());
                sObj.put("send",false);


                jArray.put(sObj);
            }

            System.out.println(jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jArray;
    }
}
