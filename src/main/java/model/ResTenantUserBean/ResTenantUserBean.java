package model.ResTenantUserBean;

import org.apache.catalina.LifecycleState;

import java.util.List;

public class ResTenantUserBean {
    public ResTenantUserBean(){}
    private int numResults;

    private List<Results> results;

    public void setNumResults(int numResults){
        this.numResults = numResults;
    }
    public int getNumResults(){
        return this.numResults;
    }
    public void setResults(List<Results> results){
        this.results = results;
    }
    public List<Results> getResults(){
        return this.results;
    }
}
