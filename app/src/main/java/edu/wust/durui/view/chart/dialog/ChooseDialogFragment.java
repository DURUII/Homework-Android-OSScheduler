package edu.wust.durui.view.chart.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.wust.durui.R;
import edu.wust.durui.databinding.DialogFragmentChooseBinding;
import edu.wust.durui.view.chart.timeline.TimelineBottomSheet;
import edu.wust.durui.viewmodel.Repository;

public class ChooseDialogFragment extends DialogFragment {
    public static final String TAG = "ChooseDialogFragment";
    DialogFragmentChooseBinding binding = null;
    Repository viewModel = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_choose, null, false);
        viewModel = new ViewModelProvider(getActivity()).get(Repository.class);

        builder.setView(binding.getRoot())
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Time Travel", (dialog, id) -> {
                    viewModel.setTag(binding.algorithmMenu.getText());
                    TimelineBottomSheet bottomSheet = new TimelineBottomSheet();
                    bottomSheet.show(getActivity().getSupportFragmentManager(), TimelineBottomSheet.TAG);
                });

        return builder.create();
    }
}
