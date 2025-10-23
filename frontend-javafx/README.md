# Discussion Forum - JavaFX Frontend

A modern, professional, and aesthetically pleasing JavaFX frontend for the Discussion Forum application.

## Features

- **Modern UI Design**: Clean and professional interface with smooth animations
- **Topic Management**: Browse, create, and select topics
- **Real-time Messaging**: View and send messages within topics
- **Responsive Layout**: Adapts to different window sizes
- **Smooth Animations**: Fade transitions and hover effects for better UX
- **Professional Styling**: Custom CSS with modern color palette and typography

## Architecture

### Components

- **ForumApplication**: Main application entry point
- **MainView**: Root layout with split pane for topics and messages
- **HeaderView**: Top navigation bar with action buttons
- **TopicListView**: Left sidebar displaying all topics
- **MessagesView**: Right panel showing messages for selected topic

### Models

- **Topic**: Represents a discussion topic with title and message count
- **Message**: Represents a message with content, timestamp, and topic reference

### Services

- **ForumApiService**: Singleton service for REST API communication with backend

## Prerequisites

- Java 17 or higher
- Backend server running on `http://localhost:8080`

## Running the Application

### Option 1: Using Gradle (Windows)

```powershell
cd frontend-javafx
.\gradlew run
```

### Option 2: Using Gradle (Unix/Mac)

```bash
cd frontend-javafx
./gradlew run
```

### Building an executable JAR

```powershell
.\gradlew build
```

The JAR will be created in `build/libs/`

## Project Structure

```
frontend-javafx/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── forum/
│       │           └── javafx/
│       │               ├── ForumApplication.java
│       │               ├── model/
│       │               │   ├── Message.java
│       │               │   └── Topic.java
│       │               ├── service/
│       │               │   └── ForumApiService.java
│       │               └── view/
│       │                   ├── MainView.java
│       │                   └── components/
│       │                       ├── HeaderView.java
│       │                       ├── TopicListView.java
│       │                       └── MessagesView.java
│       └── resources/
│           └── styles/
│               └── application.css
├── build.gradle
└── settings.gradle
```

## Design Features

### Color Palette

- **Primary Blue**: #4A90E2 - Used for buttons and highlights
- **Accent Teal**: #50E3C2 - Used for accents and success states
- **Neutral Grays**: Various shades for backgrounds and borders
- **Status Colors**: Green for success, red for errors, orange for warnings

### Typography

- Font Family: "Segoe UI", "Roboto", "Helvetica Neue"
- Font Sizes: Hierarchical sizing from 12px to 32px
- Font Weights: Regular (400) and Bold (600-700)

### UI Components

1. **Topic Cards**
   - Rounded corners with subtle shadows
   - Hover effects with color transitions
   - Selected state with border highlight

2. **Message Cards**
   - Clean white background
   - Timestamp display
   - Wrapped text for long messages

3. **Input Areas**
   - Focused state with blue border
   - Placeholder text
   - Character input validation

4. **Buttons**
   - Primary and secondary variants
   - Hover and pressed states
   - Smooth scale transitions

### Animations

- Fade in on application start
- Fade transitions between views
- Scale animations on topic card entrance
- Hover effects on interactive elements

## API Endpoints Used

- `GET /topic` - Fetch all topics
- `POST /topic` - Create a new topic
- `GET /topic/{topicId}/message` - Fetch messages for a topic
- `POST /topic/{topicId}/message` - Post a new message

## Configuration

The backend URL is configured in `ForumApiService.java`:

```java
private static final String BASE_URL = "http://localhost:8080";
```

To change the backend URL, modify this constant.

## Keyboard Shortcuts

- **Ctrl+Enter**: Send message (when message input is focused)

## Troubleshooting

### Backend Connection Issues

If you see "Failed to load topics" error:

1. Ensure the backend server is running
2. Check that the backend is accessible at `http://localhost:8080`
3. Verify firewall settings aren't blocking the connection

### Build Issues

If Gradle build fails:

1. Ensure Java 17+ is installed: `java -version`
2. Clean the build: `.\gradlew clean`
3. Rebuild: `.\gradlew build`

## Development

### Adding New Features

1. **New Views**: Add to `view/components/` package
2. **New Models**: Add to `model/` package
3. **Styling**: Update `resources/styles/application.css`
4. **API Calls**: Extend `ForumApiService.java`

### Styling Guidelines

- Use CSS variables defined in `.root` selector
- Follow existing naming conventions
- Add hover/focus states for interactive elements
- Use consistent spacing and padding

## License

This project follows the same license as the backend forum application.

## Credits

Built with:
- JavaFX 21
- Gson for JSON parsing
- ControlsFX for enhanced controls
- Ikonli for icons support
