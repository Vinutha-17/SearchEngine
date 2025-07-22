# Search Web Application

A simple Java-based web application built with **Servlets**, **JSP**, and **MySQL** that allows users to search for employee details by keyword. Results are displayed in a formatted table.

---

## 🚀 Features

- Search for employees using any keyword (name, email, department, category, etc.)
- Display results in a styled HTML table
- User-friendly UI with a responsive layout

---

## 🏗️ Technologies Used

- Java Servlet
- HTML/CSS
- JDBC
- MySQL
- Apache Tomcat (v10 or v11 recommended)

---

# 🗃️ MySQL Database Setup

##1. **Create Database**

```sql
CREATE DATABASE employee_db;
USE employee_db;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(100),
    email VARCHAR(100),
    author VARCHAR(100),
    category VARCHAR(100),
    tags VARCHAR(255),
    joined_on DATE
);
```
---
##🛠️ How to Run the Project
##1. Clone or Download
Place the project files inside a Dynamic Web Project folder in Eclipse.

###2. Configure MySQL JDBC Driver
Download the latest MySQL Connector/J from:

👉 https://dev.mysql.com/downloads/connector/j/

Then:

Right-click on the project in Eclipse

Go to Build Path > Configure Build Path

Under Libraries, click Add External JARs and select mysql-connector-j-8.x.x.jar

##3. Set Up Database Credentials
In SearchEmployeeServlet.java, update the credentials:

private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
private static final String USER = "root";
private static final String PASS = "your_password";

##4. Deploy on Tomcat
Use Apache Tomcat 10+ (recommended)

Right-click the project > Run As > Run on Server

##5. Access the App

http://localhost:8080/EmployeeSearchProject/

---

## 📁 Project Folder Structure

```
EmployeeSearchProject/
│
├── WebContent/
│   ├── index.html
│   ├── results.jsp
│   ├── styles.css
│
├── src/
│   └── com.empsearch/
│       ├── SearchEmployeeServlet.java
│
├── lib/
│   └── mysql-connector-j-8.x.x.jar
---
```
