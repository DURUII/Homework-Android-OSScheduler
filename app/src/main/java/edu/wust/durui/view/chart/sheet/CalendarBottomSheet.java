package edu.wust.durui.view.chart.sheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.roundtableapps.timelinedayviewlibrary.Event;
import com.roundtableapps.timelinedayviewlibrary.EventView;

import edu.wust.durui.R;
import edu.wust.durui.databinding.BottomSheetCalendarBinding;
import edu.wust.durui.model.cornerstone.Task;
import edu.wust.durui.viewmodel.Repository;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CalendarBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "CalendarBottomSheet";
    BottomSheetCalendarBinding binding;
    Repository viewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_calendar, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(Repository.class);

        // TODO: timeline-view

        return binding.getRoot();
    }
}
