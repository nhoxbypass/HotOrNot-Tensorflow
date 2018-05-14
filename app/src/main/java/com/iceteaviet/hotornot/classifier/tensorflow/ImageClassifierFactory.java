package com.iceteaviet.hotornot.classifier.tensorflow;

import android.content.res.AssetManager;

import com.iceteaviet.hotornot.utils.Constants;
import com.iceteaviet.hotornot.utils.FileUtils;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.IOException;
import java.util.List;

/**
 * Created by Genius Doan on 3/4/2018.
 */

public class ImageClassifierFactory {
    public static ImageClassifier create(AssetManager assetManager, String graphFilePath, String labelsFilePath,
                                  int imageSize, String inputName, String outputName) {
        List<String> labels = null;
        try {
            labels = FileUtils.getLabels(assetManager, labelsFilePath);
            return new ImageClassifier(inputName, outputName, imageSize, labels, new int[imageSize*imageSize], new float[imageSize * imageSize * Constants.COLOR_CHANNELS], new float[labels.size()], new TensorFlowInferenceInterface(assetManager, graphFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
