package dbo;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {


    String message;

    String comment;

    public Response(String data) throws JSONException {

        JSONObject obj = new JSONObject(data).getJSONObject("response");
        message = obj.getString("message");
        comment = obj.getString("comment");

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
