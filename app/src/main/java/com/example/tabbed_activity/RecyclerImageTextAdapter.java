package com.example.tabbed_activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecyclerImageTextAdapter extends RecyclerView
        .Adapter<RecyclerImageTextAdapter.ViewHolder> implements SectionTitleProvider {
    private ArrayList<ContactRecyclerItem> mData = null;
    private Context cont;

    onItemClickListner onItemClickListner;

//    public void setOnItemClickListner() {
//        setOnItemClickListner();
//    }

    public void setOnItemClickListner(RecyclerImageTextAdapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner{
        void onClick(String str) throws ExecutionException, InterruptedException;//pass your object types.
    }
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
////        String data = mStringList.get(position); // if you pass object of class then create that class object.
////        holder.textView.setText(data);
//
//        final String data = mData.get(position).getPhone();
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickListner.onClick(data);
//            }
//        });
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        ImageView call;
        ImageView msg;
        TextView name;
        TextView phone;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            mView = itemView;
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            call = itemView.findViewById(R.id.call);
            msg = itemView.findViewById(R.id.msg);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerImageTextAdapter(ArrayList<ContactRecyclerItem> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.contact_recyclerview_item, parent, false);
        ViewHolder vh = new ViewHolder(view);


        cont = context;

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        boolean isvisible = false;
        final ContactRecyclerItem item = mData.get(position);
        if (item.getImageStr() != null) {
            byte[] b;
            Bitmap bitmap = null;
            String str = item.getImageStr();

            b = org.bson.internal.Base64.decode(str);
            Log.d("ERRORCHECK", item.getName());
            Log.d("ERRORCHECK", item.getImageStr());
            Log.d("ERRORCHECK", String.valueOf(item.getImageStr() == null));

            ByteArrayInputStream stream = new ByteArrayInputStream(b);
            bitmap = BitmapFactory.decodeStream(stream);


//            ByteBuffer buffer = ByteBuffer.wrap(b);
//            buffer.rewind();
//            bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
//            bitmap.copyPixelsFromBuffer(buffer);

//        if(bitmap != null) {
//            holder.icon.setImageBitmap(bitmap);
//            holder.name.setText(item.getName());
//            holder.phone.setText(item.getPhone());
//        }
//        else{
//            Log.d("ERROR", "BITMAPNULL");
//        }

            Drawable drawable = new BitmapDrawable(cont.getResources(), bitmap);
//        holder.icon.setImageBitmap(bitmap);
            holder.icon.setImageDrawable(drawable);
        } else {
            holder.icon.setImageDrawable(item.getIcon());
        }
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String tel = "tel:" + item.getPhone();
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse(tel));
//                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(),
                        "Click!", Toast.LENGTH_SHORT).show();

                Button bb = holder.mView.findViewById(R.id.delete_btn);
                if(bb.getVisibility() == View.GONE) {
                    Log.d("DELETE", "makevisible");
                    bb.setVisibility(View.VISIBLE);
                }
                else {
                    Log.d("DELETE", "make invisible");
                    bb.setVisibility(View.GONE);
                }
                bb.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        boolean success = deleteContact(v.getContext(), item.getPhone(), item.getName());
                        Button bb = holder.mView.findViewById(R.id.delete_btn);
                        if(success){
                            Toast.makeText(v.getContext(),
                                    "Delete Success!", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(v.getContext(),
                                    "Delete Fail!", Toast.LENGTH_SHORT).show();
                        }
                        bb.setVisibility(View.GONE);
                        try {
                            onItemClickListner.onClick(item.getPhone());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });

//                bb.setVisibility(View.GONE);
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "tel:" + item.getPhone();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(tel));
                v.getContext().startActivity(intent);
            }
        });

        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "smsto:" + item.getPhone();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(tel));
                v.getContext().startActivity(intent);
            }
        });

//        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // 오랫동안 눌렀을 때 이벤트가 발생됨
//                b = holder.mView.findViewById(R.id.delete_btn);
//                b.setVisibility(View.VISIBLE);
//                b.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v){
//                        boolean success = deleteContact(v.getContext(), item.getPhone(), item.getName());
//                        if(success){
//                            Toast.makeText(v.getContext(),
//                                    "Delete Success!", Toast.LENGTH_SHORT).show();
//                            b.setVisibility(View.GONE);
//                        }
//                        else{
//                            Toast.makeText(v.getContext(),
//                                    "Delete Fail!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });
//
//                // 리턴값이 있다
//                // 이메서드에서 이벤트에대한 처리를 끝냈음
//                //    그래서 다른데서는 처리할 필요없음 true
//                // 여기서 이벤트 처리를 못했을 경우는 false
//                return true;
//            }
//        });

    }



    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        return mData.get(position).getName().substring(0, 1);
    }

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Log.d("DELETE", phone + "    " + name);
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }

                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        } finally {
            cur.close();
        }



        return false;
    }
}