package org.c_base.ampelcontrol;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class AboutFragment extends DialogFragment {
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_about, null, false);

        TextView appVersion = (TextView) view.findViewById(R.id.version_info);
        String versionText = context.getString(R.string.app_version, getAppVersion(context));
        appVersion.setText(versionText);

        return new AlertDialog.Builder(context)
                .setTitle(R.string.title_about)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    private String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return context.getString(R.string.app_version_error);
        }
    }
}
