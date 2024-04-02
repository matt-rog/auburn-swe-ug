transducers <- read.csv("Transducers.csv")
hist(transducers[,1], main="Histogram of Transducers", xlab="Number Defective Tranducers", ylab="Density", freq=FALSE)