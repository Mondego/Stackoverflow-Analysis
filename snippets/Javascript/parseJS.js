// parse and log the error
var DiContent = read(“C:\\StackOverflow\\JS\\json\\postbr_snippet.json“);
var DiObj = JSON.parse(DiContent);

var DiCount=0; 

for(var DI=0; DI<8844;DI++){ //total count of snippets
	var DiStr = DiObj.DATA.ROW[DI].snippet; 
	//print(DI);
	//print(DiObj.DATA.ROW[DI].id);
	try{ 
		Reflect.parse(DiStr);
	}catch(DiError){ 
		print(DiError); 
		DiCount=DiCount+1;
	}
}
print(DiCount);	
// this is the number that does not parse



// run all snippets and log all errors
var DiContent = read(“C:\\StackOverflow\\JS\\json\\postas_snippet.json“);
var DiObj = JSON.parse(DiContent);

var DiCount=0; 

for(var DI=0; DI<7221;DI++){ //total count of snippets
	var DiStr = DiObj.DATA.ROW[DI].snippet; 
	print(DI);
	print(DiObj.DATA.ROW[DI].id);
	try{ 
		eval(DiStr);
	}catch(DiError){ 
		print(DiError); 
		DiCount=DiCount+1;
	}
}
print(DiCount);	
// this is the number that does not run




