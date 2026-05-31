```markdown
# 📚 Library Management System

## نظام إدارة المكتبة

---

## 1. Project Overview | نظرة عامة

**Library Management System** is a JavaFX desktop application designed to help librarians manage library books, members, user profiles, and view useful library statistics through a simple and user-friendly interface.

هذا المشروع عبارة عن تطبيق سطح مكتب باستخدام **JavaFX** لإدارة المكتبة، ويتيح للمستخدم إدارة الكتب والأعضاء وعرض الإحصائيات وتعديل الملف الشخصي من خلال واجهة رسومية سهلة الاستخدام.

---

## 2. Project Information | معلومات المشروع

| Item | Details |
|---|---|
| Project Name | Library Management System |
| Application Type | Desktop Application |
| Programming Language | Java 21 |
| UI Framework | JavaFX 21.0.2 |
| Database | MySQL |
| Database Connection | JDBC |
| Build Tool | Maven |
| Project Pattern | MVC-like Structure |
| Number of Scenes | 6 Scenes |

---

## 3. Main Features | الخصائص الرئيسية

The application includes the following features:

- Login system connected to MySQL database.
- User authentication using username and password.
- Home page accessible only after successful login.
- Dashboard with library statistics.
- Books management with full CRUD operations.
- Members management with full CRUD operations.
- User profile page for updating user information.
- Search functionality for books and members.
- JavaFX TableView, Buttons, Labels, TextFields, PasswordFields, Alerts, Charts, and Layouts.
- External MySQL database using JDBC.
- CSS styling for better visual design.
- Error handling for database operations and invalid input.

---

## 4. Project Requirements Coverage | تغطية متطلبات المشروع

| Requirement | Status | Implementation |
|---|---|---|
| Desktop Application | ✅ Completed | JavaFX desktop application |
| 4-6 Scenes | ✅ Completed | Login, Home, Dashboard, Books, Members, Profile |
| Login Page | ✅ Completed | Validates username and password from database |
| Error Messages | ✅ Completed | Shows errors for empty fields and invalid login |
| Home Page | ✅ Completed | Displays authenticated user information and navigation |
| Dashboard / Services | ✅ Completed | Shows statistics and chart |
| External Database | ✅ Completed | MySQL database |
| JDBC Connection | ✅ Completed | `DatabaseConnection.java` |
| CRUD Operations | ✅ Completed | Books and Members |
| UI Controls | ✅ Completed | Buttons, Labels, TextFields, Tables, Alerts |
| Layouts | ✅ Completed | BorderPane, VBox, HBox, SplitPane |
| Events | ✅ Completed | Button clicks, Enter key, table selection, mouse events |
| Styling | ✅ Completed | CSS file |
| Submission Files | ✅ Completed | Source code, SQL file, README, Maven configuration |

---

## 5. Application Scenes | شاشات التطبيق

The application contains 6 main scenes:

```text
1. Login Scene      → User login and authentication
2. Home Scene       → Main page after successful login
3. Dashboard Scene  → Library statistics and chart
4. Books Scene      → Manage books with CRUD operations
5. Members Scene    → Manage library members with CRUD operations
6. Profile Scene    → Update user profile information
```

---

## 6. Technologies Used | التقنيات المستخدمة

| Technology | Purpose |
|---|---|
| Java 21 | Main programming language |
| JavaFX | Building the desktop user interface |
| FXML | Designing application scenes |
| CSS | Styling the user interface |
| MySQL | External relational database |
| JDBC | Connecting Java application to MySQL |
| Maven | Project build and dependency management |

---

## 7. Requirements Before Running | متطلبات التشغيل

Before running the project, make sure the following tools are installed:

| Tool | Required Version |
|---|---|
| Java JDK | 21 or later |
| Maven | 3.8 or later |
| MySQL Server | 8.0 or later |
| IntelliJ IDEA | Recommended |

---

## 8. Database Setup | إعداد قاعدة البيانات

The project uses a MySQL database named:

```text
library_db
```

To create and prepare the database:

1. Open **MySQL Workbench**, phpMyAdmin, or any MySQL client.
2. Open the SQL file:

```text
docs/library_db.sql
```

3. Run the full SQL script.
4. The script will create:
  - Database `library_db`
  - Table `users`
  - Table `books`
  - Table `members`
  - Table `borrowings`
  - Default admin user
  - Sample books data

---

## 9. Default Login Account | بيانات الدخول الافتراضية

After importing the database, use the following account to login:

| Field | Value |
|---|---|
| Username | `admin` |
| Password | `admin123` |

---

## 10. Database Connection Configuration | إعداد الاتصال بقاعدة البيانات

The database connection is configured in:

```text
src/main/java/com/library/librarymanagementsystem/database/DatabaseConnection.java
```

Default configuration:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

If your MySQL server has a password, update the `PASSWORD` value:

```java
private static final String PASSWORD = "your_mysql_password";
```

---

## 11. How to Run the Project | طريقة تشغيل المشروع

### Option 1: Run using Maven

From the project root folder, run:

```bash
mvn clean javafx:run
```

### Option 2: Run using IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Choose **Open Project**.
3. Select the `LibraryManagementSystem` folder.
4. Wait until Maven downloads all dependencies.
5. Make sure MySQL is running.
6. Import `docs/library_db.sql`.
7. Run `Main.java` or `Launcher.java`.

---

## 12. Project Structure | هيكل المشروع

```text
LibraryManagementSystem/
├── .mvn/
├── docs/
│   ├── lib.md
│   ├── library_db.sql
│   ├── main.txt
│   └── README.md
├── src/
│   └── main/
│       ├── java/
│       │   ├── com/library/librarymanagementsystem/
│       │   │   ├── controllers/
│       │   │   │   ├── BooksController.java
│       │   │   │   ├── DashboardController.java
│       │   │   │   ├── HomeController.java
│       │   │   │   ├── LoginController.java
│       │   │   │   ├── MembersController.java
│       │   │   │   └── ProfileController.java
│       │   │   ├── database/
│       │   │   │   └── DatabaseConnection.java
│       │   │   ├── models/
│       │   │   │   ├── Book.java
│       │   │   │   ├── Member.java
│       │   │   │   └── User.java
│       │   │   ├── utils/
│       │   │   │   └── SceneManager.java
│       │   │   ├── Launcher.java
│       │   │   └── Main.java
│       │   └── module-info.java
│       └── resources/
│           └── com/library/librarymanagementsystem/
│               ├── books.fxml
│               ├── dashboard.fxml
│               ├── home.fxml
│               ├── login.fxml
│               ├── members.fxml
│               ├── profile.fxml
│               └── styles.css
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

## 13. Database Structure | هيكل قاعدة البيانات

The database contains 4 main tables:

| Table | Description |
|---|---|
| `users` | Stores system users and login credentials |
| `books` | Stores book information |
| `members` | Stores library member information |
| `borrowings` | Stores book borrowing records |

### Tables Summary

```text
users
- id
- username
- password
- full_name
- email
- role
- profile_pic
- created_at

books
- id
- title
- author
- isbn
- category
- quantity
- available
- price
- published_year
- added_at

members
- id
- full_name
- email
- phone
- address
- joined_at

borrowings
- id
- book_id
- member_id
- borrow_date
- return_date
- status
```

---

## 14. User Guide | دليل الاستخدام

### 14.1 Login Page

The Login page is the first screen in the application.

User can:

- Enter username.
- Enter password.
- Click Login button.
- Press Enter from keyboard to login.
- See error messages if fields are empty or credentials are invalid.

---

### 14.2 Home Page

The Home page appears after successful login.

It contains:

- Welcome message.
- Current user full name.
- User role.
- Navigation buttons:
  - Dashboard
  - Books
  - Members
  - Profile
- Logout button.

---

### 14.3 Dashboard Page

The Dashboard page displays important library statistics.

It shows:

- Total number of books.
- Total number of members.
- Number of currently borrowed books.
- Bar chart to visualize statistics.

---

### 14.4 Books Page

The Books page is used to manage library books.

Available operations:

| Operation | Description |
|---|---|
| Add | Add a new book |
| Update | Update selected book |
| Delete | Delete selected book |
| Search | Search by title or author |
| Clear | Clear input fields |

Book fields:

- Title
- Author
- ISBN
- Category
- Quantity
- Price
- Published Year

---

### 14.5 Members Page

The Members page is used to manage library members.

Available operations:

| Operation | Description |
|---|---|
| Add | Add a new member |
| Update | Update selected member |
| Delete | Delete selected member |
| Search | Search by name or email |
| Clear | Clear input fields |

Member fields:

- Full Name
- Email
- Phone
- Address

---

### 14.6 Profile Page

The Profile page allows the logged-in user to update personal information.

User can update:

- Full name.
- Email.
- Password.

The username is displayed but cannot be changed.

---

## 15. Navigation Map | خريطة التنقل

```text
Login
  |
  v
Home
  |
  |----> Dashboard
  |
  |----> Books
  |
  |----> Members
  |
  |----> Profile
  |
  v
Logout -> Login
```

---

## 16. Error Handling | معالجة الأخطاء

The application handles common errors such as:

- Empty login fields.
- Invalid username or password.
- Database connection failure.
- SQL exceptions.
- Missing selected row before update or delete.
- Invalid required fields.
- Duplicate database values such as repeated username, email, or ISBN.

---

## 17. Common Problems and Solutions | المشاكل الشائعة وحلولها

| Problem | Possible Solution |
|---|---|
| Database connection failed | Make sure MySQL is running and database credentials are correct |
| Unknown database `library_db` | Run `docs/library_db.sql` |
| Invalid username or password | Make sure the SQL file was imported successfully |
| JavaFX application does not start | Make sure Java 21 is installed |
| Maven dependencies not found | Run `mvn clean install` or reload Maven project |
| Tables do not exist | Re-import `docs/library_db.sql` |
| MySQL password error | Update `PASSWORD` in `DatabaseConnection.java` |

---

## 18. Submission Instructions | تعليمات التسليم

For Edugate submission, it is recommended to upload the full project as a ZIP file.

Recommended ZIP name:

```text
StudentName_ID_LibraryManagementSystem.zip
```

The ZIP file should include:

```text
LibraryManagementSystem/
├── src/
├── docs/
│   ├── library_db.sql
│   ├── main.txt
│   └── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .mvn/
```

Important files for grading:

| File / Folder | Importance |
|---|---|
| `src/` | Contains Java source code and FXML files |
| `pom.xml` | Contains Maven dependencies and run configuration |
| `docs/library_db.sql` | Required to create the MySQL database |
| `docs/README.md` | Explains setup and usage |
| `.mvn/`, `mvnw`, `mvnw.cmd` | Allow running Maven wrapper |

> Do not submit only an EXE or JAR file. The source code and SQL file are required for reviewing and grading the project.

---

## 19. Notes | ملاحظات

- Make sure MySQL server is running before opening the application.
- Make sure the database script is imported before login.
- If the database password is different on another computer, update `DatabaseConnection.java`.
- The default user is only for testing and demonstration.
- This project is developed for Object-Oriented Programming 2 course requirements.

---

## 20. Final Summary | الخلاصة

This project satisfies the main course requirements:

- JavaFX Desktop Application.
- 6 Scenes.
- MySQL external database.
- JDBC connection.
- Login authentication.
- CRUD operations.
- Dashboard statistics.
- User-friendly interface.
- CSS styling.
- Error handling.
- Maven project structure.

---

Developed using:

```text
Java 21
JavaFX 21.0.2
MySQL
JDBC
Maven
```