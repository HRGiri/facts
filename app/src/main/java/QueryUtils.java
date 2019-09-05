import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public final class QueryUtils {
    private static final String SAMPLE="{" +
            "\"text\": \"2012 is the year that the century's second and last solar transit of Venus occurs on June 6.\"," +
            "\"found\": true," +
            "\"number\": 2012," +
            "\"type\": \"year\"," +
            "\"date\": \"June 6\"}";

    private QueryUtils(){}
    public static int getNumber(){
        int number=0;
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject root = new JSONObject(SAMPLE);
            number = root.getInt("number");


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return number;
    }
}
