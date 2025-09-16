Here’s the quickest end‑to‑end on Windows PowerShell.

1) Build and run the backend
- From backend folder:
```powershell
cd E:\Projects\DiscussionForumMain\backend
.\gradlew.bat clean bootJar --no-daemon
java -jar .\build\libs\backend.jar
```
- Wait until you see “Tomcat started on port 8080”.

2) Verify API
- New PowerShell window:
```powershell
Invoke-RestMethod http://localhost:8080/topic | ConvertTo-Json -Depth 5
```
- Create a topic:
```powershell
Invoke-RestMethod http://localhost:8080/topic -Method Post -ContentType "application/json" -Body '{"title":"Demo Topic"}'
```
- List messages for topic 1:
```powershell
Invoke-RestMethod http://localhost:8080/topic/1/message | ConvertTo-Json -Depth 5
```
- Add a message to topic 1:
```powershell
Invoke-RestMethod http://localhost:8080/topic/1/message -Method Post -ContentType "application/json" -Body '{"content":"Hello from API"}'
```

3) Open the frontend
- In the same window:
```powershell
Start-Process "http://localhost:8080/webjars/forum/index.html"
```

4) Sanity checks in UI
- Load the page, confirm topics are listed.
- Create a topic and a message from the UI; refresh the list to see updates.

5) If something doesn’t load
- Confirm the frontend jar is on the classpath:
```powershell
jar tf .\build\libs\backend.jar | Select-String "webjars/forum"
```
- If missing: make sure `backend/libs/forum-frontend.jar` exists and `build.gradle` includes:
  - implementation files('libs/forum-frontend.jar')
- Rebuild and rerun:
```powershell
.\gradlew.bat clean bootJar --no-daemon
java -jar .\build\libs\backend.jar
```

6) Optional: run via Gradle (instead of java -jar)
```powershell
.\gradlew.bat bootRun --no-daemon
```

That’s it. Follow the order exactly and you’ll have API + UI working at http://localhost:8080.