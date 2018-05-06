package com.iceteaviet.hotornot;

/**
 * Created by Genius Doan on 3/4/2018.
 */

public class Constants {
    /**
     * path to our classifier file in the assets folder.
     *
     * We’ll be using words ‘classifier’ or ‘graph’ alternately.
     * Graph because our classifier is a saved neural network graph. You can also call it a ‘model’.
     */
    public static final String GRAPH_FILE_PATH = "file:///android_asset/graph.pb";

    /**
     * path to our labels file in assets folder.
     *
     * Simply put, labels are our possible results.
     * Our classifier was trained to tell if a photo is hot or not.
     * This is all it can do, so our labels are just ‘hot’ and ‘not’.
     */
    public static final String LABELS_FILE_PATH = "file:///android_asset/labels.txt";

    /**
     *  name of the classifier’s input. We pass the image to the classifier via input
     */
    public static final String GRAPH_INPUT_NAME = "input";

    /**
     * name of the classifier’s output. We get the result of the classification from the output
     */
    public static final String GRAPH_OUTPUT_NAME = "final_result";

    /**
     * the size of the image in pixels.
     * Our classifier can understand images which are 224x224 pixels — that’s how it was trained
     */
    public static final int IMAGE_SIZE = 224;


    public static final int COLOR_CHANNELS = 3;


    public static final String ASSETS_PATH = "file:///android_asset/";
}
