package tfg.solcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Referencia los EditText para la contraseña y confirmar contraseña
        passwordEditText = findViewById(R.id.editTextContraseña);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmarContraseña);

        // Referencia el botón de ir al Login y agrega un listener
        Button loginButton = findViewById(R.id.buttonRegistrar);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePasswords()) {
                    goToLogin();
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
}

