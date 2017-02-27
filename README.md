# Reflex-Agent-VS-Neural-Agent
Genetic algorithm to train neural network to battle a reflex agent

Artificial intelligence course assignment 3

Two teams of robots battle to destroy the other team's flag.

The blue team is powered by a "reflex agent". 
That means it contains a bunch of hard-coded human intelligence. 
The red team is powered by a "neural agent". 
That means an artificial neural network directs its behavior at a high level.

A genetic algorithm is used to "evolve" the neural agent.

Pseudocode:
Initialize a population of virtual chromosomes.
do for a long time:
	(1) Promote diversity within the population.
	(2) Select the "more fit" chromosomes to survive. Kill the "less fit" ones.
	(3) Replenish the population.
  
A matrix of 100 rows have 291 columns that represent chromosomes.
The program will first train these agents and at the end will pick a winning agent from the population to battle against the reflex agent.
