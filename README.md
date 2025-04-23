# 📦 Messaging SDK

The **Messaging SDK** is a robust library designed to handle real-time communication for chat applications. Built with **Kotlin** and **Spring Boot**, it supports features like user management, private and group chats, real-time messaging, and chat history retrieval. The SDK uses **MongoDB** as the database and leverages **Docker** for easy deployment.

---

## 🚀 Features

- **User Authentication:** Login using only a username (no email required).
- **User Directory:** View all registered users without sending friend requests.
- **Private Messaging:** One-on-one chats between users.
- **Group Chats:** Create and manage multi-user group chats.
- **Real-Time Messaging:** Send and receive messages instantly using RSocket.
- **Chat History:** Retrieve full chat history for private and group conversations.
- **Duplicate Prevention:** SDK ensures no duplicate users or chats exist.

---

## ⚙️ Technologies Used

- **Backend:** Kotlin, Spring Boot (Reactive)
- **Database:** MongoDB (Dockerized)
- **Networking:** RSocket (WebSocket transport)
- **Containerization:** Docker & Docker Compose

---

## 📥 Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-repo/messaging-sdk.git
   cd messaging-sdk
   ```

2. **Build the SDK:**
   ```bash
   ./gradlew clean build -x test
   ```

3. **Run the Application with Docker:**
   ```bash
   docker-compose up --build
   ```

---

## ⚡ SDK Usage

### 🔐 **User Authentication**

```kotlin
val userService = UserService()
val user = userService.login(username = "john_doe")
```

### 👥 **Fetching All Users**

```kotlin
val allUsers = userService.getAllUsers(excludeCurrentUser = true)
```

### 💬 **Creating a Private Chat**

```kotlin
val chatService = ChatService()
val chat = chatService.createPrivateChat(userId1 = "123", userId2 = "456")
```

### 🗣️ **Creating a Group Chat**

```kotlin
val groupChat = chatService.createGroupChat(groupName = "Study Group", userIds = listOf("123", "456", "789"))
```

### 📩 **Sending a Message**

```kotlin
val messageService = MessageService()
messageService.sendMessage(chatId = "abc123", senderId = "123", content = "Hello, world!")
```

### 📜 **Retrieving Chat History**

```kotlin
val history = messageService.getChatHistory(chatId = "abc123")
```

---

## 🐳 Docker Configuration

### `docker-compose.yaml`
```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:latest
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: secret
    ports:
      - "27017:27017"
    networks:
      - messaging-net

  messaging-server:
    build: .
    depends_on:
      - mongodb
    ports:
      - "8081:8080"
      - "7001:7001"
    networks:
      - messaging-net

networks:
  messaging-net:
    driver: bridge
```

---

## 📝 API Endpoints (if needed)

| Endpoint                | Method | Description                  |
| ----------------------- | ------ | ---------------------------- |
| `/login`                | POST   | User login                   |
| `/users`                | GET    | Fetch all users              |
| `/chats/private`        | POST   | Create a private chat        |
| `/chats/group`          | POST   | Create a group chat          |
| `/messages/send`        | POST   | Send a message               |
| `/messages/history`     | GET    | Retrieve chat history        |

---

## 🐛 Troubleshooting

1. **Port Conflict (8080 or 7001):**
   ```bash
   lsof -i :8080
   kill -9 <PID>
   ```

2. **Database Connection Error:**
   Ensure MongoDB is running:
   ```bash
   docker-compose up -d mongodb
   ```

3. **Rebuild the SDK:**
   ```bash
   ./gradlew clean build -x test
   docker-compose down
   docker-compose up --build
   ```

---

## 🤝 Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a Pull Request.

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

## ✨ Acknowledgments

- Built with ❤️ using Kotlin & Spring Boot.
- Special thanks to the contributors and the open-source community.
```

Let me know if you'd like to adjust or add more details! 🚀
