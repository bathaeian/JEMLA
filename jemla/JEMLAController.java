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
public class JEMLAController {
    
    private DataSet rsTrain;//data set for trainning
        
    
    public JEMLAController(DataSet rs1) {
        rsTrain=rs1;
    }

/**
     * *For each requested attribute calls entropy Calculator.
     * @param k : list of attributes.
     * @param missingAction : method of handling missing values 
     * @return : table of calculated entropies and thresholds.
     */
    public double[][] calculateEntropy(String k, int missingAction)
    {
        int i,j;
        double e[][];
        int l= rsTrain.getNumberOfFeatures();
        e= new double[2][l+1];
        ListOfValues c,a;
        Entropy ent= new Entropy();
        if(missingAction==5){
            rsTrain.closestFit();
            missingAction=0;
        }
        c= rsTrain.getListOfVals(l);
        e[0][l]= ent.getSumOfEntropies(c);
        if(k.equals("all")){
            for(i=0;i<l;i++){
                a= rsTrain.getListOfVals(i);
                e[0][i]=ent.getSumOfEntropies(a, c, missingAction);
                e[1][i]=ent.getThreshold();
            }
            return e;
        }
        String []vs= k.split(",");
        for(i=0;i<vs.length;i++){
                j=Integer.parseInt(vs[i])-1;
                a= rsTrain.getListOfVals(j);
                e[0][j]=ent.getSumOfEntropies(a, c, missingAction);
                e[1][j]=ent.getThreshold();
            }
        return e;
    }
    
    
    
    
}
