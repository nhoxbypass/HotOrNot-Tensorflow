package com.iceteaviet.hotornot.classifier.tensorflow;

import android.graphics.Bitmap;

import com.iceteaviet.hotornot.classifier.Classifier;
import com.iceteaviet.hotornot.classifier.Result;
import com.iceteaviet.hotornot.utils.Constants;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Genius Doan on 3/4/2018.
 */

public class ImageClassifier implements Classifier {
    private final boolean ENABLE_LOG_STATS = true;

    private String inputName;

    private String outputName;

    private int imageSize;
    /**
     * list of labels from text file
     */
    private List<String> labels;

    /**
     * allocate based on image size. To fill with bitmap pixels during classification
     */
    private int[] imageBitmapPixels;

    /**
     *  allocate based on the image size and color channels.
     *  Our classifier uses normalized values so we have to convert integers to floats later on
     */
    private float[] imageNormalizedPixels;

    /**
     * The classifier returns probability for each label
     */
    private float[] results;

    /**
     * API from the TensorFlow library.
     * It takes the asset manager and the graph file path to load the graph
     */
    private TensorFlowInferenceInterface tensorFlowInference;

    public ImageClassifier(String inputName, String outputName, int imageSize, List<String> labels,
                           int[] imageBitmapPixels, float[] imageNormalizedPixels, float[] results, TensorFlowInferenceInterface tensorFlowInference) {
        this.inputName = inputName;
        this.outputName = outputName;
        this.imageSize = imageSize;
        this.labels = labels;
        this.imageBitmapPixels = imageBitmapPixels;
        this.imageNormalizedPixels = imageNormalizedPixels;
        this.results = results;
        this.tensorFlowInference = tensorFlowInference;
    }

    @Override
    public Result recognizeImage(Bitmap bitmap) {
        // preprocess bitmap pixels into normalized values
        preprocessImageToNormalizedFloats(bitmap);

        // classify the image
        classifyImageToOutputs();

        // map the results to our Result object
        // fill with the results and poll the one with the highest confidence
        PriorityQueue<Result> outputQueue = getResults();
        return outputQueue.poll();
    }

    private PriorityQueue<Result> getResults() {
        PriorityQueue<Result> outputQueue = createOutputQueue();
        for (int i = 0; i < results.length; i++) {
            outputQueue.add(new Result(labels.get(i), results[i]));
        }
        return outputQueue;
    }

    private PriorityQueue<Result> createOutputQueue() {
        Comparator<Result> comparator = new Comparator<Result>() {
            @Override
            public int compare(Result l, Result r) {
                return Float.compare(l.getConfidence(), r.getConfidence());
            }
        };

        return new PriorityQueue<>(labels.size(), comparator);
    }

    private void classifyImageToOutputs() {
        //feed the classifier with the data via input
        tensorFlowInference.feed(inputName, imageNormalizedPixels,
                1L, imageSize, imageSize, Constants.COLOR_CHANNELS);

        //run the classification
        tensorFlowInference.run(new String[] {outputName}, ENABLE_LOG_STATS);

        //get the results from the ouptput
        tensorFlowInference.fetch(outputName, results);
    }

    private void preprocessImageToNormalizedFloats(Bitmap bitmap) {
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
    }
}
