# Introduction

```
This is essentially a copy of the C# IPInterface module from PC-EFTPOS. It doesn't have all the classes the C# does, but it has the ones that most POS vendors will use, and exempts the niche classes.
Most of the classes have explicit getters and setters for the properties. The setters are there because I was copying the C#, and the {get;set;} syntax didn't work. I realised some time through that it was unnecessary.
However, the JSON.org library I've included to make response displaying a bit easier requires the getProperty and isProperty methods to be defined in the class so it can work.
If you are using this for your POS, strictly speaking you don't need either of these methods because they're implicit (unless you want private properties).

There are two types of asynchronous sockets with their own PCEFTPOSControl classes.
The first one I did AsyncSocketThread uses AtomicIntegers as a way to control concurrency (?) but tbh I don't really get it.
It also had the issue of recursively calling the read function using the same buffer. This would be ok, but the buffer doesn't actually get cleared using the .clear() function, it just pretends to clear it.

I had a look around the internets and came across the template for the AsyncSocket class, which uses Selectors, Channels and Keys.
Tbh I don't really understand this one either but it looks a little nicer, it runs in its own thread and it doesn't keep the same buffer so I'm not getting the previous transaction receipt data in my purchaseAnalysisData field.

I've elected to keep them both in, just in case someone prefers the use of one over the other.

 - Samuel Coianiz
```

```aidl
# Getting Started

1.	Installation process

* Clone the repository.
* Open project.
* Build.


I built a .JAR file without the dialogs (and one with the dialogs) and was able to use the classes just fine.
In fact, using the JAR with the window classes, I could build the whole project with one method
"```
    public static void main([] args){
    AsyncMainWindow.main(null);
    }
"```
and it created the UI, the sockets, and I was able to carry on like I had the original project.

    - Samuel Coianiz
```