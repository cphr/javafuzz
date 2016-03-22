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
