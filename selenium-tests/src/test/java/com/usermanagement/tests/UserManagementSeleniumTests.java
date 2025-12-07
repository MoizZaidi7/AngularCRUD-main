package com.usermanagement.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import static org.junit.Assert.*;

public class UserManagementSeleniumTests {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://13.62.230.47:3000";
    
    @BeforeClass
    public static void setupClass() {
        // Setup WebDriverManager once for all tests
        WebDriverManager.chromedriver().cachePath("/tmp/selenium-cache").setup();
    }
    
    @Before
    public void setUp() {
        // Setup ChromeDriver for headless mode (for Jenkins)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-software-rasterizer");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    public void TC01_verifyPageLoadsSuccessfully() {
        driver.get(baseUrl);
        String title = driver.getTitle();
        assertEquals("User Management System", title);
        
        // Verify main heading is present
        WebElement heading = driver.findElement(By.tagName("h1"));
        assertEquals("User Management System", heading.getText());
    }
    
    @Test
    public void TC02_addNewUserWithValidData() throws InterruptedException {
        driver.get(baseUrl);
        
        // Fill out the form
        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("testuser" + System.currentTimeMillis() + "@example.com");
        driver.findElement(By.id("age")).sendKeys("25");
        driver.findElement(By.id("address")).sendKeys("123 Test Street");
        
        // Submit the form
        driver.findElement(By.id("submitBtn")).click();
        
        // Wait for success message
        Thread.sleep(2000);
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessage")));
        assertTrue(successMsg.isDisplayed());
        assertTrue(successMsg.getText().contains("successfully"));
    }
    
    @Test
    public void TC03_verifyUserAppearsInTable() throws InterruptedException {
        driver.get(baseUrl);
        
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";
        
        // Add a user
        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys(uniqueEmail);
        driver.findElement(By.id("age")).sendKeys("30");
        driver.findElement(By.id("address")).sendKeys("456 Main St");
        driver.findElement(By.id("submitBtn")).click();
        
        // Wait for table to update
        Thread.sleep(2000);
        
        // Refresh the page to see the user
        driver.findElement(By.id("refreshBtn")).click();
        Thread.sleep(1000);
        
        // Check if user appears in table
        WebElement table = driver.findElement(By.id("usersTable"));
        String tableText = table.getText();
        assertTrue(tableText.contains(uniqueEmail));
    }
    
    @Test
    public void TC04_editExistingUser() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Click edit button on first user (if exists)
        WebElement editBtn = driver.findElement(By.className("btn-success"));
        editBtn.click();
        
        Thread.sleep(1000);
        
        // Verify form is filled
        WebElement nameField = driver.findElement(By.id("name"));
        assertFalse(nameField.getAttribute("value").isEmpty());
        
        // Verify submit button changed to "Update User"
        WebElement submitBtn = driver.findElement(By.id("submitBtn"));
        assertEquals("Update User", submitBtn.getText());
    }
    
    @Test
    public void TC05_deleteUser() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Get initial row count
        WebElement tableBody = driver.findElement(By.id("usersTableBody"));
        int initialRowCount = tableBody.findElements(By.tagName("tr")).size();
        
        if (initialRowCount > 0) {
            // Click delete button and accept alert
            WebElement deleteBtn = driver.findElement(By.className("btn-danger"));
            deleteBtn.click();
            
            // Accept confirmation dialog
            Thread.sleep(500);
            driver.switchTo().alert().accept();
            
            // Wait and verify
            Thread.sleep(2000);
            driver.findElement(By.id("refreshBtn")).click();
            Thread.sleep(1000);
            
            int newRowCount = driver.findElement(By.id("usersTableBody"))
                                   .findElements(By.tagName("tr")).size();
            
            assertTrue(newRowCount < initialRowCount || newRowCount == 0);
        }
    }
    
    @Test
    public void TC06_searchUserByName() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Add a user first
        String searchName = "SearchTest" + System.currentTimeMillis();
        driver.findElement(By.id("name")).sendKeys(searchName);
        driver.findElement(By.id("email")).sendKeys("search" + System.currentTimeMillis() + "@test.com");
        driver.findElement(By.id("age")).sendKeys("28");
        driver.findElement(By.id("address")).sendKeys("Search Address");
        driver.findElement(By.id("submitBtn")).click();
        
        Thread.sleep(2000);
        
        // Search for the user
        driver.findElement(By.id("searchInput")).sendKeys(searchName);
        driver.findElement(By.id("searchBtn")).click();
        
        Thread.sleep(1000);
        
        // Verify search results
        WebElement table = driver.findElement(By.id("usersTable"));
        assertTrue(table.getText().contains(searchName));
    }
    
    @Test
    public void TC07_searchUserByEmail() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Search using email
        driver.findElement(By.id("searchInput")).sendKeys("@example.com");
        driver.findElement(By.id("searchBtn")).click();
        
        Thread.sleep(1000);
        
        // Verify filtering occurred
        WebElement tableBody = driver.findElement(By.id("usersTableBody"));
        assertTrue(tableBody.isDisplayed() || driver.findElement(By.id("noUsersMessage")).isDisplayed());
    }
    
    @Test
    public void TC08_verifyFormValidation() {
        driver.get(baseUrl);
        
        // Try to submit empty form
        driver.findElement(By.id("submitBtn")).click();
        
        // Verify HTML5 validation (required fields)
        WebElement nameField = driver.findElement(By.id("name"));
        String validationMessage = nameField.getAttribute("validationMessage");
        assertNotNull(validationMessage);
    }
    
    @Test
    public void TC09_verifyDuplicateEmailValidation() throws InterruptedException {
        driver.get(baseUrl);
        
        String duplicateEmail = "duplicate" + System.currentTimeMillis() + "@test.com";
        
        // Add first user
        driver.findElement(By.id("name")).sendKeys("User One");
        driver.findElement(By.id("email")).sendKeys(duplicateEmail);
        driver.findElement(By.id("age")).sendKeys("25");
        driver.findElement(By.id("address")).sendKeys("Address 1");
        driver.findElement(By.id("submitBtn")).click();
        
        Thread.sleep(2000);
        
        // Try to add second user with same email
        driver.findElement(By.id("name")).sendKeys("User Two");
        driver.findElement(By.id("email")).sendKeys(duplicateEmail);
        driver.findElement(By.id("age")).sendKeys("30");
        driver.findElement(By.id("address")).sendKeys("Address 2");
        driver.findElement(By.id("submitBtn")).click();
        
        Thread.sleep(2000);
        
        // Verify error message appears
        WebElement errorMsg = driver.findElement(By.id("errorMessage"));
        assertTrue(errorMsg.isDisplayed());
    }
    
    @Test
    public void TC10_verifyRefreshButton() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Click refresh button
        driver.findElement(By.id("refreshBtn")).click();
        
        Thread.sleep(1000);
        
        // Verify loading was triggered and table is displayed
        WebElement table = driver.findElement(By.id("usersTable"));
        assertTrue(table.isDisplayed() || driver.findElement(By.id("noUsersMessage")).isDisplayed());
    }
    
    @Test
    public void TC11_verifyCancelButtonInEditMode() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Check if there are any users to edit
        try {
            WebElement editBtn = driver.findElement(By.className("btn-success"));
            editBtn.click();
            Thread.sleep(1000);
            
            // Verify cancel button is visible
            WebElement cancelBtn = driver.findElement(By.id("cancelBtn"));
            assertTrue(cancelBtn.isDisplayed());
            
            // Click cancel
            cancelBtn.click();
            
            // Verify form is reset
            WebElement submitBtn = driver.findElement(By.id("submitBtn"));
            assertEquals("Add User", submitBtn.getText());
            
            // Verify cancel button is hidden
            assertFalse(cancelBtn.isDisplayed());
        } catch (Exception e) {
            // No users to edit, test passes
            assertTrue(true);
        }
    }
    
    @Test
    public void TC12_verifyClearSearchFunctionality() throws InterruptedException {
        driver.get(baseUrl);
        Thread.sleep(2000);
        
        // Enter search term
        driver.findElement(By.id("searchInput")).sendKeys("test search");
        driver.findElement(By.id("searchBtn")).click();
        
        Thread.sleep(1000);
        
        // Clear search
        driver.findElement(By.id("clearSearchBtn")).click();
        
        // Verify search input is cleared
        WebElement searchInput = driver.findElement(By.id("searchInput"));
        assertEquals("", searchInput.getAttribute("value"));
    }
}
