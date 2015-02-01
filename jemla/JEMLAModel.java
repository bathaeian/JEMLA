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
public class JEMLAModel {

    private java.util.ArrayList<Feature> atts;
    private Feature labelAtt;
    private DataSet rsTrain;
    private DataSet rsTest;
    private java.io.File file2save;
    private java.util.ArrayList<String []> arffAtts;

      
    public JEMLAModel(String f1N)//f1:train in arff format
    {
        java.io.File f1= new java.io.File(f1N);
        setArffSpecs(f1);
        try{
            rsTrain= readArffFile(f1);
        }catch(java.io.IOException e){
            System.err.println("can not read training data");
        }
        System.out.println("JEMLAModel: "+rsTrain.size());
        file2save= new java.io.File(f1N+"spec.txt");
        int i;
        Feature att;
        for(i=0;i<atts.size();i++){
            att= atts.get(i);
            if(!labelAtt.equals(att)){
                if(att.isNumeric)
                    writeFile(att.title+",numeric");
                else
                    writeFile(att.title+","+ att.size);
            }
        }
        writeFile(labelAtt.title+","+ labelAtt.size);
        file2save= new java.io.File(f1N+"train.txt");
        Instance r;
        for(i=0;i<rsTrain.size();i++){
            r= rsTrain.get(i);
            writeFile(r.toString());
        }
    }
    public JEMLAModel(String f1N,String f2N)//f1:train f2:spec
    {
        java.io.File f1= new java.io.File(f1N);
        java.io.File f2= new java.io.File(f2N);
        setSpecs(f2);//reads spec file and sets atts
        //Reading train data
        try{
            rsTrain= readFile(f1);
        }catch(java.io.IOException e){
            System.err.println("can not read training data");
        }
        System.out.println("JEMLAModel: "+rsTrain.size());
    }

    
    
    private void setSpecs(java.io.File f)
    {
        atts= new java.util.ArrayList();
        Feature a= new Feature();
        java.io.FileReader fr;
        java.io.BufferedReader br;
        String content;
        String[] vs;
        try{
            fr = new java.io.FileReader(f);
            br = new java.io.BufferedReader(fr);
            while((content=br.readLine())!=null){
                vs =content.split(",");
                a= new Feature();
                a.title= vs[0];
                if(vs[1].equals("numeric")){
                    a.isNumeric=true;
                    if(vs.length>2) a.size=Integer.parseInt(vs[2]);
                    else a.size=2;
                }
                else    a.size= Integer.parseInt(vs[1]);
                if(a.title.equals("label")){
                    a.classLabel=true;
                    a.isNumeric=false;
                    labelAtt=a;
                } 
                atts.add(a);
            }
        }catch(java.io.IOException e){
            System.err.println("can not read spec data");
        }
    }
    
    private void setArffSpecs(java.io.File f)
    {
        atts= new java.util.ArrayList();
        arffAtts= new java.util.ArrayList();
        Feature a= new Feature();
        java.io.FileReader fr;
        java.io.BufferedReader br;
        String content;
        String[] vs1,vs2;
        int i,j;
        try{
            fr = new java.io.FileReader(f);
            br = new java.io.BufferedReader(fr);
            while(!(content=br.readLine()).startsWith("@attribute"));
            do{
                vs1 =content.substring(11).split(" ");
                a= new Feature();
                if(vs1[0].contains("class"))
                    a.title= "label";
                else
                    a.title= vs1[0];
                if(vs1[1].equals("numeric")){
                    a.isNumeric=true;
                    if(vs1.length>2) a.size=Integer.parseInt(vs1[3]);
                    else a.size=2;
                }
                else    if(vs1[1].equals("string")) a.size= 0;
                else {
                    i= content.indexOf("{");
                    j= content.indexOf("}");
                    vs1[1]= content.substring(i+1, j);
                    vs1[1].replaceAll(" ", "");
                    vs2=vs1[1].split(",");
                    a.size=vs2.length;
                    i=atts.size();
                    arffAtts.add(i,vs2);
                }
                if(a.title.equals("label")){
                    a.classLabel=true;a.isNumeric=false;
                    labelAtt=a;
                } 
                atts.add(a);
                
            }while((content=br.readLine()).startsWith("@attribute"));
        }catch(java.io.IOException e){
            System.err.println("can not read spec data");
        }
        if(labelAtt==null){
            int n= atts.size()-1;
            atts.get(n).title= "label";
            atts.get(n).classLabel=true;
            labelAtt=atts.get(n);
        }
        for(i=0;i<arffAtts.size();i++){
            String []temp= arffAtts.get(i);
                    String t=new String("");
            for(j=0;j<temp.length;j++)
                t=t+"--"+j+":"+temp[j];
            System.out.println(t);
        }
    }

    private DataSet readFile(java.io.File f)throws java.io.IOException
    {
        //pre processing is done: all data are converted to numbers
        java.util.ArrayList<Feature> atts2= (java.util.ArrayList<Feature>)atts.clone();
        java.util.ArrayList<ValueOfFeature> atList;
        ValueOfFeature at1,at2;
        atts2.remove(labelAtt);
        int i;
        at2= new ValueOfFeature(false,false);
        DataSet rs=new DataSet(atts2,labelAtt);
        Instance r;
        java.io.FileReader fr;
        java.io.BufferedReader br;
        String content;
        fr = new java.io.FileReader(f);
        br = new java.io.BufferedReader(fr);
        String[] vs;
        while((content=br.readLine())!=null){
            atList= new java.util.ArrayList();
            vs =content.split(",");
            for(i=0;i<vs.length;i++)
                if(atts.get(i).classLabel)
                    at2= new ValueOfFeature(Double.parseDouble(vs[i]),labelAtt.isNumeric);
                else if(vs[i].contains("?")){
                    at1= new ValueOfFeature(true,atts.get(i).isNumeric);
                    atList.add(at1);
                }
                else{
                    at1= new ValueOfFeature(Double.parseDouble(vs[i]),atts.get(i).isNumeric);
                    atList.add(at1);
                }
                
            r=new Instance(atList,at2);
            rs.add(r);
        }
        br.close();
        return rs;
    }
    
    private DataSet readArffFile(java.io.File f)throws java.io.IOException
    {
        //reading Arff data
        java.util.ArrayList<Feature> atts2= (java.util.ArrayList<Feature>)atts.clone();
        java.util.ArrayList<ValueOfFeature> atList;
        ValueOfFeature at1,at2;
        atts2.remove(labelAtt);
        int i;
        at2= new ValueOfFeature(false,false);
        DataSet rs=new DataSet(atts2,labelAtt);
        Instance r;
        java.io.FileReader fr;
        java.io.BufferedReader br;
        String content;
        fr = new java.io.FileReader(f);
        br = new java.io.BufferedReader(fr);
        String[] vs;
        String s;
        while(!(content=br.readLine()).startsWith("@data"));
        while((content=br.readLine())!=null && !content.startsWith(" ") && !content.startsWith("%")){
            atList= new java.util.ArrayList();
            vs =content.split(",");
            for(i=0;i<vs.length;i++){
                s= virtualValueArff(i,vs[i]);
                if(atts.get(i).classLabel && !s.equals("?"))
                    at2= new ValueOfFeature(Double.parseDouble(s),labelAtt.isNumeric);
                else if(s.contains("?")){
                    at1= new ValueOfFeature(true,atts.get(i).isNumeric);
                    atList.add(at1);
                }
                else{
                    at1= new ValueOfFeature(Double.parseDouble(s),atts.get(i).isNumeric);
                    atList.add(at1);
                }
            }
            if(atList.size()==atts2.size()){    
                r=new Instance(atList,at2);
                rs.add(r);
            }
        }
        br.close();
        return rs;
    }
    
    private String virtualValueArff(int i , String x)
    {
        String s= new String("?");
        if(atts.get(i).size==0){
            return s;
        }
        String []ss= arffAtts.get(i);
        if(ss!=null){
            int j;
            for(j=0;j<ss.length;j++)
                if(ss[j].contains(x) || x.contains(ss[j]))
                    s=""+j;
        }
        return s;
    }
    
    
    /**
     * *returns the dataset for train. 
     * @return :  train dataset
     */
    public DataSet getTrainData(){
        return rsTrain;
    }
    
    
    /**
     * *determines the output file
     * @param f1N : name of the output file
     */
    public void setOutFile(String f1N)
    {
        file2save= new java.io.File(f1N);
    }
    
    /**
     * *writes to the output file
     * @param s : output string
     */
    public void writeFile(String s)
    {
        java.io.FileWriter fw;
        java.io.BufferedWriter bw;
        java.io.PrintWriter out = null;
        try {
            fw= new java.io.FileWriter(file2save,true);
            bw= new java.io.BufferedWriter(fw);
            out = new java.io.PrintWriter(bw);
            out.println(s);
        }catch (java.io.IOException e) {
            System.err.println(e);
        }finally{
            if(out != null){
                out.close();
            }
        }
    }

    /**
     * *writes to the output file
     * @param e : output table
     */
    public void writeFile(double e[][])
    {
        java.io.FileWriter fw;
        java.io.BufferedWriter bw;
        java.io.PrintWriter out = null;
        String s= new String();
        java.text.DecimalFormat decim = new java.text.DecimalFormat("0.00");
        int i,j;
        try {
            fw= new java.io.FileWriter(file2save,true);
            bw= new java.io.BufferedWriter(fw);
            out = new java.io.PrintWriter(bw);
            for(i=0;i<e.length;i++){
                for(j=0;j<e[0].length;j++)
                    s=s+decim.format(e[i][j])+",";
                out.println(s);
                s="";
            }
        }catch (java.io.IOException err) {
            System.err.println(err);
        }finally{
            if(out != null){
                out.close();
            }
        }
    }
    
    @Override
    public String toString()
    {
        String s= new String();
        s="training: "+rsTrain.size()+" instances ";
        if(rsTest!=null)s=s+"and testing: "+rsTest.size()+". ";
        int i;
        for(i=0;i<atts.size();i++)
            s=s+atts.get(i).title;
        
        return s;
    }
    
}
