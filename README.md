# Timetable Constraint Validator
[Click here to see Presentation Video](https://youtu.be/yRWhX7InblU)

A comprehensive Java desktop application for managing and validating academic timetables. The application provides a user-friendly GUI for managing teachers, rooms, student groups, time slots, and course assignments with built-in constraint validation.

---

## 👥 Group Members

| Name | CMS ID | Section |
|------|--------|---------|
| Kuldeep| 023-25--0056 | Sec: E |
| Sandeep| 023-25-0097 | Sec: E |


---

## Purpose — What problem does the system solve? Who are the users?

This application solves the **complex problem of academic timetable creation and validation**. Here's what it does:

1. **Data Management** - Allows users to:
   - Register and manage faculty members with their departments
   - Define available classrooms with capacity constraints
   - Create student groups and their sizes
   - Set up time slots across the week (days and specific hours)
   - Link all these resources together through course assignments

2. **Schedule Creation** - Users can create course assignments by:
   - Specifying course code and title
   - Assigning a teacher to the course
   - Selecting an available room
   - Assigning a student group
   - Choosing a time slot for the class

3. **Constraint Validation** - The system validates:
   - **Teacher Conflicts** - Ensures no teacher is assigned to multiple classes at the same time
   - **Room Conflicts** - Ensures no room is booked for multiple classes at the same time
   - **Capacity Checks** - Verifies that selected rooms have sufficient capacity for student groups
   - **Resource Availability** - Ensures all resources are properly assigned

4. **Modern User Interface**:
   - Tabbed interface for easy navigation between different data types
   - Color-coded buttons for quick action identification
   - Professional styling with gradient headers and alternating table rows
   - Real-time table views of all entered data
   - Intuitive data entry forms with validation

5. **Data Persistence** - Stores all data in a MySQL database for:
   - Persistent storage across sessions
   - Easy data retrieval and updates
   - Data integrity with relationships between entities

**Real-world Use Case**: A university registrar can use this system to:
- Input all professors, classrooms, student cohorts, and available time slots
- Create course assignments automatically or manually
- Validate that no scheduling conflicts exist
- Generate reports of conflicts (if any)
- Export final timetables for distribution

---

## Core modules — Main packages or groups of classes

The system is organized into the following core modules:

```
com.timetablevalidator/
├── App.java                          # Application entry point
├── model/                            # Data models
│   ├── Teacher.java
│   ├── Room.java
│   ├── StudentGroup.java
│   ├── TimeSlot.java
│   └── Assignment.java
├── services/                         # Business logic & database operations
│   ├── TeacherService.java
│   ├── RoomService.java
│   ├── StudentGroupService.java
│   ├── TimeSlotService.java
│   └── AssignmentService.java
├── ui/                               # GUI components
│   ├── MainFrame.java                # Main application window
│   ├── UIStyles.java                 # Centralized styling utilities
│   ├── TeacherInputPanel.java        # Teacher management tab
│   ├── RoomInputPanel.java           # Room management tab
│   ├── StudentGroupInputPanel.java   # Student group tab
│   ├── TimeSlotInputPanel.java       # Time slot tab
│   └── AssignmentInputPanel.java     # Assignment tab
├── database/                         # Database utilities
│   ├── DBHelper.java                 # Database connection handler
│   └── dummyData/
│       └── dataScript.sql            # Database schema and sample data
├── rules/                            # Constraint validation rules
│   ├── ConstraintRule.java
│   ├── TeacherConflict.java
│   ├── RoomConflict.java
│   └── CapacityCheck.java
└── report/                           # Reporting components
    ├── Reportable.java
    └── ConflictReport.java
```

### Module Descriptions

- **model/** - Entity classes representing core domain objects (Teacher, Room, StudentGroup, TimeSlot, Assignment)
- **services/** - Business logic layer handling CRUD operations and database interactions
- **ui/** - Swing GUI components providing user interface with tabs for data management
- **database/** - Database connection management and schema initialization
- **rules/** - Constraint validation engine for detecting scheduling conflicts
- **report/** - Reporting and conflict analysis components

---

## Key OOP features — Separate entity classes, inheritance or interfaces, private fields with accessors, validation and exceptions

### Encapsulation (Private Fields with Accessors)
All entity classes enforce encapsulation through **private fields and public getter/setter methods**:
- [Teacher.java](src/main/java/com/timetablevalidator/model/Teacher.java) - `id`, `name`, `department` (all private with accessors)
- [Room.java](src/main/java/com/timetablevalidator/model/Room.java) - `id`, `roomNumber`, `capacity`
- [StudentGroup.java](src/main/java/com/timetablevalidator/model/StudentGroup.java) - `id`, `groupName`, `size`
- [TimeSlot.java](src/main/java/com/timetablevalidator/model/TimeSlot.java) - `id`, `day`, `startTime`, `endTime`
- [Assignment.java](src/main/java/com/timetablevalidator/model/Assignment.java) - Composes Teacher, Room, StudentGroup, TimeSlot objects

### Interfaces & Polymorphism
- **ConstraintRule Interface** ([rules/ConstraintRule.java](src/main/java/com/timetablevalidator/rules/ConstraintRule.java)) - Defines contract: `validate(List<Assignment>)` and `getRuleName()`
  - **Multiple Implementations:**
    - [TeacherConflict.java](src/main/java/com/timetablevalidator/rules/TeacherConflict.java) - Validates no teacher is double-booked
    - [RoomConflict.java](src/main/java/com/timetablevalidator/rules/RoomConflict.java) - Validates no room is double-booked
    - [CapacityCheck.java](src/main/java/com/timetablevalidator/rules/CapacityCheck.java) - Validates room capacity
- **Reportable Interface** ([report/Reportable.java](src/main/java/com/timetablevalidator/report/Reportable.java)) - Extensible reporting system

### Service Layer Pattern
Separates business logic from UI (prevents tight coupling):
- [TeacherService.java](src/main/java/com/timetablevalidator/services/TeacherService.java) - `addTeacher()`, `getAllTeachers()`, `deleteTeacher()`
- [RoomService.java](src/main/java/com/timetablevalidator/services/RoomService.java) - Room CRUD operations
- [StudentGroupService.java](src/main/java/com/timetablevalidator/services/StudentGroupService.java) - Student group operations
- [TimeSlotService.java](src/main/java/com/timetablevalidator/services/TimeSlotService.java) - Time slot management
- [AssignmentService.java](src/main/java/com/timetablevalidator/services/AssignmentService.java) - Assignment creation & retrieval

### Composition Example
[Assignment.java](src/main/java/com/timetablevalidator/model/Assignment.java) demonstrates **composition** - it links multiple objects representing real-world relationships:
```java
public class Assignment {
    private int id;
    private String courseCode;
    private String courseTitle;
    private Teacher teacher;        // Composed object
    private Room room;              // Composed object
    private StudentGroup studentGroup;  // Composed object
    private TimeSlot timeSlot;      // Composed object
}
```

## 🛡️ Input Validation & Exception Handling

### Form Input Validation
All UI input panels validate user data before database operations:
- [TeacherInputPanel.java](src/main/java/com/timetablevalidator/ui/TeacherInputPanel.java#L104) - Checks `name.isEmpty() || department.isEmpty()`
- [RoomInputPanel.java](src/main/java/com/timetablevalidator/ui/RoomInputPanel.java#L99) - Validates room number and capacity inputs
- [StudentGroupInputPanel.java](src/main/java/com/timetablevalidator/ui/StudentGroupInputPanel.java#L99) - Verifies group name and size
- [AssignmentInputPanel.java](src/main/java/com/timetablevalidator/ui/AssignmentInputPanel.java#L151) - Checks all dropdowns and text fields are populated

**Example validation pattern:**
```java
String name = nameField.getText().trim();          // Remove whitespace
String department = departmentField.getText().trim();

if (name.isEmpty() || department.isEmpty()) {      // Validate not empty
    JOptionPane.showMessageDialog(null, "Please fill all fields");
    return;
}
```

### Database Exception Handling
All database operations wrapped in try-catch blocks with proper error reporting:
- [TeacherService.java](src/main/java/com/timetablevalidator/services/TeacherService.java) - SQLException handling in all CRUD methods
- [DBHelper.java](src/main/java/com/timetablevalidator/database/DBHelper.java) - ClassNotFoundException for JDBC driver loading
```java
try (Connection conn = DBHelper.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, name);
    pstmt.executeUpdate();
    return true;
} catch (SQLException e) {
    e.printStackTrace();
    return false;
}
```

### Null Checks in Validation Rules
[ConstraintRule](src/main/java/com/timetablevalidator/rules/ConstraintRule.java) implementations validate object references before accessing properties.

---

## How to run — JDK version, how to compile/run main, and how to load SQL and config

### Prerequisites

- **Java Development Kit (JDK) 17 or higher**
- **MySQL Server 5.7 or higher**
- **MySQL JDBC Driver** (mysql-connector-java-8.x.x.jar)
- **Windows, Linux, or macOS**

### Installation & Setup

### 1. Clone or Download the Project
#### a. Clone
```bash
git clone https://github.com/kuldeepiba24-png/TimeTableValidator.git
```
#### b. Opne Project
```bash
cd d:\Java\TimeTableValidator
```

### 2. Create Database and Tables
```bash
mysql -u root -p < src/main/java/com/timetablevalidator/database/dummyData/dataScript.sql
```

Or manually run the SQL script:
1. Open MySQL Workbench
2. Create a new query tab
3. Copy contents of `dataScript.sql`
4. Execute

### 3. Add MySQL JDBC Driver
Download from: https://dev.mysql.com/downloads/connector/j/

Place the JAR file in your classpath or add to project libraries.

### 4. Update Database Configuration (if needed)
Edit `src/main/java/com/timetablevalidator/database/DBHelper.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/timetable_validator";
private static final String USER = "root";
private static final String PASSWORD = "";
```

## 🔨 Compilation

### Option 1: Quick Compile
```bash
cd "d:\Java\timetablevalidator" && javac -d target/classes -sourcepath src/main/java \
  src/main/java/com/timetablevalidator/ui/*.java \
  src/main/java/com/timetablevalidator/App.java 2>&1 && echo "Compilation successful"
```

### Option 2: Full Compilation
```bash
javac -d target/classes -sourcepath src/main/java \
  src/main/java/com/timetablevalidator/App.java \
  src/main/java/com/timetablevalidator/ui/*.java \
  src/main/java/com/timetablevalidator/model/*.java \
  src/main/java/com/timetablevalidator/services/*.java \
  src/main/java/com/timetablevalidator/rules/*.java \
  src/main/java/com/timetablevalidator/report/*.java \
  src/main/java/com/timetablevalidator/database/*.java
```

## ▶️ Running the Application

### Run Command:
```bash
java -cp target/classes com.timetablevalidator.App
```

Or with background execution:
```bash
java -cp target/classes com.timetablevalidator.App 2>&1 &
```

## 📖 Usage Guide

### Teachers Tab
1. Enter teacher name and department
2. Click "Add Teacher" to save
3. View all teachers in the table below
4. Select a teacher and click "Delete Selected" to remove

### Rooms Tab
1. Enter room number and capacity
2. Click "Add Room" to save
3. Manage classroom resources

### Student Groups Tab
1. Enter group name and size
2. Click "Add Group" to save
3. Organize student cohorts

### Time Slots Tab
1. Select day from dropdown
2. Enter start and end times (HH:MM:SS format)
3. Click "Add Time Slot" to save

### Assignments Tab
1. Enter course code and title
2. Select teacher from dropdown
3. Select room from dropdown
4. Select student group from dropdown
5. Select time slot from dropdown
6. Click "Add Assignment" to create the assignment

## 🗄️ Database Schema

### Tables
- **teachers** - Teacher information (id, name, department)
- **rooms** - Room details (id, room_number, capacity)
- **student_groups** - Student group information (id, group_name, size)
- **time_slots** - Class schedules (id, day, start_time, end_time)
- **assignments** - Course assignments linking all entities

### Sample Data
The `dataScript.sql` includes:
- 8 sample teachers
- 10 sample rooms
- 8 sample student groups
- 19 sample time slots
- 16 sample assignments

## ⚙️ Configuration

### Database Connection
File: `src/main/java/com/timetablevalidator/database/DBHelper.java`

### UI Styles
File: `src/main/java/com/timetablevalidator/ui/UIStyles.java`

Colors, fonts, and styling constants can be modified here.

## 🐛 Troubleshooting

### "No suitable driver found for jdbc:mysql"
- Download MySQL JDBC driver
- Add to classpath or project libraries
- Ensure MySQL server is running

### Database Connection Errors
- Verify MySQL server is running
- Check database credentials in DBHelper.java
- Ensure database and tables are created using dataScript.sql

### Compilation Errors
- Ensure JDK 17+ is installed
- Check all source files are present
- Verify correct paths in compile command

## 📝 Sample Workflow

1. **Start the application** - Launch MainFrame
2. **Add Teachers** - Use Teachers tab to add faculty
3. **Add Rooms** - Define available classrooms
4. **Add Student Groups** - Create student cohorts
5. **Add Time Slots** - Define class periods
6. **Create Assignments** - Link courses to resources
7. **Validate Timetable** - Check for conflicts (future feature)

---

## Academic Integrity — Code must be your group's work. Cite any tutorials or snippets you adapt.

### Original Work Statement
This project is original work developed by **[TIME TABLE VALIDATOR]**. All core logic, UI design, and database architecture were implemented independently by the group members.

### Design Patterns & Inspiration
- **Service Layer Pattern** - Standard architectural pattern for separating business logic from presentation (applied independently)
- **Strategy Pattern** - Used in ConstraintRule interface for flexible validation rules (standard pattern)
- **JDBC Database Connectivity** - Standard Java API for database operations (Java official documentation)

### External References Used
- **Java Swing Documentation** - Official Java GUI framework used for UI development
- **MySQL JDBC Driver** - Standard connector for database integration
- **GridBagLayout** - Standard Swing layout manager (Java documentation)

### Code Explanation Capability
All group members understand and can explain:
- **ConstraintRule interface design** - Why use interfaces for validation rules (Open/Closed Principle)
- **Service layer separation** - Benefits of separating business logic from UI (testability, maintainability)
- **Composition in Assignment** - How Assignment class links multiple model objects
- **Exception handling patterns** - Why try-catch blocks are used for database operations
- **Validation strategy** - Input validation before processing and database error handling

## 🔄 Future Enhancements

- [ ] Advanced constraint validation
- [ ] Conflict reporting system
- [ ] Export timetable to PDF/Excel
- [ ] Graphical timetable view
- [ ] Import data from CSV
- [ ] User authentication
- [ ] Data backup/restore functionality

## 📄 License

Open source project for educational purposes.

## 👨‍💻 Development

Built with Java Swing for cross-platform desktop application support.

---

**Last Updated:** May 2026
**Version:** 1.0.0
