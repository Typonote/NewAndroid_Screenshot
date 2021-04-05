package com.example.screenshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = findViewById(R.id.btnCapture);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView();

                File screenShot = ScreenShot(rootView);
                if (screenShot != null) {
                    //갤러리에 추가
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                    showToast("갤러리에 저장되었습니다.");
                }
            }
        });
    } // onCreate()끝

    // 화면 캡쳐하기 메서드 작성
    public File ScreenShot(View view) {

        // 캡쳐가 저장될 외부 저장소 경로 설정
        final String CAPTURE_PATH = "/CROPS";

        // 사진이 담길 폴더 이름 설정
        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH;
        File folder = new File(strFolderPath);
        if (!folder.exists()) { // 폴더가 없다면 생성
            folder.mkdirs();
        }

        // 파일 이름 설정(캡처 시간으로 설정)
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDate.format(mDate);
        String mPath = strFolderPath + "/" + getTime + ".jpg";

        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        File file = new File(mPath);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;

    } // 화면 캡쳐하기 메서드 끝

    // 토스트 메서드 작성
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    } // 토스트 메서드 끝

}