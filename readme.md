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
