package org.sandrm.std_eight;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * flatMap Operation
 * 
 * unique words
 * 
 * Suppose you would like to find all unique words in a file. How would you go about it?
 * 
 * http://www.oracle.com/technetwork/articles/java/architect-streams-pt2-2227132.html
 *
 */
public class App {
	static Stream <String> words = Stream.of ("Java", "Журнал", "есть", "лучший");
	static final ClassLoader loader = App.class.getClassLoader();
	static Path path;
	
	
	static{
        String sURL = loader.getResource("testFile.txt").toString();
        System.out.println("fURL: " + sURL);
        
        URL url = loader.getResource("testFile.txt");
		File file = new File(loader.getResource("testFile.txt").getFile());
		
		System.out.println("File exists: " + file.exists());
		System.out.println("File length: " + file.length());

		
		String sPath = url.getPath();
        sPath = sPath.replaceFirst("^/(.:/)", "$1");
        path = Paths.get(sPath);
        
		System.out.println("Path created: " + path.toString());
		System.out.println("Lile length: " + path.toFile().length());
	}


	static final Predicate<Map.Entry<?, String>> valueNotNullOrEmpty = e -> e.getValue() != null && !e.getValue().isEmpty();
	//.filter(valueNotNullOrEmpty)

	//.filter(StringUtils::isNotBlank)
	
	public static class Strings {
		public static boolean isNotNullOrEmpty (String str) {
			return str != null && !str.isEmpty();
		}
	}

	
    public static void main( String[] args ){
    	System.out.println("");
    	System.out.println( "Hello Java 8!" );
    	System.out.println("");
        
        groupping();
        
		flatMapBad1();
		
		flatMapBad2();

		flatMapGood();
		
		System.out.println("");
		
		flatMap_Filter_Sorted();
    
    }

    
	private static void groupping() {
		Map<String, Long> letterToCount =
	           words.map(w -> w.split(""))
                   .flatMap(Arrays::stream)
                   .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		Set<Map.Entry<String, Long>> entries = letterToCount.entrySet();
		entries.forEach(System.out::println);
	}
	
	
	//may be used
	//	
	//.filter(line -> !StringUtils.isEmpty(line))
	//.filter(Strings::isNotNullOrEmpty)

	//Comparator<String> compByLength = (aName, bName) -> aName.length() - bName.length();
	//.sorted() alphabetical
	private static void flatMap_Filter_Sorted() {
		try {
			Files.lines(path)
				.filter(line -> StringUtils.isNotBlank(line))
				.sorted((aName, bName) -> bName.length() - aName.length())
				.map(line -> line.split("\\s+")) // Stream<String[]>
				.flatMap(Arrays :: stream)		 // Stream<String>
				.distinct()
				.forEach(System.out :: println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		.sorted((aName, bName) -> bName.length() - aName.length())		
//		десятилетия
//		Журнал
//		лучший
//		журнал
//		этого
//		Java
//		есть
		
	}


	private static void flatMapGood() {
		try {
			Files.lines(path)
				.map(line -> line.split("\\s+")) // Stream<String[]>
				.flatMap(Arrays :: stream)		 // Stream<String>
				.distinct()
				.forEach(System.out :: println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		Java
//		Журнал
//		есть
//		лучший
//
//		журнал
	}


	private static void flatMapBad2() {
		try {
			Files.lines(path)
				.map(line -> line.split("\\s+")) // Stream<String[]>
				.map(Arrays::stream) // Stream<Stream<String>>
				.distinct() // Stream<Stream<String>>
				.forEach(System.out::println);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		java.util.stream.ReferencePipeline$Head@404b9385
//		java.util.stream.ReferencePipeline$Head@6d311334
//		java.util.stream.ReferencePipeline$Head@682a0b20
//		java.util.stream.ReferencePipeline$Head@3d075dc0
	}


	private static void flatMapBad1() {
		try {
			//Files.lines(Paths.get("D:\\Dev\\neon_ws\\std-eight\\resources\\testFile.txt")) ;
			Files.lines(path)
				.map(line -> line.split("\\s"))
				.distinct()
				.forEach(System.out::println);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//		[Ljava.lang.String;@1b28cdfa
//		[Ljava.lang.String;@eed1f14
//		[Ljava.lang.String;@7229724f
//		[Ljava.lang.String;@4c873330
	}

}
