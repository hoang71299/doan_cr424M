package com.example.huynhtrannhathoang_bai16_karaoke;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.huynhtrannhathoang_bai16_karaoke.MainActivity;
import com.example.huynhtrannhathoang_bai16_karaoke.R;
import com.example.huynhtrannhathoang_bai16_karaoke.SubActivity;

import java.util.ArrayList;

public class myarrayAdapter extends ArrayAdapter<Item> {
    Activity context = null;
    ArrayList<Item> arrayList = null;
    int layoutId;


    public myarrayAdapter(Activity context, ArrayList<Item> arrayList, int layoutId) {
        super(context, layoutId, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);
        Item myItem = arrayList.get(position);
        TextView tieude = convertView.findViewById(R.id.txtTenbh);
        tieude.setText(myItem.getTieude());
        TextView maso = convertView.findViewById(R.id.txtMaso);
        maso.setText(myItem.getMaso());
        ImageView btnTym = convertView.findViewById(R.id.btnlike);
        if (myItem.getThich() == 1){
            btnTym.setImageResource(R.drawable.love);
        }else {
            btnTym.setImageResource(R.drawable.unlike);
        }
        //Bắt sự kiện tym và Cập nhật vào csdl
        btnTym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int like = myItem.getThich();
                ContentValues values = new ContentValues();
                if (like == 0){
                    btnTym.setImageResource(R.drawable.love);
                    like = 1;
                }else {
                    btnTym.setImageResource(R.drawable.unlike);
                    like = 0;
                }
                //Cập nhật lại trạng thái thích cho mảng
                myItem.setThich(like);
                values.put("yeuthich", like);
                //Cập nhật vào csdl
                MainActivity.database.update("ArirangSongList", values, "MABH = ?", new String[]{myItem.getMaso()});
            }
        });
        /*Bắt sự kiện khi click vào tiêu đề của bài hát trên listview
         Chuyên textview tiêu đề và mã số sang màu đỏ
         Khai báo intent và Bundle đê lấy maso truyền qua SubActivity   */
        tieude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tieude.setTextColor(Color.RED);
                maso.setTextColor(Color.RED);
                Intent intent = new Intent(context, SubActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("maso", myItem.getMaso());
                intent.putExtra("package", bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
