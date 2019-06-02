package com.example.readyourresults.MySavedResults;

public class SavedResultsItem {

    private String title;
    private String testType;
    private String testOutcome;
    private String testDate;
    private boolean expanded;

    public SavedResultsItem(String title, String testType, String testOutcome, String testDate) {
        this.title = title;
        this.testType = testType;
        this.testOutcome =testOutcome;
        this.testDate = testDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String title) {
        this.testType = testType;
    }

    public String getTestOutcome() {
        return testOutcome;
    }

    public void setTestOutcome(String title) {
        this.testOutcome = testOutcome;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String title) {
        this.testDate = testDate;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}