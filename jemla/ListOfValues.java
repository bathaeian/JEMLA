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
public class ListOfValues {
    ValueOfFeature [] a;
    Feature at;
    public ListOfValues(ValueOfFeature [] a,Feature at){
        this.a=a;
        this.at=at;
    }
    
    /**
     * *finds the subset of list matching with specified class label.
     * @param c : list of class labels
     * @param x : specified value
     * @return : a list of values
     */
    public ListOfValues subRelative(ListOfValues c, ValueOfFeature x)
    {
        int t=0;
        int i,j;
        for(i=0;i<c.a.length;i++)
            if(c.a[i].equals(x))    t++;
        ValueOfFeature [] v= new ValueOfFeature[t];  
        for(i=0,j=0;i<a.length;i++)
            if(c.a[i].equals(x)){
                v[j]=a[i];
                j++;
            }
        ListOfValues a2= new ListOfValues(v,at);
        
        return a2;
    }
    
    public ListOfValues clone(){
        ValueOfFeature [] b= new ValueOfFeature[a.length];
        int i;
        if(at.isNumeric)
            for(i=0;i<a.length;i++) 
                if(a[i].isMissing)
                    b[i]= new ValueOfFeature(true,true);
                else
                    b[i]= new ValueOfFeature(a[i].doubleType,true);
        else
            for(i=0;i<a.length;i++) 
                if(a[i].isMissing)
                    b[i]= new ValueOfFeature(true,false);
                else
                    b[i]= new ValueOfFeature(a[i].intType,false);
        ListOfValues c= new ListOfValues(b,at);
        return c;
    }
    
    /**
     * *interchanges two values in an attribute.
     * @param i : index of first value
     * @param j : index of second value
     */
    public void interchange(int i,int j)
    {
        ValueOfFeature temp;
        temp=a[i].clone();
        a[i]=a[j].clone();
        a[j]=temp.clone();
        return;
    }
    
    /**
     * *counts the number of non missing values.
     * @return : number of non missing values.
     */
    public int noMissingCount()
    {
        int t=0;
        int i;
        for(i=0;i<a.length;i++)
            if(a[i].isMissing)
                t++;
        return a.length-t;
    }

    /**
     * *counts the number of values matching specified value.
     * @param x : specified value
     * @return : number of values.
     */
    public int valuesCount(ValueOfFeature x)
    {
        int t=0;
        int i;
        for(i=0;i<a.length;i++)
            if(a[i].exactEquals(x))
                t++;
        return t;
    }
    
    /**
     * *check whether all values are same or not.
     * @return : true or false.
     */
    public boolean allSame()
    {
        boolean flag= true;
        int i;
        ValueOfFeature x= a[0];
        for(i=1;i<a.length;i++)
            if(!a[i].equals(x))
                flag=false;
        return flag;
    }
    
    /**
     * *figures out the most common value for categorical list or mean for numerical list
     * @return : the most common value or mean.
     */
    public ValueOfFeature mostCommonOrMean ()
    {
        ValueOfFeature x= new ValueOfFeature(at,false);
        int t= noMissingCount();
        int i;
        if(at.isNumeric){
            double m=0;
            for(i=0; i<a.length; i++)
                if(!a[i].isMissing) m=m+a[i].doubleType;
            m=m/t;
            x=new ValueOfFeature(at,m);
        }
        else{
            int []y= new int[at.size];
            int val=0;
            for(i=0;i<a.length;i++)
                if(!a[i].isMissing)  y[a[i].intType]++;
            for(i=1;i<at.size;i++)
                if(y[val]>y[i]){val=i;}
            x= new ValueOfFeature(at,val);
        }
        return x;
    }
    
    public void discretize()
    {
        int []d= new int[2];
        return ;
    }
    
    private void sort()
    {
        int i,j;
        double t;
        if(at.isNumeric)
            for(i=0;i<a.length-1;i++) 
                for(j=i+1;j<a.length;j++)
                    if(a[i].doubleType>a[j].doubleType){
                        t=a[i].doubleType;
                        a[i].doubleType=a[j].doubleType;
                        a[j].doubleType=t;
                    }
    }
    
    /**
     * *figures out the middle value of this list.
     * @return : the middle value.
     */
    public ValueOfFeature mid()
    {
        ListOfValues a2= this.clone();
        ValueOfFeature x;
        a2.sort();
        int i=a2.a.length/2;
        while(a[i].isMissing)   i++;
        x= new ValueOfFeature(at,a[i].doubleType);
        return x;
    }

    @Override
    public String toString()
    {
        String s= new String(""+a.length+":");
        int i;
        for(i=0;i<a.length;i++)
            s=s+a[i].toString()+" ";
        return s;
    }
}
