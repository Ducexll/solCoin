package tfg.solcoin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegistroProfesoresActivity extends AppCompatActivity {

    private EditText passwordEditText, nombreEditText, correoEditText, confirmPasswordEditText, contrasennaDocente;
    private String opcionesSeleccionadas ;
    private CheckBox checkBox1SMRD, checkBox1SMRV, checkBox2SMRV, checkBox2SMRD, checkBox1ASIRV, checkBox2ASIRV;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_profesores);

        // Referencia los EditText del layout
        passwordEditText = findViewById(R.id.editTextContraseña);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmarContraseña);
        nombreEditText = findViewById(R.id.editTextNombre);
        correoEditText = findViewById(R.id.editTextCorreo);
        contrasennaDocente = findViewById(R.id.editTextContrasennaDocente);

        // Referencia los CheckBox del layout
        checkBox1SMRD = findViewById(R.id.checkBox1SMRD);
        checkBox1SMRV = findViewById(R.id.checkBox1SMRV);
        checkBox2SMRD = findViewById(R.id.checkBox2SMRD);
        checkBox2SMRV = findViewById(R.id.checkBox2SMRV);
        checkBox1ASIRV = findViewById(R.id.checkBox1ASIRV);
        checkBox2ASIRV = findViewById(R.id.checkBox2ASIRV);

        // Referencia el botón de registro y agrega un listener
        Button loginButton = findViewById(R.id.buttonRegistrar);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                opcionesSeleccionadas = "";

                if (checkBox1SMRD.isChecked()) {
                    opcionesSeleccionadas += "1-SMR-D,";
                }
                if (checkBox1SMRV.isChecked()) {
                    opcionesSeleccionadas += "1-SMR-V,";
                }
                if (checkBox2SMRD.isChecked()) {
                    opcionesSeleccionadas += "2-SMR-D,";
                }
                if (checkBox2SMRV.isChecked()) {
                    opcionesSeleccionadas += "2-SMR-V,";
                }
                if (checkBox1ASIRV.isChecked()) {
                    opcionesSeleccionadas += "1-ASIR-V,";
                }
                if (checkBox2ASIRV.isChecked()) {
                    opcionesSeleccionadas += "2-ASIR-V,";
                }
                //Como siempre va a tener una coma al final, se la quitamos para dejar el curClase adaptado para la consulta sql
                opcionesSeleccionadas = opcionesSeleccionadas.substring(0, opcionesSeleccionadas.length() - 1);


                if (validatePasswords()) {

                    if(validatePasswordsDocente()){

                        if (opcionesSeleccionadas.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "NO SE HA SELECCIONADO NINGÚN CURSO", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), opcionesSeleccionadas, Toast.LENGTH_SHORT).show();
                            ejecutarServicio(getString(R.string.url) + "insertarUsuarioProfesor.php");
                        }

                    }
                    else{
                        Toast.makeText(RegistroProfesoresActivity.this, "La contraseña de docente no coincide", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegistroProfesoresActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
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

    //Validar contraseña de docente
    private boolean validatePasswordsDocente() {
        String password = contrasennaDocente.getText().toString();
        String confirmPassword = "hola";
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
                parametros.put("curClase",opcionesSeleccionadas);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
