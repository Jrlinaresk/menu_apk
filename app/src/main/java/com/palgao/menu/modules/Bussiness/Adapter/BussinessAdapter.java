package com.palgao.menu.modules.Bussiness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.R;
import com.palgao.menu.modules.Bussiness.Fragments.BussinessViewModel;
import com.palgao.menu.modules.Bussiness.entityes.Bussiness;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BussinessAdapter extends RecyclerView.Adapter<BussinessAdapter.BussinessViewHolder> {
    private List<Bussiness> bussineList;
    private List<Bussiness> filteredBussinessList;
    private List<Bussiness> listTemp;
    private BussinessViewModel bussinessViewModel;
    private int isHorizontalLayout;
    private RecyclerView recyclerView;
    private Context context;
    private FragmentManager fragmentManager;

    public BussinessAdapter(Context context, List<Bussiness> bussineList, int isHorizontalLayout, BussinessViewModel bussinessViewModel, RecyclerView recyclerView, FragmentManager fragmentManager) {
        this.bussineList = bussineList != null ? bussineList : new ArrayList<>();
        this.recyclerView = recyclerView;
        this.filteredBussinessList = new ArrayList<>(this.bussineList);
        this.listTemp = new ArrayList<>(this.filteredBussinessList);
        this.isHorizontalLayout = isHorizontalLayout;
        this.bussinessViewModel = bussinessViewModel;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public BussinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.item_bussiness) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bussiness, parent, false);
        } else if (viewType == R.layout.item_producto_plano) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_plano, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_plano, parent, false);
        }
        return new BussinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BussinessViewHolder holder, int position) {
        Bussiness bussine = filteredBussinessList.get(position);

        holder.bind(bussine);
    }

    @Override
    public int getItemViewType(int position) {
        switch (isHorizontalLayout) {
            case 1:
                return R.layout.item_bussiness;
            case 2:
                return R.layout.item_producto_plano;
            case 3:
                return R.layout.item_producto_plano;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return filteredBussinessList != null ? filteredBussinessList.size() : 0;
    }

    public void setBussiness(List<Bussiness> bussineList) {
        this.bussineList = bussineList != null ? bussineList : new ArrayList<>();
        this.filteredBussinessList = new ArrayList<>(this.bussineList);
        this.listTemp = new ArrayList<>(this.filteredBussinessList);

        // updateUI(bussineList);
        notifyDataSetChanged();
    }
    public void setBussinessByType(String type) {
        if  (type.equals("Todos"))
        {
            this.bussineList = bussineList != null ? bussineList : new ArrayList<>();
            this.filteredBussinessList = new ArrayList<>(this.bussineList);
            this.listTemp = new ArrayList<>(this.filteredBussinessList);
        }
        else if (this.bussineList != null) {
            // Filtrando la lista de bussineos por el campo 'type'
            List<Bussiness> filteredList = this.bussineList.stream()
                    .filter(bussine -> type.equals(bussine.getType()))
                    .collect(Collectors.toList());

            // Asignar la lista filtrada a bussineList
            this.filteredBussinessList = filteredList;
            this.listTemp = new ArrayList<>(this.filteredBussinessList);
        } else {
            // Si la lista es null, se asigna una lista vacía
            this.bussineList = new ArrayList<>();
            this.filteredBussinessList = new ArrayList<>();
            this.listTemp = new ArrayList<>(this.filteredBussinessList);
        }


        // Actualizar la UI con la lista filtrada y notificar los cambios
       // updateUI(this.bussineList);
        notifyDataSetChanged();
    }


    private void updateUI(List<Bussiness> bussiness) {
        if (bussiness.isEmpty()) {
            showNoResultsFragment();
        } else {
            showBussinessListFragment();
        }
    }

    public void setLayoutType(int isHorizontalLayout) {
        this.isHorizontalLayout = isHorizontalLayout;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredBussinessList.clear();
        if (query.isEmpty()) {
            filteredBussinessList.addAll(listTemp);
        } else {
            String queryLower = query.toLowerCase();
            for (Bussiness bussine : listTemp) {
                if (bussine.getName().toLowerCase().contains(queryLower)) {
                    filteredBussinessList.add(bussine);
                }
            }
            if(filteredBussinessList.isEmpty())
            {
                for (Bussiness bussine : listTemp) {
                    if (bussine.getDescription().toLowerCase().contains(queryLower)) {
                        filteredBussinessList.add(bussine);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    static class BussinessViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_bussiness_name;
        private final TextView tv_isOpen;
        private final ImageView bussinessImage;
        private final TextView tv_details, mtv_type;
        public BussinessViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_bussiness_name = itemView.findViewById(R.id.tv_bussiness_name);
            tv_isOpen = itemView.findViewById(R.id.tv_isOpen);
            bussinessImage = itemView.findViewById(R.id.bussinessImage);
            tv_details = itemView.findViewById(R.id.tv_details);
            mtv_type = itemView.findViewById(R.id.tv_type);
        }

        public void bind(Bussiness bussine) {
            if (!bussine.getDescription().isEmpty())
            {
                tv_details.setText(bussine.getDescription());
            }
            else {
                tv_details.setVisibility(View.GONE);
            }
            tv_bussiness_name.setText(bussine.getName());
            tv_isOpen.setText(String.valueOf(bussine.getHorario())); // cambiar luego por estado es decir abierto o cerrado

            String profilePictureUrl;
            profilePictureUrl = "https://pc3ld10h-8080.usw3.devtunnels.ms/uploads/" + bussine.getImageLogoUrl(); //

            if (profilePictureUrl != null && !profilePictureUrl.isEmpty())
                Picasso.get()
                        .load(profilePictureUrl)
                        .placeholder(R.drawable.ic_closet) // Imagen de placeholder si la original no está disponible
                        .error(R.drawable.ic_usuario) // Imagen de error si ocurre algún problema
                        .into(bussinessImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                // Imagen cargada con éxito
                                int a = 0;
                            }

                            @Override
                            public void onError(Exception e) {
                                // Error al cargar la imagen
                                e.printStackTrace();
                            }
                        });
        }
    }

    private void showNoResultsFragment() {
        //ocultar el rv y mostrar que no se encontro elementos
        recyclerView.setVisibility(View.GONE);
    }

    private void showBussinessListFragment() {
        recyclerView.setVisibility(View.VISIBLE);
    }
}
