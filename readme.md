# Word snake
This is a java console application that calculates the fastest route from one word to another. 
```
cat > cot > cog > dog
```
The following document explains the "challenge": 
https://nwrug.org/quizzes/word-chains-kata

## Techniques 
This java application creates a tree for all possibilities one word can become. All children are modifications of their parent node. We use threads to find those children. 
When one of those children is the word we are looking for, the program returns the path to the start node. This ensures us we've got the shortest path and therefore the fastest route from one word to another. 

### Why Threads? 
Finding a permutation of a word with length n will take 26*n = O(n) because we are only allowed to modify one letter at the time. For every permutation we use "Binary Search" to verify the word we've created exist in a dictionary Binary Search takes O(n log n). Therefore O(n * n log n) = O(n^2 log n). 
The load is really high for just one word. So we use threads to make the calculation asynchronous and therefore speed things up :).
