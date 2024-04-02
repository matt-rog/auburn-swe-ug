transducers <- read.csv("Transducers.csv")
at_most_5 <- sum(transducers <= 5)
proportion <- at_most_5 / length(transducers[,1])
proportion