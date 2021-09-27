/*
 * Copyright (C) 2019 - 2019-2021 Vishnu Sanal. T
 *
 * This file is part of Quotes Status Creator.
 *
 * Quotes Status Creator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package phone.vishnu.quotes.fragment;

import static phone.vishnu.quotes.activity.MainActivity.fontDialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import phone.vishnu.quotes.R;

public class FontOptionPickFragment extends BottomSheetDialogFragment {

    private RadioGroup radioGroup;

    public FontOptionPickFragment() {}

    public static FontOptionPickFragment newInstance() {
        return new FontOptionPickFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.font_option_pick_dialog, container, false);

        radioGroup = inflate.findViewById(R.id.fontOptionPickRadioGroup);

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup.setOnCheckedChangeListener(
                (group, id) -> {
                    if (id == R.id.fontOptionPickFontRadioButton) {

                        fontDialog = new ProgressDialog(requireContext(), R.style.DialogTheme);
                        fontDialog.setMessage("Please Wait....");
                        fontDialog.show();
                        fontDialog.setCancelable(false);

                        FontFragment.newInstance()
                                .show(
                                        requireActivity().getSupportFragmentManager(),
                                        "FontFragment");

                        dismiss();

                    } else if (id == R.id.fontOptionPickColorRadioButton) {

                        ColorPickFragment.newInstance(ColorPickFragment.PICK_FONT_COLOR_REQ_CODE)
                                .show(
                                        requireActivity().getSupportFragmentManager(),
                                        "ColorPickFragment");

                        dismiss();

                    } else if (id == R.id.fontOptionPickSizeRadioButton) {

                        FontSizeFragment.newInstance()
                                .show(
                                        requireActivity().getSupportFragmentManager(),
                                        "FontSizeFragment");

                        dismiss();
                    }
                });
    }
}
