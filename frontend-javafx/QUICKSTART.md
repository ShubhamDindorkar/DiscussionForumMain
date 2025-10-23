# Quick Start Guide - JavaFX Discussion Forum

## 🚀 Getting Started

### Step 1: Verify Prerequisites

Make sure you have:
- ✅ Java 17 or higher installed
- ✅ Backend server running on `http://localhost:8080`

Check Java version:
```powershell
java -version
```

### Step 2: Navigate to the frontend-javafx Directory

```powershell
cd c:\Users\Hp\DiscussionForumMain\frontend-javafx
```

### Step 3: Run the Application

```powershell
.\gradlew run
```

First run will download dependencies (may take a few minutes).

### Step 4: Start Using the Forum

1. **Create a Topic**: Click the "+ New Topic" button in the header
2. **View Messages**: Click on any topic in the left sidebar
3. **Post a Message**: Type in the text area and click "Send Message" or press Ctrl+Enter
4. **Refresh**: Click the "⟳ Refresh" button to reload topics
5. **Navigate**: Use the "← Back" button to return to the welcome screen

## 🎨 Features Highlight

### Visual Design
- Modern color scheme with blue and teal accents
- Smooth animations and transitions
- Hover effects on interactive elements
- Professional card-based layout

### User Experience
- Intuitive navigation
- Real-time updates
- Responsive layout
- Keyboard shortcuts

### Technical Features
- REST API integration
- Asynchronous data loading
- Error handling with user feedback
- Connection status monitoring

## 🛠️ Troubleshooting

### Backend Not Running
**Error**: "Failed to load topics. Make sure backend is running."

**Solution**: Start the backend server first:
```powershell
cd ..\backend
.\gradlew bootRun
```

### Port Conflict
**Error**: Connection refused

**Solution**: Ensure backend is running on port 8080. Check `application.properties`.

### Build Errors
**Error**: Gradle build fails

**Solution**:
```powershell
.\gradlew clean build
```

## 📱 Application Layout

```
┌─────────────────────────────────────────────────────────────┐
│  Discussion Forum        [⟳ Refresh]  [+ New Topic]         │
├────────────────┬────────────────────────────────────────────┤
│  Topics        │  Welcome to Discussion Forum               │
│  ────────      │                                            │
│  ┌──────────┐  │  Select a topic from the left to view     │
│  │ Topic 1  │  │  messages                                  │
│  │ 5 msgs   │  │                                            │
│  └──────────┘  │  or create a new topic to start a         │
│                │  discussion                                │
│  ┌──────────┐  │                                            │
│  │ Topic 2  │  │                                            │
│  │ 3 msgs   │  │                                            │
│  └──────────┘  │                                            │
│                │                                            │
└────────────────┴────────────────────────────────────────────┘
```

## 🎯 Next Steps

### For Users
1. Explore existing topics
2. Create your first discussion topic
3. Engage with the community

### For Developers
1. Review the code structure in `src/main/java/`
2. Customize styles in `src/main/resources/styles/application.css`
3. Extend functionality by adding new views or features

## 📚 Additional Resources

- Full documentation: See `README.md`
- Backend API: See `../backend/README.md`
- JavaFX Documentation: https://openjfx.io/

## 🐛 Known Issues

None currently. If you encounter issues, please check:
1. Java version compatibility
2. Backend server status
3. Network connectivity
4. Firewall settings

## 💡 Tips

- Use Ctrl+Enter to send messages quickly
- Hover over topics to see the hover effect
- The interface auto-refreshes after creating topics/messages
- Messages are displayed with timestamps for context

---

**Enjoy using the Discussion Forum!** 🎉
