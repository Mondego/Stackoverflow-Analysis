This folder includes the source code and StackOverflow code snippets associated with the paper: From Query to Usable Code: An Analysis of Stack Overflow Code Snippets, in proceedings of MSR'16. The paper can be downloaded at https://arxiv.org/abs/1605.04464

Under each folder for different languages, you can find the following three parts:

The source code for parsing and compiling/running the code snippets.
The StackOverflow code snippets after preprocessing.
Auxiliary folder for extracting and preprocessing the StackOverflow code snippets.
In the source codes, we chose an interpreter for each language and used its built-in parser and compiler, or parsing and compiler/running functions, to evaluate the parsable and compilable/runnable rates for each language. Detailed description can be found in the paper.
