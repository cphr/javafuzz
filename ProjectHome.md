JavaFuzz is a **java classes fuzzer** based on the the Java Reflection
API. The reflection API represents, or reflects, the classes, interfaces,
and objects in the current Java Virtual Machine. Using the reflection
API it can contruct and invoke any given class (or list of classes).
After getting the types that a class accepts will construct the class using
expected and/or inappropriate values. If the option **-m** is issued then
all the methods will be invoked as well. Have look in the [Examples](Examples.md) article.

Note : Check the subversion repository for the latest version, Downloads get the latest
packaged version, not the latest available version. For example the latest code base has a fix for the variable position brute forcing option.

Latest version : [0.7.5](http://javafuzz.googlecode.com/files/JavaFuzz-current.zip)