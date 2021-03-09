package uteq.student.project.clientrentof.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import uteq.student.project.clientrentof.R;

public class ReporteContrato extends AppCompatActivity {

    TextView apellidos, nombres, cedula1, telefono1, email1;
    FirebaseFirestore mFirestore;

    //OBTENER UID DE INICIO DE SESIÓN
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String x = currentFirebaseUser.getUid();
    String var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_contrato);
        var = getIntent().getExtras().getString("id_duenio");
        //INICIALIZAR VARIABLES
        mFirestore = FirebaseFirestore.getInstance();
        apellidos = findViewById(R.id.idApellidos);
        nombres = findViewById(R.id.idNombres);
        cedula1 = findViewById(R.id.idCedula);
        telefono1 = findViewById(R.id.idTelefono);
        email1 = findViewById(R.id.idEmail);
        //LLAMAR FUNCION
        obtenerDatosCliente();
    }

    private void obtenerDatosCliente() {
        mFirestore.collection("cliente").document(x).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nombre = documentSnapshot.getString("nombres");
                    String Apellido_materno = documentSnapshot.getString("apellido_materno");
                    String Apellido_Paterno = documentSnapshot.getString("apellido_paterno");
                    String cedula = documentSnapshot.getString("cedula");
                    String telefono = documentSnapshot.getString("telefono");
                    String email = documentSnapshot.getString("email");
                    nombres.setText(nombre);
                    apellidos.setText(Apellido_Paterno + " " + Apellido_materno);
                    cedula1.setText(cedula);
                    telefono1.setText(telefono);
                    email1.setText(email);
                }
            }
        });
    }

    private void s() {
        mFirestore.collection("contratos")
                .whereEqualTo("fecha", "21/07/2020")
                .whereEqualTo("idCliente", x)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String Apellido_materno = document.getString("estado");
                                String Apellido_Paterno = document.getString("limite");
                                apellidos.setText(Apellido_Paterno + " " + Apellido_materno);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}