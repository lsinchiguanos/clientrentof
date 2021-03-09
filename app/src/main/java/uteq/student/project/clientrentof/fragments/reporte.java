package uteq.student.project.clientrentof.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import uteq.student.project.clientrentof.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

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
    TextView apellidos, nombres, cedula1, telefono1, email1, email2, fecha, limite, limiteChoque, SuperVelocidad, Tanque, ValorDiario, Geografico, placa;
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
        email2 = c.findViewById(R.id.idTitulo);
        //Datos Contratos
        fecha = c.findViewById(R.id.idFecha);
        limite = c.findViewById(R.id.idLimite);
        limiteChoque = c.findViewById(R.id.idLimiteChoques);
        SuperVelocidad = c.findViewById(R.id.idSuperVelocidad);
        Tanque = c.findViewById(R.id.idTanqueFull);
        ValorDiario = c.findViewById(R.id.idValorDiario);
        Geografico = c.findViewById(R.id.idGeografico);
        placa = c.findViewById(R.id.idVehiculo);
        obtenerDatosCliente();
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

    public void Cargar(View v) {
        String a = email2.getText().toString();
        mFirestore.collection("contratos")
                .whereEqualTo("idCliente", x)
                .whereEqualTo("titulo", a)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String fecha2 = document.getString("fecha");
                                String limite2 = document.getString("limite");
                                String limiteChoques2 = document.getString("limite_choques");
                                String super_velocidad2 = document.getString("super_velocidad");
                                String tanque_full = document.getString("tanque_full");
                                String valor_diario = document.getString("valor_diario");
                                String valor_geografico = document.getString("valor_geografico");
                                String vehiculo = document.getString("vehiculo");

                                fecha.setText(fecha2);
                                limite.setText(limite2);
                                limiteChoque.setText(limiteChoques2);
                                SuperVelocidad.setText(super_velocidad2);
                                Tanque.setText(tanque_full);
                                ValorDiario.setText(valor_diario);
                                Geografico.setText(valor_geografico);
                                placa.setText(vehiculo);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    public void createpdf(View v) {
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String pdfPath = "/" + "REPORTE   DE CONTRATO :" + mydate + "" + ".pdf";
        File file1 = new File(pdfPath);
        Document document = new Document(PageSize.A4);
        try {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file1.getAbsoluteFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //PdfWriter writer = new PdfWriter(file1);
        //PdfDocument pdfDocument = new PdfDocument(writer);
        document.open();
        try {
//DATOS PERSONALES
            String nombre = nombres.getText().toString();
            String apellido = apellidos.getText().toString();
            String cedula2 = cedula1.getText().toString();
            String tele = telefono1.getText().toString();
            String ema = email1.getText().toString();
            //DATOS CONTRATOS
            String fecha3 = fecha.getText().toString();
            String limite3 = limite.getText().toString();
            String limiteChoques3 = limiteChoque.getText().toString();
            String super_velocidad3 = SuperVelocidad.getText().toString();
            String tanque_full3 = Tanque.getText().toString();
            String valor_diario3 = ValorDiario.getText().toString();
            String valor_geografico3 = Geografico.getText().toString();
            String vehiculo3 = placa.getText().toString();
            document.add(new Paragraph("DATOS PERSONALES"));
            document.add(new Paragraph("NOMBRES : " + nombre));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("APELLIDOS : " + apellido));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CÉDULA : " + cedula2));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CELULAR : " + tele));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CORREO : " + ema));
            document.add(new Paragraph("DATOS DE CONTRATOS"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("FECHA : " + fecha3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("limite : " + limite3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("LIMITE CHOQUES : " + limiteChoques3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("VELOCIDAD : " + super_velocidad3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Tanque : " + tanque_full3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Valor_diario : " + valor_diario3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("GEOGRAFICO : " + valor_geografico3));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("PLACA VEHICULO : " + vehiculo3));
            document.add(new Paragraph("\n"));

        } catch (Exception e) {

        }
        Toast.makeText(getApplicationContext(), "confirmado", Toast.LENGTH_LONG).show();
        document.close();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}