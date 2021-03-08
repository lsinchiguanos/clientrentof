package uteq.student.project.clientrentof;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import uteq.student.project.clientrentof.fragments.MenuFragment;
import uteq.student.project.clientrentof.interfaces.IComunicacionFragments;

public class MainActivity extends AppCompatActivity implements IComunicacionFragments, MenuFragment.OnFragmentInteractionListener {

    private Fragment fragmentMenu;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String emailUser,id_duenio;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        emailUser = getIntent().getExtras().getString("email");
        id_duenio = getIntent().getExtras().getString("id_duenio");
        fragmentMenu = new MenuFragment();
        preferences = getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("email", emailUser);
        editor.putString("id_duenio", id_duenio);
        editor.apply();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, fragmentMenu).commit();
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
}