# Timetable Validator - Running Guide

## Step-by-Step Instructions

### Prerequisites
- **Java 21 or higher** (Java 26 recommended)
- The project is located at: `d:\Projects\Second Semester\timetablevalidator`

---

## Method 1: Quick Start (Recommended)

### Step 1: Open PowerShell
```powershell
# Navigate to project directory
cd "d:\Projects\Second Semester\timetablevalidator"
```

### Step 2: Compile the Project
```powershell
# Compile all Java source files to target/classes
$files = Get-ChildItem -Path src/main/java -Filter *.java -Recurse | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d target/classes $files
```

### Step 3: Run the Application
```powershell
# Run the application with proper classpath
java -cp "target/classes;lib/*" com.timetablevalidator.App
```

The GUI window should appear automatically.

---

## Method 2: Using Maven (If Maven is installed)

```powershell
# Navigate to project
cd "d:\Projects\Second Semester\timetablevalidator"

# Clean and compile
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.timetablevalidator.App"
```

---

## How to Use the Application

### Overview
The application has 6 tabs:
1. **📊 Conflict Report** (Main tab - for validation)
2. **👨‍🏫 Teachers**
3. **🏫 Rooms**
4. **👥 Student Groups**
5. **⏰ Time Slots**
6. **📋 Assignments**

---

### Step 1: Add Teachers
1. Go to **"👨‍🏫 Teachers"** tab
2. Enter teacher details:
   - **Name**: e.g., "Dr. Smith"
   - **Department**: e.g., "Computer Science"
3. Click **"Add Teacher"** button
4. Click **"Refresh"** to see the list
5. Repeat for multiple teachers

**Example:**
```
Teacher 1: Dr. Smith | Computer Science
Teacher 2: Dr. Johnson | Mathematics
Teacher 3: Prof. Brown | Physics
```

---

### Step 2: Add Rooms
1. Go to **"🏫 Rooms"** tab
2. Enter room details:
   - **Room Number**: e.g., "A101", "Lab-01"
   - **Capacity**: e.g., "30", "50"
3. Click **"Add Room"** button
4. Click **"Refresh"** to see the list

**Example:**
```
Room: A101 | Capacity: 30
Room: A102 | Capacity: 25
Room: Lab-01 | Capacity: 40
```

---

### Step 3: Add Student Groups
1. Go to **"👥 Student Groups"** tab
2. Enter group details:
   - **Group Name**: e.g., "CS-1A", "CS-1B"
   - **Size**: e.g., "25", "28"
3. Click **"Add Student Group"** button
4. Click **"Refresh"** to see the list

**Example:**
```
Group: CS-1A | Size: 25
Group: CS-1B | Size: 28
Group: MATH-1A | Size: 30
```

---

### Step 4: Add Time Slots
1. Go to **"⏰ Time Slots"** tab
2. Enter time slot details:
   - **Day**: e.g., "Monday", "Tuesday", "Friday"
   - **Start Time**: e.g., "09:00", "10:30", "14:00"
   - **End Time**: e.g., "10:00", "11:30", "15:00"
3. Click **"Add Time Slot"** button
4. Click **"Refresh"** to see the list

**Example:**
```
Monday 09:00-10:00
Monday 10:30-11:30
Tuesday 14:00-15:00
```

---

### Step 5: Add Assignments (Auto Room Allocation)
1. Go to **"📋 Assignments"** tab
2. Fill in the assignment details:
   - **Course Code**: e.g., "CS101", "MATH201"
   - **Subject/Title**: e.g., "Data Structures", "Calculus"
   - **Teacher**: Select from dropdown
   - **Student Group**: Select from dropdown
   - **Time Slot**: Select from dropdown
3. Click **"Add Assignment"** button
   - Room will be **automatically assigned** based on availability and capacity
4. Click **"Refresh"** to see the list

**Important Notes:**
- Room is auto-allocated if available for the selected time slot
- If no room is available, you'll see an error message
- Teacher must be available (not already assigned at that time)

---

### Step 6: Validate & Detect Conflicts
1. Go to **"📊 Conflict Report"** tab (first tab)
2. Click **"Run Validation"** button
3. The system will check for:
   - **Room Conflicts**: Two classes in same room at same time
   - **Teacher Conflicts**: Teacher assigned to multiple classes at same time
   - **Capacity Conflicts**: Student group size exceeds room capacity

**Expected Results:**
- ✅ **Green status**: "No conflicts detected!" - Timetable is valid
- ⚠️ **Orange status**: Shows number of conflicts found
- 🔴 **Red status**: Database or validation error

**To Export Report:**
- Click **"Export Report"** button to see detailed conflict list

---

## Example: Complete Workflow

### Create a Sample Timetable

**Step 1 - Teachers (Add 2 teachers)**
```
Name: Dr. Ahmed
Department: Computer Science

Name: Dr. Fatima
Department: Mathematics
```

**Step 2 - Rooms (Add 3 rooms)**
```
Room Number: A101
Capacity: 30

Room Number: A102
Capacity: 25

Room Number: A103
Capacity: 35
```

**Step 3 - Student Groups (Add 2 groups)**
```
Group Name: CS-Batch1
Size: 28

Group Name: MATH-Batch1
Size: 30
```

**Step 4 - Time Slots (Add 2 slots)**
```
Day: Monday
Start Time: 09:00
End Time: 10:00

Day: Monday
Start Time: 10:30
End Time: 11:30
```

**Step 5 - Assignments (Add 2 assignments)**
```
Course Code: CS101
Subject: Data Structures
Teacher: Dr. Ahmed
Student Group: CS-Batch1
Time Slot: Monday 09:00-10:00

Course Code: MATH101
Subject: Calculus
Teacher: Dr. Fatima
Student Group: MATH-Batch1
Time Slot: Monday 10:30-11:30
```

**Step 6 - Validate**
- Click "📊 Conflict Report" tab
- Click "Run Validation" button
- Expected: ✅ "No conflicts detected!"

---

## Troubleshooting

### Issue: "Could not find or load main class com.timetablevalidator.App"
**Solution:** Ensure the project is compiled:
```powershell
$files = Get-ChildItem -Path src/main/java -Filter *.java -Recurse | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d target/classes $files
```

### Issue: "No available room" error
**Solution:** 
- Ensure rooms are added with sufficient capacity
- Check if a room is already booked at that time slot
- Try a different time slot

### Issue: "Teacher is not available" error
**Solution:**
- The teacher is already assigned to another class at that time
- Choose a different teacher or time slot

### Issue: Application won't start
**Solution:**
- Verify Java is installed: `java -version`
- Check classpath includes all libraries: `java -cp "target/classes;lib/*" com.timetablevalidator.App`

---

## What Gets Validated?

The conflict detection checks three main constraints:

### 1. **Room Conflict Detection**
- Ensures no two classes are in the same room at the same time
- Example conflict: "Room A101 is double-booked at Monday 09:00-10:00"

### 2. **Teacher Conflict Detection**
- Ensures no teacher teaches multiple classes at the same time
- Example conflict: "Teacher Dr. Smith is assigned to multiple classes at Monday 09:00-10:00"

### 3. **Room Capacity Check**
- Ensures the student group fits within the room capacity
- Example conflict: "Room A101 capacity exceeded for CS-Batch1 (28 students > 25 capacity)"

---

## Tips & Best Practices

✅ **Do:**
- Start by adding all teachers
- Add all rooms with correct capacities
- Add student groups with accurate sizes
- Add time slots for the week
- Then create assignments and validate

✅ **Best Practice Order:**
1. Teachers → Rooms → Student Groups → Time Slots → Assignments → Validate

✅ **Common Issues to Avoid:**
- Don't add student groups larger than all available rooms
- Ensure teachers are added before creating assignments
- Add at least one time slot before creating assignments
- Use consistent naming conventions (e.g., "Monday", "MONDAY")

---

## Database

The application uses SQLite database (automatically created at first run):
- Located in the project directory
- Stores all teachers, rooms, groups, time slots, and assignments
- Data persists between sessions

---

## Summary Command

One-liner to compile and run:
```powershell
cd "d:\Projects\Second Semester\timetablevalidator"; ($files = Get-ChildItem -Path src/main/java -Filter *.java -Recurse | Select-Object -ExpandProperty FullName; javac -encoding UTF-8 -d target/classes $files); java -cp "target/classes;lib/*" com.timetablevalidator.App
```

---

Enjoy using the Timetable Constraint Validator! 🎓
