package tfg.solcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

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

                // Realiza la validación de inicio de sesión
                if (validateCredentials(username, password)) {
                    // Si las credenciales son correctas, inicia la actividad principal
                    Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Si las credenciales son incorrectas, muestra un mensaje de error
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
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
    }

    // Realiza la validación de las credenciales de inicio de sesión
    private boolean validateCredentials(String username, String password) {
        // Aquí puedes agregar la lógica para validar las credenciales
        // Por ejemplo, comunicándote con un servidor de autenticación o una base de datos
        // Este código de ejemplo siempre devuelve true
        return true;
    }

}
