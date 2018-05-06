package com.iceteaviet.hotornot;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius Doan on 3/25/2018.
 */

public final class FileUtils {
    public static List<String> getLabels(AssetManager assetManager, String labelsFilePath) throws IOException {
        String actualLabelName = getLabelsFileName(labelsFilePath);
        return getLabelsFromFile(assetManager, actualLabelName);
    }

    static List<String> getLabelsFromFile(AssetManager assetManager, String actualFileName) throws IOException {
        List<String> labelNames = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(actualFileName)));
        String line = null;

        while ((line = reader.readLine()) != null) {
            labelNames.add(line);
        }

        reader.close();
        return labelNames;
    }


    static String getLabelsFileName(String labelFileNamePath) {
        return labelFileNamePath.split(Constants.ASSETS_PATH)[0];
    }
}
