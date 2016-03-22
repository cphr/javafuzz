#JavaFuzz examples

A small example on how to spot bugs in Java using JavaFuzz:

```
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
Valid in 1.5.0\_10-b03 and prior, now it is fixed
http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5050278

By trying diferrent numbers close to the Integer.MAX\_VALUE (2147483647) we can find the exact
number that will create the overflow, in our case it is 2147483641 and over.


If you compile and run the code below you will get the NegativeArraySizeException
```
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


```
for width x height = 46341 46341

Java will throw :
Exception: java.lang.NegativeArraySizeException
```

JavaFuzz options for the above are :
java -Xmx500M -jar JavaFuzz.jar -c java.awt.image.BufferedImage -o -a 10 -k int=46341 -v






