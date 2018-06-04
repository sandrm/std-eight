package org.sandrm.std_eight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.sandrm.std_eight.model.City;
import org.sandrm.std_eight.model.Currency;
import org.sandrm.std_eight.model.Transaction;
import org.sandrm.std_eight.model.TransactionType;

import static java.util.stream.Collectors.*; 

public class AppStream {
	public static List<Transaction> transactions = new ArrayList<Transaction>();
	
	static{
		transactions.add(new Transaction(10, 300, TransactionType.GROCERY, Currency.UAH, City.KYIV));
		transactions.add(new Transaction(20, 20000, TransactionType.ELECTRONIC, Currency.USD, City.NY));
		transactions.add(new Transaction(21, 15000, TransactionType.ELECTRONIC, Currency.USD, City.NY));
		transactions.add(new Transaction(22, 5000, TransactionType.ELECTRONIC, Currency.USD, City.LOS_ANGELOS));
		transactions.add(new Transaction(30, 4000, TransactionType.DRESS, Currency.USD, City.LOS_ANGELOS));
		transactions.add(new Transaction(40, 400, TransactionType.GROCERY, Currency.UAH, City.KYIV));
		transactions.add(new Transaction(50, 500, TransactionType.GROCERY, Currency.UAH, City.KYIV));
	}
	
	
	public static void main(String[] args) {
		
		//filtering by type
		transactions.stream()
			.filter(e -> e.getType() == TransactionType.GROCERY)
			.forEach(System.out :: println);
		
		System.out.println("------------------");
		
		//sorting by value reversed
		transactions.stream()
			.filter(e -> e.getType() == TransactionType.GROCERY)
			.sorted((t1, t2) ->  Long.compare(t2.getValue(), t1.getValue()))	//reversed
			.forEach(System.out :: println);
		
		//get a list of the IDs for all the expensive transactions
		List<Long> expensiveTransactionsIds = transactions.stream()
			.filter(t -> t.getValue() > 1000)
			.map(Transaction :: getId)
			.collect(toList());
			
		expensiveTransactionsIds.stream()
			.forEach(System.out :: println);

		
/* Summarizing */
		
/* Listing 11 */
		//counting()
		long howManyTransactions = transactions.stream()
				.collect(Collectors.counting());
		
		System.out.println("Counting howManyTransactions = " + howManyTransactions);

		
		
/* Listing 12 */ 
		//summingInt()
		//(based on @FunctionalInterface)
		//@param mapper a function extracting the property to be summed
		//Collector<? super T, A, R> collector = Collectors.summingInt(mapper)
		int totalValue = transactions.stream()
				.filter(e -> e.getType() == TransactionType.GROCERY)
				.collect(Collectors.summingInt(Transaction::getValue));
		
		System.out.println("totalValue = " + totalValue);

		
		
/* Listing 13 */
		//averagingInt()
		double average = transactions.stream()
				.collect(Collectors.averagingInt(Transaction::getValue));
		
		System.out.println("average = " + average);
		System.out.println("------------------");


/* Listing 14 */
//using maxBy() and minBy() you can calculate the maximum and minimum element of a stream
//you need to define an order for the elements of a stream to be able to compare them
//static method comparing(), which generates a Comparator object from a function passed as an argument
		
//		Comparator<Transaction> transactionValueComparator = Comparator.comparing(Transaction::getValue);
//		Optional<Transaction> highestTransaction = transactions.stream()
//				.collect(Collectors.maxBy(transactionValueComparator));

		Optional<Transaction> highestTransaction = transactions.stream()
				.collect(Collectors.maxBy(Comparator.comparing(Transaction :: getValue)));

		System.out.println("highestTransaction = " + highestTransaction);
		
		//highestTransaction = Optional[Transaction [id=20, value=20000, type=ELECTRONIC]]

		System.out.println("------------------");


/* Listing 15 */
//reducing() {see too: reduce(), max(), and min()}
//combine all elements in a stream by repetitively applying an operation until a result is produced
		
		//alternative way to calculate the sum of all transactions using reducing()
		int totalValue_alternative = transactions.stream()
				.collect(Collectors.reducing(0, Transaction :: getValue, Integer::sum));
		
		System.out.println("totalValue_alternative = " + totalValue_alternative);
		
		//An initial value (it is returned if the stream is empty); in this case, it is 0.
		//A function to apply to each element of a stream; in this case, we extract the value of each transaction.
		//An operation to combine two values produced by the extracting function; in this case, we just add up the values.
		
		System.out.println("------------------");
		

/* Grouping */
		//before Java 8 style
		seeGrouppingJava7();
		
//groupingBy()
//@see #groupingBy(Function)
//@see #groupingBy(Function, Collector)		
		
//The groupingBy() factory method takes as an argument a function for extracting the key used to classify the transactions. We call it a classification function. 

/* Listing 17 */
//to group a list of transactions by currency		
		Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream()
				.collect(Collectors.groupingBy(Transaction :: getCurrency));
		
		System.out.println("transactionsByCurrencies = " + transactionsByCurrencies);
		/*
		 * transactionsByCurrencies = {UAH=[Transaction [id=10, value=300, type=GROCERY, currency=UAH], Transaction [id=40, value=400, type=GROCERY, currency=UAH], Transaction [id=50, value=500, type=GROCERY, currency=UAH]], 
		 *							USD=[Transaction [id=20, value=20000, type=ELECTRONIC, currency=USD], Transaction [id=21, value=15000, type=ELECTRONIC, currency=USD], Transaction [id=22, value=5000, type=ELECTRONIC, currency=USD], Transaction [id=30, value=4000, type=DRESS, currency=USD]]}
		 *
		 */
		System.out.println("------------------");

		
/* Partitioning */
//partitioningBy() 
//that can be viewed as a special case of groupingBy(). 
//It takes a predicate as an argument (that is, a function that returns a boolean) and groups the elements of a stream according to whether or not they match that predicate.		
	
/* Listing 18 */
// to group the transactions into two lists—cheap and expensive
		Map<Boolean, List<Transaction>> partitionedTransactions = transactions.stream()
				.collect(Collectors.partitioningBy(t -> t.getValue() > 1000));
		
		System.out.println("partitionedTransactions = " + partitionedTransactions);
		/*
		 * partitionedTransactions = {false=[Transaction [id=10, value=300, type=GROCERY, currency=UAH], Transaction [id=40, value=400, type=GROCERY, currency=UAH], Transaction [id=50, value=500, type=GROCERY, currency=UAH]], 
		 * 								true=[Transaction [id=20, value=20000, type=ELECTRONIC, currency=USD], Transaction [id=21, value=15000, type=ELECTRONIC, currency=USD], Transaction [id=22, value=5000, type=ELECTRONIC, currency=USD], Transaction [id=30, value=4000, type=DRESS, currency=USD]]}
		 */
		System.out.println("------------------");

		
/* Composing collectors. */
//overloaded version of groupingBy() that takes another collector object as a second argument
//@see #groupingBy(Function, Collector)			

/* Listing 19 */
		Map<City, Integer> cityToSum = 
		           transactions.stream()
		           .collect(Collectors.groupingBy(Transaction::getCity, Collectors.summingInt(Transaction::getValue)));
		
		System.out.println("cityToSum = " + cityToSum);
		
		//Here, we tell groupingBy to use the method getCity() as a classification function. 
		//As a result, the keys of the resulting Map will be cities.
		/*
		 * cityToSum = {NY=35000, KYIV=1200, LOS_ANGELOS=9000}
		 */
		System.out.println("------------------");
		
		
	}


	
/* Listing 16 */	
//Java 7
	private static void seeGrouppingJava7() {
		Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap< >();
		for(Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency();
			List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
			if (transactionsForCurrency == null) {
                transactionsForCurrency = new ArrayList<>();
                transactionsByCurrencies.put(currency, transactionsForCurrency);
			}
			transactionsForCurrency.add(transaction);
		}
	}

	
}
