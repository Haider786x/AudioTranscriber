🎧 Audio Transcriber

A full-stack audio transcription web application built with Spring Boot and React, using AssemblyAI for cloud-based speech-to-text.

Users can upload audio files and receive accurate transcriptions in real time.

🚀 Live Demo

Frontend (Netlify)
👉 https://audiotranscribe.netlify.app

Backend (Render)
👉 https://audiotranscriber-9jau.onrender.com

🛠 Tech Stack
Frontend

React (Vite)

Axios

HTML / CSS

Hosted on Netlify

Backend

Spring Boot

REST APIs

AssemblyAI Speech-to-Text API

Docker

Hosted on Render

✨ Features

Upload audio files (mp3, wav, etc.)

Cloud-based transcription using AssemblyAI

Responsive UI (mobile + desktop)

CORS-secured backend

One-repo monorepo setup (Frontend + Backend)

Production-ready deployment

📂 Project Structure
AudioTranscriber/
│
├── Backend/                 # Spring Boot backend
│   ├── src/main/java/
│   ├── application.yml
│   ├── Dockerfile
│   └── pom.xml
│
├── Frontend/
│   └── audio-transcriber-ui/
│       ├── src/
│       ├── index.html
│       ├── package.json
│       └── vite.config.js
│
├── netlify.toml
└── README.md

🔑 Environment Variables
Backend (Render)

Set this in Render Environment Variables:

ASSEMBLYAI_API_KEY=your_api_key_here


⚠️ Never commit API keys to GitHub

🧪 Run Locally
Backend
cd Backend
./mvnw spring-boot:run


Runs on:

http://localhost:8080

Frontend
cd Frontend/audio-transcriber-ui
npm install
npm run dev


Runs on:

http://localhost:5173

🔐 CORS Configuration

Backend allows requests from:

http://localhost:5173

https://audiotranscribe.netlify.app

Configured via a global Spring Boot CORS config.

📦 Deployment
Backend

Dockerized Spring Boot app

Deployed on Render

Frontend

Built with Vite

Deployed on Netlify

netlify.toml handles monorepo setup

📈 Future Improvements

Upload progress indicator

Download transcription as text file

Speaker diarization

Language detection

Authentication
