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
public class JEMLA {

    /**
     * *The main method first loads the input file then calculates entropies of the specified attributes.
     * @param args :
     *      1- input file (txt or arff), 2- file for specifications of the input file , 
     *      3- output file, 4- list of attributes separated by comma or all, 
     *      5- letter i followed by type of handling missing values 
     */
    public static void main(String[] args) {
        
        String attList=new String("all");
        int ig=0;
        JEMLAModel theModel;
        if(args[0].contains("help")){
            System.out.println("param: (parameters of first to third are mandatory)");
            System.out.println("    input file"); 
            System.out.println("    file for specifications of the input file, if input file is an arff file then spec file is same as input file"); 
            System.out.println("    output file"); 
            System.out.println("    letter a followed by list of attributes separated by comma or all");
            System.out.println("    letter i followed by type of handling missing values");
            System.out.println("            0:ignoring "
                                        + " 1:replacement with most common value or mean"
                                        + " 2:replacement with common value or mean for given class "
                                        + " 3:fractional replacement"
                                        + " 4:replacement with common value or mean for given class "
                                        + " 5:preprocesses the data set and finds the global closest fit for that");
            System.exit(0);
        }
        if(args[0].endsWith("arff")){
            theModel= new JEMLAModel(args[0]);
        }
        else theModel= new JEMLAModel(args[0],args[1]);
        JEMLAController theController= new JEMLAController(theModel.getTrainData());
        theModel.setOutFile(args[2]);
        theModel.writeFile(args[0]);
        if(args.length>3){
            if(args[3].startsWith("a")) attList=args[3].substring(1, args[3].length());
            else if(args[3].startsWith("i")) ig=Integer.parseInt(args[3].substring(1, args[3].length()));
            else System.err.println("Error in forth parameter. please ask for help!!");
            if(args.length>4)
                if(args[4].startsWith("a")) attList=args[4].substring(1, args[4].length());
                else if(args[4].startsWith("i")) ig=Integer.parseInt(args[4].substring(1, args[4].length()));
                else System.err.println("Error in fifth parameter. please ask for help!!");
        }
        theModel.writeFile(theController.calculateEntropy(attList,ig));
    }
    
}
