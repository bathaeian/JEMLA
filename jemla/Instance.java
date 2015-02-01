/*
 * The MIT License
 *
 * Copyright 2015 a.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jemla;
/**
 *
 * @author Narges S. Bathaeian
 */
public class Instance{
    
    private int classN;
    private java.util.ArrayList<ValueOfFeature> feature_value;
    private ValueOfFeature classLabel;
    
    public Instance(java.util.ArrayList<ValueOfFeature> atList, ValueOfFeature cl){
        feature_value=new java.util.ArrayList();
        feature_value= (java.util.ArrayList<ValueOfFeature>)atList.clone();
        classN=cl.intType;//classification not regression
        classLabel= new ValueOfFeature(false,false);
        classLabel=cl;
        return;
    }
    
    
    /**
     * *Outputs the class label of this instance.
     * @return : class label of this instance.
     */
    public int getClassN(){
        return classN;
    }
    
    /**
     * *Outputs the value of a certain feature.
     * @param i : index of the feature.
     * @return : the value of a certain feature.
     */
    public ValueOfFeature featureValue(int i){
        return feature_value.get(i);
    }  
    
    
    private int size(){
        return feature_value.size();
    }
    
    /**
     * *Figures out the Manhattan distance between two instances.
     * @param r : the second instance
     * @return : the Manhattan distance
     */
    public double manhattanDistance(Instance r)
    {
        int i;
        double d=0;
        for(i=0;i<this.size();i++)
            d=d+this.featureValue(i).distance(r.featureValue(i));
        return d;
    }
    
    /**
     * *Checks whether two this instance has a missing value or not.
     * @return : true or false
     */
    public boolean hasMissing()
    {
        int i;
        for(i=0;i<this.size();i++)
            if(this.featureValue(i).isMissing)  return true;
        return false;
    }

    /**
     * *Checks whether two instances are appropriate for choosing values to replace a missing value.
     * @param r : the second instance
     * @return : true or false
     */
    public boolean okForReplace(Instance r)
    {
        int i;
        for(i=0;i<this.size();i++)
            if(this.featureValue(i).isMissing && r.featureValue(i).isMissing)  return false;
        return true;
    }
    
    /**
     * *replaces a missing value with an existing value of the same feature from another instance.
     * @param r : the second instance
     */
    public void fixEachOther(Instance r)
    {
        int i;
        for(i=0;i<this.size();i++){
            if(this.featureValue(i).isMissing)  r.feature_value.set(i, this.featureValue(i));
            if(r.featureValue(i).isMissing) this.feature_value.set(i,r.featureValue(i));
        }
        return ;
    }
    
    @Override
    public String toString(){
        String s=new String("");
        ValueOfFeature x;
        int i;
        for(i=0;i<feature_value.size();i++){
            x= feature_value.get(i);
            if(x.isMissing)
                s=s+"?,";
            else if(x.isNumeric)
                s=s+x.doubleType+",";
            else
                s=s+x.intType+",";
        } 
            
        s=s+classN;
        return s;
    }
}
