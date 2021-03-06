package zilic.kolokvij1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.ItemClickListener {

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new Adapter(this);
        adapter.setItemClickListener(this);
        RecyclerView lista = findViewById(R.id.lista);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        new REST().execute("https://oziz.ffos.hr/nastava20202021/tzilic_20/v2/mjerenja");

    }

    @Override
    public void onItemClick(Mjerenje mjerenje) {
        Log.d("Stigao", "---");
        //ako nije String, npr. datum ili broj, morate ga pretvoriti u tekst s String.valueOf(ovdje ide varijabla)
        Toast.makeText(this, "Datum: "+String.valueOf(mjerenje.getDatum())+" ,Iznos: "+ String.valueOf(mjerenje.getIznos()),Toast.LENGTH_LONG).show();
    }


    private class REST extends AsyncTask<String,Void, List<Mjerenje>>{

        @Override
        protected List<Mjerenje> doInBackground(String... strings) {
            String stringUrl = strings[0];
            Log.wtf(">>>>> ",stringUrl);
            List<Mjerenje> vrati=null;
            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                Type listType = new TypeToken<ArrayList<Mjerenje>>(){}.getType();
                vrati = new Gson().fromJson(reader, listType);
                reader.close();
                streamReader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return vrati;
        }

        @Override
        protected void onPostExecute(List<Mjerenje> mjerenjes) {
            adapter.setMjerenja(mjerenjes);
            adapter.notifyDataSetChanged();
        }
    }
}