# Selenium Test Setup Guide

This document provides step-by-step instructions for setting up and running Selenium tests for the User Management System.

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome browser (for local testing)
- ChromeDriver (or use WebDriverManager for automatic setup)

## Project Structure

```
selenium-tests/
├── pom.xml                           # Maven configuration
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── usermanagement/
│   └── test/
│       └── java/
│           └── com/
│               └── usermanagement/
│                   └── tests/
│                       └── UserManagementSeleniumTests.java
└── README.md
```

## Installation Steps

### 1. Install Java

**Windows:**
```powershell
# Download from: https://www.oracle.com/java/technologies/downloads/
# Or using Chocolatey:
choco install openjdk11
```

Verify installation:
```powershell
java -version
```

### 2. Install Maven

**Windows:**
```powershell
# Download from: https://maven.apache.org/download.cgi
# Or using Chocolatey:
choco install maven
```

Verify installation:
```powershell
mvn -version
```

### 3. Setup ChromeDriver (Optional if using WebDriverManager)

**Option A: Manual Download**
1. Download ChromeDriver from: https://chromedriver.chromium.org/downloads
2. Match the version with your Chrome browser
3. Add to PATH or specify in code

**Option B: Use WebDriverManager (Recommended)**
WebDriverManager is already included in `pom.xml`. It will automatically download and setup the correct ChromeDriver version.

## Running Tests Locally

### Method 1: Using Maven Command Line

```bash
# Navigate to selenium-tests directory
cd selenium-tests

# Clean and compile
mvn clean compile

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=UserManagementSeleniumTests#TC01_verifyPageLoadsSuccessfully

# Run tests with verbose output
mvn test -X
```

### Method 2: Using IDE (IntelliJ IDEA / Eclipse)

1. **Import Project:**
   - Open IDE
   - File → Open → Select `selenium-tests` folder
   - IDE will detect Maven project and import dependencies

2. **Run Tests:**
   - Right-click on test class
   - Select "Run UserManagementSeleniumTests"

3. **Run Single Test:**
   - Right-click on specific test method
   - Select "Run TC01_verifyPageLoadsSuccessfully"

## Test Configuration

### Update Base URL

Before running tests, update the `baseUrl` in `UserManagementSeleniumTests.java`:

```java
private String baseUrl = "http://localhost:3000"; // For local testing
// or
private String baseUrl = "http://your-ec2-ip:3000"; // For deployed app
```

### Headless vs. GUI Mode

**Headless Mode (for Jenkins/CI):**
```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless");
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
```

**GUI Mode (for local debugging):**
```java
ChromeOptions options = new ChromeOptions();
// Comment out or remove the headless argument
// options.addArguments("--headless");
```

## Test Cases Overview

| Test ID | Test Name | Description |
|---------|-----------|-------------|
| TC01 | verifyPageLoadsSuccessfully | Verify homepage loads with correct title |
| TC02 | addNewUserWithValidData | Test adding a new user with valid data |
| TC03 | verifyUserAppearsInTable | Verify added user appears in the table |
| TC04 | editExistingUser | Test editing user functionality |
| TC05 | deleteUser | Test deleting a user |
| TC06 | searchUserByName | Test search functionality by name |
| TC07 | searchUserByEmail | Test search functionality by email |
| TC08 | verifyFormValidation | Test form validation for empty fields |
| TC09 | verifyDuplicateEmailValidation | Test duplicate email prevention |
| TC10 | verifyRefreshButton | Test refresh button functionality |
| TC11 | verifyCancelButtonInEditMode | Test cancel button in edit mode |
| TC12 | verifyClearSearchFunctionality | Test clear search button |

## Viewing Test Reports

After running tests, Maven generates reports:

### Surefire Reports Location:
```
selenium-tests/target/surefire-reports/
```

### View HTML Report:
```bash
# Generate site with reports
mvn site

# Open in browser
start target/site/surefire-report.html
```

### Test Results Files:
- `TEST-*.xml` - JUnit XML format (for Jenkins)
- `*.txt` - Text format results
- HTML reports in `target/site/`

## Running Tests in Docker (Headless Chrome)

### Using Pre-built Image:

```bash
docker run --rm -v ${PWD}/selenium-tests:/app \
  -w /app \
  markhobson/maven-chrome:latest \
  mvn clean test
```

### Using Custom Dockerfile:

Create `Dockerfile.tests`:

```dockerfile
FROM markhobson/maven-chrome:latest

WORKDIR /app

COPY selenium-tests/pom.xml .
RUN mvn dependency:go-offline

COPY selenium-tests/ .

CMD ["mvn", "clean", "test"]
```

Build and run:
```bash
docker build -f Dockerfile.tests -t selenium-tests .
docker run --rm selenium-tests
```

## Jenkins Integration

### Jenkins Pipeline Configuration

1. **Install Required Plugins:**
   - Docker Pipeline
   - Email Extension Plugin
   - JUnit Plugin
   - GitHub Plugin

2. **Create Pipeline Job:**
   - New Item → Pipeline
   - Configure SCM (GitHub)
   - Pipeline script from SCM
   - Select Jenkinsfile

3. **Configure Email Notifications:**
   - Manage Jenkins → Configure System
   - Extended E-mail Notification
   - SMTP Server settings

4. **GitHub Webhook:**
   - GitHub Repo → Settings → Webhooks
   - Payload URL: `http://your-jenkins-url/github-webhook/`
   - Content type: `application/json`
   - Events: Push events

### Jenkinsfile Stages

The Jenkinsfile includes:
1. **Checkout**: Pull code from GitHub
2. **Build**: Install dependencies
3. **Test**: Run Selenium tests in Docker
4. **Deploy**: Deploy application (optional)
5. **Post**: Send email notifications

## Troubleshooting

### Common Issues

#### 1. ChromeDriver Version Mismatch
**Error**: `This version of ChromeDriver only supports Chrome version XX`

**Solution**: Use WebDriverManager (already in pom.xml)
```java
import io.github.bonigarcia.wdm.WebDriverManager;

@Before
public void setUp() {
    WebDriverManager.chromedriver().setup();
    // ... rest of setup
}
```

#### 2. Connection Refused
**Error**: `Connection refused to http://localhost:3000`

**Solution**: 
- Ensure application is running
- Check if port is correct
- Use correct IP for remote testing

#### 3. Element Not Found
**Error**: `NoSuchElementException`

**Solution**:
- Increase implicit wait time
- Add explicit waits
- Verify element IDs/selectors

#### 4. Tests Fail in Headless Mode but Pass in GUI
**Solution**:
- Increase window size: `options.addArguments("--window-size=1920,1080")`
- Add more wait time
- Check for animation delays

#### 5. Maven Build Fails
**Error**: `Failed to execute goal`

**Solution**:
```bash
# Clear Maven cache
mvn clean
mvn dependency:purge-local-repository

# Re-download dependencies
mvn clean install -U
```

## Best Practices

1. **Use Unique Test Data**: Generate unique emails using timestamps
2. **Clean Up After Tests**: Delete test data in `@After` method
3. **Use Explicit Waits**: Better than Thread.sleep()
4. **Independent Tests**: Each test should be able to run independently
5. **Meaningful Assertions**: Use descriptive assertion messages
6. **Page Object Model**: Consider implementing POM for larger test suites

## Example: Running Full Test Suite

```bash
# 1. Start the application
cd backend
npm start

# 2. In another terminal, run tests
cd selenium-tests
mvn clean test

# 3. View results
mvn surefire-report:report
start target/site/surefire-report.html
```

## CI/CD Workflow

1. Developer pushes code to GitHub
2. GitHub webhook triggers Jenkins pipeline
3. Jenkins pulls latest code
4. Runs tests in Docker container (headless Chrome)
5. Generates test reports
6. Sends email with results to committer

## Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
- [JUnit 4 Documentation](https://junit.org/junit4/)

## Support

For issues or questions:
- Check application logs
- Review test reports
- Check ChromeDriver compatibility
- Verify network connectivity

---

**Note**: Remember to update test data and URLs according to your deployment environment.
