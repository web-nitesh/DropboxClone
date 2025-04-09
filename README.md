# 📦 Dropbox Clone – Full Stack Project

This is a **simplified Dropbox clone** built with:

- **Frontend**: React.js
- **Backend**: Spring Boot
- **Database**: PostgreSQL (via Docker)

---

## 🛠️ Tech Stack

- React.js (Frontend)
- Spring Boot (Backend)
- PostgreSQL (Docker Image)
- Docker Compose (for container orchestration)

---

## 🚀 How to Run the Project

### 📁 Clone the Repository

- Make sure docker is up and running
- First navigate to "Dropbox-backend" folder open in IDE
- First command to run in the terminal "mvn clean package -DskipTests"
- Then Run "docker-compose up --build"
- You can go and check in docker the containers
- This will start the backend server at  http://localhost:8080/api/files
- Now navigate to "DropBox_Frontend"  folder first run command "npm install"
- This will install everything required to run the frontend app
- Now run command "npm start" the frontend will start running at "http://localhost:3000/"
  
