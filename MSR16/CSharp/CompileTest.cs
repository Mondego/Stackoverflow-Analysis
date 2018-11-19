using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.IO;

using Microsoft.CSharp;
using System.CodeDom.Compiler;
using System.Xml;
using Ast.OpenCsv;

namespace CompileTest
{
    class CompileTest
    {
        // compile each snippet, log the errors, output the count of compilable snippets


        public static string compilable(string code)
        {

            //Create the provider and parameters of the compiler
            CSharpCodeProvider provider = new CSharpCodeProvider();
            CompilerParameters parameters = new CompilerParameters();


            //Define parameters of the compiler (optional) – at this point, we can add a reference to external libraries. 
            //We can also define whether our compiled code will be generated only in the memory or into the DLL or EXE file
            // Reference to System.Drawing library
            parameters.ReferencedAssemblies.Add("System.Drawing.dll");
            // True - memory generation, false - external file generation
            parameters.GenerateInMemory = true;
            // True - exe file generation, false - dll file generation
            parameters.GenerateExecutable = true;

            //compile assembly
            CompilerResults results = provider.CompileAssemblyFromSource(parameters, code);


            //check errors
            if (results.Errors.HasErrors)
            {
                StringBuilder sb = new StringBuilder();

                foreach (CompilerError error in results.Errors)
                {
                    sb.AppendLine(String.Format("error {0}: {1}", error.ErrorNumber, error.ErrorText));
                }

                // throw new InvalidOperationException(sb.ToString());

                //   Console.WriteLine(sb.ToString());
                return sb.ToString();
            }
            return "";

        }

        public static int compileTest(string inFile, string outFile1)
        {

            int compilableCount = 0;

            string[] lines = System.IO.File.ReadAllLines(inFile);

            foreach (string line in lines)
            {
                string[] elements = line.Split(new string[] { "Di2015UniqueSeparator" }, StringSplitOptions.None);
                string snippet = XmlConvert.DecodeName(elements[1]);

                snippet = snippet.Replace("Di2015NewLine", "\n").Replace("<br>", "\n");

                string errMsg = "";
                if (snippet != null)
                {
                    errMsg = compilable(snippet);
                }


                using (System.IO.StreamWriter file1 = new System.IO.StreamWriter(outFile1, true))
                {


                    if (errMsg != null && errMsg != "")
                    {

                        file1.Write(errMsg);// log the error messages


                    }
                    else
                    {
                        compilableCount++;
                    }



                }
            }
            Console.WriteLine(compilableCount);
            return compilableCount;
        }





        static void Main(string[] args)
        {
            string[] postSplits = {
                  "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
                  "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
                  "as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
                  "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl",
                  "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv",
                  "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
                  "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp",
                  "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz",
                  "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
                  "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt",
                  "du", "dv" };



            string inFile = "";
            string outFile1 = "";


            // initial compile
            using (System.IO.StreamWriter file = new System.IO.StreamWriter("C:\\StackOverflow\\C#\\compileErrorInitial\\compilableCountInitial.txt"))
            {
                foreach (String post in postSplits)
                {
                    Console.WriteLine("processing post" + post + " ......");
                    inFile = "C:\\StackOverflow\\C#\\parsableInitial\\post" + post + "_parsable.txt";
                    outFile1 = "C:\\StackOverflow\\C#\\compileErrorInitial\\post" + post + "_error.txt";

                    file.WriteLine("post" + post + "Di2015UniqueSeparator" + compileTest(inFile, outFile1));
                }
            }


            /*
            // compile after remove
                                using (System.IO.StreamWriter file = new System.IO.StreamWriter("C:\\StackOverflow\\C#\\compileErrorAfterRemoves\\compilableCountAfterRemoves.txt"))
                                {
                                    foreach (String post in postSplits)
                                    {
                                        Console.WriteLine("processing post" + post + " ......");
                                        inFile = "C:\\StackOverflow\\C#\\parsableAfterRemoves\\post" + post + "_parsable.txt";
                                        outFile1 = "C:\\StackOverflow\\C#\\compileErrorAfterRemoves\\post" + post + "_error.txt";
                                        file.WriteLine("post" + post + "Di2015UniqueSeparator" + compileTest(inFile, outFile1));
                                    }
                                }
              */
            /*
          // compile after fix2
          using (System.IO.StreamWriter file = new System.IO.StreamWriter("C:\\StackOverflow\\C#\\compileErrorAfterFix2\\compilableCountAfterFix2.txt"))
          {
              foreach (String post in postSplits)
              {
                  Console.WriteLine("processing post" + post + " ......");
                  inFile = "C:\\StackOverflow\\C#\\parsableAfterFix2\\post" + post + "_parsable.txt";
                  outFile1 = "C:\\StackOverflow\\C#\\compileErrorAfterFix2\\post" + post + "_error.txt";
                  file.WriteLine("post" + post + "Di2015UniqueSeparator" + compileTest(inFile, outFile1));
              }
          }
        */
        }


    }


}



