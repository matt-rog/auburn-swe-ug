# Data given from Galileo experiment
Location <- c("A", "A", "A", "B", "B", "B", "C")
Height <- c(100,200,300,450,600,800,1000)
Distance <- c(253,337,395,451,495,534,573)

# Part a
Galileo <- data.frame(
  Location,
  Height,
  Distance
)

Galileo

# Part b
mean(Galileo$Distance)
median(Galileo$Distance)
var(Galileo$Distance)
IQR(Galileo$Distance)

# Part c
Galileo$D.Hat <- 200 + (0.708 * Galileo$Height) - (0.000344 * Galileo$Height^2)

Galileo$LO <- Galileo$D.Hat < Galileo$Distance

G_sub <- Galileo[!Galileo$LO,]
G_sub

# Part d
plot(Galileo$Height, Galileo$Distance, xlab = "Height", ylab = "Distance")
lines(Galileo$Height, Galileo$D.Hat, col = "blue")