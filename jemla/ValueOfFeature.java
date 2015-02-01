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
public class ValueOfFeature {
    int intType;     
    double doubleType;//x: numeric ==> intType:branch 
    boolean isNumeric;//false
    boolean isMissing;
    public ValueOfFeature(double x, boolean flo){
        this.isNumeric=flo;
        isMissing=false;
        if(isNumeric)
            doubleType=x;
        else 
            intType=(int)x;
    }
    
    public ValueOfFeature(boolean flo, boolean n){
        this.isMissing=flo;
        this.isNumeric=n;
    }
    
    public ValueOfFeature(Feature x, int v){
        isNumeric= x.isNumeric;
        if(!isNumeric)intType=v;
        else doubleType=v;
        isMissing= false;
    }
    
    public ValueOfFeature(Feature x, double v){
        isNumeric= x.isNumeric;
        doubleType=v;
        isMissing= false;
    }
    
    public ValueOfFeature(Feature x, boolean v){
        isNumeric= x.isNumeric;
        isMissing= v;
    }
    
    /**
     * *Compares to value of an attribute return true if this value is more than the other. 
     * @param x : value of an another attribute.
     * @return : true or false.
     */
    public boolean lessThan(ValueOfFeature x)
    {
        if(this.isMissing)
            return true;
        if(this.doubleType<x.doubleType)
            return true;
        else return false;
    }
    
    /**
     * *Compares to value of an attribute return true if this value is more than the other. 
     * @param x : value of an another attribute.
     * @return : true or false.
     */
    public boolean moreThan(ValueOfFeature x)
    {
        if(this.isMissing)
            return true;
        if(this.doubleType>=x.doubleType)
            return true;
        else return false;
    }
    
    /**
     * *Compares to value of an attribute return true if this value is less than the other.
     * @param x : double value of an another attribute.
     * @return : true or false.
     */
    public boolean lessThan(double x)
    {
        if(this.isMissing)
            return true;
        if(this.doubleType<x)
            return true;
        else return false;
    }
    
    /**
     * *Compares to value of an attribute return true if this value is more than the other 
     * @param x : value of an another numerical attribute
     * @return : true or false
     */
    public boolean moreThan(double x)
    {
        if(this.isMissing)
            return true;
        if(this.doubleType>=x)
            return true;
        else return false;
    }

    /**
     * *Compares to value of an attribute return true if this value is equal to the other. 
     * @param x : value of an another attribute
     * @return : true or false
     */
    public boolean equals(ValueOfFeature x)
    {
        if(isMissing)
            return true;
        if(isNumeric){
            if(this.doubleType==x.doubleType)
                return true;
        }
        else
            if(this.intType==x.intType)
                return true;
        return false;    
    }

    /**
     * *Compares to value of an attribute return true if this value is equal to the other. 
     * @param x : value of an another categorical attribute.
     * @return : true or false.
     */
    public boolean equals(int x)
    {
        if(isMissing)
            return true;
        if(this.intType==x)
            return true;
        else return false;
    }

    /**
     * *Compares to value of an attribute return true if this value is equal to the other.
     * This function considers the missing value issue
     * @param x : value of an another attribute
     * @return : true or false
     */
    public boolean exactEquals(ValueOfFeature x)
    {
        if(isMissing)
            return false;
        if(isNumeric){
            if(this.doubleType==x.doubleType)
                return true;
        }
        else
            if(this.intType==x.intType)
                return true;
        return false;    
    }
    
    private double abs(double d)
    {
        if(d<0) d=-d;
        return d;
    }
    
    /**
     * *finds the linear distance between two values of attributes. 
     * @param v : value of an another attribute
     * @return : distance 
     */
    public double distance(ValueOfFeature v)
    {
        double d=0;
        if(isMissing || v.isMissing)    {d=1;   return d;}
        if(isNumeric) d=abs(doubleType-v.doubleType);
        else if(intType==v.intType) d=0;
        else    d=1;
        return d;
    }
    
    @Override
    public ValueOfFeature clone(){
        ValueOfFeature v;
        if(isMissing) v= new ValueOfFeature(true,isNumeric);
        else   if(isNumeric)  v= new ValueOfFeature(doubleType,true);
        else    v= new ValueOfFeature(intType,false);
        return v;
    }
    
    
    @Override
    public String toString()
    {
        String s= new String("");
        if(isNumeric && !isMissing)
            s=Double.toString(doubleType);
        if(!isNumeric && !isMissing)
            s=Integer.toString(intType);
        if(isMissing)
            s="?";
        return s;
    }
}
