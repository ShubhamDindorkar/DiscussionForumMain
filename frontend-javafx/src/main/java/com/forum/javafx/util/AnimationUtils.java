package com.forum.javafx.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility class for common UI animations
 */
public class AnimationUtils {
    
    /**
     * Create a fade-in animation
     */
    public static FadeTransition createFadeIn(Node node, double duration) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        return fade;
    }
    
    /**
     * Create a fade-out animation
     */
    public static FadeTransition createFadeOut(Node node, double duration) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        return fade;
    }
    
    /**
     * Create a scale animation
     */
    public static ScaleTransition createScale(Node node, double duration, 
                                             double fromX, double fromY, 
                                             double toX, double toY) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(duration), node);
        scale.setFromX(fromX);
        scale.setFromY(fromY);
        scale.setToX(toX);
        scale.setToY(toY);
        return scale;
    }
    
    /**
     * Create a slide-in animation from right
     */
    public static TranslateTransition createSlideInRight(Node node, double duration) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(duration), node);
        slide.setFromX(50);
        slide.setToX(0);
        return slide;
    }
    
    /**
     * Create a slide-in animation from left
     */
    public static TranslateTransition createSlideInLeft(Node node, double duration) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(duration), node);
        slide.setFromX(-50);
        slide.setToX(0);
        return slide;
    }
    
    /**
     * Create a pulse animation (scale up and down)
     */
    public static SequentialTransition createPulse(Node node) {
        ScaleTransition scaleUp = createScale(node, 150, 1.0, 1.0, 1.1, 1.1);
        ScaleTransition scaleDown = createScale(node, 150, 1.1, 1.1, 1.0, 1.0);
        
        SequentialTransition pulse = new SequentialTransition(scaleUp, scaleDown);
        pulse.setCycleCount(1);
        return pulse;
    }
    
    /**
     * Create a shake animation
     */
    public static TranslateTransition createShake(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        return shake;
    }
    
    /**
     * Create a rotate animation
     */
    public static RotateTransition createRotate(Node node, double duration, double angle) {
        RotateTransition rotate = new RotateTransition(Duration.millis(duration), node);
        rotate.setByAngle(angle);
        return rotate;
    }
    
    /**
     * Animate a node's entrance with combined fade and scale
     */
    public static void animateEntrance(Node node) {
        node.setOpacity(0);
        node.setScaleX(0.9);
        node.setScaleY(0.9);
        
        FadeTransition fade = createFadeIn(node, 300);
        ScaleTransition scale = createScale(node, 300, 0.9, 0.9, 1.0, 1.0);
        
        ParallelTransition parallel = new ParallelTransition(fade, scale);
        parallel.play();
    }
    
    /**
     * Animate a node's exit with combined fade and scale
     */
    public static void animateExit(Node node, Runnable onFinished) {
        FadeTransition fade = createFadeOut(node, 200);
        ScaleTransition scale = createScale(node, 200, 1.0, 1.0, 0.9, 0.9);
        
        ParallelTransition parallel = new ParallelTransition(fade, scale);
        if (onFinished != null) {
            parallel.setOnFinished(e -> onFinished.run());
        }
        parallel.play();
    }
}
