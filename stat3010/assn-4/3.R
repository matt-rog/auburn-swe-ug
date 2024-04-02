# Read in concrete load data
conc <- read.csv("hw4q3.csv", header = TRUE)

# Part a
plot(x=conc$Meas, y=conc$Calc,
     ylab="Calculated",
     xlab="Measured")

# Part b
cor(conc$Meas, conc$Calc)
