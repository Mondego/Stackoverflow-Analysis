using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.IO;
using Microsoft.CodeAnalysis;
using Microsoft.CodeAnalysis.CSharp;
using Microsoft.CodeAnalysis.CSharp.Symbols;
using Microsoft.CodeAnalysis.CSharp.Syntax;
using Microsoft.CodeAnalysis.Text;
using Ast.OpenCsv;

using System.Xml;

namespace ParseTest
{
    class ParseTest
    {
        public static List<string> parsable(string code)
        {
            List<string> errors = new List<string>();
            SyntaxTree tree = SyntaxFactory.ParseSyntaxTree(code);
            foreach (var diagnostic in tree.GetDiagnostics())
            {
                errors.Add(diagnostic.ToString());
            }
            return errors;

        }
        public static int parseTest(string inFile, string outFile, string unparsableFile)
        {
            /* for xml file
           int count = 0;
           XmlTextReader reader = new XmlTextReader("C:\\Users\\Di\\Desktop\\csharp_snippets\\postaa_snippet.xml");
           //string contents = "";
           while (reader.Read())
           {
               switch (reader.Name.ToString())
               {
                   case "snippet":
                       string code = reader.ReadString();
                       // Console.WriteLine("snippet is:" + code);

                       SyntaxTree tree = SyntaxFactory.ParseSyntaxTree(code);

                       if( tree.GetDiagnostics().Count() == 0)
                       {
                           count++;
                       }


                       break;

               }

           }
           Console.WriteLine(count);
           Console.ReadLine();
          **/
            int parsableCount = 0;

            // for csv file
            StreamReader streamReader = new StreamReader(inFile);
            string text = streamReader.ReadToEnd();
            streamReader.Close();
            DataTable inTable = CsvReader.Parse(text);
            DataTable outTable = new DataTable();
            outTable.Columns.Add("Id", typeof(string));
            outTable.Columns.Add("error", typeof(string));
            DataTable unparsableTable = new DataTable();
            unparsableTable.Columns.Add("Id", typeof(string));
            unparsableTable.Columns.Add("error", typeof(string));
            for (int i = 0; i < inTable.Rows.Count; i++)
            {
                string snippet = (string)inTable.Rows[i][1];

                // clean snippet
                snippet = XmlConvert.DecodeName(snippet);
                snippet = snippet.Replace("&#xA;", "\n").Replace("<br>", "\n");


                List<string> errmsg = parsable(snippet);

                if (errmsg.Any())
                {
                    foreach (string s in errmsg)
                    {
                        outTable.Rows.Add(inTable.Rows[i][0], s);
                    }
                    unparsableTable.Rows.Add(inTable.Rows[i][0], inTable.Rows[i][1]);
                }
                else
                {
                    Console.WriteLine(inTable.Rows[i][0]);
                    parsableCount++;
                }
            }

            string output1 = CsvWriter.DataTableToCsv(outTable, false, true);
            string output2 = CsvWriter.DataTableToCsv(unparsableTable, false, true);
            StreamWriter streamWriter = new StreamWriter(outFile);
            streamWriter.Write(output1);
            streamWriter.Flush();
            streamWriter.Close();
            streamWriter = new StreamWriter(unparsableFile);
            streamWriter.Write(output2);
            streamWriter.Flush();
            streamWriter.Close();
            Console.WriteLine(parsableCount);
            return parsableCount;


        }
        /*
        static void Main(string[] args)
        {
            string[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
                "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
                "as", "at", "au", "av", "aw", "ax", "ay", "az" };

            string inFile = "";
            string outFile = "";
            string unparsableFile = "";

            DataTable countTable = new DataTable();
            countTable.Columns.Add("file", typeof(string));
            countTable.Columns.Add("compileCount", typeof(string));
          
                        foreach (String post in postSplits)
                        {
                            Console.WriteLine("processing post" + post + " ......");
                            inFile = "C:\\StackOverflow\\C#_initClean\\post" + post + "_init.csv";
                            outFile = "C:\\StackOverflow\\C#_parseError\\post" + post + "_error.csv";
                            
                            unparsableFile = "C:\\StackOverflow\\C#_unparsableSnippet\\post" + post + "_unparsable.csv";
                            countTable.Rows.Add("post" + post, parseTest(inFile, outFile, unparsableFile));
                        }

                        string output = CsvWriter.DataTableToCsv(countTable, false, true);
                        StreamWriter streamWriter = new StreamWriter("C:\\StackOverflow\\C#_parseError\\parsableCount.csv");
           

            streamWriter.Write(output);
            streamWriter.Flush();
            streamWriter.Close();
            Console.ReadLine();
        }*/
    }

}





