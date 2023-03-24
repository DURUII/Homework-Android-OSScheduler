package edu.wust.durui.view.chart.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.wust.durui.R;
import edu.wust.durui.databinding.DialogFragmentChooseBinding;
import edu.wust.durui.view.chart.sheet.CalendarBottomSheet;
import edu.wust.durui.view.chart.sheet.TimelineBottomSheet;
import edu.wust.durui.viewmodel.Repository;

public class ChooseDialogFragment extends DialogFragment {
    public static final String TAG = "ChooseDialogFragment";
    DialogFragmentChooseBinding binding = null;
    Repository viewModel = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_choose, null, false);
        viewModel = new ViewModelProvider(getActivity()).get(Repository.class);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setView(binding.getRoot())
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Time Travel", (dialog, which) -> {
                    viewModel.setTag(binding.algorithmMenu.getText());
                    CalendarBottomSheet bottomSheet = new CalendarBottomSheet();
                    bottomSheet.show(getActivity().getSupportFragmentManager(), CalendarBottomSheet.TAG);

                })
                .setPositiveButton("Result", (dialog, id) -> {
                    viewModel.setTag(binding.algorithmMenu.getText());
                    TimelineBottomSheet bottomSheet = new TimelineBottomSheet();
                    bottomSheet.show(getActivity().getSupportFragmentManager(), TimelineBottomSheet.TAG);

                });

        return builder.create();
    }
}
