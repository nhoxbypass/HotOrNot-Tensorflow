package iceteaviet.com.hotornot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private static final int RESULT_CODE_TAKE_PICTURE = 2212;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 1602;

    private Button captureButton;
    private ImageView imagePhoto;
    private TextView resultText;
    private ConstraintLayout containerLayout;

    private Handler handler = new Handler();

    private File photoFile;
    private Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureButton = findViewById(R.id.btn_take_picture);
        imagePhoto = findViewById(R.id.image_photo);
        resultText = findViewById(R.id.text_result);
        containerLayout = findViewById(R.id.container);


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivityPermissionsDispatcher.showCameraWithPermissionCheck(MainActivity.this);
            }
        });

        createClassifier();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showCamera() {
        File folder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!folder.exists())
            folder.mkdir();

        photoFile = new File(folder, System.currentTimeMillis() + ".jpg");
        if (!photoFile.exists()) {
            try {
                photoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri currentPhotoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RESULT_CODE_TAKE_PICTURE);
        }
    }

    void createClassifier() {
        classifier = ImageClassifierFactory.create(
                getAssets(),
                Constants.GRAPH_FILE_PATH,
                Constants.LABELS_FILE_PATH,
                Constants.IMAGE_SIZE,
                Constants.GRAPH_INPUT_NAME,
                Constants.GRAPH_OUTPUT_NAME
        );
    }

    void classifyPhoto(File file) {
        // crop the bitmap to fit 224x224 pixels
        Bitmap photoBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap croppedBitmap = ImageUtils.getCroppedBitmap(photoBitmap);

        // begin recognize image
        classifyAndShowResult(croppedBitmap);
        imagePhoto.setImageBitmap(photoBitmap);
    }

    private void classifyAndShowResult(final Bitmap croppedBitmap) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Result result = classifier.recognizeImage(croppedBitmap);
                showResult(result);
            }
        });
    }

    private void showResult(Result result) {
        resultText.setText(result.getResult().toUpperCase());
        containerLayout.setBackgroundColor(getColorFromResult(result.getResult()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            if (photoFile.exists()) {
                // take a photo and pass the file to the classifyPhoto() method
                classifyPhoto(photoFile);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForWriteStorage() {
        Toast.makeText(this, R.string.permission_write_storage_denied, Toast.LENGTH_SHORT).show();
    }

    private int getColorFromResult(String result) {
        if (getString(R.string.hot).equals(result)) {
            return getResources().getColor(R.color.hot);
        } else {
            return getResources().getColor(R.color.not);
        }
    }
}
