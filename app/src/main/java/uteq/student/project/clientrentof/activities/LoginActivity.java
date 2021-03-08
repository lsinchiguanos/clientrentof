package uteq.student.project.clientrentof.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import uteq.student.project.clientrentof.MainActivity;
import uteq.student.project.clientrentof.R;
import uteq.student.project.clientrentof.models.ClienteModel;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userName;
    private TextInputLayout password;
    private ClienteModel duenioModel;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String email;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        duenioModel = new ClienteModel();
        settingcontrollers();
        firebaseAuth = FirebaseAuth.getInstance();
        session();
    }
    public void OlvidoContrasenia(View v)
    {
        Intent intent = new Intent(this, OlvidoContrasenia.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void settingcontrollers(){
        userName = findViewById(R.id.textUserName);
        password = findViewById(R.id.textPassword);
    }

    private void getDataControllers(){
        duenioModel.setEmail(Objects.requireNonNull(userName.getEditText()).getText().toString());
        duenioModel.setPassword(Objects.requireNonNull(password.getEditText()).getText().toString());
    }

    public void goToRegisterUser(View view){
        intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void loginFirebase(View view) {
        getDataControllers();
        firebaseAuth.signInWithEmailAndPassword(duenioModel.getEmail(), duenioModel.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("email", task.getResult().getUser().getEmail());
                intent.putExtra("id_duenio",task.getResult().getUser().getProviderId());
                Toast.makeText(LoginActivity.this,task.getResult().getUser().getUid(),Toast.LENGTH_LONG).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void session(){
        preferences = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        email = preferences.getString("email", null);

        if (email != null) {
            LoginActivity.this.setVisible(false);
            intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("email", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginActivity.this.setVisible(true);
    }
}