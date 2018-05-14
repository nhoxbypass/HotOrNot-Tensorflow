package com.iceteaviet.hotornot.classifier;

import android.graphics.Bitmap;

/**
 * Created by Genius Doan on 3/4/2018.
 */

public interface Classifier {
    Result recognizeImage(Bitmap bitmap);
}
