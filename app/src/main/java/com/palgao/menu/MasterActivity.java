package com.palgao.menu;

import static com.palgao.menu.tools.toolsUI.ReplaceFragment;
import static com.palgao.menu.tools.toolsUI.actualizarEstadoLocal;
import static com.palgao.menu.tools.toolsUI.convertHorario;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.modules.Bussiness.Fragments.BrandingFragment;
import com.palgao.menu.modules.Bussiness.DetailsFragment;
import com.palgao.menu.modules.Bussiness.Fragments.BusinessFragment;
import com.palgao.menu.modules.Bussiness.Fragments.BusinessViewModel;
import com.palgao.menu.modules.Bussiness.HorarioFragment;
import com.palgao.menu.modules.Bussiness.entityes.Business;
import com.palgao.menu.modules.Network.ConnectionFragment;
import com.palgao.menu.modules.Notifications.NotificationPush;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.ui.WaitingDialog;
import com.palgao.menu.modules.products.ui.ProductsFragment;

public class MasterActivity extends AppCompatActivity {

    //View Models
    private SharedLoadingViewModel sharedLoadingViewModel;
    private BusinessViewModel bussinessViewmodel;

    //Custom UI
    private WaitingDialog mWaitingDialog;
    private NotificationPush notificationPush;

    // Native UI
    private ImageView iv_notification;
    private TextView tv_actualizarEstadoLocal, tv_bussiness_name, tv_h, tv_h_end, tv_address_bussiness;
    private FragmentManager fragmentManager;
    private ConstraintLayout rootLayout;
    private View include_details, include_horario, include_branding;
    private FragmentContainerView fc_status_conection;

    // tools
    private boolean doubleBackToExitPressedOnce = false;
    private int horaOpen = 0,  horaClose = 0;  // Ejemplo: 9 AM, // Ejemplo: 10 PM


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVisual();
        initViewModels();
        Observers();
        Listeners();
        WebSocket();
        LoadInfo();

        ReplaceFragment(new ProductsFragment(), R.id.nav_host_fragment, getSupportFragmentManager());

        // notificaciones
        notificationPush = new NotificationPush(this);

        // Cargar el fragmento en el FragmentContainerView
        if (savedInstanceState == null) {  // Para evitar agregar el fragmento varias veces
            loadFragment(new ConnectionFragment());
        }
    }

    private void LoadInfo() {
        String businessId = "66f9e0b210388fd00e51ec66";
        bussinessViewmodel.getBusinessById(businessId).observe(this, new Observer<Business>() {
            @Override
            public void onChanged(Business business) {
                if (business != null) {
                    // Actualizar las vistas con los datos del negocio
                    tv_bussiness_name.setText(business.getName());
                    tv_h.setText(business.getHorario().getHora_open() + "");
                    tv_h_end.setText(business.getHorario().getHora_close()  + "");
                    tv_address_bussiness.setText(business.getAddress().fullAddress());
                    horaOpen = business.getHorario().getHora_open();
                    horaClose = business.getHorario().getHora_close();
                    //
                    String[] horarios = convertHorario(horaOpen, horaClose);
                    String apertura = horarios[0];
                    String cierre = horarios[1];
                    actualizarEstadoLocal(apertura, cierre, tv_actualizarEstadoLocal);
                }
            }
        });

    }

    private void WebSocket() {
        Intent serviceIntent = new Intent(this, NotificationPush.class);
        startForegroundService(serviceIntent);
    }

    private void Listeners() {
        include_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new DetailsFragment(), R.id.nav_info_bussiness, getSupportFragmentManager());
                iv_notification.setVisibility(View.VISIBLE);
            }
        });

        include_horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new HorarioFragment(), R.id.nav_info_bussiness, getSupportFragmentManager());
                iv_notification.setVisibility(View.VISIBLE);
            }
        });

        include_branding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new BusinessFragment(), R.id.nav_info_bussiness, getSupportFragmentManager());
                iv_notification.setVisibility(View.VISIBLE);
            }
        });

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_notification.setVisibility(View.GONE);

                // Eliminar el fragmento DetailsFragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.nav_info_bussiness);

                if (fragment != null)
                    fragmentManager.beginTransaction()
                        .remove(fragment)  // Remover el fragmento
                        .commit();  // Confirmar la transacción
                }
        });
    }

    private void Observers() {
        // Observar el estado de carga global
        sharedLoadingViewModel.getLoadingState().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                mWaitingDialog = WaitingDialog.show(this, "Login...",  true);
            } else {
                if (mWaitingDialog != null)
                {

                }
            }
        });
    }

    private void initViewModels() {
        bussinessViewmodel = new ViewModelProvider(this).get(BusinessViewModel.class);
        sharedLoadingViewModel = new ViewModelProvider(this).get(SharedLoadingViewModel.class);
    }

    private void initVisual() {
        setContentView(R.layout.activity_main);
        tv_actualizarEstadoLocal = findViewById(R.id.tv_actualizarEstadoLocal);
        rootLayout = findViewById(R.id.root_layout);
        include_details = findViewById(R.id.include_details);
        include_horario = findViewById(R.id.include_horario);
        include_branding = findViewById(R.id.include_branding);
        iv_notification = findViewById(R.id.iv_notification);
        tv_bussiness_name = findViewById(R.id.tv_bussiness_name);
        tv_h = findViewById(R.id.tv_h);
        tv_h_end = findViewById(R.id.tv_h_end);
        tv_address_bussiness = findViewById(R.id.tv_address_bussiness);
        fc_status_conection = findViewById(R.id.fc_status_conection);

        // Para habilitar el diseño en pantalla completa y respetar recortes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // EdgeToEdge.enable(this);

        // Ajuste para respetar los insets
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
                // Obtén los insets seguros
                int insetTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                int insetBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

                // Aplica los márgenes a la vista o layout que quieras ajustar
                rootLayout.setPadding(0, insetTop, 0, insetBottom);
                return insets;
            }
        });
    }

    @Override
    public void onBackPressed() {
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); // Mostrar fragmento anterior
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar el WebSocket al salir de la actividad
        // notificationPush.closeSocket();
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fc_status_conection, fragment);
        fragmentTransaction.commit();
    }

}