# Testing Checklist - User Management System

Use this checklist to verify everything is working before submission.

## ✅ Pre-Deployment Checklist

### Local Development Environment
- [ ] Node.js installed (v14+)
- [ ] MongoDB installed and running
- [ ] Git installed
- [ ] Code editor (VS Code/IntelliJ/Eclipse)

### Application Setup
- [ ] Backend dependencies installed (`npm install`)
- [ ] `.env` file created with correct MongoDB URI
- [ ] Application starts without errors (`npm run dev`)
- [ ] Application accessible at http://localhost:3000
- [ ] MongoDB connection successful

### Frontend Functionality
- [ ] Homepage loads correctly
- [ ] All CSS styles applied
- [ ] No console errors in browser

## ✅ Application Testing Checklist

### Create User (C)
- [ ] Click "Add User" form
- [ ] Fill all fields with valid data:
  - Name: John Doe
  - Email: john@example.com
  - Age: 25
  - Address: 123 Main Street
- [ ] Submit form
- [ ] Success message appears
- [ ] User appears in table
- [ ] Data saved in MongoDB

### Read Users (R)
- [ ] All users displayed in table
- [ ] Table shows: Name, Email, Age, Address, Created At, Actions
- [ ] Data matches MongoDB records
- [ ] Refresh button reloads data
- [ ] "No users" message when table empty

### Update User (U)
- [ ] Click "Edit" button on a user
- [ ] Form fills with user data
- [ ] Submit button changes to "Update User"
- [ ] Cancel button appears
- [ ] Modify data and submit
- [ ] Success message appears
- [ ] Updated data appears in table
- [ ] Cancel button resets form

### Delete User (D)
- [ ] Click "Delete" button on a user
- [ ] Confirmation dialog appears
- [ ] Click "OK" to confirm
- [ ] Success message appears
- [ ] User removed from table
- [ ] User deleted from MongoDB

### Search Functionality
- [ ] Enter search term in search box
- [ ] Click "Search" button
- [ ] Results filtered correctly
- [ ] Search by name works
- [ ] Search by email works
- [ ] Search by address works
- [ ] "Clear" button resets search
- [ ] Empty search shows all users

### Form Validation
- [ ] Submit empty form shows validation errors
- [ ] Invalid email format rejected
- [ ] Negative age rejected
- [ ] Duplicate email shows error message

### UI/UX
- [ ] Responsive design works on mobile
- [ ] All buttons clickable
- [ ] Forms are user-friendly
- [ ] Loading spinner appears during operations
- [ ] Error messages are clear
- [ ] Success messages are visible

## ✅ Selenium Tests Checklist

### Setup
- [ ] Java JDK 11+ installed
- [ ] Maven installed
- [ ] `pom.xml` in selenium-tests folder
- [ ] Test file in correct directory structure
- [ ] Application running on test URL

### Test Execution
- [ ] Navigate to selenium-tests folder
- [ ] Run `mvn clean compile` - Success
- [ ] Run `mvn test` - All tests pass
- [ ] 12/12 tests passed
- [ ] 0 failures
- [ ] Test reports generated

### Individual Test Cases
- [ ] TC01: Page loads - PASS
- [ ] TC02: Add user - PASS
- [ ] TC03: User in table - PASS
- [ ] TC04: Edit user - PASS
- [ ] TC05: Delete user - PASS
- [ ] TC06: Search by name - PASS
- [ ] TC07: Search by email - PASS
- [ ] TC08: Form validation - PASS
- [ ] TC09: Duplicate email - PASS
- [ ] TC10: Refresh button - PASS
- [ ] TC11: Cancel edit - PASS
- [ ] TC12: Clear search - PASS

### Test Reports
- [ ] XML reports in target/surefire-reports/
- [ ] HTML report generated
- [ ] Reports viewable in browser
- [ ] All test details visible

## ✅ AWS EC2 Deployment Checklist

### EC2 Instance
- [ ] EC2 instance launched (t2.micro)
- [ ] Ubuntu 22.04 LTS AMI
- [ ] Public IP assigned
- [ ] Key pair downloaded and saved
- [ ] Can SSH into instance

### Security Group
- [ ] SSH (22) - Your IP
- [ ] HTTP (80) - 0.0.0.0/0
- [ ] Custom (3000) - 0.0.0.0/0
- [ ] Custom (8080) - 0.0.0.0/0

### Software Installation
- [ ] System updated (`sudo apt update`)
- [ ] Node.js installed
- [ ] MongoDB installed
- [ ] Java 11 installed
- [ ] Maven installed
- [ ] Docker installed
- [ ] Jenkins installed
- [ ] PM2 installed globally

### Application Deployment
- [ ] Repository cloned
- [ ] Dependencies installed
- [ ] .env file created
- [ ] PM2 running application
- [ ] Application accessible via EC2 public IP
- [ ] MongoDB connected
- [ ] All CRUD operations work on EC2

### MongoDB on EC2
- [ ] MongoDB service running
- [ ] MongoDB enabled on boot
- [ ] Can connect locally
- [ ] Database created
- [ ] Collections visible

## ✅ Jenkins Setup Checklist

### Jenkins Installation
- [ ] Jenkins running on EC2
- [ ] Accessible at http://EC2_IP:8080
- [ ] Initial admin password retrieved
- [ ] Admin user created
- [ ] Suggested plugins installed

### Additional Plugins
- [ ] Docker Pipeline plugin
- [ ] Email Extension plugin
- [ ] GitHub plugin
- [ ] JUnit plugin
- [ ] All plugins activated

### Jenkins Configuration
- [ ] System configured
- [ ] Email SMTP settings configured
- [ ] Test email sent successfully
- [ ] Global credentials added (GitHub)

### Pipeline Job
- [ ] New pipeline job created
- [ ] Job name: user-management-pipeline
- [ ] GitHub project URL configured
- [ ] SCM: Git
- [ ] Repository URL correct
- [ ] Credentials added
- [ ] Branch specified (main/master)
- [ ] Jenkinsfile path: Jenkinsfile

### Docker in Jenkins
- [ ] Jenkins user in docker group
- [ ] Jenkins can run docker commands
- [ ] Docker image pulls successfully
- [ ] Tests run in container

## ✅ GitHub Integration Checklist

### Repository Setup
- [ ] Repository created on GitHub
- [ ] Code pushed to repository
- [ ] README.md visible
- [ ] All files committed
- [ ] .gitignore working (no .env in repo)

### Webhook Configuration
- [ ] Webhook added to repository
- [ ] Payload URL: http://EC2_IP:8080/github-webhook/
- [ ] Content type: application/json
- [ ] SSL verification (if applicable)
- [ ] "Just the push event" selected
- [ ] Webhook active
- [ ] Recent deliveries show success

### Collaborator
- [ ] Instructor added as collaborator
- [ ] Invitation sent
- [ ] Correct GitHub username

## ✅ Pipeline Testing Checklist

### Manual Trigger
- [ ] Build now in Jenkins
- [ ] Pipeline starts
- [ ] Checkout stage completes
- [ ] Build stage completes
- [ ] Test stage completes
- [ ] All stages green
- [ ] Build successful

### Automatic Trigger
- [ ] Make code change
- [ ] Commit and push to GitHub
- [ ] Jenkins automatically triggered
- [ ] Pipeline executes
- [ ] Email notification received

### Pipeline Stages
- [ ] Checkout: Code pulled from GitHub
- [ ] Build: Dependencies installed
- [ ] Test: Selenium tests run in Docker
- [ ] Deploy: (Optional) Deployment executed
- [ ] Post: Cleanup and notifications

### Test Results in Jenkins
- [ ] Test results published
- [ ] JUnit reports visible
- [ ] Test trend graph shown
- [ ] Download test reports

## ✅ Email Notifications Checklist

### Email Configuration
- [ ] SMTP server configured
- [ ] Port correct (587 for Gmail)
- [ ] Credentials saved
- [ ] TLS enabled
- [ ] Test email sent

### Success Email
- [ ] Subject line correct
- [ ] Job name and build number included
- [ ] Build URL included
- [ ] Status shown as SUCCESS
- [ ] Triggered by author email shown
- [ ] Test reports attached

### Failure Email
- [ ] Subject line indicates FAILURE
- [ ] Console output link included
- [ ] Error details provided
- [ ] Sent to correct recipient

## ✅ Documentation Checklist

### Screenshots Captured
- [ ] Application homepage
- [ ] Add user form
- [ ] Users table with data
- [ ] Edit user screen
- [ ] Delete confirmation
- [ ] Search results
- [ ] MongoDB Compass view
- [ ] Selenium tests running
- [ ] Maven test results
- [ ] HTML test report
- [ ] Jenkins dashboard
- [ ] Jenkins pipeline view
- [ ] Jenkins console output
- [ ] Build history
- [ ] Email notification (success)
- [ ] Email notification (failure)
- [ ] GitHub repository
- [ ] GitHub webhook settings
- [ ] EC2 instance details
- [ ] Security group rules
- [ ] PM2 status

### Report Sections
- [ ] Introduction written
- [ ] Technology stack listed
- [ ] Application features described
- [ ] Database schema documented
- [ ] API endpoints documented
- [ ] Selenium tests explained (all 12)
- [ ] Jenkins pipeline explained
- [ ] Jenkinsfile included
- [ ] AWS deployment steps documented
- [ ] Screenshots inserted with captions
- [ ] Challenges and solutions described
- [ ] Conclusion written
- [ ] References added

### Code Files
- [ ] Jenkinsfile in repository
- [ ] All test files in repository
- [ ] pom.xml in repository
- [ ] README.md complete
- [ ] .env.example in repository
- [ ] Frontend files in repository
- [ ] Backend files in repository

## ✅ Submission Checklist

### Google Form
- [ ] Form URL accessed: https://forms.gle/4fnuUPhXptQnDPUK6
- [ ] Deployment URL provided
- [ ] GitHub application code URL provided
- [ ] GitHub test code URL provided
- [ ] Form submitted
- [ ] Response visible in spreadsheet

### Response Sheet
- [ ] Spreadsheet accessed
- [ ] Your response visible
- [ ] All URLs correct
- [ ] URLs are clickable

### Final Verification
- [ ] Deployment URL accessible (test in incognito)
- [ ] Application works on deployment URL
- [ ] GitHub repository public/accessible
- [ ] Instructor added as collaborator
- [ ] Pipeline can be triggered by push
- [ ] Email notifications working

### Report Submission
- [ ] Report in PDF format
- [ ] All screenshots included
- [ ] Screenshots clearly visible
- [ ] Captions added to all screenshots
- [ ] Code snippets properly formatted
- [ ] Jenkinsfile included in appendix
- [ ] Student name and roll number on cover page
- [ ] Page numbers added
- [ ] Table of contents accurate
- [ ] File named correctly: RollNumber_Assignment3.pdf

## ✅ Final Testing Before Submission

### Complete Workflow Test
1. [ ] Make a small change in code (e.g., update README)
2. [ ] Commit changes: `git commit -m "Test pipeline"`
3. [ ] Push to GitHub: `git push origin main`
4. [ ] Verify GitHub shows latest commit
5. [ ] Wait for Jenkins to trigger automatically
6. [ ] Watch Jenkins pipeline execute
7. [ ] Verify all stages complete successfully
8. [ ] Check email for notification
9. [ ] Verify email contains test results
10. [ ] Verify test reports in Jenkins

### Instructor Perspective Test
- [ ] Can access deployment URL
- [ ] Can clone repository
- [ ] Can view Jenkinsfile
- [ ] Can view test code
- [ ] Can trigger pipeline (as collaborator)
- [ ] Will receive email notification

## ✅ Troubleshooting Done

### Common Issues Checked
- [ ] Port 3000 not in use by other process
- [ ] MongoDB service running
- [ ] Firewall allows required ports
- [ ] No CORS errors in browser console
- [ ] Environment variables loaded
- [ ] All dependencies installed
- [ ] Correct Node.js version
- [ ] Enough disk space on EC2
- [ ] Security groups properly configured
- [ ] Jenkins has correct permissions

## 🎯 Score Estimation

Based on checklist completion:

- **Part I - Selenium Tests (4 marks)**:
  - [ ] 10+ test cases implemented
  - [ ] Tests run successfully
  - [ ] Headless Chrome used
  - **Estimated**: ___ / 4

- **Part II - Jenkins Pipeline (4 marks)**:
  - [ ] Pipeline triggered by GitHub push
  - [ ] Test stage executes in Docker
  - [ ] Email notifications working
  - **Estimated**: ___ / 4

- **Report (2 marks)**:
  - [ ] Well-organized report
  - [ ] All steps documented
  - [ ] Screenshots included
  - [ ] Jenkinsfile included
  - **Estimated**: ___ / 2

**Total Estimated Score**: ___ / 10

## 📝 Notes

**Issues Encountered**:
_______________________________________
_______________________________________
_______________________________________

**Solutions Applied**:
_______________________________________
_______________________________________
_______________________________________

**Additional Comments**:
_______________________________________
_______________________________________
_______________________________________

---

**Date Completed**: _______________
**Ready for Submission**: [ ] Yes [ ] No
**Submitted On**: _______________

---

**Good luck with your assignment!** 🚀
