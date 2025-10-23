package com.forum.javafx.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date and time formatting
 */
public class DateUtils {
    
    private static final SimpleDateFormat DATE_TIME_FORMAT = 
        new SimpleDateFormat("MMM dd, yyyy HH:mm");
    
    private static final SimpleDateFormat DATE_FORMAT = 
        new SimpleDateFormat("MMM dd, yyyy");
    
    private static final SimpleDateFormat TIME_FORMAT = 
        new SimpleDateFormat("HH:mm");
    
    /**
     * Format date and time
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "Unknown";
        }
        return DATE_TIME_FORMAT.format(date);
    }
    
    /**
     * Format date only
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "Unknown";
        }
        return DATE_FORMAT.format(date);
    }
    
    /**
     * Format time only
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "Unknown";
        }
        return TIME_FORMAT.format(date);
    }
    
    /**
     * Get relative time string (e.g., "2 hours ago")
     */
    public static String getRelativeTime(Date date) {
        if (date == null) {
            return "Unknown";
        }
        
        long diff = System.currentTimeMillis() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (seconds > 10) {
            return seconds + " seconds ago";
        } else {
            return "Just now";
        }
    }
}
