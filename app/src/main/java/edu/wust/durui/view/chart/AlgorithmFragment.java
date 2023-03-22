package edu.wust.durui.view.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import edu.wust.durui.R;
import edu.wust.durui.databinding.FragmentChartBinding;
import edu.wust.durui.view.chart.dialog.ChooseDialogFragment;
import edu.wust.durui.viewmodel.Repository;


public class AlgorithmFragment extends Fragment {
    FragmentChartBinding binding = null;
    Repository viewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false);
        viewModel = new ViewModelProvider(this.getActivity()).get(Repository.class);

        binding.timelineBtn.setOnClickListener(v -> {
            DialogFragment dialog = new ChooseDialogFragment();
            dialog.show(getActivity().getSupportFragmentManager(), ChooseDialogFragment.TAG);
        });

        viewModel.rounds.observe(getViewLifecycleOwner(), valueLinePoints -> {
            binding.oneShotCubiclinechart.clearChart();
            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xFF56B7F1);
            for (ValueLinePoint algorithm : valueLinePoints) {
                series.addPoint(algorithm);
            }
            binding.oneShotCubiclinechart.addSeries(series);
            binding.oneShotCubiclinechart.startAnimation();
        });

        viewModel.histories.observe(getViewLifecycleOwner(), pieModels -> {
            binding.historyPiechart.clearChart();
            for (PieModel history : pieModels) {
                binding.historyPiechart.addPieSlice(history);
            }
            binding.historyPiechart.startAnimation();
        });

        return binding.getRoot();
    }
}