package com.droid.okhttpgsonandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.droid.okhttpgsonandroid.adapter.MuseeAdapter;
import com.google.gson.*;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String urlOpenData;
    private OkHttpClient client;
    private Request request;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView lvMusees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //récupere la listView
        lvMusees=(ListView) findViewById(R.id.lvMusees);

        Thread.currentThread().setName("UIThread");
        Log.d(TAG,"Current Thread: "+Thread.currentThread().getName());

        //URL du webservice Open Data Paris
        urlOpenData="https://opendata.paris.fr/api/records/1.0/search/?dataset=liste-musees-de-france-a-paris&pretty_print=true";

        //Préparation d'un objet OkHttp
        client=new OkHttpClient();

        //On utilise le design Pattern Builder pour construire l'objet Request
        request=new Request.Builder().url(urlOpenData).build();

        //Ici on lance une requête http asynchrone pour ne pas bloquer le thread principal
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(TAG,"OnResponse Thread: "+Thread.currentThread().getName());

                //Quand le json est téléchargé ,cette ùéthode est executée
                if(!response.isSuccessful()) {

                    throw new IOException("Unexpected code "+response);
                }else{

                    //et içi on récupère le Json
                    String json=response.body().string();
                    Log.d(TAG,"onResponse: "+json);

                    //Parsing du json avec la librairie Gson
                    //Construire un objet JSonObject
                    Gson gson= new Gson();
                    //JSonObject est un POJO:Json en Objet Java correspondant à notre classe obtenu avec GsonFormat
                   JSonObject jsonObject=gson.fromJson(json, JSonObject.class);

                    //On récupère la liste des objetcs recordsBean
                    final List <JSonObject.RecordsBean> musees=jsonObject.getRecords();
                    Log.d(TAG, "Nb de musées "+musees.size());

                    //On parcourt cette Liste
                    for (JSonObject.RecordsBean musee:musees) {
                        //On lit le nom du musee...
                        JSonObject.RecordsBean.FieldsBean fieldsBean=musee.getFields();

                        //Et on l'affiche dans LogCat
                        Log.d(TAG,"onResponse: "+fieldsBean.getNom_du_musee());
                    }

                    //MuseeAdapter museeAdapter=new MuseeAdapter(MainActivity.this,musees); ERREUR car n'est pas exécuté dans le thread principale
                    //lvMusees.setAdapter(museeAdapter);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            MuseeAdapter museeAdapter=new MuseeAdapter(MainActivity.this,musees);
                            lvMusees.setAdapter(museeAdapter);
                        }
                    });


                }

            }
        });

    }
}
