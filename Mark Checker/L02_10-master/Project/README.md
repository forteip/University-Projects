# Project directory

## How to run this project

### 1. Prerequisites:
```
a/ Internet connection (the project uses a remote cloud SQL database server)
b/ Java 8 runtime environment (the java version we tested on)
c/ Project Release (see below how to get it)
```
### 2. Download Project Release

To run the latest Project, you will need to go to the [Release](https://github.com/CSCC01F17/L02_10/releases) tab, and download the latest zip of the project


### 3. Start-up your command line interface
```
a/ Unzip the downloaded Project Release, and point your command line interface to the directory of unzip Project Release
b/ To run the Admin view of the app, type "java -jar Admin.jar". If you want to run the user version of the app, type "java -jar Student.jar"

```

### 4. What you can do with the project?

Please refer to "readme.txt" in the Project Release for what you can do with the program.

## New features
- **Added the assignment manager for Admins**
    - Can get assignments from certain courses. Double clicking the results will allow aspects of the assignment to be changed
    - Add assignments to a course
    - Insert new questions into database and can allocate questions to different assignments
- Add the marks of students to the database for certain assignments
- **Add a Student view to the app!**
- Students can view their marks in courses they are enrolled in
- Students can see assignments that are open to them.
- Students can double click on their open Assignments to answer the questions within. They can also save their progress before submission
