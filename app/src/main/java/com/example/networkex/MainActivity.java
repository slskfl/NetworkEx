package com.example.networkex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText edtSendMsg;
    Button btnSend;
    TextView revMsg;
    InputStream is;
    OutputStream os;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Socket socket;
    String rmsg, smsg; // 받는메세지, 보내는메세지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        edtSendMsg=findViewById(R.id.edtSendMsg);
        btnSend=findViewById(R.id.btnSend);
        revMsg=findViewById(R.id.revMsg);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                revMsg.setText(rmsg);
            }
        });
    } // onCreate 메서드 종료
    //네트워크 시작 처리 메서드
    public void start(){
        try{
            socket=new Socket("192.168.123.100"/*서버 아이디(IP)*/, 7000);
            sendMessage(socket);
        }catch (Exception e){
            //서버와 연결 불가
            showToast("서버 연결 불가");
        }
    }//네트워크 시작 처리 메서드 종료
    //서버에서 받은 데이터 처리 메서드
    public void receiveMessage(Socket socket){
        try{
            is=socket.getInputStream();
            ois=new ObjectInputStream(is);
            rmsg=(String)ois.readObject();
        }catch (Exception e){
            showToast("데이터를 받는 도중에 에러 발생");
        }
    } //서버에서 받은 데이터 처리 메서드 종료
    //서버로 보낼 데이터 처리 메서드
    public void sendMessage(Socket socket){
        try {
            os=socket.getOutputStream();
            oos=new ObjectOutputStream(os);
            smsg="니나 : " + edtSendMsg.getText().toString();
            oos.writeObject(smsg);
        } catch (Exception e){
            showToast("데이터를 보내는 중에 에러 발생");
        }
    } //서버로 보낼 데이터 처리 메서드 종료

    void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
