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
public class Entropy {
    private double threshold;
    ListOfValues mainAtt,target;
    
    public Entropy(){
        threshold=-9999999;
    }
    
    /**
     * *After calculating entropy of a numeric attribute, someone may get threshold of that attribute.
     * @return : the threshold
     */
    public double getThreshold(){
        return threshold;
    }
    
    /**
     * *Calculates entropy of the specified attribute.
     * @param a : list of values for an attribute 
     * @param c : list of values for class labels , 
     * @param ignoreAction : method of handling missing values 
     * @return : Calculated entropy
     */
    public double getSumOfEntropies(ListOfValues a,ListOfValues c, int ignoreAction)
    {
        double se=0;
        mainAtt= a.clone();
        target= c.clone();
        if(nonSenceData(a,c))  return 10000;
        switch(ignoreAction){
            case 0: ignoreMissing();
                    break;
            case 1:replaceMissingWithMostCommonValueOrMean();
                break;
            case 2:replaceMissingWithCommonValueOrMeanForGivenClass();
                break;
            case 3:fractionalReplacementOfMissings();
                break;
            case 4: replaceMissingWithMidForGivenClass();
        }
        //System.out.println(mainAtt.toString());
        //System.out.println(target.toString());
        if(mainAtt.at.isNumeric)
            se=binaryNumericEntropy();
        else
            se=getEntropies();
        return se;
    }
    
    private boolean nonSenceData(ListOfValues a, ListOfValues c)
    {
        if(a.a.length<2)   return true;
        if(!c.allSame() && a.allSame())    return true;
        return false;
    }
    
    private void ignoreMissing()
    {
        ValueOfFeature [] a2,c2;
        int i,j;
        int t= mainAtt.noMissingCount();
        a2= new ValueOfFeature[t];
        c2= new ValueOfFeature[t];
        j=0;
        for(i=0;i<mainAtt.a.length;i++)
            if(!mainAtt.a[i].isMissing){
                a2[j]=mainAtt.a[i];
                c2[j]=target.a[i];
                j++;
            }
        mainAtt=new ListOfValues(a2,mainAtt.at);
        target=new ListOfValues(c2,target.at);
    }
    
    private void replaceMissingWithMostCommonValueOrMean()
    {
        ValueOfFeature x= mainAtt.mostCommonOrMean();
        int i;
        for(i=0;i<mainAtt.a.length;i++)
            if(mainAtt.a[i].isMissing) mainAtt.a[i]=x;
        return;
    }
    
    private void replaceMissingWithCommonValueOrMeanForGivenClass()
    {
        int i,j;
        ListOfValues a2;
        ValueOfFeature x;
        for(i=0;i<target.at.size;i++){
                x= new ValueOfFeature(target.at,i);
                a2=mainAtt.subRelative(target, x);
                x= a2.mostCommonOrMean();
                if(x==null)x= mainAtt.mostCommonOrMean();
                for(j=0;j<mainAtt.a.length;j++)
                    if(mainAtt.a[j].isMissing && target.a[j].equals(i)) mainAtt.a[j]=x;
        }
        return;
    }
    
    private void replaceMissingWithMidForGivenClass()
    {
        int i,j;
        ListOfValues a2;
        ValueOfFeature x;
        for(i=0;i<target.at.size;i++){
                x= new ValueOfFeature(target.at,i);
                a2=mainAtt.subRelative(target, x);
                x= a2.mid();
                if(x==null)x= mainAtt.mid();
                for(j=0;j<mainAtt.a.length;j++)
                    if(mainAtt.a[j].isMissing && target.a[j].equals(i)) mainAtt.a[j]=x;
        }
        return;
    }
    
    private void fractionalReplacementOfMissings()
    {
        int i,j,k,h;
        int l1,l2,l3;
        ListOfValues a2;
        ValueOfFeature x;
        for(i=0;i<target.at.size;i++){
            x= new ValueOfFeature(target.at,i);
            a2=mainAtt.subRelative(target, x);
            l1=a2.noMissingCount();
            if(l1==0){
                x= mainAtt.mostCommonOrMean();
                for(j=0;j<mainAtt.a.length;j++)
                    if(mainAtt.a[j].isMissing && target.a[j].equals(i)) mainAtt.a[j]=x;
            }else{
                int []t;
                if(a2.at.isNumeric)
                    a2.discretize();
                t=new int[a2.at.size];
                l2=a2.a.length-l1;  //number of missings
                for(j=0; j<a2.at.size; j++){
                    x= new ValueOfFeature(a2.at,j);
                    t[j]=a2.valuesCount(x);
                }
                double r;
                int j2,max,maxv;
                for(j=0; j<a2.at.size; j++){
                    max=0;maxv=0;
                    for(j2=0;j2<t.length;j2++)
                        if(t[j2]>=maxv){maxv=t[j2];max=j2;}
                    t[max]=0;
                    if(a2.at.isNumeric){
                        r=a2.a[j].doubleType;
                        x= new ValueOfFeature(a2.at,r);
                    }else
                        x= new ValueOfFeature(a2.at,max);
                    l3=maxv;
                    for(k=0,h=0;k<mainAtt.a.length && h<=l2*l3/l1;k++)
                        if(mainAtt.a[k].isMissing && target.a[k].equals(i)){
                            mainAtt.a[k]=x;
                            h++;
                        }
                }
            }
        }
        return;
    }
    
    /**
     * *Calculates entropy of the attribute of target (labels of class).
     * @param a : list of values for class labels. 
     * @return : Calculated entropy.
     */
    public double getSumOfEntropies(ListOfValues a)
    {
        int i;
        target= a.clone();
        int[]a2=new int[a.at.size];
        for(i=0;i<a.a.length;i++)
            a2[a.a[i].intType]++;
        return entropy(a2);
    }

    private double binaryNumericEntropy()
    {
        int i,j;
        ListOfValues aSort;
        Feature atr= new Feature();
        atr.title= mainAtt.at.title;
        atr.isNumeric=false;
        atr.size=2;
        atr.classLabel= false;
        int l= mainAtt.a.length;
        for(i=0;i<l-1;i++)
            for(j=i+1;j<l;j++)
                if(mainAtt.a[i].moreThan(mainAtt.a[j])){
                    mainAtt.interchange(i, j);
                    target.interchange(i, j);
                }
        double selectedEn=l,temp3;
        ValueOfFeature []abinary= new ValueOfFeature[l];
        for(i=0;i<l-1;i++)
            if((!target.a[i].equals(target.a[i+1]))&&(!mainAtt.a[i].equals(mainAtt.a[i+1]))){
                for(j=0;j<=i;j++)
                    abinary[j]=new ValueOfFeature(0,false);
                for(j=i+1;j<l;j++)
                    abinary[j]=new ValueOfFeature(1,false);
                aSort=new ListOfValues(abinary,atr);
                temp3=getEntropies(aSort,target);
                if(selectedEn>temp3){
                    selectedEn= temp3;
                    threshold= (mainAtt.a[i].doubleType+mainAtt.a[i+1].doubleType)/2;
                }
            } 
        return selectedEn;
    }
    
    
    private double getEntropies()
    {
        int i,j;
        int cat=mainAtt.at.size;
        int c_wise= target.at.size;
        double sum=0;
        double S,Sv,e;
        S=mainAtt.a.length;
        int[][] values=new int[cat][c_wise];
        for(i=0;i<S;i++)
            if(!mainAtt.a[i].isMissing){
                    values[mainAtt.a[i].intType][target.a[i].intType]++;
                    //System.out.println("Entropy:getEntropies: "+a.a[i].intType+" "+c.a[i].intType+" "+values[a.a[i].intType][c.a[i].intType]);
            }
        for(i=0;i<cat;i++){
            Sv=0;
            for(j=0;j<c_wise;j++)
                Sv=Sv+values[i][j];//for(j=0;j<c_wise;j++)System.out.println("Entropy:getEntropies:vals:"+j+" "+values[i][j]);
            e= entropy(values[i]);
            sum=sum+(Sv/S)*e;//System.out.println("Entropy:getEntropies:"+Sv+" "+S+" "+e);
        }//System.out.println("Entropy:getEntropies:"+sum);
        return sum;
   } 
    
    private double getEntropies(ListOfValues a,ListOfValues c )
    {
        int i,j;
        int cat=a.at.size;
        int c_wise= c.at.size;
        double sum=0;
        double S,Sv,e;
        S=a.a.length;
        int[][] values=new int[cat][c_wise];
        for(i=0;i<S;i++)
            if(!a.a[i].isMissing){
                    values[a.a[i].intType][c.a[i].intType]++;
                    //System.out.println("Entropy:getEntropies: "+a.a[i].intType+" "+c.a[i].intType+" "+values[a.a[i].intType][c.a[i].intType]);
            }
        for(i=0;i<cat;i++){
            Sv=0;
            for(j=0;j<c_wise;j++)
                Sv=Sv+values[i][j];//for(j=0;j<c_wise;j++)System.out.println("Entropy:getEntropies:vals:"+j+" "+values[i][j]);
            e= entropy(values[i]);
            sum=sum+(Sv/S)*e;//System.out.println("Entropy:getEntropies:"+Sv+" "+S+" "+e);
        }//System.out.println("Entropy:getEntropies:"+sum);
        return sum;
   }
    
    
    private double entropy(int[] a){
        int l=a.length;
        double[] e=new double[l];
        int i;
        double t=0;
        double c;
        for(i=0;i<l;i++)    t=a[i]+t;
        for(i=0;i<l;i++){
            c=(double)a[i]/t;
            if(c>0) e[i]=-(c)*Math.log(c)/Math.log(2);
            else e[i]=0;
        }
        c=0;
        for(i=0;i<l;i++)    c=c+e[i];
        return c;
    }

}
