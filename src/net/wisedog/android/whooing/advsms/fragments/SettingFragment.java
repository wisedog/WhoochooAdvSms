/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.advsms.fragments;

import java.util.ArrayList;

import net.wisedog.android.whooing.advsms.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends Fragment{
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, container, false);
		if(view != null){
			TextView cardSetting = (TextView)view.findViewById(R.id.setting_text_card);
			TextView alarmSetting = (TextView)view.findViewById(R.id.setting_text_alarm_time);
			
			if(cardSetting != null && alarmSetting != null){
				final FragmentManager mgr = getActivity().getFragmentManager();
				cardSetting.setOnClickListener(new OnClickListener() {
					
					
					@Override
					public void onClick(View v) {
						CardSelectDialogFragment dialog = new CardSelectDialogFragment();
						dialog.show(mgr, "");
						
					}
				});
				
				alarmSetting.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AlarmDialogFragment dialog = new AlarmDialogFragment();
						dialog.show(mgr, "");
						
					}
				});
			}
		}
		return view;
	}
	
	static public class CardSelectDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// Set the dialog title
			builder.setTitle("")
					// R.string.pick_toppings)
					// .setSingle
					.setMultiChoiceItems(R.array.alarm_interval, null,
							new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										// If the user checked the item, add it
										// to the selected items
										mSelectedItems.add(which);
									} else if (mSelectedItems.contains(which)) {
										// Else, if the item is already in the
										// array, remove it
										mSelectedItems.remove(Integer
												.valueOf(which));
									}
								}
							})
					// Set the action buttons
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked OK, so save the
									// mSelectedItems results somewhere
									// or return them to the component that
									// opened the dialog

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

			return builder.create();
		}
	}

	static public class AlarmDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>(); // Where
																				// we
																				// track
																				// the
																				// selected
																				// items
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// Set the dialog title
			builder.setTitle("")
					// R.string.pick_toppings)
					// .setSingle
					.setMultiChoiceItems(R.array.alarm_interval, null,
							new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										// If the user checked the item, add it
										// to the selected items
										mSelectedItems.add(which);
									} else if (mSelectedItems.contains(which)) {
										// Else, if the item is already in the
										// array, remove it
										mSelectedItems.remove(Integer
												.valueOf(which));
									}
								}
							})
					// Set the action buttons
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked OK, so save the
									// mSelectedItems results somewhere
									// or return them to the component that
									// opened the dialog

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

			return builder.create();
		}
	}
}
