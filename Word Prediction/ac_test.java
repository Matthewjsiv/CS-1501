import java.io.*;
import java.util.Scanner;


public class ac_test {

	static class Node
	{
		
		
		char val;
		int weight;
		Node sibling;
		Node child;
		
		
		public Node()
		{
			
		}
		
		public Node(char value) {
			val = value;
			sibling = null;
			child = null;
			weight = 0;
			
		}

		public boolean hasSibling() {
			
			return sibling != null;
		}
		
		public boolean hasChild() {
			
			return child != null;
		}
		
	}
	
	static Node rootNode = new Node();
	static int numwords = 0;
	static String[] listWords = new String[5];
	static int[] listWeights = new int[5];
	static double totRuntime = 0;
	static int numRuns = 0;
	
	
	
	
	
	
	
	
	private static void addWord(String entry)
	{
		
		entry = entry + "*"; //add terminating character 
		
		Node current = rootNode;
		
		for(int i = 0; i< entry.length(); i++)
		{
			
			char c = entry.charAt(i);
			current = put(current, c);
			
		}
		
		
		//System.out.println("added " + entry);
		
	}
	
	
	//make sure it doesnt create new node if it already exists
	private static Node put(Node current, char c)
	{
		if(!current.hasChild())
		{
			
			Node newNode = new Node(c);
			//System.out.println("Added new node " + newNode.val);
			current.child = newNode;
			if(c == '*')
			{
				newNode.weight++;
			}
			
			
			return current.child;
		}
		else
		{
			
			current = current.child;
			
			if(current.val != c)
			{
				
				while(current.hasSibling())
				{
					if(current.sibling.val == c) 
					{
						
						return current.sibling;
					}
					
					current = current.sibling;
					
				}
				current.sibling = new Node(c);
				//System.out.println("Added new node " + current.sibling.val);
				if(c == '*')
				{
					current.sibling.weight++;
				}
				return current.sibling;
			}
			else
			{
				if(c == '*')
				{
					current.weight++;
				}
				return current;
			}
			
			
				
			
		}
	}
	
	private static void predict(String word)
	{
		
		Node current = rootNode;
		
		
		//get to last node of user input
		for(int i = 0; i< word.length(); i++)
		{
			
			char c = word.charAt(i);
			current = get(current, c);
			if(current == null)
			{
				break;
			}
			
		}
		if(current == null) 
		{
			System.out.println("No predictions found");
		}
		else
		{
			//System.out.println(current.child.val);
			numwords = 0;
			for(int i = 0; i<listWords.length; i++)
			{
				listWords[i] = "";
				listWeights[i] = 0;
			}
			
			long start = System.nanoTime();
			DFS(current.child,word);
			
			
			double finish = System.nanoTime() - start;
			double finishtime  = finish*Math.pow(10, -9);
			String finishformat = String.format("%.6f", finishtime);
			
			totRuntime += finishtime;
			numRuns++;
			numwords = 0;
			System.out.println("(" + finishformat + " s)");
			
			System.out.println("Predictions: ");
			for(int i = 0; i< listWords.length; i++)
			{
				System.out.print("(" + (i+1) + ")  " + listWords[i] + "\t\t");
			}
			System.out.println();
		}
		
		
				
	}
	
	
	//if not equal to star then print char
	//if equal to star then println
	private static void DFS(Node current, String word)
	{
		
		if(current == null)
		{
			word = word.substring(0,word.length()-1);
			//go back to last star node
			return;
		}
		
		if(current.val == '*' /*&& numwords < 5*/)
		{
			word += current.val;
			
			
			int point = 8;
			boolean shift = false;
			for(int i = 0; i < listWords.length; i++)
			{
				if(current.weight > listWeights[i])
				{
					point = i;
					shift = true;
					break;
				}
			}
			
			
			for(int i= listWords.length -1; i > point; i--)
			{
				listWords[i] = listWords[i-1];
				listWeights[i] = listWeights[i-1];
			}
			
			if(shift)
			{
				listWords[point] = word.substring(0,word.length()-1);
				listWeights[point] = current.weight;
				numwords++;
			}
			
			//System.out.println("(" + (numwords+1) + ")  " + word.substring(0,word.length()-1) + " weight = " + current.weight);
			
			
		}
		else
		{
			word += current.val;
			//System.out.println(word);
		}
		
		DFS(current.child, word);
		word = word.substring(0,word.length()-1);
		DFS(current.sibling, word);
		
		
	}
	
	
	private static boolean contains(String word)
	{
		//get to last node of user input
		Node current = rootNode;
				for(int i = 0; i< word.length(); i++)
				{
					
					char c = word.charAt(i);
					current = get(current, c);
					if(current == null)
					{
						break;
					}
					
				}
				if(current == null) //possibly doesn't work - does it work if it's one letter off
				{
					return false;
					//System.out.println("No words possible");
				}
				else
				{
					return true;
				}
	}
	
	
	private static Node get(Node current, char c)
	{
		if(!current.hasChild())
		{
			
			return null;
		}
		else
		{
			current = current.child;
			
			if(current.val != c)
			{
				
				while(current.hasSibling())
				{
					if(current.sibling.val == c) 
					{
						return current.sibling;
					}
					
					current = current.sibling;
					
				}
				
				return null;
			}
			else
			{
				return current;
			}
		}
		
		
	}
	

	
	public static void main(String args[]) throws FileNotFoundException, IOException
	{
		
		File file = new File("dictionary.txt"); 
		Scanner sc = new Scanner(file); 
		
		int i = 0;
		String word = "";
		while (sc.hasNextLine()) 
		{
			word = sc.nextLine();
			addWord(word);
			i++;
		}
			      
		sc.close();
		
		
		
		//words that don't exist must be added twice
				File hist = new File("user_history.txt");
				
				boolean exists = hist.exists();
				word = "";
				if(exists)
				{
					Scanner uh = new Scanner(hist);
					while (uh.hasNextLine()) 
					{
						
						word = uh.nextLine();
						if(contains(word))
						{
							addWord(word);
						}
						else
						{
							addWord(word);
							addWord(word);
						}
						
						
					}
					uh.close();
				}
				
				
		
		//need to keep past userhistory too
		FileWriter fileWriter = new FileWriter("user_history.txt", true); //Set true for append mode
		PrintWriter writer = new PrintWriter(fileWriter);
		
		
		
		
		
		char c = ' ';
		String findWord = "";
		while(c != '!')
		{
			
			System.out.print("Enter a character: ");
			Scanner reader = new Scanner(System.in);
			c = reader.next().charAt(0);
			
			
			if(c == '!')
			{
				break;
			}
			else if(c == '1')
			{
				addWord(listWords[0]);
				writer.println(listWords[0]);
				System.out.println();
				System.out.println("WORD COMPLETED: " + listWords[0]);
				System.out.println();
				findWord = "";
			}
			else if(c == '2')
			{
				addWord(listWords[1]);
				writer.println(listWords[1]);
				System.out.println();
				System.out.println("WORD COMPLETED: " + listWords[1]);
				System.out.println();
				findWord = "";
			}
			else if(c == '3')
			{
				addWord(listWords[2]);
				writer.println(listWords[2]);
				System.out.println();
				System.out.println("WORD COMPLETED: " + listWords[2]);
				System.out.println();
				findWord = "";
			}
			else if(c == '4')
			{
				addWord(listWords[3]);
				writer.println(listWords[3]);
				System.out.println();
				System.out.println("WORD COMPLETED: " + listWords[3]);
				System.out.println();
				findWord = "";
			}
			else if(c == '5')
			{
				addWord(listWords[4]);
				writer.println(listWords[4]);
				System.out.println();
				System.out.println("WORD COMPLETED: " + listWords[4]);
				System.out.println();
				findWord = "";
			}
			else if(c == '$')
			{
				if(contains(findWord))
				{
					addWord(findWord);
				}
				else
				{
					addWord(findWord);
					addWord(findWord);
				}
				writer.println(findWord);
				System.out.println();
				System.out.println("WORD COMPLETED: " + findWord);
				System.out.println();
				findWord = "";
			}
			else
			{
				findWord+=c;
				predict(findWord);
			}
			
			
			System.out.println();
		}
		
		writer.close();
		
		double avgRuntime = totRuntime/numRuns;
		String avgformat = String.format("%.6f", avgRuntime);
		System.out.println("Average runtime = " + avgformat + "s");
		
		
		
	}

	

	
	
	
}

