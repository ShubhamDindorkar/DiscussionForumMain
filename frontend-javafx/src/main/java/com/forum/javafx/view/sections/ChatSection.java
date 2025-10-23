package com.forum.javafx.view.sections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.forum.javafx.model.ChatMessage;
import com.forum.javafx.model.Contact;
import com.forum.javafx.service.AuthService;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatSection extends BorderPane {
    private VBox messagesArea;
    private TextField messageInput;
    private ListView<Contact> contactsList;
    private Map<String, List<ChatMessage>> conversations;
    private Contact selectedContact;
    private Label chatTitleLabel;
    private Circle statusDot;
    private Button sendBtn;
    
    public ChatSection() {
        conversations = new HashMap<>();
        setupUI();
        loadDefaultContacts();
    }
    
    private void setupUI() {
        getStyleClass().add("chat-section");
        
        // Left: Contacts list
        VBox leftPanel = new VBox(15);
        leftPanel.getStyleClass().add("users-panel");
        leftPanel.setPrefWidth(280);
        
        // Search box
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search contacts...");
        searchField.getStyleClass().add("search-field");
        
        Label contactsLabel = new Label("Messages");
        contactsLabel.getStyleClass().add("section-title");
        
        contactsList = new ListView<>();
        contactsList.getStyleClass().add("contacts-list");
        contactsList.setCellFactory(lv -> new ContactCell());
        contactsList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectContact(newVal);
                }
            }
        );
        VBox.setVgrow(contactsList, Priority.ALWAYS);
        
        Button newChatBtn = new Button("➕ New Conversation");
        newChatBtn.getStyleClass().add("primary-button");
        newChatBtn.setOnAction(e -> showNewConversationDialog());
        
        leftPanel.getChildren().addAll(searchField, contactsLabel, contactsList, newChatBtn);
        
        // Center: Chat area
        VBox centerPanel = new VBox();
        centerPanel.getStyleClass().add("chat-area");
        
        // Chat header
        HBox chatHeader = new HBox(15);
        chatHeader.getStyleClass().add("chat-header");
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        
        statusDot = new Circle(5, Color.web("#9E9E9E"));
        chatTitleLabel = new Label("Select a contact to start chatting");
        chatTitleLabel.getStyleClass().add("chat-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button callBtn = new Button("📞");
        Button videoBtn = new Button("📹");
        Button infoBtn = new Button("ℹ️");
        callBtn.getStyleClass().add("icon-button");
        videoBtn.getStyleClass().add("icon-button");
        infoBtn.getStyleClass().add("icon-button");
        
        chatHeader.getChildren().addAll(statusDot, chatTitleLabel, spacer, callBtn, videoBtn, infoBtn);
        
        // Messages area
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("messages-scroll");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        messagesArea = new VBox(10);
        messagesArea.getStyleClass().add("messages-area");
        messagesArea.setPadding(new Insets(15));
        scrollPane.setContent(messagesArea);
        
        // Input area
        HBox inputArea = new HBox(10);
        inputArea.getStyleClass().add("input-area");
        inputArea.setPadding(new Insets(15));
        
        Button attachBtn = new Button("📎");
        attachBtn.getStyleClass().add("icon-button");
        
        messageInput = new TextField();
        messageInput.setPromptText("Type a message...");
        messageInput.getStyleClass().add("message-input");
        messageInput.setDisable(true);
        HBox.setHgrow(messageInput, Priority.ALWAYS);
        
        Button emojiBtn = new Button("😊");
        emojiBtn.getStyleClass().add("icon-button");
        
        sendBtn = new Button("Send");
        sendBtn.getStyleClass().add("send-button");
        sendBtn.setOnAction(e -> sendMessage());
        sendBtn.setDisable(true);
        
        messageInput.setOnAction(e -> sendMessage());
        
        inputArea.getChildren().addAll(attachBtn, messageInput, emojiBtn, sendBtn);
        
        centerPanel.getChildren().addAll(chatHeader, scrollPane, inputArea);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        setLeft(leftPanel);
        setCenter(centerPanel);
    }
    
    private void loadDefaultContacts() {
        // Create 10 default contacts with different statuses
        Contact[] defaultContacts = {
            createContact("1", "Dr. Sarah Anderson", "sarah.anderson@college.edu", "online", 
                "Available for office hours", "10:30 AM"),
            createContact("2", "Prof. Michael Chen", "michael.chen@college.edu", "online",
                "Remember to submit your assignment!", "Yesterday"),
            createContact("3", "Emma Wilson", "emma.wilson@student.edu", "away",
                "Working on the Java project", "Yesterday"),
            createContact("4", "James Rodriguez", "james.r@student.edu", "online",
                "Did you understand the algorithm?", "2 days ago"),
            createContact("5", "Sophia Taylor", "sophia.t@student.edu", "offline",
                "Thanks for the notes!", "3 days ago"),
            createContact("6", "Liam Johnson", "liam.j@student.edu", "online",
                "Let's meet at the library", "Mon"),
            createContact("7", "Olivia Brown", "olivia.b@student.edu", "away",
                "Can you help with the database design?", "Sun"),
            createContact("8", "Noah Martinez", "noah.m@student.edu", "offline",
                "👍", "Sat"),
            createContact("9", "Ava Davis", "ava.d@student.edu", "online",
                "Great presentation today!", "Fri"),
            createContact("10", "Study Group - CS201", "cs201-group@college.edu", "online",
                "Alice: Meeting at 3 PM tomorrow", "11:45 AM")
        };
        
        for (Contact contact : defaultContacts) {
            contactsList.getItems().add(contact);
            // Initialize empty conversation
            conversations.put(contact.getId(), new ArrayList<>());
        }
        
        // Add sample messages for first few contacts
        addSampleConversation("1");
        addSampleConversation("2");
        addSampleConversation("4");
    }
    
    private Contact createContact(String id, String name, String email, String status, 
                                  String lastMessage, String lastTime) {
        Contact contact = new Contact(id, name, email, status);
        contact.setLastMessage(lastMessage);
        contact.setLastMessageTime(lastTime);
        contact.setProfilePicture(String.valueOf(name.charAt(0)));
        
        // Set unread count for some contacts
        if (id.equals("2") || id.equals("4")) {
            contact.setUnreadCount((int)(Math.random() * 3) + 1);
        }
        
        return contact;
    }
    
    private void addSampleConversation(String contactId) {
        List<ChatMessage> messages = conversations.get(contactId);
        if (messages == null) return;
        
        switch (contactId) {
            case "1":
                messages.add(createChatMessage(AuthService.getInstance().getCurrentUser().getName(), 
                    "1", "Hi! When are your office hours today?", "09:00"));
                messages.add(createChatMessage("Dr. Sarah Anderson", 
                    "1", "Hello! I'm available from 2-4 PM today.", "09:15"));
                messages.add(createChatMessage("Dr. Sarah Anderson", 
                    "1", "Available for office hours", "10:30"));
                break;
            case "2":
                messages.add(createChatMessage("Prof. Michael Chen", 
                    "2", "Don't forget - Assignment 3 is due Friday!", "Yesterday"));
                messages.add(createChatMessage("Prof. Michael Chen", 
                    "2", "Remember to submit your assignment!", "Yesterday"));
                break;
            case "4":
                messages.add(createChatMessage("James Rodriguez", 
                    "4", "Hey, are you free to discuss the sorting algorithms?", "2 days ago"));
                messages.add(createChatMessage("James Rodriguez", 
                    "4", "Did you understand the algorithm?", "2 days ago"));
                break;
        }
    }
    
    private ChatMessage createChatMessage(String senderName, String contactId, String content, String timeStr) {
        ChatMessage msg = new ChatMessage();
        msg.setSenderName(senderName);
        msg.setContent(content);
        msg.setRecipientId(contactId);
        return msg;
    }
    
    private void selectContact(Contact contact) {
        selectedContact = contact;
        
        // Update header
        chatTitleLabel.setText(contact.getName());
        Color statusColor = switch (contact.getStatus()) {
            case "online" -> Color.web("#4CAF50");
            case "away" -> Color.web("#FFC107");
            default -> Color.web("#9E9E9E");
        };
        statusDot.setFill(statusColor);
        
        // Enable input
        messageInput.setDisable(false);
        sendBtn.setDisable(false);
        
        // Clear unread count
        contact.setUnreadCount(0);
        contactsList.refresh();
        
        // Load conversation
        loadConversation(contact.getId());
    }
    
    private void loadConversation(String contactId) {
        messagesArea.getChildren().clear();
        
        List<ChatMessage> messages = conversations.get(contactId);
        if (messages != null && !messages.isEmpty()) {
            for (ChatMessage msg : messages) {
                boolean isSent = msg.getSenderName().equals(AuthService.getInstance().getCurrentUser().getName());
                String timeStr = msg.getTimestamp() != null ? 
                    new java.text.SimpleDateFormat("HH:mm").format(msg.getTimestamp()) : "";
                addMessageBubble(msg.getContent(), msg.getSenderName(), timeStr, isSent);
            }
        } else {
            // Show welcome message
            Label welcomeLabel = new Label("Start a conversation with " + 
                contactsList.getItems().stream()
                    .filter(c -> c.getId().equals(contactId))
                    .findFirst()
                    .map(Contact::getName)
                    .orElse("this contact"));
            welcomeLabel.getStyleClass().add("welcome-message");
            messagesArea.getChildren().add(welcomeLabel);
        }
    }
    
    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (!text.isEmpty() && selectedContact != null) {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            String currentUser = AuthService.getInstance().getCurrentUser().getName();
            
            // Add to conversation history
            ChatMessage message = new ChatMessage();
            message.setSenderName(currentUser);
            message.setContent(text);
            message.setRecipientId(selectedContact.getId());
            conversations.get(selectedContact.getId()).add(message);
            
            // Update UI
            addMessageBubble(text, currentUser, currentTime, true);
            
            // Update contact's last message
            selectedContact.setLastMessage(text);
            selectedContact.setLastMessageTime(currentTime);
            contactsList.refresh();
            
            messageInput.clear();
            
            // Simulate reply after 2 seconds
            simulateReply();
        }
    }
    
    private void addMessageBubble(String text, String sender, String time, boolean isSent) {
        VBox messageBox = new VBox(5);
        messageBox.getStyleClass().add("message-box");
        messageBox.setAlignment(isSent ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        if (!isSent) {
            Label senderLabel = new Label(sender);
            senderLabel.getStyleClass().add("message-sender");
            messageBox.getChildren().add(senderLabel);
        }
        
        Label messageLabel = new Label(text);
        messageLabel.getStyleClass().add(isSent ? "message-sent" : "message-received");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(450);
        
        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("message-time");
        
        messageBox.getChildren().addAll(messageLabel, timeLabel);
        messagesArea.getChildren().add(messageBox);
    }
    
    private void simulateReply() {
        if (selectedContact == null) return;
        
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> {
                    String[] replies = {
                        "Thanks for your message!",
                        "Got it, I'll check that out.",
                        "Sounds good!",
                        "Let me get back to you on that.",
                        "Sure, let's discuss this later."
                    };
                    String reply = replies[(int)(Math.random() * replies.length)];
                    String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                    
                    ChatMessage replyMsg = new ChatMessage();
                    replyMsg.setSenderName(selectedContact.getName());
                    replyMsg.setContent(reply);
                    replyMsg.setRecipientId(selectedContact.getId());
                    conversations.get(selectedContact.getId()).add(replyMsg);
                    
                    addMessageBubble(reply, selectedContact.getName(), time, false);
                    
                    selectedContact.setLastMessage(reply);
                    selectedContact.setLastMessageTime(time);
                    contactsList.refresh();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void showNewConversationDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New Conversation");
        dialog.setHeaderText("Start a new conversation");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Contact name");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        
        content.getChildren().addAll(
            new Label("Contact Name:"), nameField,
            new Label("Email:"), emailField
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty()) {
                // Create new contact
                String newId = String.valueOf(contactsList.getItems().size() + 1);
                Contact newContact = createContact(newId, nameField.getText(), 
                    emailField.getText(), "offline", "New conversation", "Just now");
                
                contactsList.getItems().add(0, newContact);
                conversations.put(newId, new ArrayList<>());
                
                // Select the new contact
                contactsList.getSelectionModel().select(newContact);
                
                return newContact.getName();
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    // Custom cell for contact list
    private class ContactCell extends ListCell<Contact> {
        private HBox content;
        private Label nameLabel;
        private Label messageLabel;
        private Label timeLabel;
        private Circle profilePic;
        private Circle statusIndicator;
        private Label unreadBadge;
        private Label initialLabel;
        
        public ContactCell() {
            super();
            
            // Profile picture
            profilePic = new Circle(25);
            profilePic.setFill(Color.web("#4A90E2"));
            
            StackPane profileStack = new StackPane();
            profileStack.setPrefSize(50, 50);
            initialLabel = new Label();
            initialLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
            profileStack.getChildren().addAll(profilePic, initialLabel);
            
            statusIndicator = new Circle(6);
            statusIndicator.setStroke(Color.WHITE);
            statusIndicator.setStrokeWidth(2);
            StackPane.setAlignment(statusIndicator, Pos.BOTTOM_RIGHT);
            profileStack.getChildren().add(statusIndicator);
            
            // Contact info
            VBox textBox = new VBox(5);
            nameLabel = new Label();
            nameLabel.getStyleClass().add("contact-name");
            
            messageLabel = new Label();
            messageLabel.getStyleClass().add("contact-message");
            messageLabel.setMaxWidth(150);
            
            textBox.getChildren().addAll(nameLabel, messageLabel);
            HBox.setHgrow(textBox, Priority.ALWAYS);
            
            // Time and badge
            VBox rightBox = new VBox(5);
            rightBox.setAlignment(Pos.TOP_RIGHT);
            
            timeLabel = new Label();
            timeLabel.getStyleClass().add("contact-time");
            
            unreadBadge = new Label();
            unreadBadge.getStyleClass().add("unread-badge");
            unreadBadge.setVisible(false);
            
            rightBox.getChildren().addAll(timeLabel, unreadBadge);
            
            content = new HBox(15, profileStack, textBox, rightBox);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(10));
        }
        
        @Override
        protected void updateItem(Contact contact, boolean empty) {
            super.updateItem(contact, empty);
            
            if (empty || contact == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(contact.getName());
                messageLabel.setText(contact.getLastMessage());
                timeLabel.setText(contact.getLastMessageTime());
                
                // Set profile initial
                initialLabel.setText(contact.getProfilePicture());
                
                // Set status color
                Color statusColor = switch (contact.getStatus()) {
                    case "online" -> Color.web("#4CAF50");
                    case "away" -> Color.web("#FFC107");
                    default -> Color.web("#9E9E9E");
                };
                statusIndicator.setFill(statusColor);
                
                // Show unread badge
                if (contact.getUnreadCount() > 0) {
                    unreadBadge.setText(String.valueOf(contact.getUnreadCount()));
                    unreadBadge.setVisible(true);
                } else {
                    unreadBadge.setVisible(false);
                }
                
                setGraphic(content);
            }
        }
    }
}
