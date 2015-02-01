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
import java.util.ArrayList;

/**
 *
 * @author Narges S. Bathaeian
 */
public class DataSet extends ArrayList<Instance>{
   private java.util.ArrayList<Feature> titles;
   private Feature classLab;
   private int majorityClass;
   private double majorityErr;

   public DataSet(ArrayList<Feature> s,Feature lab){
       super();
       titles= new java.util.ArrayList();
       titles=s;
       classLab= lab;
   } 


   /**
     * *Outputs the number of features.
     * @return : number of features.
     */
   public int getNumberOfFeatures(){return titles.size();}
   
   
   
   /**
     * *Outputs a list of values of a certain feature.
     * @param x : index of feature
     * @return : list of values.
     */
   public ListOfValues getListOfVals(int x)
   {
       int i;
       ListOfValues loa;
       ValueOfFeature [] a= new ValueOfFeature[this.size()];
       if(x<titles.size()){
           for(i=0;i<this.size();i++)
               a[i]=this.get(i).featureValue(x);
           loa= new ListOfValues(a,titles.get(x));
       }
       else{
           for(i=0;i<this.size();i++)
               a[i]= new ValueOfFeature(this.get(i).getClassN(),false);
           loa= new ListOfValues(a,classLab);
       }
       return loa;
   }
   
 
  /**
     * *replaces a missing attribute value with an existing value of the same attribute
     * from another case that resembles as much as possible the case with missing attribute values.
     */
    public void closestFit()
    {
        double d1,d2;
        int i,j;
        Instance r1,r2,r3;
        d1= this.size()* this.size();
        r3= this.get(0);
        for(i=0;i<this.size();i++){
            r1=this.get(i);
            if(r1.hasMissing()){
                for(j=0;j<this.size();j++){
                    r2=this.get(j);
                    d2= r1.manhattanDistance(r2);
                    if(d2<d1 && r1.okForReplace(r2)){
                        r3= r2;
                        d1=d2;
                    }
                }
                if(r3!=null)    r1.fixEachOther(r3);
            }
        }
        return;
    }
       
    @Override
   public String toString(){
       String s=new String("");
       int i;
       for(i=0;i<this.size();i++)
           s=s+this.get(i).toString()+":";
       return s;
   }
}
