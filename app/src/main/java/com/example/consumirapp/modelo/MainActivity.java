package com.example.consumirapp.modelo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.consumirapp.R;
import com.example.consumirapp.interfaces.productoAPP;
import com.example.consumirapp.modelo.producto;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText txtenvio;
    TextView txtresultado;
    Button bntBuscar;
    Button bntBuscarVolley;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtenvio = findViewById(R.id.txtvalue);
        bntBuscar = findViewById(R.id.bntBotonRetro);
        txtresultado = findViewById(R.id.txtResultado);
        bntBuscarVolley = findViewById(R.id.bntbotonvolley);

        bntBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtresultado.setText("");

                if(txtenvio.getText().length()>0) {
                    resulta(txtenvio.getText().toString());
                    txtenvio.setText("");
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ingrese el id  a buscar", Toast.LENGTH_SHORT);
                    toast.show();

                }

            }
        });
        bntBuscarVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtresultado.setText("");
                if(txtenvio.getText().length()>0) {
                    resultadoVolley(txtenvio.getText().toString());
                    txtenvio.setText("");
                }else{Toast toast = Toast.makeText(getApplicationContext(), "Ingrese el id  a buscar", Toast.LENGTH_SHORT);
                    toast.show();}
            }
        });

    }

    public void resultadoVolley(String dato) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = "https://revistas.uteq.edu.ec/ws/issues.php?j_id="+dato;
        JsonObjectRequest jsonAr = new JsonObjectRequest(Request.Method.GET,
                url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response) {

                        txtresultado.append("Response: "+ response.toString());

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtresultado.setText(error.getMessage());
            }
        }
        );
        queue.add(jsonAr);
    }

    public void resulta(String q) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://revistas.uteq.edu.ec/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        productoAPP postService = retrofit.create(productoAPP.class);
        Call<List<producto>> call = postService.find(q);
        call.enqueue(new Callback<List<producto>>() {
            @Override
            public void onResponse(Call<List<producto>> call, Response<List<producto>> response) {
                List<producto> postList = response.body();
                for (producto p : postList) {
                    String resultado = "";
                    resultado += "issue_id:" + p.getIssue_id() + "\n";
                    resultado += "volume:" + p.getVolumen() + "\n";
                    resultado += "number:" + p.getNumber() + "\n";
                    resultado += "year:" + p.getYear() + "\n";
                    resultado += "date_published:" + p.getDate_published() + "\n";
                    resultado += "title:" + p.getTitle() + "\n";
                    resultado += "doi:" + p.getDoi() + "\n";
                    resultado += "cover:" + p.getCover() + "\n";
                    txtresultado.append(resultado);


                }
            }

            @Override
            public void onFailure(Call<List<producto>> call, Throwable t) {
                txtresultado.setText(t.getMessage());

            }

        });
    }
}

