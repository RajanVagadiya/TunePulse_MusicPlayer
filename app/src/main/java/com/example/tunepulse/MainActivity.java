package com.example.tunepulse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
 //RecyclerView recyclerView;
    //ArrayList<MusicModel> arrMusics = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        listView = findViewById(R.id.listView);
        //recyclerView = findViewById(R.id.recyclerView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                        String[] items = new String[mySongs.size()];
                        //to populate single items from items object
                        for(int i=0;i<mySongs.size();i++){
                            items[i] = mySongs.get(i).getName().replace(".mp3"," ");
                            //MusicModel music = new MusicModel(R.drawable.music_icon,mySongs.get(i).getName());
                           // arrMusics.add(music);

                        }

                       ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);

                        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        //data are set into the ContactModel class object
//                        for(int i=0;i<mySongs.size();i++){
//                            MusicModel music = new MusicModel(R.drawable.music_icon,mySongs.get(i).getName());
//                            //pass object into arraylist to add
//                            arrMusics.add(music);
//                        }

                        //RecyclerMusicAdapter adapter = new RecyclerMusicAdapter(getApplicationContext(),arrMusics);
                        //recyclerView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList",mySongs);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                      if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                          Toast.makeText(MainActivity.this, "Required Storage Permission!! Please Allow from Settings", Toast.LENGTH_LONG).show();
                      }
                      else {
                          ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                      }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                      permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }

    public ArrayList<File> fetchSongs(File file){
        ArrayList arrayList = new ArrayList();
        File[] songs = file.listFiles();
        if(songs != null){
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else {
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "you have no songs bro!!", Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }
//ArrayList<String> fetchSongs(String rootPath) {
//    ArrayList<String> fileList = new ArrayList<>();
//    try{
//        File rootFolder = new File(rootPath);
//        File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
//        for (File file : files) {
//            if (file.isDirectory()) {
//                if (fetchSongs(file.getAbsolutePath()) != null) {
//                    fileList.addAll(fetchSongs(file.getAbsolutePath()));
//                } else {
//                    break;
//                }
//            } else if (file.getName().endsWith(".mp3")) {
//                fileList.add(file.getAbsolutePath());
//            }
//        }
//        return fileList;
//    }catch(Exception e){
//        return null;
//    }
//}
}