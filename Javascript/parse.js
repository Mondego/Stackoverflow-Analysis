
var content = read(“/Users/Di/Desktop/js_snippets.json“);
var obj = JSON.parse(content);
var count=0; 
for(var i=0; i<2211;i++){ //total count of snippets
var s = obj.ROW[i].snippet;  
try{ Reflect.parse(s);}
catch(error){count=count+1;}
}
print(count);	

//Reflect.parse(s): building AST, only syntax errors
//evalcx(s): all errors, including reference error, type error, etc