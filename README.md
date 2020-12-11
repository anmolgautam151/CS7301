# CS7301
1)The folder a0 contains the assignment 1. 
    The Java8 folder contains all the files regarding Java8 part of the assignment. 
    Use the following to setup the antlr4 for running the files
    $ sudo curl -O http://www.antlr.org/download/antlr-4.7-complete.jar
    $ mv antlr-4.7-complete.jar /usr/local/lib/
    $ export CLASSPATH="/usr/local/lib/antlr-4.7-complete.jar:$CLASSPATH"
    All the modified files and generated files are included in this folder as well. 
    The hello folder contains the initial setup testrun files of the assingment.
    
2) The folder a1 contains the assignment 2.
      All the modified files can be found under the src folder.
      To run the codes provided use the following : 
          sudo curl https://soot-build.cs.uni-paderborn.de/public/origin/master/soot/soot-master/3.3.0/build/sootclasses-trunk-jar-with-dependencies.jar -o soot-3.3.0.jar
      And the add the downloaded file to the build path of the project. The file is already downlaoded and provided under lib folder.
      The output generated is in under the folder sootOutput.
      Run the codes provided under src folder for verification.
