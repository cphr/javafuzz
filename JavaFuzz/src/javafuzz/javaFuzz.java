/*
 * Maintainer: Emmanouel Kellinis (me@cipher.org.uk) 
 * Java Classes Fuzzer - Reflection Based
 * http://www.cipher.org.uk
 */

package javafuzz;

import gnu.getopt.Getopt;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.*;

/**
 *
 * @author 
 */
public class javaFuzz {
    
    /** Creates a new instance of javaFuzz */
    public javaFuzz()  {
       
            
    }
    
    /**
     * @param args the command line arguments
     */
    public static int Exceed =0;
    //Global Overflowing Data
    public static  int    ExceedInt   =0;// Integer.MAX_VALUE;
    public static  double ExceedDouble=0;// Double.MAX_VALUE;
    public static  float  ExceedFloat =0;// Float.MAX_VALUE;
    public static  short  ExceedShort =0;// Short.MAX_VALUE;
    public static  long   ExceedLong  =0;// Short.MAX_VALUE;
    //Recursion 
    public static  int   Recursion  =20;// Default
    //Array Size
    public static  int   ArraySize  =800;// Default
    //String size
     public static  int   StringSize  =1024;// Default
    public static String Start="";

public static void main(String[] args) {
        String[] argv= args;
        Getopt g = new Getopt("JavaFuzz", argv, ":vf:c:e:s:r:a:l:");
int c;
String arg;
int vv=0,rr=0;
String ff="",ee="",cc="",ss="";

 while ((c = g.getopt()) != -1)
   {
     switch(c)
       {
          case 'v':
             //Verbose
            vv=1;
            break;
          case 'f':
            //Classes File
            arg = g.getOptarg();
            ff=arg;
            break;
          case 'c':
            //Class
            arg = g.getOptarg();
            cc=arg;
            break;
          case 'e':
            //Extend
            arg = g.getOptarg();
            ee=arg;
            break;
          case 'r':
            //Recursions
            arg = g.getOptarg();
            try {Recursion= Integer.parseInt(arg);} catch (Exception e){usage();System.exit(0);}
            break;
          case 'a':
            //Array Size
            arg = g.getOptarg();
            try {ArraySize= Integer.parseInt(arg);} catch (Exception e){usage();System.exit(0);}
            break;
          case 'l':
            //String Size
            arg = g.getOptarg();
            try {StringSize= Integer.parseInt(arg);} catch (Exception e){usage();System.exit(0);}
            break;
          case 's':
           //String
            arg = g.getOptarg();
            ss=arg;
            Start=ss;
            break;
          case ':':
            usage();
            break;
          case '?':
            //usage();  
            break; // getopt() already printed an error
            //
         default:
            usage();
            break;
       }
   }
         if     (ee.equals("int"))      { ExceedInt    = Integer.MAX_VALUE;}
         else if(ee.equals("double"))   { ExceedDouble = Double.MAX_VALUE; }
         else if(ee.equals("float"))    { ExceedFloat  = Float.MAX_VALUE;  }
         else if(ee.equals("short"))    { ExceedShort  = Short.MAX_VALUE;  }
         else if(ee.equals("long"))     { ExceedLong   = Long.MAX_VALUE;   }

       if ((!ff.equals("") && !cc.equals(""))||(ff.equals("") && cc.equals(""))){usage();}
       else {
    
       if      (!cc.equals("")) {
                try {   summarize(cc,vv);
                } catch (Exception ex) {
                  usage();
                  System.out.println("+Invalid Class");
                }}
       else if (!ff.equals("")) {
                try {   recursiveAttack(ff,vv);
                } catch (Exception ex) {
                  usage();
                  System.out.println("+Classes File ERROR");
                }}
       }
        
       
        
    }
   
  public static void summarize(String className, int v)
    throws Exception
  { 
        Exceed=1;
   Class cls = Class.forName(className);
    Constructor[] a = cls.getConstructors();
    Object[] args ;
     System.out.println("--------------------------------------");
   for (int f=0;f<a.length;f++){
       Class[] ff =  a[f].getParameterTypes();
       Class[] types =  ff ;
       System.out.print("Constructor -> \t"+a[f].getName()+"\nTypes -> \t(");
       for (int k=0;k<ff.length;k++)
       {System.out.print(" "+ff[k].getName());}
       System.out.print(" )\n");
       System.out.println("Invoke -> \t"+className);
       Constructor cons = cls.getConstructor(types);
       //High Values
       args =  slapObject(ff,1,Exceed) ;
       System.out.print("[MAX] Status -> \t");
       try {
       if (args.length>0){cons.newInstance(args);}
       else {cons.newInstance();}
       System.out.print("[MAX] No Problem\n");
       }
       catch(Exception e){
       if (v==1){    System.out.print("Exception("+e.getCause() +")\n");}
       else {System.out.print("Exception\n");}
       }
       //Low Values
       args =  slapObject(ff,0,Exceed) ;
       System.out.print("[MIN] Status -> \t");
       try {
       if (args.length>0){cons.newInstance(args);}
       else {cons.newInstance();}
       System.out.print("[MIN] No Problem\n");
       }
       catch(Exception e){
       if (v==1){    System.out.print("Exception("+e.getCause() +")\n");}
       else {System.out.print("Exception\n");}
       }
     
       System.out.println("--------------------------------------");
       
      
   }
       
   
 
     
  }  
  public static int  etternalLoop=0;
    public static Object[] slapObject (Class[] cls,int hilow,int E) throws Exception{
        etternalLoop++;;
      
        
        
    Object[] list = new Object[cls.length];
    //int ArraySize = 100000;
    //Exceed normal E=1 / Normal E=0
    E=0;
    //TYPES: byte,short,int,long,float,double,boolean,char,string
    //Limits : byte -128 and a maximum value of 127 (inclusive)
    byte bmin = -128;
    byte bmax = 127;
    byte[] ab= new byte[ArraySize];//{bmax,bmin};
    //Multi-dimensional arrays -- monkey business at the moment will do it more genericly soon
    byte[][] abb= new byte[ArraySize][ArraySize];//{bmax,bmin};
    //Limits : short -32,768 and a maximum value of 32,767
    short smin = Short.MIN_VALUE;;
    short smax = Short.MAX_VALUE;;
    short[] as= new short[ArraySize];//{smax,smin};
    short[][] ass= new short[ArraySize][ArraySize];//{smax,smin};
    //Limits : int minimum value of -2,147,483,648 and a maximum value of 2,147,483,647 
    int imin = Integer.MIN_VALUE;;
    int imax = Integer.MAX_VALUE;;
    int[] ai= new int[ArraySize];//{imax,imin};
    int[][] aii= new int[ArraySize][ArraySize];//{imax,imin};
    //Limits : long minimum value of -9,223,372,036,854,775,808 and a maximum value of 9,223,372,036,854,775,807
    long lmin= Long.MIN_VALUE;;
    long lmax= Long.MAX_VALUE;;
    long[] al= new long[ArraySize];//{lmax,lmin};
    long[][] all= new long[ArraySize][ArraySize];
    //Limits : float  single-precision 32-bit IEEE 754 floating point
    float fmin=Float.MIN_VALUE;;
    float fmax=Float.MIN_VALUE;;
    float[] af= new float[ArraySize];//{fmax,fmin};
    float[][] aff= new float[ArraySize][ArraySize];//{fmax,fmin};
    //Limits : double double-precision 64-bit IEEE 754 floating point
    double dmin=Double.MIN_VALUE;;
    double dmax=Double.MAX_VALUE;;
    double[] ad= new double[ArraySize];//{dmax,dmin};
    double[][] add= new double[ArraySize][ArraySize];//{dmax,dmin};
    //Limits : boolean true/false - this one doesnt make much sense but anyways
    boolean bomin=false;
    boolean bomax=true;
    boolean[] abo= new boolean[ArraySize];//{bomax,bomin};
    boolean[][] aboo= new boolean[ArraySize][ArraySize];//{bomax,bomin};
    //Limits : char '\u0000' to '\uffff' 
    char cmin='\u0000';
    char cmax='\uffff';
    char[] ac= new char[ArraySize];//{cmax,cmin};
    char[][] acc= new char[ArraySize][ArraySize];//{cmax,cmin};
    //Limits : string 
    String stmin ="1";
    //Strinh SIZE
    String stmax = BigString("1",StringSize);
    stmax = Start+stmax;
    String[] ast = new String[ArraySize];//{stmin,stmax};
    String[][] astt = new String[ArraySize][ArraySize];//{stmin,stmax};
    
    
    for (int k=0;k<cls.length;k++){
    String current = cls[k].getName();
     
    boolean max=false;
    if(hilow==1){max=true;}
    if (current.equals("int")) {
        if(max){list[k]=(imax+ExceedInt);}
        else {list[k]=imin-(ExceedInt);}
    }
    else if (current.equals("[I")){list[k]=ai;}
    else if (current.equals("[[I")){list[k]=aii;}
    else if (current.equals("char")){
        if(max){list[k]=cmax;}
        else {list[k]=cmin;}
   }
    else if (current.equals("[C")){list[k]=ac;}
    else if (current.equals("[[C")){list[k]=acc;}
    else if (current.equals("float")){
        if(max){list[k]=fmax+ExceedFloat;}
        else {list[k]=fmin-(ExceedFloat);}
   }
    else if (current.equals("[F")){list[k]=af;}
    else if (current.equals("[[F")){list[k]=aff;}
    else if (current.equals("short")){
        if(max){list[k]=smax+ExceedShort;}
        else {list[k]=smin-(ExceedShort);}
   }
    else if (current.equals("[S")){list[k]=as;}
    else if (current.equals("[[S")){list[k]=ass;}
    else if (current.equals("boolean")){
        if(max){list[k]=bomax;}
        else {list[k]=bomin;}
   }
    else if (current.equals("[Z")){list[k]=abo;}
    else if (current.equals("[[Z")){list[k]=aboo;}
    else if (current.equals("double")){
        if(max){list[k]=dmax+ExceedDouble;}
        else {list[k]=dmin-(ExceedDouble);}
   }
    else if (current.equals("[D")){list[k]=ad;}
    else if (current.equals("[[D")){list[k]=add;}
    else if (current.equals("long")){
        if(max){list[k]=lmax+ExceedLong;}
        else {list[k]=lmin-(ExceedLong);}
   }
    else if (current.equals("[J")){list[k]=al;}
    else if (current.equals("[[J")){list[k]=all;}
    else if (current.equals("byte")){
        if(max){list[k]=bmax;}
        else {list[k]=bmin;}
   }
    else if (current.equals("[B")){list[k]=ab;}
    else if (current.equals("[[B")){list[k]=abb;}
    else if (current.equals("java.lang.String")){
        if(max)
        {
            //Valid URL String list[k]="http://"+stmax+".com";
            list[k]=stmax;
        }
        else {list[k]=stmin;}
   }
    else if (current.equals("[Ljava/lang/String")){list[k]=ast;}
    else if (current.equals("[[Ljava/lang/String")){list[k]=astt;}
    else {
        //Exception from within - verbose=0/1
        int v=1;
        try {
        Class tmp = Class.forName(current);
        
        
        Constructor[] a = tmp.getConstructors();
        if (a.length >0){
        Object[] args ;
        Class[] ff =  a[0].getParameterTypes();
        Class[] types =  ff ;
        Constructor cons = tmp.getConstructor(types);
         if (etternalLoop<Recursion){args =  slapObject(ff,0,E) ;}
         else {System.out.println("****Class in Infinite Loop****"); args=null;}
            try {
             //   if (args.length==0){
                    list[k]=cons.newInstance(args);
                     
                //}
             //  else               {list[k]=null;}
                }
            catch(Exception e){ 
                if (v==1){   /*e.printStackTrace();*/System.out.print("Exception("+e.getCause() +")\n");}
                else {System.out.print("Exception\n");}
                }
       }
        
   }
        
        
        catch(Exception e){ /*e.printStackTrace();*/;}
   
     
    }
    

   
    }
    

     return list;
    
    }
    
    static String BigString (String str,int size){
    String tmp="";
    for (int a=0;a<size;a++){tmp=tmp+str;}
   return tmp; }
   
    
static void recursiveAttack(String FileName,int v) throws Exception {
    
     
                               
       FileInputStream fstream = new FileInputStream(FileName);
	 DataInputStream in = new DataInputStream(fstream);
             while (in.available() !=0)
		 { try {  javaFuzz.summarize(in.readLine(),v);}catch(Exception e){} }
            in.close();
         
}

    private static void usage() {
                System.out.println("\nJavaFuzzer - Classes Fuzzing (Reflection Based)\n");
               String output =
                                "\n"+"FLAGS"+
                                "\n"+"-v: Verbose - Fully Print Exceptions"+
                                "\n"+"-f: Read Class names from a file"+
                                "\n"+"-c: Input is Class name, you cannot use -f at the same time"+
                                "\n"+"-s: You can set the start of the fuzzing String, for example http://"+
                                "\n"+"-e: You can set the type you want to overflow with the MAX_VALUE on top "+
                                "\n"+"    Values can be : int or double or float or long or short"+
                                "\n"+"-r: Number of recursions until constructs the class [Default 20]"+
                                "\n"+"    If needs more it will set type to null and consider it Infinite"+
                                "\n"+"-a: Set size of used array when fuzzing  [Default 800]"+
                                "\n"+"-l: Set size of used String when fuzzing [Default 1024]"+
                                "\n\n"+"EXAMPLES"+
                                ""+""+
                                "\n"+"java -jar JavaFuzz.jar -c java.lang.String -v"+
                                "\n"+"java -jar JavaFuzz.jar -f classes.txt -v -e int"+
                                "\n"+"java -jar JavaFuzz.jar -c java.net.URL -e int -s http://";
               System.out.println(output);

    }
    
    
   
 
}

