# Discussion Forum Rules & Guidelines

## 1. Authentication & Security
- Only **official institute email IDs** (via SSO or OAuth) are allowed for login.
- Strict **role-based access control (RBAC)**:
  - **Admin**: Full system control.
  - **Faculty**: Verified accounts with a badge (✅).
  - **Students**: Limited permissions, role-based privileges.
- All data must be transmitted over **HTTPS**.
- Implement **JWT-based authentication** (with refresh tokens).
- Enforce **rate-limiting** to prevent spam & abuse.
- Store passwords securely using **bcrypt hashing** (if not using SSO).
- Sessions must auto-expire after inactivity.

---

## 2. Posting & Forums
- Students are automatically added to forums by **Department → Semester → Subject**.
- Posts must contain a **Title + Description (optional sub-text)**.
- Attachments allowed:
  - Images (compressed & sanitized).
  - PDFs / Docs (virus scanned before storage).
- Posts must be **constructive, respectful, and academic in nature**.
- Faculty & Students can reply; threads remain unlimited.
- Students can **upvote** if they have the same question.

---

## 3. Roles & Contributions
- **Faculty**:
  - Only verified faculty accounts get a ✅ badge.
  - Can post official clarifications.
- **Students**:
  - Earn **badges** based on contributions and upvotes.
  - Can create posts, reply, upvote, and bookmark.
- **Admins**:
  - Manage users, content, and security.
  - Can remove spam/abuse posts.

---

## 4. Personal Chats
- Allowed between students & peers.
- Supported:
  - Text, Voice Notes, Images, Files.
  - One-on-one & Group Voice/Video Calls.
- **End-to-End Encryption (E2EE)** required for personal chats.
- No sharing of sensitive personal information (e.g., passwords, IDs).

---

## 5. AI Features
- **AI Chatbot**:
  - Can summarize discussions and provide quick answers.
  - Cannot replace faculty-approved responses.
- **AI Reply Assistant**:
  - Suggests similar past questions before posting.
  - Helps reduce duplicates.
- **AI Summarizer**:
  - Provides condensed versions of long threads.

---

## 6. Additional Forum Features
- Tags: **Solved / Unsolved**.
- Bookmark & Save Posts.
- Trending Topics & Questions.
- Contribution Dashboard:
  - Posts Created
  - Replies Given
  - Upvotes Earned
  - Badges Unlocked

---

## 7. Community Rules
- Respect all members (faculty, peers, admins).
- No hate speech, plagiarism, or harassment.
- Spam & promotional content is strictly prohibited.
- Violations will result in warnings, temporary suspension, or permanent ban.

---

## 8. Admin Controls
- Admins can:
  - Moderate posts & users.
  - Manage roles & permissions.
  - Monitor suspicious activity (audit logs).
  - Enforce disciplinary actions.
