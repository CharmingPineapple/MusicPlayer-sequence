package com.example.musicplayer_sequence;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewSong);

        runtimePermission();

    }

    // Запрос на чтение файлов с телефона
    public void runtimePermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }

    // Метод, который возвращает массив с файлами музыки
    public ArrayList<File> findSong (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
///////////////////////////////////////////////////
        File[] files = file.listFiles();

        assert files != null;
        for (File singelfile: files)
        {
            if (singelfile.isDirectory() && !singelfile.isHidden())
            {
                arrayList.addAll(findSong(singelfile));
            }
            else
            {
                if (singelfile.getName().endsWith(".mp3") || singelfile.getName().endsWith(".wav") || singelfile.getName().endsWith(".ogg"))
                {
                    arrayList.add(singelfile);
                }
            }
        }
        return arrayList;
    }

    // Отображение названий песен
    void displaySongs()
    {
        final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());

        items = new String[mySong.size()];
        for (int i = 0; i<mySong.size(); i++)
        {
            items[i] = mySong.get(i).getName().toString().replace(".mp3", "").replace(".wav", "").replace(".ogg", "");
        }
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(myAdapter);
    }
}