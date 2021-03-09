package uteq.student.project.clientrentof.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reporte#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reporte extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView apellidos, nombres, cedula1, telefono1, email1;
    FirebaseFirestore mFirestore;

    //OBTENER UID DE INICIO DE SESIÓN
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String x = currentFirebaseUser.getUid();
    String var;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public reporte() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reporte.
     */
    // TODO: Rename and change types and number of parameters
    public static reporte newInstance(String param1, String param2) {
        reporte fragment = new reporte();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View c = inflater.inflate(R.layout.fragment_reporte, container, false);
        //var = this.getIntent().getExtras().getString("id_duenio");
        //INICIALIZAR VARIABLES
        mFirestore = FirebaseFirestore.getInstance();
        apellidos = c.findViewById(R.id.idApellidos);
        nombres = c.findViewById(R.id.idNombres);
        cedula1 = c.findViewById(R.id.idCedula);
        telefono1 = c.findViewById(R.id.idTelefono);
        email1 = c.findViewById(R.id.idEmail);
        //LLAMAR FUNCION
        obtenerDatosCliente();
        return c;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}