package edu.wust.durui.view.entry;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import edu.wust.durui.R;
import edu.wust.durui.databinding.ActivityMainBinding;
import edu.wust.durui.model.cornerstone.Task;
import edu.wust.durui.viewmodel.Repository;

public class MainActivity extends AppCompatActivity {
    private Page src;

    enum Page {
        Index, Submit, Chart
    }

    ActivityMainBinding binding = null;
    NavController navController = null;
    Repository viewModel = null;


    static {
        // night-mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // redirect to the index page
        binding.bottomNavigation.setSelectedItemId(R.id.item_index);
        src = Page.Index;
        // navigation among fragments
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        // view-model
        viewModel = new ViewModelProvider(this).get(Repository.class);

        // update badge
        viewModel.tasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                BadgeDrawable badge = binding.bottomNavigation.getOrCreateBadge(R.id.item_task);
                badge.setVisible(true);
                badge.setNumber(tasks.size());
            }
        });

        viewModel.counter.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    BadgeDrawable badge = binding.bottomNavigation.getOrCreateBadge(R.id.item_algorithm);
                    badge.setVisible(true);
                    badge.setNumber(integer);
                }
            }
        });

        // response to item selection
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_index:
                        if (src == Page.Submit) {
                            navController.navigate(R.id.action_submit_fragment_to_main_fragment);
                        } else if (src == Page.Chart) {
                            navController.navigate(R.id.action_chart_fragment_to_main_fragment);
                        }
                        src = Page.Index;
                        return true;
                    case R.id.item_task:
                        if (src == Page.Chart) {
                            navController.navigate(R.id.action_chart_fragment_to_main_fragment);
                            src = Page.Index;
                        }
                        if (src == Page.Index) {
                            navController.navigate(R.id.action_main_fragment_to_submit_fragment);
                        }
                        src = Page.Submit;
                        return true;
                    case R.id.item_algorithm:
                        if (src == Page.Submit) {
                            navController.navigate(R.id.action_submit_fragment_to_main_fragment);
                            src = Page.Index;
                        }
                        if (src == Page.Index) {
                            navController.navigate(R.id.action_main_fragment_to_chart_fragment);
                        }
                        src = Page.Chart;
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // response to going back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("提示")
                    .setMessage("确认退出嘛？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> finish()).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}