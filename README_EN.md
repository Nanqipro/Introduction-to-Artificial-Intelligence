# Introduction to Artificial Intelligence - Digital Textbook Platform

> An interactive digital learning platform based on the "Introduction to Artificial Intelligence and Applications" textbook, designed to provide students with a more engaging and interesting AI learning experience.

## 📖 Project Overview

This project is a digital web-based textbook platform developed for the "Introduction to Artificial Intelligence and Applications" textbook. It transforms traditional paper textbooks into interactive online learning platforms using modern web technologies. The project adopts a front-end and back-end separation architecture to provide students with a more intuitive and interactive learning experience, helping them better understand and master core concepts and applications of artificial intelligence.

### 🎯 Project Goals

- **Digital Transformation**: Transform traditional textbooks into modern digital learning platforms
- **Enhanced Learning Experience**: Improve learning effectiveness through interactive interfaces and multimedia content
- **Stimulate Learning Interest**: Make AI learning more interesting using modern web technologies
- **Convenient Access**: Access learning content anytime, anywhere through the network

## 📚 Textbook Content Overview

This digital platform is based on the "Introduction to Artificial Intelligence and Applications" textbook, covering the following core chapters:

### 📋 Chapter Structure

- **Prologue** - Overview of AI in Daily Life
- **Chapter 1** - Getting to Know Artificial Intelligence
  - Definition of Artificial Intelligence
  - Definition of Intelligence
  - Development History and Current Status
- **Chapter 2** - Fundamentals of Artificial Intelligence
  - Relationship between AI, ML, and DL
  - Basic Theories and Concepts
- **Chapter 3** - Advanced Machine Learning
- **Chapter 4** - Deep Learning Applications
- **Chapter 5** - Natural Language Processing
- **Chapter 6** - Computer Vision
- **Chapter 7** - Future and Challenges of AI

## 🏗️ Technical Architecture

### Frontend Tech Stack
- **Framework**: Vue.js 3.5.17
- **Build Tool**: Vite 7.0.4
- **Language**: JavaScript/TypeScript
- **UI Components**: Modern responsive design

### Backend Tech Stack
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Build Tool**: Maven
- **Dependencies**: Lombok

### Project Structure
```
Introduction-to-Artificial-Intelligence/
├── AI-GOODLAB/                 # Original LaTeX textbook source
│   ├── book.pdf               # Compiled PDF textbook
│   ├── chapter_*.tex          # Chapter LaTeX source files
│   ├── figures/               # Image resources
│   └── gallery/               # Display images
├── client/                    # Vue.js frontend project
│   ├── src/
│   │   ├── components/        # Vue components
│   │   ├── assets/           # Static resources
│   │   └── App.vue           # Main application component
│   ├── package.json          # Frontend dependencies
│   └── vite.config.js        # Vite configuration
└── server/                    # Spring Boot backend project
    ├── src/main/java/        # Java source code
    ├── src/main/resources/   # Configuration files and static resources
    └── pom.xml              # Maven configuration
```

## 🚀 Quick Start

### Requirements

- **Node.js**: >= 16.0.0
- **Java**: >= 17
- **Maven**: >= 3.6.0

### Installation and Running

#### 1. Clone the project
```bash
git clone https://github.com/your-username/Introduction-to-Artificial-Intelligence.git
cd Introduction-to-Artificial-Intelligence
```

#### 2. Start frontend service
```bash
cd client
npm install
npm run dev
```
Frontend service will start at `http://localhost:5173`

#### 3. Start backend service
```bash
cd server
mvn clean install
mvn spring-boot:run
```
Backend service will start at `http://localhost:8080`

## 📱 Features

### 🎨 User Interface
- **Responsive Design**: Support for desktop, tablet, and mobile access
- **Modern UI**: Clean and beautiful user interface design
- **Interactive Navigation**: Convenient chapter navigation and content search
- **Theme Switching**: Support for light and dark theme modes

### 📖 Learning Features
- **Chapter Browsing**: Structured content display organized by chapters
- **Multimedia Support**: Display of images, charts, animations, and other multimedia content
- **Search Function**: Quick search for relevant knowledge points and concepts
- **Learning Progress**: Record and track learning progress
- **Bookmark Collection**: Bookmark important content for easy review
- **Note Taking**: Online learning note recording

### 🔧 Technical Features
- **Frontend-Backend Separation**: Clear architecture design for easy maintenance and expansion
- **RESTful API**: Standardized interface design
- **Modular Development**: Component-based frontend architecture
- **Hot Reload**: Real-time updates in development environment
- **Cache Optimization**: Improve page loading speed
- **SEO Friendly**: Search engine optimization support

## 📖 Original Textbook

This project is based on the "Introduction to Artificial Intelligence and Applications" textbook written by the GoodLab team. The textbook is typeset using LaTeX and covers:

- Basic concepts and development history of artificial intelligence
- Core theories of machine learning and deep learning
- Natural language processing technologies and applications
- Computer vision technologies and applications
- Practical application cases of artificial intelligence
- Development trends and challenges of artificial intelligence

## 🤝 Contributing

We welcome contributions of any kind! Please follow these steps:

1. Fork this project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 👥 Development Team

- **GoodLab** - Textbook content writing
- **Development Team** - Digital platform development

## 📞 Contact Us

If you have any questions or suggestions, please contact us through:

- Project Issues: [GitHub Issues](https://github.com/your-username/Introduction-to-Artificial-Intelligence/issues)
- Email: contact@goodlab.com

## 🙏 Acknowledgments

Thanks to all developers and educators who contributed to this project, especially:

- GoodLab team for providing quality textbook content
- Vue.js and Spring Boot communities for technical support
- All test users for valuable feedback

---

⭐ If this project helps you, please give us a star!

📚 Let's promote the digital transformation of AI education together!
