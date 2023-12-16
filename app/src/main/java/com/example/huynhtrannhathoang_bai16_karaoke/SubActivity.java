package com.example.huynhtrannhathoang_bai16_karaoke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    TextView txtmaso, txtname,txtloiBH, txtTacGia;
    ImageView btnLike;
    int trangthai = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        txtmaso = findViewById(R.id.txtmaso1);
        txtloiBH = findViewById(R.id.txtloibaihat);
        txtname = findViewById(R.id.txtbaihat);
        txtTacGia = findViewById(R.id.txttacgia);
        btnLike = findViewById(R.id.btnlikesub);

        Intent callerIntent = getIntent();
        Bundle backageCaller = callerIntent.getBundleExtra("package");
        String maso = backageCaller.getString("maso");
        /*Truy vấn dữ liệu từ maso nhận được và hiển thị tất cả nội dung lên MainActivity*/

        Cursor cursor = MainActivity.database.rawQuery("SELECT * FROM ArirangSongList WHERE MABH LIKE'" + maso + "'", null);
        txtmaso.setText(maso);
        cursor.moveToFirst();
        txtname.setText(cursor.getString(2));
        txtloiBH.setText(cursor.getString(3));
        txtTacGia.setText(cursor.getString(4));
        trangthai = cursor.getInt(6);
        if (trangthai == 0){
            btnLike.setImageResource(R.drawable.unlike);
        }else {
            btnLike.setImageResource(R.drawable.love);
        }
        cursor.close();

        /*Bắt sự kiện click vào Button love xong cập nhât lại vào csdl, thay đổi trạn thái love */

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                if (trangthai == 0){
                    trangthai = 1;
                    btnLike.setImageResource(R.drawable.love);
                }else {
                    trangthai = 0;
                    btnLike.setImageResource(R.drawable.unlike);
                }
                values.put("yeuthich",trangthai);
                MainActivity.database.update("ArirangSongList", values, "MABH = ?", new String[]{txtmaso.getText().toString()});
            }
        });
    }
}