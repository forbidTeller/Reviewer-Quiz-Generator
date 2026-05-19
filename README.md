# Reviewer-Quiz-Generator
Reviewer Quiz Generator is a desktop app that turns **text, Word, or PDF files** into interactive study materials. By extracting user-highlighted terms, it generates **Multiple Choice, Identification, and True/False** quizzes. It acts as a gamified mental warm-up to **exercise your memorization** before a real exam.
# Features
* **Multi-Format File Support**:  Allows users to upload standard text files (.txt), Word documents (.docx), and PDF files (.pdf).
* **Automated Keyword Targeting**: The program automatically scans the uploaded documents to find highlighted terms, specifically looking for text wrapped in asterisks for .txt files or bolded text in .docx and .pdf files.
* **Real-Time Corrections**: If a user answers incorrectly (or runs out of time), the system intercepts with a correction screen that visually displays the right answer before moving on.
# Built With
* **Java** - The main programming language
* **Java Swing** - Desktop GUI Framework
* **MySQL** - Required database
* **Apache PDFBox / Apache POI** - Required: Add these if you used them for reading PDF/Word files
# Getting Started
To get a local copy up and running, follow these steps.
## Prerequisites
* [JDK 24 or higher] (https://www.oracle.com/java/technologies/downloads/)
* Any Java-compatible IDE (e.g., Apache NetBeans, IntelliJ IDEA, Eclipse, or VS Code)
* Git
* External Libraries (JAR Files listed below)
## Required Libraries (Download Links)

Since this project does not use Maven/Gradle, you must manually download these `.jar` files and add them to your project's Build Path/Libraries folder. All files can be downloaded safely from the Maven Central Repository:

**Database Connection:**
* [MySQL Connector/J (v8.3.0)](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar)

**Document Processing (Word & PDF):**
* [Apache PDFBox App (v2.0.30)](https://repo1.maven.org/maven2/org/apache/pdfbox/pdfbox-app/2.0.30/pdfbox-app-2.0.30.jar)
* [Apache POI Core (v5.2.3)](https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.3/poi-5.2.3.jar)
* [Apache POI OOXML (v5.2.3)](https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.3/poi-ooxml-5.2.3.jar)
* [Apache POI OOXML Full (v5.2.3)](https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml-full/5.2.3/poi-ooxml-full-5.2.3.jar)
* [Apache XMLBeans (v5.1.1)](https://repo1.maven.org/maven2/org/apache/xmlbeans/xmlbeans/5.1.1/xmlbeans-5.1.1.jar)

**Apache Commons (Dependencies for POI):**
* [Commons Collections4 (v4.4)](https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar)
* [Commons Compress (v1.22)](https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.22/commons-compress-1.22.jar)
* [Commons IO (v2.11.0)](https://repo1.maven.org/maven2/commons-io/commons-io/2.11.0/commons-io-2.11.0.jar)

**Logging Frameworks:**
* [Log4j Core (v2.19.0)](https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-core/2.19.0/log4j-core-2.19.0.jar)
* [SLF4J Simple (v2.0.13)](https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.13/slf4j-simple-2.0.13.jar)

>  **Note for Users:** These are direct download links. Clicking them will immediately download the `.jar` files to your computer.

## Installation

1. **Clone the repository:**

     ```bash
        git clone [https://github.com/your-username/Review-Quiz-Generator.git](https://github.com/your-username/Review-Quiz-Generator.git)
2. **Setup Libraries:**
   * Create a folder named `lib` in your project root.
   * Move the downloaded JARs into the `lib` folder.
   * In your IDE, right-click the JARs and select **"Add as Library"** or **"Add to Build Path"**.
3. **Run the Application:**
   * Locate `QuizGenerator.java` and run it.


