package com.palgao.menu;

import static com.palgao.menu.tools.toolsUI.ReplaceFragment;
import static com.palgao.menu.tools.toolsUI.actualizarEstadoLocal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.modules.Bussiness.BrandingFragment;
import com.palgao.menu.modules.Bussiness.DetailsFragment;
import com.palgao.menu.modules.Bussiness.HorarioFragment;
import com.palgao.menu.modules.Notifications.NotificationPush;
import com.palgao.menu.modules.Notifications.NotificationWebSocketClient;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.ui.WaitingDialog;
import com.palgao.menu.modules.products.ui.ProductsFragment;

public class MasterActivity extends AppCompatActivity {


    //View Models
    private SharedLoadingViewModel sharedLoadingViewModel;

    //Custom UI
    private WaitingDialog mWaitingDialog;
    private NotificationPush notificationPush;

    // Native UI
    private ImageView iv_notification;
    private TextView tv_actualizarEstadoLocal;
    private FragmentManager fragmentManager;
    private ConstraintLayout rootLayout;
    private View include_details, include_horario, include_branding;

    // tools
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVisual();
        initViewModels();
        Observers();
        Listeners();
        WebSocket();

        ReplaceFragment(new ProductsFragment(), R.id.nav_host_fragment, getSupportFragmentManager());

        // notificaciones
        notificationPush = new NotificationPush(this);
    }

    private void WebSocket() {
        Intent serviceIntent = new Intent(this, NotificationWebSocketClient.class);
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
                ReplaceFragment(new BrandingFragment(), R.id.nav_info_bussiness, getSupportFragmentManager());
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

        // Para habilitar el diseño en pantalla completa y respetar recortes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // EdgeToEdge.enable(this);

        String apertura = "08:00"; // Hora de apertura
        String cierre = "19:00";   // Hora de cierre

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

        actualizarEstadoLocal(apertura, cierre, tv_actualizarEstadoLocal);
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


}