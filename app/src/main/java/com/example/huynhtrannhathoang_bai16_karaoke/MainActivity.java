package com.example.huynhtrannhathoang_bai16_karaoke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database =null;
    public static String DATABASE_NAME="arirang.sqlite";
    EditText edtFind;
    ListView lv1, lv2, lv3;
    ArrayList<Item> list1, list2,list3;
    myarrayAdapter myArr1, myArr2, myArr3;
    TabHost tab;
    ImageButton btnDel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Copy csdl arirang.sqlite
        processCopy();
        //Mở csdl đã copy. Lưu vào biến database
        database = openOrCreateDatabase("arirang.sqlite", MODE_PRIVATE,null);
        //Hàm thêm các Control
        addControl();
        //Xử lí tìm kiếm
        addFind();
        addEvent();
    }

    private void addControl() {
        btnDel = findViewById(R.id.btnxoa);
        tab = findViewById(R.id.tabhost);
        tab.setup();
        TabHost.TabSpec tab1 = tab.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("", ResourcesCompat.getDrawable(getResources(),R.drawable.search,null));
        tab.addTab(tab1);
        TabHost.TabSpec tab2 = tab.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",ResourcesCompat.getDrawable(getResources(),R.drawable.list, null));
        tab.addTab(tab2);
        TabHost.TabSpec tab3 = tab.newTabSpec("t3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("",ResourcesCompat.getDrawable(getResources(), R.drawable.favourite, null));
        tab.addTab(tab3);
        edtFind = findViewById(R.id.edttim);
        lv1 = findViewById(R.id.lv1);
        lv2 = findViewById(R.id.lv2);
        lv3 = findViewById(R.id.lv3);
        list1 = new ArrayList<Item>();
        list2 = new ArrayList<Item>();
        list3 = new ArrayList<Item>();
        myArr1 = new myarrayAdapter(MainActivity.this, list1, R.layout.listitem);
        myArr2 = new myarrayAdapter(MainActivity.this, list2, R.layout.listitem);
        myArr3 = new myarrayAdapter(MainActivity.this, list3, R.layout.listitem);
        lv1.setAdapter(myArr1);
        lv2.setAdapter(myArr2);
        lv3.setAdapter(myArr3);
    }

    //Xử lí sự kiện khi chuyển qua lại giữa các Tab danh sách và yêu thích
    private void addEvent() {
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase("t2")){
                    addDanhSach();
                }
                if (tabId.equalsIgnoreCase("t3")){
                    addYeuThich();
                }
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFind.setText("");
            }
        });
    }

    private void addYeuThich() {
        myArr3.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE YEUTHICH = 1", null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            list3.add(new Item(c.getString(1), c.getString(2),c.getInt(6)));
            c.moveToNext();
        }
        c.close();
        myArr3.notifyDataSetChanged();
    }

    private void addDanhSach() {
        myArr2.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList", null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            list2.add(new Item(c.getString(1), c.getString(2), c.getInt(6)));
            c.moveToNext();
        }
        c.close();
        myArr2.notifyDataSetChanged();
    }

    //Hàm xử lí tìm kiếm bài hát theo tiêu đề và mã số
    private void addFind() {
        //Bắt sự kiện thay đổi text trong edt Find
        edtFind.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData();
            }
            private void getData() {
                String data = edtFind.getText().toString();
                myArr1.clear();
                if (!edtFind.getText().toString().equals("")){
                    Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE TENBH1 LIKE '" + "%" + data + "%" + "' OR MABH LIKE '" + "%" + data + "%" + "'", null);
                    c.moveToFirst();
                    while (c.isAfterLast() == false){
                        list1.add(new Item(c.getString(1), c.getString(2), c.getInt(6)));
                        c.moveToNext();
                    }
                    c.close();
                }
                myArr1.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    //Hàm copy CSDL từ thư mục assets vài hệ thống thư mục cài đăt
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()){
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath(){
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
    private void CopyDataBaseFromAsset() {
        try {
            InputStream inputStream;
            inputStream = getAssets().open(DATABASE_NAME);
            String outFileName= getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}