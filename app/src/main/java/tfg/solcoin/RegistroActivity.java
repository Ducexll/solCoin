package tfg.solcoin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class RegistroActivity extends AppCompatActivity {

    private EditText passwordEditText, nombreEditText, correoEditText, confirmPasswordEditText;
    private Spinner spCurso, spClase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Referencia los EditText del layout
        passwordEditText = findViewById(R.id.editTextContraseña);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmarContraseña);
        nombreEditText = findViewById(R.id.editTextNombre);
        correoEditText = findViewById(R.id.editTextCorreo);
        spCurso = findViewById(R.id.spinnerCurso);
        spClase = findViewById(R.id.spinnerClase);

        ArrayList<String> opcionesCurso = new ArrayList<String>();
        opcionesCurso.add("1");
        opcionesCurso.add("2");

        ArrayAdapter<String> adapterCurso = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesCurso);
        spCurso.setAdapter(adapterCurso);

        ArrayList<String> opcionesClase = new ArrayList<String>();
        opcionesClase.add("SMR");
        opcionesClase.add("DAM");

        ArrayAdapter<String> adapterClase = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesClase);
        spClase.setAdapter(adapterClase);

        // Deshabilitar verificación de SSL (NO RECOMENDADO PARA PRODUCCIÓN)
        // Asegúrate de que esto solo se utilice para fines de prueba y desarrollo
        // NO USES ESTO EN PRODUCCIÓN
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        // Referencia el botón de ir al Login y agrega un listener
        Button loginButton = findViewById(R.id.buttonRegistrar);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePasswords()) {
                    ejecutarServicio(getString(R.string.url)+"insertarUsuario.php");
                } else {
                    Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    // Realiza la validación de las contraseñas
    private boolean validatePasswords() {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        return password.equals(confirmPassword);
    }

    // Inicia la actividad de Login
    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                goToLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("nombre",nombreEditText.getText().toString());
                parametros.put("correo",correoEditText.getText().toString());
                parametros.put("contrasenna",passwordEditText.getText().toString());
                parametros.put("curso",spCurso.getSelectedItem().toString());
                parametros.put("clase",spClase.getSelectedItem().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}