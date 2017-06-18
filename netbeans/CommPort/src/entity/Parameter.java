package entity;

import java.util.Set;

public class Parameter implements HostDictionary {

    private int id;
    private String name;
    private Set<Test> tests;
    private Test testDefault;
    private Analysis analysis;

    public Parameter() {
    }

    public Parameter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Parameter(int id, String name, Set<Test> tests) {
        this.id = id;
        this.name = name;
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }

    public Test getTestDefault() {
        return testDefault;
    }

    public void setTestDefault(Test testDefault) {
        this.testDefault = testDefault;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public String toString() {
        return "Parameter{" + "id=" + id + ", name=" + name + ", tests=" + tests + ", testDefault=" + testDefault + ", analysis=" + analysis + '}';
    }

   

}
