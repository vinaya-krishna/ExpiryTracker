package com.cs646.expirytracker.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cs646.expirytracker.BuildConfig;
import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.FileCompressor;
import com.cs646.expirytracker.helper.Helper;
import com.cs646.expirytracker.helper.NotificationScheduler;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    File mPhotoFile;
    private FloatingActionButton mAddPhoto;
    private boolean EDIT_MODE = true;
    private FileCompressor mCompressor;
    private ImageView itemImage;
    private Calendar calendar;

    private NotificationScheduler notificationScheduler;
    DatePickerDialog datePickerDialog;

    private EditText mItemName;
    private ImageView mItemNameVoice, mItemExpiryDateVoice, mItemReminderVoice, mItemExpiryDateCalendar, mItemReminderCalendar;
    private TextView mItemExpiryDate,mItemReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mCompressor = new FileCompressor(this);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.edit_collapsing_toolbar);

        notificationScheduler = new NotificationScheduler(this, ViewItemActivity.class);


        mAddPhoto = findViewById(R.id.button_add_photo);
        itemImage = findViewById(R.id.edit_item_photo);

        mItemName = findViewById(R.id.edit_item_name);
        mItemNameVoice = findViewById(R.id.edit_item_name_voice);

        mItemExpiryDate = findViewById(R.id.edit_item_expiry_date);
        mItemExpiryDateVoice = findViewById(R.id.edit_item_expiry_date_voice);
        mItemExpiryDateCalendar = findViewById(R.id.edit_item_expiry_date_calendar);


        mItemReminder = findViewById(R.id.edit_item_reminder);
        mItemReminderVoice = findViewById(R.id.edit_item_reminder_voice);
        mItemReminderCalendar = findViewById(R.id.edit_item_reminder_calendar);

        mAddPhoto.setOnClickListener(this);
        mItemNameVoice.setOnClickListener(this);
        mItemExpiryDateVoice.setOnClickListener(this);
        mItemExpiryDateCalendar.setOnClickListener(this);
        mItemReminderVoice.setOnClickListener(this);
        mItemReminderCalendar.setOnClickListener(this);


        Intent intent = getIntent();
        final TrackItem trackItem = intent.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);

        if(intent.hasExtra(Helper.EXTRA_TRACK_ITEM)){
            collapsingToolbarLayout.setTitle("Edit Item");
            EDIT_MODE = true;
            mItemName.setText(trackItem.getName());
            mItemExpiryDate.setText(Helper.getStringFromDate(trackItem.getDateExpiry()));
            Glide.with(this).load(new File(trackItem.getItemImagePath())).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(itemImage);
        }else{
            collapsingToolbarLayout.setTitle("Add Item");
            EDIT_MODE = false;
        }
    }


    @Override
    public void onClick(View v) {
        System.out.println("Here");
        switch (v.getId()){
            case R.id.edit_item_name_voice:
                promptSpeechInput("Tell me item name",Helper.REQUEST_SPEECH_INPUT_ITEM_NAME);
                break;
            case R.id.edit_item_expiry_date_voice:
                promptSpeechInput("Tell me Expiry date",Helper.REQUEST_SPEECH_INPUT_EXPIRY_DATE);
                break;
            case R.id.edit_item_expiry_date_calendar:
                setDateFromCalendar(mItemExpiryDate);

                break;
            case R.id.edit_item_reminder_voice:
                promptSpeechInput("Tell me Reminder date",Helper.REQUEST_SPEECH_INPUT_REMINDER_DATE);
                break;
            case R.id.edit_item_reminder_calendar:
                setDateFromCalendar(mItemReminder);

                break;
            case R.id.button_add_photo:
                selectImage();

                break;

        }
    }



    private void setDateFromCalendar(final TextView textView){
        calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(EditItemActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                calendar.set(mYear, mMonth, mDay);
                textView.setText(Helper.getStringFromDate(calendar.getTime()));
            }

        }, year,month,day);

        datePickerDialog.show();
    }


    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput(String title, int request_code) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, title);
        //\n Eg: Tomorrow / Day after tomorrow / 21st May / Next month 22nd
        try {
            startActivityForResult(intent, request_code);
        } catch (ActivityNotFoundException a) {
           Helper.showMessage(this,"Sorry! Your device doesn't support speech input");
        }
    }

    private void saveItem(){
        String itemName = mItemName.getText().toString().trim();
        String itemExpiryDate = mItemExpiryDate.getText().toString().trim();
        String itemReminderDate = mItemReminder.getText().toString().trim();

        if(itemName.isEmpty() || itemExpiryDate.isEmpty() || itemReminderDate.isEmpty() || mPhotoFile == null){
            Helper.showMessage(this, "Please Enter All details");
            return;
        }

        Intent data = new Intent();
        TrackItem updatedTrackItem;
        if(EDIT_MODE){

            updatedTrackItem = getIntent().getParcelableExtra(Helper.EXTRA_TRACK_ITEM);
            updatedTrackItem.setName(itemName);
            updatedTrackItem.setDateExpiry(Helper.getDateFromString(itemExpiryDate));
            updatedTrackItem.setItemImagePath(mPhotoFile.toString());
        }
        else{

            //schedule notification

            updatedTrackItem = new TrackItem(itemName, new Date(),
                                            Helper.getDateFromString(itemExpiryDate),
                                            Helper.getDateFromString(itemReminderDate),
                                            Integer.parseInt("1"),
                                            mPhotoFile.toString());



        }

        notificationScheduler.scheduleNotification(updatedTrackItem);

        data.putExtra(Helper.EXTRA_TRACK_ITEM,updatedTrackItem);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                saveItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Alert dialog for capture or select from galley
     */
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    requestStoragePermission(true);
                } else if (items[which].equals("Choose from Library")) {
                    requestStoragePermission(false);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(final boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                               dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Helper.showMessage(EditItemActivity.this, "Error");
            }
        })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Helper.REQUEST_TAKE_PHOTO);

            }
        }
    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, Helper.REQUEST_GALLERY_PHOTO);
    }


    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Helper.REQUEST_TAKE_PHOTO:

                    try {
                        mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(EditItemActivity.this).
                            load(mPhotoFile).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(itemImage);

                    break;
                case Helper.REQUEST_GALLERY_PHOTO:

                    Uri selectedImage = data.getData();
                    try {
                        mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(EditItemActivity.this).
                            load(mPhotoFile).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(itemImage);

                    break;

                case Helper.REQUEST_SPEECH_INPUT_ITEM_NAME:
                    ArrayList<String> result_name = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try{
                        mItemName.setText(result_name.get(0));
                    }catch (Exception e){
                        Helper.showMessage(this, e.getMessage());
                    }

                    break;

                case Helper.REQUEST_SPEECH_INPUT_EXPIRY_DATE:
                    ArrayList<String> result_exp_dates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try{

                        getDateFromSpeech(mItemExpiryDate, result_exp_dates.get(0));
                    }catch (Exception e){
                        Helper.showMessage(this, e.getMessage());
                    }

                    break;

                case Helper.REQUEST_SPEECH_INPUT_REMINDER_DATE:
                    ArrayList<String> result_rem_dates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try{

                        getDateFromSpeech(mItemReminder, result_rem_dates.get(0));
                    }catch (Exception e){
                        Helper.showMessage(this, e.getMessage());
                    }

                    break;
            }

        }
    }


    private void getDateFromSpeech(final TextView textView, String speechString){

        speechString = speechString.replaceAll("\\s", "%20");

        String url = "https://api.wit.ai/message?v=20190508&q=" + speechString;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject valuesObj = response.getJSONObject("entities").
                                    getJSONArray("datetime").
                                    getJSONObject(0).getJSONArray("values")
                                    .getJSONObject(0);

                            if (valuesObj.getString("type").equals("value")) {
                                String value = valuesObj.getString("value");

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = dateFormat.parse(value);
                                    textView.setText(Helper.getStringFromDate(date));


                                } catch (ParseException e) {
                                    Helper.showMessage(EditItemActivity.this, e.getMessage());
                                }


                            } else if (valuesObj.getString("type").equals("interval")) {
                                String value = valuesObj.getJSONObject("from").getString("value");


                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                try {

                                    Date date = dateFormat.parse(value);
                                    textView.setText(Helper.getStringFromDate(date));

                                } catch (ParseException e) {
                                    Helper.showMessage(EditItemActivity.this, e.getMessage());
                                }

                            }

                        } catch (JSONException e) {
                            Helper.showMessage(EditItemActivity.this, "Not Recognised");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Helper.showMessage(EditItemActivity.this, error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer GHT67LFYOCSXTP6A4Q6REO5X4QACJECE");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

}
