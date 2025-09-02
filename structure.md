# Project Structure: Discussion Forum

Tech Stack:
- **Frontend**: Next.js (TypeScript, TailwindCSS, shadcn/ui)
- **Backend**: Java Spring Boot (Spring Security, JPA, REST APIs, WebSocket)
- **Database**: PostgreSQL
- **Auth**: JWT + OAuth (Institute Email Only)
- **Storage**: Cloud (AWS S3 / Firebase / Supabase Buckets with virus scanning)
- **Real-Time Chat**: WebSocket (Spring + Socket.io)

---

## 1. Frontend (Next.js)

/frontend
/src
/app
/auth # Login, Register, OAuth callbacks
/dashboard # Student/Faculty dashboards
/forum # Semester → Subject → Threads
/chat # Personal and group chats
/admin # Admin panel
/components
/ui # Reusable UI components
/forum # Post, Reply, Upvote, Thread UI
/chat # Chat bubbles, Calls, Media upload
/dashboard # Stats, badges, contribution insights
/hooks
useAuth.ts # Auth state & hooks
useSocket.ts # Real-time chat hook
/services
api.ts # Axios/fetch wrappers
authService.ts
forumService.ts
chatService.ts
adminService.ts
/context
AuthContext.tsx
ThemeContext.tsx
/utils
validators.ts # Input validation
security.ts # XSS sanitizers, token helpers


---

## 2. Backend (Spring Boot)

/backend
/src/main/java/com/forum
/config
SecurityConfig.java # JWT, OAuth, RBAC setup
WebSocketConfig.java # Real-time chat config
CorsConfig.java # Secure CORS handling
/controllers
AuthController.java
ForumController.java
ChatController.java
AdminController.java
UserController.java
/models
User.java
Department.java
Semester.java
Subject.java
Post.java
Reply.java
Upvote.java
Badge.java
ChatMessage.java
CallSession.java
/repositories
UserRepository.java
PostRepository.java
ChatRepository.java
BadgeRepository.java
/services
AuthService.java
ForumService.java
ChatService.java
AdminService.java
FileStorageService.java
/dto
LoginRequest.java
RegisterRequest.java
PostDTO.java
ReplyDTO.java
ChatMessageDTO.java
/utils
JwtUtil.java
FileValidator.java
RateLimiter.java
/websocket
ChatSocketHandler.java
PresenceTracker.java



---

## 3. Database Schema (PostgreSQL)

**Users**
- id (PK)
- name
- email (unique, verified)
- role (ADMIN / FACULTY / STUDENT)
- badges[]
- created_at, updated_at

**Departments / Semesters / Subjects**
- dept_id, sem_id, subject_id
- relationships: Department → Semester → Subject

**Posts**
- post_id (PK)
- subject_id (FK)
- title, content
- created_by (FK → Users)
- solved (boolean)
- created_at

**Replies**
- reply_id (PK)
- post_id (FK)
- content, created_by
- created_at

**Upvotes**
- id (PK)
- user_id (FK)
- post_id (FK)

**Chats**
- message_id (PK)
- sender_id, receiver_id
- type (TEXT / FILE / VOICE / IMAGE)
- content (encrypted if sensitive)
- timestamp

**Admin Logs**
- log_id (PK)
- action, actor_id
- timestamp

---

## 4. Features by Module

### Forum
- Department → Semester → Subject → Thread
- Post (title, description, attachments)
- Reply & unlimited threading
- Upvotes
- Badges for contribution

### Personal Chat
- Text, Voice Notes, Files, Images
- One-on-One & Group calls
- End-to-End Encryption (E2EE)
- WebSocket based real-time updates

### Admin Panel
- User management (ban, suspend, promote)
- Role assignment
- Content moderation
- Logs & analytics

### AI Features
- AI Chatbot for summarization
- AI Reply Assistant (suggests similar posts)
- Trending questions/topics
- Contribution dashboard

---

## 5. Security Considerations
- **Auth**:
  - JWT with refresh tokens
  - OAuth restricted to official email domain
- **Transport Security**:
  - Enforce HTTPS everywhere
- **Data Security**:
  - Passwords hashed (bcrypt)
  - File uploads sanitized & virus scanned
  - DB queries parameterized (no SQL injection)
- **API Security**:
  - Rate limiting
  - Role-based access control
  - CORS restricted to known frontend domains
- **Chat Security**:
  - End-to-end encryption for private messages
  - Logs stored encrypted
- **Monitoring**:
  - Audit logs for admin actions
  - Alerts for suspicious activity

---

# ✅ This structure ensures:
- Clean separation of concerns (Frontend, Backend, DB).
- Secure by design (JWT, RBAC, HTTPS, E2EE).
- Scalable architecture for forums + real-time chat + AI features.
