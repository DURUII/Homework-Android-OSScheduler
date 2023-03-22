package edu.wust.durui.view.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import edu.wust.durui.R;
import edu.wust.durui.databinding.FragmentIndexBinding;

public class IndexFragment extends Fragment {
    FragmentIndexBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // index -> only display a introductory text
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_index, container, false);
        binding.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "软件工程2003 - 杜睿 - 操作系统课程设计", Snackbar.LENGTH_SHORT)
                        .setAction("KNOW", view -> {
                        })
                        .show();
            }
        });
        return binding.getRoot();
    }
}