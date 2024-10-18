/*
 * Copyright (c) 2024 jawis.net
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the “Software”), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR 
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.jawis.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/**
 *
 * @author Redbad
 */
public class Config {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Config.class);
    
    private static final String URL_JSON = "https://www.jawis.net/json";
    
    private String versionCurrent = "1.0.1";
    private String releaseDateCurrent = "2024-10-20";
    private String versionLatest = "NA";
    private String releaseDateLatest = "NA";
    private boolean preRelease = false;
    private boolean deprecated = false;
    private boolean preReleaseLatest = false;
    private boolean deprecatedLatest = false;
    private String urlGit = "https://github.com/jawis-net/SimpleBulkRenamer";
    private String url;
    
    private JSONObject json;
    private boolean isFetched = false;
    
    private List<String> listOtherApps = new ArrayList<>();
    private List<String> listOtherAppsUrl = new ArrayList<>();
    
    public Config() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    json = fetchJson();
                    JSONObject sbr = json.getJSONObject("SBR");
                    versionLatest = sbr.getString("version");
                    releaseDateLatest = sbr.getString("release-date");
                    urlGit = sbr.getString("url-git");
                    url = sbr.getString("url");
                    preReleaseLatest = sbr.getBoolean("pre-release");
                    deprecatedLatest = sbr.getBoolean("deprecated");
                    isFetched = true;
                } catch (Exception ex) {
                    isFetched = false;
                    LOGGER.warn(ex.getMessage());
                }
            }
        });
    }
    
    public static JSONObject fetchJson() throws Exception {
        URL obj = new URL(URL_JSON);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response);
        } else {
            throw new Exception("Failed to fetch JSON: " + responseCode);
        }
    }
    
    public static String getUrlJson() {
        return URL_JSON;
    }
    public String getVersionCurrent() {
        return versionCurrent;
    }
    public String getReleaseDateCurrent() {
        return releaseDateCurrent;
    }
    public String getVersionLatest() {
        return versionLatest;
    }
    public String getReleaseDateLatest() {
        return releaseDateLatest;
    }
    public boolean isPreRelease() {
        return preRelease;
    }
    public boolean isDeprecated() {
        return deprecated;
    }
    public boolean isPreReleaseLatest() {
        return preReleaseLatest;
    }
    public boolean isDeprecatedLatest() {
        return deprecatedLatest;
    }
    public String getUrlGit() {
        return urlGit;
    }
    public String getUrl() {
        return url;
    }
    public JSONObject getJson() {
        return json;
    }
    public boolean isFetched() {
        return isFetched;
    }

    public List<String> getListOtherApps() {
        return listOtherApps;
    }
    public List<String> getListOtherAppsUrl() {
        return listOtherAppsUrl;
    }
    
    public String getOtherAppUrl(int index) {
        return listOtherAppsUrl.get(index);
    }
    
}
