package edu.wust.durui.view.submit;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.wust.durui.R;
import edu.wust.durui.databinding.FragmentTaskBinding;
import edu.wust.durui.model.cornerstone.Task;
import edu.wust.durui.view.submit.dialog.AddDialogFragment;
import edu.wust.durui.view.submit.dialog.StartDialogFragment;
import edu.wust.durui.viewmodel.Repository;


public class TaskFragment extends Fragment {
    FragmentTaskBinding binding = null;
    Repository viewModel = null;
    TaskAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(Repository.class);
        initRecycler();
        initButton();
        return binding.getRoot();
    }

    private void initRecycler() {
        // vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.tasksRecycler.setLayoutManager(layoutManager);

        adapter = new TaskAdapter(new TaskAdapter.OnClickListener() {
            @Override
            public void peep() {
                Toast.makeText(getActivity(), "PEEP TO-DO", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clear() {
                viewModel.clearTask();
            }

            @Override
            public void delete() {
                int position = adapter.getPosition();
                viewModel.deleteTask(position);
            }
        });
        binding.tasksRecycler.setAdapter(adapter);
        registerForContextMenu(binding.tasksRecycler);

        // list -> notify -> view-holder
        viewModel.tasks.observe(getActivity(), tasks -> {
            List<Task> swap = new ArrayList<>();
            for (Task t : tasks) {
                swap.add((Task) t.clone());
            }
            adapter.update(swap);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getPosition();
        Task task = viewModel.getTask(position);
        switch (item.getItemId()) {
            case R.id.item_clear:
                TaskAdapter.mOnClickListener.clear();
                break;
            case R.id.item_delete:
                TaskAdapter.mOnClickListener.delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void initButton() {
        binding.addBtn.setOnClickListener(v -> {
            DialogFragment dialog = new AddDialogFragment();
            dialog.show(getActivity().getSupportFragmentManager(), AddDialogFragment.TAG);
        });

        binding.startBtn.setOnClickListener(v -> {
            DialogFragment dialog = new StartDialogFragment();
            dialog.show(getActivity().getSupportFragmentManager(), StartDialogFragment.TAG);
        });
    }

}