package com.example.cameraapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/*import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;*/

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Initialize variables


    private static final int REQUEST_CODE = 22;

    //Texts
    private EditText containmentInput;
    private EditText numberofitems;

    //ImageViews
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;

    //Variables
    private float pictureCount;

    //Buttons
    private ImageView qrCodeIV;
    private Button qrCodeGenBtn;
    Button btnpicture;
    Button uploadbutton;
    private Button floatingactionbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnpicture = findViewById(R.id.btncamera_id);

        //Variables
        pictureCount = 0;

        //ImageViews
        imageView = findViewById(R.id.PictureView1);
        imageView2 = findViewById(R.id.PictureView2);
        imageView3 = findViewById(R.id.PictureView3);
        imageView4 = findViewById(R.id.PictureView4);

        uploadbutton = findViewById(R.id.uploadButton);

        containmentInput = findViewById(R.id.containmentInput);
        numberofitems = findViewById(R.id.editTextNumber);

        floatingactionbutton = (Button) findViewById(R.id.fab);
        floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        btnpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent, REQUEST_CODE);
            }
        });
        //QR CODE GENERATION TASK: Make It
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final EditText textInput = findViewById(R.id.containmentInput);
        final Button idBtnGenerateQR = findViewById(R.id.qrCodeGen);

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idBtnGenerateQR.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap photo1 = (Bitmap) data.getExtras().get("data");
            Bitmap photo2 = (Bitmap) data.getExtras().get("data");
            Bitmap photo3 = (Bitmap) data.getExtras().get("data");
            Bitmap photo4 = (Bitmap) data.getExtras().get("data");
            if(pictureCount == 0){
                imageView.setImageBitmap(photo1);
                pictureCount ++;
            }
            if(pictureCount == 1){
                imageView2.setImageBitmap(photo2);
                pictureCount ++;
            }
            if(pictureCount == 2){
                imageView3.setImageBitmap(photo3);
                pictureCount ++;
            }
            if(pictureCount == 3){
                imageView4.setImageBitmap(photo4);
                pictureCount ++;
            }


            uploadbutton.setVisibility(View.VISIBLE);
            uploadbutton.setEnabled(true);
        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }

        uploadbutton.setOnClickListener(new View.OnClickListener(){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if (photo != null ){
                    photo.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, byteArrayOutputStream);

                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    final EditText inputString = (EditText) findViewById(R.id.containmentInput);

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String keyip = getIntent().getStringExtra("keyip");
                    String url = "http://"+keyip+":3000/upload";
                    System.out.println(url);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                    if(response.equals("succes")){
                                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                    else Toast.makeText(getApplicationContext(), "failed to upload image", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            System.out.println(error.getLocalizedMessage());
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("image", base64Image);
                            paramV.put("input_string", inputString.getText().toString() + " | " + numberofitems.getText().toString());
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                } else Toast.makeText(getApplicationContext(), "Select the image first", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openSettings(){
        Intent intent2 = new Intent(this, SettingsActivity.class);
        startActivity(intent2);
    }

}