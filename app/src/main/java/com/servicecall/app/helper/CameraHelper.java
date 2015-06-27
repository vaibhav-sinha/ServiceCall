package com.servicecall.app.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.servicecall.app.base.BaseFragment;
import com.servicecall.app.interfaces.CameraHelperCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraHelper {


    public static abstract class CameraUtilFragment extends BaseFragment implements CameraHelperCallback {

    }

    private String imagePath;
    int REQUEST_CAMERA = 0, SELECT_SOURCE = 1;
    private static final String IMAGE_NAME_INSTANCE = "image_name";
    private boolean showGallery;

    private CameraUtilFragment fragment;

    public CameraHelper() {

    }

    public CameraHelper(CameraUtilFragment fragment) {
        this.fragment = fragment;
        setShowGallery(true);
    }

    public boolean isShowGallery() {
        return showGallery;
    }

    public void setShowGallery(boolean showGallery) {
        this.showGallery = showGallery;
    }

    public void setFragment(CameraUtilFragment fragment) {
        this.fragment = fragment;
        setShowGallery(true);
    }

    public void setImageName(String imageName) {
        this.imagePath = imageName;
    }

    public String getImageName() {
        return imagePath;
    }

    public void openOnlyGalleryIntent() {
        selectImage("Gallery");
    }

    public void openOnlyCameraIntent() {
        selectImage("Camera");
    }

    public void openImageIntent() {

        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Select Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fragment.getActivity().startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    fragment.getActivity().startActivityForResult(
                            Intent.createChooser(intent, "Select Source"),
                            SELECT_SOURCE);
                }
            }
        });
        builder.show();

    }

    public void onCameraPicTaken() {
        fragment.onCameraPicTaken();
    }

    public void onGalleryPicChosen() {
        fragment.onGalleryPicChosen();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_NAME_INSTANCE, imagePath);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imagePath = savedInstanceState.getString(IMAGE_NAME_INSTANCE);
        }
    }

    private void selectImage(final String userInput) {

        if (userInput.contentEquals("Camera")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fragment.getActivity().startActivityForResult(intent, REQUEST_CAMERA);
        } else if (userInput.contentEquals("Gallery")) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            fragment.getActivity().startActivityForResult(
                    Intent.createChooser(intent, "Select Source"),
                    SELECT_SOURCE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory() + File.separator + "interfinder" + File.separator,
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagePath = destination.getAbsolutePath();
                setImageName(imagePath);
                onCameraPicTaken();
            } else {
            Cursor cursor = null;
            String selectedImagePath = null;
            try {
                Uri selectedImageUri = handleImageUri(data.getData());
                String[] projection = {MediaStore.Images.Media.DATA};
                try {
                    cursor = fragment.getActivity().getContentResolver().query(selectedImageUri, projection, null, null,
                            null);
                } catch (Exception e) {
                    cursor = fragment.getActivity().managedQuery(selectedImageUri, projection, null, null,
                            null);
                }
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            BitmapFactory.decodeFile(selectedImagePath, options);
            setImageName(selectedImagePath);
            onGalleryPicChosen();
        }
    }
}

    public static Uri handleImageUri(Uri uri) {
        Pattern pattern = Pattern.compile("(content://media/.*\\d)");
        if (uri.getPath().contains("content")) {
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find())
                return Uri.parse(matcher.group(1));
            else
                throw new IllegalArgumentException("Cannot handle this URI");
        } else
            return uri;
    }

}
