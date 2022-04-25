package com.example.ex_01_arfirst;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Session;

public class MainActivity extends AppCompatActivity {

    Session mSession;

    GLSurfaceView mySerView;

    MainRenderer mRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySerView = (GLSurfaceView) findViewById(R.id.glsurfaceview);

        MainRenderer.RenderCallBack mr = new MainRenderer.RenderCallBack() {
            // 익명 이너클래스. 생성시에 한번만 사용(정의)가능. 단발적
            @Override
            public void preRender() {

                // session 객체와 연결해서 화면 그리기 하기
                mSession.setCameraTextureName(mRenderer.getTextureId());

            }
        };

        mRenderer = new MainRenderer(mr);

        // pause 시 관련 데이터가 사라지는 것을 막는다
        mySerView.setPreserveEGLContextOnPause(true);

        // 오픈 EGL 사용시 버전을 2.0 사용
        mySerView.setEGLContextClientVersion(2);

        // 화면을 그리는 Renderer 를 지정한다.
        // 새로 정의한 MainRenderer를 사용한다.
        mySerView.setRenderer(mRenderer);

    }

    @Override
    protected void onResume() {
        // 화면을 띄울때마다 실행
        super.onResume();
        cameraPerm();

        try {
            if(mSession == null) {

//                Log.d("session requestInstall ? ",
//                        ArCoreApk.getInstance().requestInstall(this,true)+"");

                // ARcore 가 정상적으로 설치 돼 있는가
                switch (ArCoreApk.getInstance().requestInstall(this,true)){

                    case INSTALLED: // ARcore 정상설치됨
                        //ARcore가 정상설치되어서 session 을 생성 가능한 형태임
                        mSession = new Session(this);
                        Log.d("session 인감","session 생성됨");
                        break;

                    case INSTALL_REQUESTED: // ARcore 설치 필요

                        Log.d("session 인감","ARcore INSTALL_REQUESTED");
                        break;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //카메라 퍼미션 요청
    void cameraPerm(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            !=PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0
                    );
        }
    }
}