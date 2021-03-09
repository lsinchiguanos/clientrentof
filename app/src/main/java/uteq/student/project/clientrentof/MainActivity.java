package uteq.student.project.clientrentof;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import uteq.student.project.clientrentof.activities.ReporteContrato;
import uteq.student.project.clientrentof.fragments.MenuFragment;
import uteq.student.project.clientrentof.fragments.PublicacionFragment;
import uteq.student.project.clientrentof.interfaces.IComunicacionFragments;

public class MainActivity extends AppCompatActivity implements IComunicacionFragments, MenuFragment.OnFragmentInteractionListener, PublicacionFragment.OnFragmentInteractionListener {

    private Fragment fragmentPublicacion;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String emailUser,id_cliente;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Bundle b = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        emailUser = getIntent().getExtras().getString("email");
        id_cliente = getIntent().getExtras().getString("id_cliente");
        fragmentPublicacion = new PublicacionFragment();
        preferences = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("email", emailUser);
        editor.putString("id_cliente", id_cliente);
        editor.apply();
        b.putString("id_cliente", id_cliente);
        fragmentPublicacion.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, fragmentPublicacion).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void contratos() {

    }

    @Override
    public void monitoreo() {

    }
    @Override
    public void publicacion() {
    }
    @Override
    public void signUp() {
        FirebaseAuth.getInstance().signOut();
        preferences = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear();
        editor.apply();
        onBackPressed();
    }
    @Override
    public void ReporteContrato() {
        Intent intent = new Intent(this, ReporteContrato.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
       // intent.putExtra("id_duenio",id_duenio);
        startActivity(intent);
    }
}