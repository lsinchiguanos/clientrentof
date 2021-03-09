package uteq.student.project.clientrentof.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class ReporteContrato extends AppCompatActivity {

    TextView apellidos, nombres, cedula1, telefono1, email1, email2, fecha, limite, limiteChoque, SuperVelocidad, Tanque, ValorDiario, Geografico, placa;
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
        email2 = findViewById(R.id.idTitulo);
        //Datos Contratos
        fecha = findViewById(R.id.idFecha);
        limite = findViewById(R.id.idLimite);
        limiteChoque = findViewById(R.id.idLimiteChoques);
        SuperVelocidad = findViewById(R.id.idSuperVelocidad);
        Tanque = findViewById(R.id.idTanqueFull);
        ValorDiario = findViewById(R.id.idValorDiario);
        Geografico = findViewById(R.id.idGeografico);
        placa = findViewById(R.id.idVehiculo);
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

    public void createpdf(View v) {
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String pdfPath = getExternalFilesDir(null).toString() + "/" + "REPORTE   DE CONTRATO :" + mydate + "" + ".pdf";
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

}