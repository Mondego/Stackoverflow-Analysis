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
            //Write your code – most important part. 
            //Here you can write classes and methods into the string variable. We will add only Main method:


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
                    sb.AppendLine(String.Format("Error ({0}): {1}", error.ErrorNumber, error.ErrorText));
                }

                // throw new InvalidOperationException(sb.ToString());

             //   Console.WriteLine(sb.ToString());
                return sb.ToString();
            }
            return "";

        }

        public static int compileTest(string inFile, string outFile, string uncompilableFile)
        {
            /* // for xml files

              XmlTextReader reader = new XmlTextReader("C:\\Users\\Di\\Desktop\\csharp_snippets\\postaa_snippet.xml");
              string contents = "";
              while (reader.Read())
              {
                  switch (reader.Name.ToString())
                  {
                      case "snippet":
                          //   Console.WriteLine("snippet is:" + reader.ReadString());
                          string snippet = reader.ReadString();
                          if (compilable(snippet))
                          {
                              contents = contents + count + "------" + snippet + "\n";
                              count++;
                          }
                          break;

                  }

              }
              System.IO.File.WriteAllText(@"C:\\Users\\Di\\Desktop\\postaa_pass.txt", contents);
              Console.WriteLine(count);
              Console.ReadLine();
       */

            int compilableCount = 0;

            // for csv file
            StreamReader streamReader = new StreamReader(inFile);
            string text = streamReader.ReadToEnd();
            streamReader.Close();
            DataTable inTable = CsvReader.Parse(text);
            DataTable outTable = new DataTable();
            outTable.Columns.Add("Id", typeof(string));
            outTable.Columns.Add("error", typeof(string));
            DataTable uncompilableTable = new DataTable();
            uncompilableTable.Columns.Add("Id", typeof(string));
            uncompilableTable.Columns.Add("error", typeof(string));

            for (int i = 0; i < inTable.Rows.Count; i++)
            {
                string snippet = (string)inTable.Rows[i][1];

                // clean snippet
                snippet = XmlConvert.DecodeName(snippet);
                snippet = snippet.Replace("&#xA;", "\n").Replace("<br>", "\n");


                string errmsg = compilable(snippet);

                if (!errmsg.Equals(""))
                {
                    errmsg = errmsg.Trim();
                    object[] array = errmsg.Split('\n');
                    foreach (object a in array)
                    {
                        outTable.Rows.Add(inTable.Rows[i][0], a);
                    }

                    uncompilableTable.Rows.Add(inTable.Rows[i][0], inTable.Rows[i][1]);
                }
                else
                {
                    Console.WriteLine(inTable.Rows[i][0]);
                    compilableCount++;
                }
            }

            string output1 = CsvWriter.DataTableToCsv(outTable, false, true);
            string output2 = CsvWriter.DataTableToCsv(uncompilableTable, false, true);
            StreamWriter streamWriter = new StreamWriter(outFile);
            streamWriter.Write(output1);
            streamWriter.Flush();
            streamWriter.Close();
            streamWriter = new StreamWriter(uncompilableFile);
            streamWriter.Write(output2);
            streamWriter.Flush();
            streamWriter.Close();
            Console.WriteLine(compilableCount);
            return compilableCount;

            /*   // single snippet test 

                 string snippet = "List<KeyValuePair<string, string>> myList = aDictionary.ToList();&#xA;&#xA;myList.Sort(&#xA;    delegate(KeyValuePair<string, string> firstPair,&#xA;    KeyValuePair<string, string> nextPair)&#xA;    {&#xA;        return firstPair.Value.CompareTo(nextPair.Value);&#xA;    }&#xA;);&#xA;";
                 // clean snippet
                 snippet = XmlConvert.DecodeName(snippet);
                 snippet = snippet.Replace("&#xA;", "\n").Replace("<br>", "\n");
                // Console.WriteLine(snippet);
                 DataTable outTable = new DataTable();
                 outTable.Columns.Add("Id", typeof(string));
                 outTable.Columns.Add("error", typeof(string));

                 string errmsg = compilable(snippet);
                 errmsg = errmsg.Trim();
                 object[] array = errmsg.Split('\n');
                 foreach (object a in array)
                 {
                    // Console.WriteLine(a);           
                    outTable.Rows.Add(1, a);                      
                 }
                 string output = CsvWriter.DataTableToCsv(outTable, false, true);
                 StreamWriter streamWriter = new StreamWriter("C:\\StackOverflow\\C#_compileError\\postaa.csv");
                 streamWriter.Write(output);
                 streamWriter.Flush();
                 streamWriter.Close();
             //    Console.Read();
             */
        }

   



        static void Main(string[] args)
        {
            string[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
                "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
                "as", "at", "au", "av", "aw", "ax", "ay", "az" };

            string inFile = "";
            string outFile = "";
            string uncompilableFile = "";

            DataTable countTable = new DataTable();
            countTable.Columns.Add("file", typeof(string));
            countTable.Columns.Add("compileCount", typeof(string));
            foreach (String post in postSplits)
            {
                Console.WriteLine("processing post" + post);
                inFile = "C:\\StackOverflow\\C#_snippet\\post" + post + "_snippet.csv";
                outFile = "C:\\StackOverflow\\C#_compileError\\post" + post + "_error.csv";
                uncompilableFile = "C:\\StackOverflow\\C#_uncompilableSnippet\\post" + post + "_snippet.csv";
                countTable.Rows.Add("post" + post, compileTest(inFile, outFile, uncompilableFile));
            }


            string output = CsvWriter.DataTableToCsv(countTable, false, true);
            StreamWriter streamWriter = new StreamWriter("C:\\StackOverflow\\C#_compileError\\compilableCount.csv");
            /*
            foreach (String post in postSplits)
            {
                Console.WriteLine("processing post" + post + " after remove......");
                inFile = "C:\\StackOverflow\\C#_remove\\post" + post + "_remove.csv";
                outFile = "C:\\StackOverflow\\C#_compileErrorAfterRemove\\post" + post + "_error.csv";
                countTable.Rows.Add("post" + post, compileTest(inFile, outFile));
            }
            

            string output = CsvWriter.DataTableToCsv(countTable, false, true);
            StreamWriter streamWriter = new StreamWriter("C:\\StackOverflow\\C#_compileErrorAfterRemove\\compilableCountAfterRemove.csv");
            */
            /*
                        foreach (String post in postSplits)
                        {
                            Console.WriteLine("processing post" + post + " after add class header......");
                            inFile = "C:\\StackOverflow\\C#_addClassHeader\\post" + post + "_addClassHeader.csv";
                            outFile = "C:\\StackOverflow\\C#_compileErrorAfterAddClassHeader\\post" + post + "_error.csv";
                            countTable.Rows.Add("post"+post, compileTest(inFile, outFile));
                        }

                        string output = CsvWriter.DataTableToCsv(countTable, false, true);
                        StreamWriter streamWriter = new StreamWriter("C:\\StackOverflow\\C#_compileErrorAfterAddClassHeader\\compilableCountAfterAddClassHeader.csv");
            */
            /*          foreach (String post in postSplits)
                        {
                            Console.WriteLine("processing post" + post + " after add semicolon......");
                            inFile = "C:\\StackOverflow\\C#_addSemicolon\\post" + post + "_addSemicolon.csv";
                            outFile = "C:\\StackOverflow\\C#_compileErrorAfterAddSemicolon\\post" + post + "_error.csv";
                            countTable.Rows.Add("post" + post, compileTest(inFile, outFile));
                        }

                        string output = CsvWriter.DataTableToCsv(countTable, false, true);
                        StreamWriter streamWriter = new StreamWriter("C:\\StackOverflow\\C#_compileErrorAfterAddSemicolon\\compilableCountAfterAddSemicolon.csv");
             */
            streamWriter.Write(output);
            streamWriter.Flush();
            streamWriter.Close();
            Console.ReadLine();
        }
    }
}



