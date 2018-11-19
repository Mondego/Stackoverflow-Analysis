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
using System.Xml;

namespace ParseTest
{
    class ParseTest2
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

        public static int parseTest(string inFile, string outFile1, string outFile2, string outFile3)
        {
            int parsableCount = 0;

            string[] lines = System.IO.File.ReadAllLines(inFile);

            foreach (string line in lines){
                string[] elements = line.Split(new string[] { "Di2015UniqueSeparator" }, StringSplitOptions.None);

                string snippet = XmlConvert.DecodeName(elements[1]);

                snippet = snippet.Replace("Di2015NewLine", "\n").Replace("<br>", "\n");


                List<string> errmsg = parsable(snippet);

                using (System.IO.StreamWriter file1 = new System.IO.StreamWriter(outFile1, true))
                {
                    using (System.IO.StreamWriter file2 = new System.IO.StreamWriter(outFile2, true))
                    {
                        using (System.IO.StreamWriter file3 = new System.IO.StreamWriter(outFile3, true))
                        {
                            if (errmsg.Any())
                            {
                                foreach (string s in errmsg)
                                {
                                    file1.WriteLine(elements[0] + "Di2015UniqueSeparator" + s);// log the error messages
                                }
                                // store the unparsable snippets
                                file2.WriteLine(line);
                            }
                            else
                            {
                                // store the parsable snippets
                                file3.WriteLine(line);
                                parsableCount++;
                            }
                        }

                    }


                }
            }

            Console.WriteLine(parsableCount);
            return parsableCount;
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
             string outFile2 = "";
            string outFile3 = "";
           /* 
              // Initial parse
              using (System.IO.StreamWriter file = new System.IO.StreamWriter("C:\\StackOverflow\\C#\\parseErrorInitial\\parsableCountInitial.txt"))
                          {
                              foreach (String post in postSplits)
                              {
                                  Console.WriteLine("processing post" + post + " ......");
                                  inFile = "C:\\StackOverflow\\C#\\snippets\\post" + post + "_snippet.txt";
                                  outFile1 = "C:\\StackOverflow\\C#\\parseErrorInitial\\post" + post + "_error.txt";
                                  outFile2 = "C:\\StackOverflow\\C#\\unparsableInitial\\post" + post + "_unparsable.txt";
                                  outFile3 = "C:\\StackOverflow\\C#\\parsableInitial\\post" + post + "_parsable.txt";
                                  file.WriteLine("post" + post + "Di2015UniqueSeparator" + parseTest(inFile, outFile1, outFile2, outFile3));
                              }
                          }

              
                              // parse after remove single word

                                            using (System.IO.StreamWriter file = new System.IO.StreamWriter("C:\\StackOverflow\\C#\\parseErrorAfterRemoves\\parsableCountAfterRemoves.txt"))
                                            {
                                                foreach (String post in postSplits)
                                                {
                                                    Console.WriteLine("processing post" + post + " ......");
                                                    inFile = "C:\\StackOverflow\\C#\\removeSingleWord\\post" + post + "_remove.txt";
                                                    outFile1 = "C:\\StackOverflow\\C#\\parseErrorAfterRemoves\\post" + post + "_error.txt";
                                                    outFile2 = "C:\\StackOverflow\\C#\\unparsableAfterRemoves\\post" + post + "_unparsable.txt";
                                                    outFile3 = "C:\\StackOverflow\\C#\\parsableAfterRemoves\\post" + post + "_parsable.txt";
                                                    file.WriteLine("post" + post+ "Di2015UniqueSeparator" + parseTest(inFile,outFile1, outFile2, outFile3));
                                                }
                                            }

                     */

            
             // parse after fix
                                                  using (System.IO.StreamWriter file = new System.IO.StreamWriter("C:\\StackOverflow\\C#\\parseErrorAfterFix2\\parsableCountAfterFix2.txt"))
                                                  {
                                                      foreach (String post in postSplits)
                                                      {
                                                          Console.WriteLine("processing post" + post + " ......");
                                                          inFile = "C:\\StackOverflow\\C#\\fix2\\post" + post + "_fix2.txt";
                                                          outFile1 = "C:\\StackOverflow\\C#\\parseErrorAfterFix2\\post" + post + "_error.txt";
                                                          outFile2 = "C:\\StackOverflow\\C#\\unparsableAfterFix2\\post" + post + "_unparsable.txt";
                                                          outFile3 = "C:\\StackOverflow\\C#\\parsableAfterFix2\\post" + post + "_parsable.txt";
                                                          file.WriteLine("post" + post + "Di2015UniqueSeparator" + parseTest(inFile, outFile1, outFile2, outFile3));
                                                      }
                                                  }


    
    

            Console.ReadLine();

                 
        }
        

    }

    }





