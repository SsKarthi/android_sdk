// TODO: add header comments

package com.adeven.adjustio;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

// TODO: change public to protected where possible
public class TrackingPackage implements Serializable {
    private static final long serialVersionUID = -35935556512024097L;

    // data
    public String path;
    public String userAgent;
    public Map<String, String> parameters;

    // logs
    public String kind;
    public String suffix;

    public String toString() {
        return kind + suffix + " " + path;
    }

    public String parameterString() {
        try {
            String parameterString = "Parameters:";
            for (Map.Entry<String, String> entity : parameters.entrySet()) {
                parameterString += String.format("\n\t%-16s %s", entity.getKey(), entity.getValue());
            }
            return parameterString;
        }
        catch (NullPointerException e) {
            return "Parameters: null";
        }
    }

    public String getSuccessMessage() {
        return "Tracked " + kind + suffix;
    }

    public String getFailureMessage() {
        return "Failed to track " + kind + suffix;
    }

    public HttpUriRequest getRequest() throws UnsupportedEncodingException {
        String url = AdjustIo.BASE_URL + path;
        HttpPost request = new HttpPost(url);

        String language = Locale.getDefault().getLanguage();
        request.addHeader("Accept-Language", language);
        request.addHeader("Client-SDK", AdjustIo.CLIENT_SDK);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entity : parameters.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entity.getKey(), entity.getValue());
            pairs.add(pair);
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
        entity.setContentType(URLEncodedUtils.CONTENT_TYPE);
        request.setEntity(entity);

        return request;
    }
}