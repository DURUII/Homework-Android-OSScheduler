package edu.wust.durui.view.chart.timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edu.wust.durui.R;
import edu.wust.durui.databinding.BottomSheetTimelineBinding;
import edu.wust.durui.viewmodel.Repository;

public class TimelineBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "TimelineBottomSheet";
    BottomSheetTimelineBinding binding;
    Repository viewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_timeline, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(Repository.class);

        viewModel.snap.observe(this.getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.snapShotText.setText(s);
            }
        });

        return binding.getRoot();
    }
}
