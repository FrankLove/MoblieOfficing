package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.untils.OkHttpClientManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.r2.scau.moblieofficing.untils.OkHttpClientManager.okHttpClient;

public class SendNoticeActivity extends AppCompatActivity {

    private EditText titleET;
    private EditText authorET;
    private EditText contentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);

        initView();
    }

    public void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_send_notice);
        setSupportActionBar(toolbar);

        titleET = (EditText) findViewById(R.id.et_notice_title);
        authorET = (EditText) findViewById(R.id.et_notice_author);
        contentET = (EditText) findViewById(R.id.et_notice_content);

        titleET.setSingleLine();
        authorET.setSingleLine();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_send_notice_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_notice_send){
            String title = titleET.getText().toString();
            String author = authorET.getText().toString();
            String content = contentET.getText().toString();

            Log.e("notice", "send");
            if (title.equals("") || author.equals("") || content.equals("")){
                Toast toast=Toast.makeText(getApplicationContext(), "标题、作者、内容都不能为空", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }

            Toast toast=Toast.makeText(getApplicationContext(), "合格", Toast.LENGTH_SHORT);
            toast.show();

            send(title, author, content);
        }

        return true;
    }

    public void send(String title, String author, String content){
        FormBody formBody = new FormBody.Builder()
                .add("aTitle", title)
                .add("aContent", content)
                .add("aCreatedUserPhone", "12345678900")
                .add("aType", "notice1")
                .build();


        //step 3: 创建请求
        Request request = new Request.Builder().url("http://192.168.13.19:8080/permission/publicAnnouncement.shtml")
                .addHeader("cookie", OkHttpClientManager.loginSessionID)
                .post(formBody)
                .build();

        //step 4： 建立联系 创建Call对象
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                Log.e("sendNotice", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Log.e("sendNotice", response.body().string());

            }
        });
    }
}
