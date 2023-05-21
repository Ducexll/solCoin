package tfg.solcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private String url = "http://192.168.83.201:80/developjulio/login.php";
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencia los EditText para el nombre de usuario y contraseña
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Referencia el botón de inicio de sesión y agrega un listener
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene los valores de los EditText para el nombre de usuario y contraseña
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                validateCredentials(username, password);
            }
        });

        // Referencia el botón de registro y agrega un listener
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicia la actividad de registro de usuario
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        preferencias = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
    }

    // Realiza la validación de las credenciales de inicio de sesión
    private void validateCredentials(String username, String password) {
        StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuesta) {
                        try {
                            JSONObject jsonObjectResultado = new JSONObject(respuesta);
                            String estado = jsonObjectResultado.getString("estado");
                            if (estado.equals("exito")) {
                                JSONArray datosArray = jsonObjectResultado.getJSONArray("datos");
                                if (datosArray.length() > 0) {
                                    JSONObject datosUsuario = datosArray.getJSONObject(0);
                                    String correo = datosUsuario.getString("correo");
                                    String nombre = datosUsuario.getString("nombre");
                                    String contrasenna = datosUsuario.getString("contrasenna");


                                    //Guardamos el correo en preferencias
                                    SharedPreferences.Editor editor = preferencias.edit();
                                    editor.putString("correo", usernameEditText.getText().toString());
                                    editor.apply();
                                    String correopref = preferencias.getString("correo","correo error");
                                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso con correo: " + correopref, Toast.LENGTH_SHORT).show();

                                    // Por ejemplo, puedes guardar el estado de inicio de sesión en SharedPreferences y abrir la siguiente actividad
                                    // Guardar el estado de inicio de sesión en SharedPreferences
                                    // SharedPreferences sharedPreferences = getSharedPreferences("nombre_preferencias", MODE_PRIVATE);
                                    // SharedPreferences.Editor editor = sharedPreferences.edit();
                                    // editor.putBoolean("sesion_iniciada", true);
                                    // editor.apply();
                                    // Abrir la siguiente actividad
                                    // Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                                    // startActivity(intent);
                                    // finish();
                                    Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String mensaje = jsonObjectResultado.getString("mensaje");
                                Toast.makeText(LoginActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error al comunicarse con el servidor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Agregar los datos de inicio de sesión como parámetros
                Map<String, String> params = new HashMap<>();
                params.put("correo", username);
                params.put("contrasenna", password);
                return params;
            }
        };
        // Agregar la solicitud a la cola de Volley
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(solicitud);
    }
}
