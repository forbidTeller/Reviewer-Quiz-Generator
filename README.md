# Reviewer-Quiz-Generator
Reviewer Quiz Generator is a desktop app that turns text, Word, or PDF files into interactive study materials. By extracting user-highlighted terms, it generates Multiple Choice, Identification, and True/False quizzes. It acts as a gamified mental warm-up to exercise your memorization before a real exam.
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
* [JDK 17 or higher] (https://www.oracle.com/java/technologies/downloads/)
* Any Java-compatible IDE (e.g., Apache NetBeans, IntelliJ IDEA, Eclipse, or VS Code)
* Git
* External Libraries (JAR Files listed below)
## Required Libraries (Download Links)

Since this project does not use Maven/Gradle, you must manually download these `.jar` files and add them to your project's Build Path/Libraries folder. All files can be downloaded safely from the Maven Central Repository:

**Database Connection:**
* [MySQL Connector/J (v8.3.0)](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j/8.3.0)

**Document Processing (Word & PDF):**
* [Apache PDFBox App (v2.0.30)](https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox-app/2.0.30)
* [Apache POI Core (v5.2.3)](https://mvnrepository.com/artifact/org.apache.poi/poi/5.2.3)
* [Apache POI OOXML (v5.2.3)](https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml/5.2.3)
* [Apache POI OOXML Full (v5.2.3)](https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml-full/5.2.3)
* [Apache XMLBeans (v5.1.1)](https://mvnrepository.com/artifact/org.apache.xmlbeans/xmlbeans/5.1.1)

**Apache Commons (Dependencies for POI):**
* [Commons Collections4 (v4.4)](https://mvnrepository.com/artifact/org.apache.commons/commons-collections4/4.4)
* [Commons Compress (v1.22)](https://mvnrepository.com/artifact/org.apache.commons/commons-compress/1.22)
* [Commons IO (v2.11.0)](https://mvnrepository.com/artifact/commons-io/commons-io/2.11.0)

**Natural Language Processing:**
* [Apache OpenNLP Tools](https://mvnrepository.com/artifact/org.apache.opennlp/opennlp-tools) *(Download the version matching your jar)*

**Logging Frameworks:**
* [Log4j API & Core (v2.19.0)](https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core/2.19.0)
* [SLF4J API & Simple (v2.0.13)](https://mvnrepository.com/artifact/org.slf4j/slf4j-simple/2.0.13)

> **Note for Users:** When downloading from MVNRepository, simply click on the "jar" link in the "Files" row to get the direct download.

