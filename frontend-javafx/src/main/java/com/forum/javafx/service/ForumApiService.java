package com.forum.javafx.service;

import com.forum.javafx.model.Message;
import com.forum.javafx.model.Topic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ForumApiService {
    
    private static final String BASE_URL = "http://localhost:8080";
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    
    private static ForumApiService instance;
    
    private ForumApiService() {
    }
    
    public static ForumApiService getInstance() {
        if (instance == null) {
            instance = new ForumApiService();
        }
        return instance;
    }
    
    /**
     * Get all topics from the backend
     */
    public List<Topic> getAllTopics() throws Exception {
        URL url = new URL(BASE_URL + "/topic");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            Type listType = new TypeToken<ArrayList<Topic>>(){}.getType();
            return gson.fromJson(response.toString(), listType);
        } else {
            throw new Exception("Failed to fetch topics: HTTP " + responseCode);
        }
    }
    
    /**
     * Create a new topic
     */
    public Topic createTopic(String title) throws Exception {
        URL url = new URL(BASE_URL + "/topic");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        
        Topic newTopic = new Topic();
        newTopic.setTitle(title);
        
        String jsonInputString = gson.toJson(newTopic);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return gson.fromJson(response.toString(), Topic.class);
        } else {
            throw new Exception("Failed to create topic: HTTP " + responseCode);
        }
    }
    
    /**
     * Get all messages for a specific topic
     */
    public List<Message> getMessagesForTopic(long topicId) throws Exception {
        URL url = new URL(BASE_URL + "/topic/" + topicId + "/message");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
            List<Message> messages = gson.fromJson(response.toString(), listType);
            
            // Set topicId for each message
            for (Message msg : messages) {
                msg.setTopicId(topicId);
            }
            
            return messages;
        } else {
            throw new Exception("Failed to fetch messages: HTTP " + responseCode);
        }
    }
    
    /**
     * Add a new message to a topic
     */
    public Message addMessage(long topicId, String content) throws Exception {
        URL url = new URL(BASE_URL + "/topic/" + topicId + "/message");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        
        Message newMessage = new Message();
        newMessage.setContent(content);
        
        String jsonInputString = gson.toJson(newMessage);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            Message message = gson.fromJson(response.toString(), Message.class);
            message.setTopicId(topicId);
            return message;
        } else {
            throw new Exception("Failed to add message: HTTP " + responseCode);
        }
    }
    
    /**
     * Test connection to the backend
     */
    public boolean testConnection() {
        try {
            URL url = new URL(BASE_URL + "/topic");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}
