package com.cs646.expirytracker.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cs646.expirytracker.BuildConfig;
import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.FileCompressor;
import com.cs646.expirytracker.helper.Helper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    File mPhotoFile;
    private EditText mItemName;
    private EditText mItemCount;
    private TextView mItemExpiryDate;
    private FloatingActionButton mAddPhoto;
    private boolean EDIT_MODE = true;
    private FileCompressor mCompressor;
    private ImageView itemImage;
    private Calendar calendar;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mCompressor = new FileCompressor(this);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.edit_collapsing_toolbar);

        mItemName = findViewById(R.id.edit_item_name);
        mItemCount = findViewById(R.id.edit_item_count);
        mItemExpiryDate = findViewById(R.id.edit_item_expiry_date);
        mAddPhoto = findViewById(R.id.button_add_photo);
        itemImage = findViewById(R.id.edit_item_photo);

        Intent intent = getIntent();
        final TrackItem trackItem = intent.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);

        if(intent.hasExtra(Helper.EXTRA_TRACK_ITEM)){
            collapsingToolbarLayout.setTitle("Edit Item");
            EDIT_MODE = true;
            mItemName.setText(trackItem.getName());
            mItemCount.setText(""+trackItem.getItemCount());
            mItemExpiryDate.setText(Helper.getStringFromDate(trackItem.getDateExpiry()));
            Glide.with(this).load(new File(trackItem.getItemImagePath())).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(itemImage);
        }else{
            collapsingToolbarLayout.setTitle("Add Item");
            EDIT_MODE = false;
        }


        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mItemExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH);
                final int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        calendar.set(mYear, mMonth, mDay);
                        mItemExpiryDate.setText(Helper.getStringFromDate(calendar.getTime()));
                    }

                }, year,month,day);

                datePickerDialog.show();
            }
        });



    }


    private void saveItem(){
        String itemName = mItemName.getText().toString().trim();
        String itemCount = mItemCount.getText().toString().trim();
        String itemExpiryDate = mItemExpiryDate.getText().toString().trim();

        if(itemName.isEmpty() || itemCount.isEmpty() || itemExpiryDate.isEmpty()){
            Helper.showMessage(this, "Please Enter All details");
            return;
        }

        Intent data = new Intent();
        TrackItem updatedTrackItem;
        if(EDIT_MODE){
            updatedTrackItem = getIntent().getParcelableExtra(Helper.EXTRA_TRACK_ITEM);
            updatedTrackItem.setName(itemName);
            updatedTrackItem.setDateExpiry(Helper.getDateFromString(itemExpiryDate));
            updatedTrackItem.setItemCount(Integer.parseInt(itemCount));
            updatedTrackItem.setItemImagePath(mPhotoFile.toString());
        }
        else{
            updatedTrackItem = new TrackItem(itemName, new Date(),
                                            Helper.getDateFromString(itemExpiryDate),
                                            Integer.parseInt(itemCount),
                                            mPhotoFile.toString());

        }

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

    // navigating user to app settings
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Helper.REQUEST_TAKE_PHOTO) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(EditItemActivity.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(itemImage);
            } else if (requestCode == Helper.REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(EditItemActivity.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(itemImage);

            }
        }
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
}
