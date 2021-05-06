# Project directory

## How to build and run this project

### 1. Prerequisites:
  1. Internet connection
  2. Java 8 runtime environment (the java version we tested on)
  3. Source code
  4. Gradle
### 2. Download

Clone the repo onto your computer


### 3. Build and run
  1. Go into the directory "Project/" in your terminal
  2. run "Gradle" to run the program
  3. run "Gradle jar" to only build a runnable jar

### 4. Import into IDE
  1. Open your Java IDE (Such as Eclipse or IntelliJ)
  2. Import Project
  3. Gradle Project
  4. Use "Project/" as the root folder

### 5. What you can do with the project?

  * Add student
  * Add Course
  * Add Assignment to Course
    * Assignments can have a limited number of attempts or infinite number of attempts
    * Assignments can be set to visibile or not to students
    * Assignments can be have their visibility be set automatically be open and close times
  * Add Question to Assignment
    * Questions can be multiple choice or text field
    * Questions can be auto marked if desired
  * Register as a student if a professor has added them to a course
  * Complete assignments as a student
  * See assignment marks
  * Export Assignment to a PDF
  * See summary of class average

## New features in current build
- Users can register and login
  - Professors have to be added to DB before they can register
  - Students need to be added by a professor to a course first
- Questions can be a text field question instead of multiple choice
- Questions may not be specified to not auto mark
- Export Assignment to a PDF
- Professors can see a summary of their class' work on an assignment
