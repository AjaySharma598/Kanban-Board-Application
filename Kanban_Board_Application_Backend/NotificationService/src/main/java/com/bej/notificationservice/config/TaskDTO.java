/*
 * Author:Ajay Sharma
 * Date : 16-02-2023
 *Created With : Intellij IDEA Community Edition
 */

package com.bej.notificationservice.config;
import org.json.simple.JSONObject;
public class TaskDTO {

    private JSONObject jsonObject;

    public TaskDTO() {
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
