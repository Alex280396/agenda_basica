package com.example.contactapp;

import static com.example.contactapp.BaseDatos.TABLA;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    BaseDatos baseDatos;
    SQLiteDatabase sqLiteDatabase;
    ImageView imagen;
    EditText nombre, telefono;
    Button submit,display,edit;
    int id=0;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String[]cameraPermission;
    String[]storagePermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        baseDatos =new BaseDatos(this);
        findid();
        insertData();
        imagePick();
        editData();
    }

    private void editData() {
        if (getIntent().getBundleExtra("contacto")!=null){
            Bundle bundle=getIntent().getBundleExtra("contacto");
            id=bundle.getInt("id");
            //for image
            byte[]bytes=bundle.getByteArray("imagen");
            Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imagen.setImageBitmap(bitmap);
            //for set name
            nombre.setText(bundle.getString("nombre"));
            telefono.setText(bundle.getString("telefono"));
            //visible edit button and hide submit button
            submit.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
        }
    }

    private void imagePick() {
        imagen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int imagen=0;
                if (imagen==0){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        pickFromGallery();
                    }
                }else if (imagen==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission,CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result2=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    private void insertData() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv=new ContentValues();
                cv.put("imagen",ImageViewToByte(imagen));
                cv.put("nombre",nombre.getText().toString());
                cv.put("telefono",nombre.getText().toString());
                sqLiteDatabase= baseDatos.getWritableDatabase();
                Long recinsert=sqLiteDatabase.insert(TABLA,null,cv);
                if (recinsert!=null){
                    Toast.makeText(MainActivity.this, "Contacto registrado exitosamente", Toast.LENGTH_LONG).show();
                    //clear when click on submit button
                    imagen.setImageResource(R.mipmap.ic_launcher);
                    nombre.setText("");
                    telefono.setText("");
                }
            }
        });
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MostrarDatos.class));
            }
        });
        //for storing new data or update data
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv=new ContentValues();
                cv.put("imagen",ImageViewToByte(imagen));
                cv.put("nombre",nombre.getText().toString());
                cv.put("telefono",telefono.getText().toString());
                sqLiteDatabase= baseDatos.getWritableDatabase();
                long editreg=sqLiteDatabase.update(TABLA,cv,"id="+id,null);
                if (editreg!=-1){
                    Toast.makeText(MainActivity.this, "Contacto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    //clear data after submit
                    imagen.setImageResource(R.mipmap.ic_launcher);
                    nombre.setText("");
                    telefono.setText("");
                    //edit hide and submit visible
                    edit.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private byte[] ImageViewToByte(ImageView imageView) {
        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }

    private void findid() {
        imagen=findViewById(R.id.avatar);
        nombre=findViewById(R.id.edit_name);
        telefono=findViewById(R.id.edit_numero);
        submit=findViewById(R.id.btn_submit);
        display=findViewById(R.id.btn_display);
        edit=findViewById(R.id.btn_edit);
    }
    //overrid method onRequestPermissionResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accept&&storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Habilitar permisos de camara y almacenamiento", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if (grantResults.length>0){
                    boolean storage_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Porfavor habilitar permisos de almacenamiento", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }
    //overrid method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                Picasso.get().load(resultUri).into(imagen);
            }
        }
    }
}