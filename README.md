# javafuzz

<p>JavaFuzz is a java classes fuzzer based on the the Java Reflection API. The reflection API represents, or reflects, the classes, interfaces, and objects in the current Java Virtual Machine. Using the reflection API it can contruct and invoke any given class (or list of classes). After getting the types that a class accepts will construct the class using expected and/or inappropriate values. If the option -m is issued then all the methods will be invoked as well. Have look in the Examples article.

Note : Check the repository for the latest version, Downloads get the latest packaged version, not the latest available version. For example the latest code base has a fix for the variable position brute forcing option.
</p>

* Latest version : 0.7.5

* Download: https://github.com/cphr/javafuzz/releases/tag/0.7.5

###### Changes: FIXES/UPDATES

* Version >= 0.7 Replace a constructor, the flag is -g (with automation) Speed Improvements

* Enforce a Constant and bruteforce the position. The type can be int,double,float,short,string
flag is -p

* Version >= 0.6 You can filter in and out method(s) and you can supply multiple types with -k Minor error handling fixes

* Version >= 0.5 The bugs listed in <=0.3 are fixed. If you find the same problem let me know

Manual
------

    NAME
         JavaFuzz - Java Class Fuzzer

    SYNOPSIS
         JavaFuzz.jar [-v] -c class [-e type] [-l StringLength]...

    DESCRIPTION
         JavaFuzz is a java classes fuzzer based on the the Java Reflection
         API. The reflection API represents, or reflects, the classes, interfaces,
         and objects in the current Java Virtual Machine. Using the reflection
         API JavaFuzz can contruct and invoke any given class (or list of classes).
         After getting the types that a class accepts will construct the classes using
         large values.


    OPTIONS
         -v   Verbose - Fully Print Exceptions. Very usuafull and you better use
              that if you want to spot any weird exceptions.

         -m   Fuzz methods of a Class, Can take Long time to finish

         -c   Classname
              Input is Class name e.g java.net.URL , you cannot use -f at the same
              time.

         -f   Read Class names from a file. Classnames should be on in each line.

         -s   You can set the fuzzing String, for example http://www.example.com
              if you dont want repeats, use it with -l1

         -e   You can set the type you want to overflow with the MAX_VALUE on top
              for example if you want to pass twice the size of a double to a class
              which is defined to accept only double you do "-e double"
              Warning: If you do that with an integer it will overflow  and
              become -2.
              Values can be : int, double, float, long, short.

         -r   Number of recursions until constructs the class [Default 20]
              If needs more it will set type to null and consider it Infinite.
              Usually when trying to construct types that dont get any arguments
              it will be fine, if it  will try to construct classes that their
              types accept arguments and so on... JavaFuzz will keep constructing
              types until it gets the asked types.

         -k   Set the value for int,float,long,short,double
              e.g. -k int=100  or -k double=20000 or -k int=19,float=49 and so on.

         -a   Set size of used array when fuzzing  [Default 800]
              This option can be maximum Integer.MAX_VALUE

         -l   Set length of used Strings when fuzzing [Default 1024]
              This option can be maximum Integer.MAX_VALUE

         -o   Find if a specific class requires a cosntant and brute-force
              all possible possitions until the constant is in the correct
              positiont. [This option will add further delays]

         -i   JavaFuzz will ignore the specified method(s) helpful when you found a bug
              in a method but you want to dig deeper. (Seperate methods with commas)
              e.g. for java.awt.Image you could use -i getGraphics,getScaledInstance

         -n   JavaFuzz will fuzz the specified method(s) only
              e.g. for java.awt.Font you could use -n applySize,pDispose
              NOTE: You cannot use -i at the same time

         -u   Fuzz only high or low values respectively e.g. Integer high is +MAX_VALUE
              and low value is -MAX_VALUE (or MIN_VALUE) [-u low or -u high]

        - p   Enforce a Constant and bruteforce the position.  Thetype can
              be int,double,float,short,string   e.g. -p double=1

         -g   Use it when you want to replace a class, for example it could be used to replace
              abstract classes or interfaces -g org.replace.this=org.with.this
              the auto replacement mode can be invoked using -g org.replace.this={A}
              and for complete automation use -ga



    EXAMPLES

             java -jar JavaFuzz.jar -c java.lang.String -v
             java -jar JavaFuzz.jar -f classes.txt -v -e int
             java -jar JavaFuzz.jar -c java.net.URL -e int -s http://www.example.com

    BUGS
             Version <= 0.3
             It cannot construct classes with types :
             a) Multidimensional array that is not int,double,float,short,long,string
             b) Array that is not int,double,float,short,long,string

    FIXES/UPDATES
             Version >= 0.7
             Enforce a Constant and bruteforce the position.  The type can be int,double,float,short,string
             flag is -p

             Version >= 0.6
             You can filter in and out method(s) and you can supply multiple types with -k
             Minor error handling fixes

             Version >= 0.5
             The bugs listed in <=0.3 are fixed. If you find the same problem let me know

    AUTHOR
         Emmanouel Kellinis <me at cipher dot org dot uk>


License
-------

The code is under GPLv2 unless specified otherwise in the single files.

#JavaFuzz examples

A small example on how to spot bugs in Java using JavaFuzz:

```java
java -jar JavaFuzz.jar -c java.math.BigInteger -v
Constructor ->  java.math.BigInteger
Types ->        ( int java.util.Random )
Invoke ->       java.math.BigInteger
[MAX] Status ->         Exception(java.lang.NegativeArraySizeException)
[MIN] Status ->         Exception(java.lang.IllegalArgumentException: numBits must be non-negative)
```


So by looking at the exception we know that something is not right. Why?
because we construct a BigInteger using an Integer (2147483647) and java.util.Random
and we get a NegativeArraySizeException exception.

How is this possible when we supplied a positive integer which is a "valid" type?

Well, JavaFuzz tried to create a BigIntiger using the Maximum and Minimum possible values of an 
integer, by doing that we do not leave any space for the number to grow more during the BigIntiger 
construction, and therefore if during the process there is any "mis-calculation" we get an Integer 
overflow.

This specific bug is not exploitable, but you get the idea.
Valid in 1.5.0_10-b03 and prior, now it is fixed
http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5050278

By trying diferrent numbers close to the Integer.MAX_VALUE (2147483647) we can find the exact 
number that will create the overflow, in our case it is 2147483641 and over.


If you compile and run the code below you will get the NegativeArraySizeException
```java
public class Main {

public Main() {
   }
   public static void main(String[] args) {
       long lmax =  (2^63)-1;
       int  imax =  2147483641;
       try {
       		new java.math.BigInteger(imax,new java.util.Random(lmax));
       }
       catch(Exception e){System.out.println(e);}
   }
} 
```

Another similar one is with  java.awt.image.BufferedImage( int int int )


```java
for width x height = 46341 46341

Java will throw :
Exception: java.lang.NegativeArraySizeException
```
```java
JavaFuzz options for the above are :
java -Xmx500M -jar JavaFuzz.jar -c java.awt.image.BufferedImage -o -a 10 -k int=46341 -v 
```
