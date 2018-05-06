package iceteaviet.com.hotornot;

/**
 * Created by Genius Doan on 3/4/2018.
 */

public class Result {
    /**
     *  Result string (‘hot’ or ‘not’)
     */
    private String result;

    /**
     * confidence of the result, use after the classification
     */
    private Float confidence;


    public Result(String result, Float confidence) {
        this.result = result;
        this.confidence = confidence;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }
}
