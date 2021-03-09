package uteq.student.project.clientrentof.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import uteq.student.project.clientrentof.R;
import uteq.student.project.clientrentof.interfaces.IComunicacionFragments;
import uteq.student.project.clientrentof.models.PublicacionModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicacionFragment extends Fragment {

    Activity activity;
    private RecyclerView mFirestore_list;
    private FirebaseFirestore firebaseFirestore;
    private FirestorePagingAdapter adapter;
    Query query;
    PagedList.Config config;
    String id_cliente;
    Bundle b = new Bundle();
    FloatingActionButton btnAdd;
    IComunicacionFragments iComunicacionFragments;
    MenuFragment.OnFragmentInteractionListener onFragmentInteractionListener;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PublicacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicacionFragment newInstance(String param1, String param2) {
        PublicacionFragment fragment = new PublicacionFragment();
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

        firebaseFirestore = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_publicacion, container, false);

        mFirestore_list = view.findViewById(R.id.firestore_listP);
        btnAdd = view.findViewById(R.id.fbAddPublicacion);
        id_cliente = this.getArguments().getString("id_cliente");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
               // iComunicacionFragments.addpublicacion(id_duenio);
            }
        });
        loadPublicaciones();
        return  view;
    }


    void loadPublicaciones() {
        query = firebaseFirestore.collection("publicacion").whereEqualTo("estado", "VIGENTE");
        config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(7)
                .setPageSize(2)
                .build();

        FirestorePagingOptions<PublicacionModel> option = new FirestorePagingOptions.Builder<PublicacionModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<PublicacionModel>() {
                    @NonNull
                    @Override
                    public PublicacionModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        PublicacionModel cardsModel = snapshot.toObject(PublicacionModel.class);
                        String item_id = snapshot.getId();
                        cardsModel.setId(item_id);
                        return cardsModel;
                    }
                }).build();

        adapter = new FirestorePagingAdapter<PublicacionModel, PublicacionFragment.CardsViewHolder>(option) {
            @NonNull
            @Override
            public PublicacionFragment.CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_fragment_publicacion, parent, false);
                return new PublicacionFragment.CardsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CardsViewHolder holder, int position, @NonNull PublicacionModel model) {
                holder.tituloPublicacion.setText(model.getTitulo());

                firebaseFirestore.collection("vehiculo").document(model.getVehiculo()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            holder.marca.setText(documentSnapshot.getString("marca"));
                            holder.anioC.setText(documentSnapshot.getString("anio"));
                            Glide.with(PublicacionFragment.this).load(documentSnapshot.getString("url"))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.img2);
                        } else {
                        }
                    }
                });
                holder.costodiario.setText(String.valueOf(model.getValor_diario()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //iComunicacionFragments.editpublicacion(id_duenio, model.getId(),model.getVehiculo());
                    }
                });

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {
                    case LOADING_INITIAL:
                        Log.d("PAGING_LOG", "Loading Initial Date");
                        break;
                    case LOADING_MORE:
                        Log.d("PAGING_LOG", "Total Next Page");
                        break;
                    case FINISHED:
                        Log.d("PAGING_LOG", "All Date Loaded");
                        break;
                    case ERROR:
                        Log.d("PAGING_LOG", "Error Loading Data");
                        break;
                    case LOADED:
                        Log.d("PAGING_LOG", "Total Items Loaded:" + getItemCount());
                        break;
                }
            }
        };
        mFirestore_list.setHasFixedSize(true);
        mFirestore_list.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestore_list.setAdapter(adapter);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class CardsViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloPublicacion, marca, anioC, costodiario;
        private ImageView img2;

        public CardsViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloPublicacion = itemView.findViewById(R.id.tituloPublicacion);
            marca = itemView.findViewById(R.id.marca);
            anioC = itemView.findViewById(R.id.anioC);
            costodiario = itemView.findViewById(R.id.costodiario);
            img2 = itemView.findViewById(R.id.img2);
        }
    }
}