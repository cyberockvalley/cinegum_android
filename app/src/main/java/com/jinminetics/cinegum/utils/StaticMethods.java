package com.jinminetics.cinegum.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinminetics.cinegum.BuildConfig;
import com.jinminetics.cinegum.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StaticMethods {
    private static final String TAG = "StaticMethods";

    public static String objectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return object.toString();
        }
    }
    public static void log(Object message) {
        System.out.println(objectToString(message));
    }

    public static void log(Object title, Object message) {
        System.out.println(objectToString(title) + ": " +objectToString(message));
    }

    public static void log(Object title, Object subTitle, Object message) {
        System.out.println(objectToString(title) + ": " + objectToString(subTitle) + " -> " + objectToString(message));
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Displays a snackbar to the user with a string message.
     *
     * @param activity the activity needed to create a snackbar.
     * @param message  the string message to show to the user.
     */
    public static void showSnackbar(@NonNull Activity activity, @NonNull String message, int length) {
        View view = activity.findViewById(R.id.root);
        if (view == null) return;
        Snackbar.make(view, message, length).show();
    }

    public static void showAlert(Activity activity, String title, String message) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(activity)
                        .setMessage(Html.fromHtml(message))
                        .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        if (title != null) {
            builder.setTitle(title);
        }
        builder.create().show();
    }

    public static void showAlert(Activity activity, String title, String message, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(activity)
                        .setMessage(Html.fromHtml(message))
                        .setPositiveButton(activity.getString(R.string.ok), clickListener)
                        .setCancelable(false);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.create().show();
    }

    public static void showAlert(Activity activity, String title, String message, boolean cancelable, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(activity)
                        .setMessage(Html.fromHtml(message))
                        .setPositiveButton(activity.getString(R.string.ok), clickListener)
                        .setCancelable(cancelable);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.create().show();
    }

    public static AlertDialog getTextAlert(Activity activity, String title, String message, final int textType, final Interfaces.OnTextAlertSubmit submitListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (title != null) {
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        final EditText editText = new EditText(activity);
        editText.setInputType(textType);
        editText.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        builder.setView(editText);
        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitListener.onSubmit(editText.getText().toString());
            }
        });
        return builder.create();
    }

    public static File saveToStorage(File storage, byte[] content, String folder, String filename) {
        if (folder != null) {
            if (!folder.startsWith("/")) {
                folder = "/" + folder;
            }
            if (!folder.endsWith("/")) {
                folder += "/";
            }
        }
        if (filename != null) {
            filename = filename.replaceFirst("/", "");
        }
        File dir = null;
        if (folder != null) {
            dir = new File(storage + folder);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    dir = null;
                }
            }
        }
        File file = null;
        if (filename != null) {
            if (folder != null) {
                if (dir != null) {
                    file = new File(dir, filename);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            file = null;
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                file = new File(storage, filename);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        file = null;
                        e.printStackTrace();
                    }
                }
            }
        }
        if (file != null && content != null && content.length > 0) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                fos.write(content);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File saveToAppFilesFolder(Activity activity, String content, String folder, String filename) {
        byte[] data = (content != null) ? content.getBytes() : null;
        return saveByteToAppFilesFolder(activity, data, folder, filename);
    }

    public static File saveByteToAppFilesFolder(Activity activity, byte[] content, String folder, String filename) {
        return saveToStorage(activity.getFilesDir(), content, folder, filename);
    }

    public static void saveToAppCacheFolder(Activity activity, String content, String folder, String filename) {
        byte[] data = (content != null) ? content.getBytes() : null;
        saveByteToAppCacheFolder(activity, data, folder, filename);
    }

    public static void saveByteToAppCacheFolder(Activity activity, byte[] content, String folder, String filename) {
        saveToStorage(activity.getCacheDir(), content, folder, filename);
    }

    public static String genUniqueTag(int size) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    public static List<String[]> getMatches(String regex, String text) {
        List<String[]> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(regex).matcher(text);
        int groupCount = m.groupCount();
        String[] groups;
        while (m.find()) {
            groups = new String[groupCount];
            int groupIndex = 1;
            while (groupIndex < groupCount + 1) {
                if (m.group(groupIndex) != null) {
                    groups[groupIndex - 1] = m.group(groupIndex);
                }
                groupIndex++;
            }
            groupIndex = 1;
            allMatches.add(groups);
        }
        return allMatches;
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static boolean isValidPhoneNumber(String number) {
        String regex = "^[^*]+$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.find();
    }

    public static String getVersion(boolean includeAppName) {
        String returnValue = "1.0";
        returnValue = BuildConfig.VERSION_NAME;
        return (includeAppName) ? String.format("%s %s", R.string.app_name, returnValue) : returnValue;
    }

    public static String getOS() {
        return String.format("Android %s; %s Build/%s",
                Build.VERSION.RELEASE,
                Build.MODEL,
                Build.ID);
    }

    // the rate-apps caller
    public static void rate(final Context mContext) {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
        try {
            mContext.startActivity(localIntent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(mContext, R.string.playstore_not_found, Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    // the share-apps caller
    public static void shareit(final Context mContext) {
        Intent localIntent = new Intent("android.intent.action.SEND");
        localIntent.setType("text/plain");
        String text = String.format("Want to share your screenshots to people? Check out %s at %s now",
                mContext.getString(R.string.app_name),
                "http://play.google.com/store/apps/details?id=" + mContext.getPackageName());
        localIntent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            mContext.startActivity(Intent.createChooser(localIntent, mContext.getString(R.string.share_with)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(mContext, R.string.share_app_not_found, Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    // the feedback-apps caller
    public static void feedback(final Context mContext) {
        AlertDialog.Builder optionsViewBuilder = new AlertDialog.Builder(mContext);
        optionsViewBuilder.setTitle(R.string.feedback_cap);
        optionsViewBuilder.setMessage(R.string.your_feedback_is_valuable_to_us);
        final EditText localEditText = new EditText(mContext);
        localEditText.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        optionsViewBuilder.setView(localEditText);
        //optionsViewBuilder.setIcon(R.drawable.feedback);
        optionsViewBuilder.setPositiveButton(R.string.send_cap, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                String feedbackText = localEditText.getText().toString();
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.putExtra("android.intent.extra.EMAIL", new String[]{mContext.getString(R.string.webmaster_email)});
                localIntent.putExtra("android.intent.extra.TEXT", feedbackText);
                localIntent.setType("message/rfc822");
                try {
                    mContext.startActivity(Intent.createChooser(localIntent, mContext.getString(R.string.choose_an_email_client)));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(mContext, R.string.email_client_not_found, Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        });
        optionsViewBuilder.setNegativeButton(R.string.cancel_cap, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.cancel();
            }
        });
        optionsViewBuilder.show();
    }

    // the feedback-apps caller
    public static void contactUs(final Context mContext) {
        Intent localIntent = new Intent("android.intent.action.SEND");
        localIntent.putExtra("android.intent.extra.EMAIL", new String[]{mContext.getString(R.string.webmaster_email)});
        localIntent.setType("message/rfc822");
        try {
            mContext.startActivity(Intent.createChooser(localIntent, mContext.getString(R.string.choose_an_email_client)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(mContext, R.string.email_client_not_found, Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
