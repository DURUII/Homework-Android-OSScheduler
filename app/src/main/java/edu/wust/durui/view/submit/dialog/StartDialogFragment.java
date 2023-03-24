package edu.wust.durui.view.submit.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.wust.durui.R;
import edu.wust.durui.databinding.DialogFragmentStartBinding;
import edu.wust.durui.viewmodel.Repository;

public class StartDialogFragment extends DialogFragment {
    public static final String TAG = "StartDialogFragment";
    DialogFragmentStartBinding binding = null;
    Repository viewModel = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_start, null, false);
        viewModel = new ViewModelProvider(this.getActivity()).get(Repository.class);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setView(binding.getRoot())
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Start", (dialog, id) -> {
                    viewModel.setQuantum(binding.timesliceEdit.getText());
                    viewModel.setFactor(binding.factorEdit.getText());
                    viewModel.startSchedule();
                });

        return builder.create();
    }
}
