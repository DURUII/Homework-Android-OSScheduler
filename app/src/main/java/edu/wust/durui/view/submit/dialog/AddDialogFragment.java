package edu.wust.durui.view.submit.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import edu.wust.durui.R;
import edu.wust.durui.databinding.DialogFragmentAddBinding;
import edu.wust.durui.viewmodel.Repository;

public class AddDialogFragment extends DialogFragment {
    public static final String TAG = "AddDialogFragment";
    DialogFragmentAddBinding binding = null;
    Repository viewModel = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add, null, false);
        // FIXME viewModelStoreOwner
        viewModel = new ViewModelProvider(this.getActivity()).get(Repository.class);

        binding.estimatedTimeBtn.setOnClickListener(v -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder().setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK).setTimeFormat(TimeFormat.CLOCK_24H).setHour(0).setMinute(0).setTitleText("Select Estimated time").build();
            picker.show(getActivity().getSupportFragmentManager(), "estimatedTimeBtn");
            picker.addOnPositiveButtonClickListener(view -> {
                viewModel.setEstimatedRequireTime(picker.getHour(), picker.getMinute());
                binding.estimatedTimeText.setText(viewModel.getEstimatedRequireTimeText());
            });
        });

        binding.submitTimeBtn.setOnClickListener(v -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder().setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK).setTimeFormat(TimeFormat.CLOCK_24H).setHour(0).setMinute(0).setTitleText("Select Submitted time").build();
            picker.show(getActivity().getSupportFragmentManager(), "submitTimeBtn");
            picker.addOnPositiveButtonClickListener(view -> {
                viewModel.setJobSubmitTime(picker.getHour(), picker.getMinute());
                binding.submitTimeText.setText(viewModel.getJobSubmitTimeText());
            });
        });

        builder.setView(binding.getRoot())
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, id) -> {
                    viewModel.setPriority(binding.priorityEdit.getText());
                    if (!viewModel.addTask()) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
