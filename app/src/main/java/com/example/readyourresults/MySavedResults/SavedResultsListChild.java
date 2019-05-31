package com.example.readyourresults.MySavedResults;

public class SavedResultsListChild {

    public String testType;
    public String testOutcome;
    public String testDate;

    public SavedResultsListChild(String testType, String testOutcome, String testDate) {
        this.testType = testType;
        this.testOutcome =testOutcome;
        this.testDate = testDate;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestOutcome() {
        return testOutcome;
    }

    public void setTestOutcome(String testOutcome) {
        this.testOutcome = testOutcome;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }
}