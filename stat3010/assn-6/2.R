install.packages('qcc')
library(qcc)

faucet <- read.csv(file = "hw6q2.csv", header = TRUE)
faucet <- faucet[,-1]

RChart <- qcc(faucet, type="R")
xbarChart <- qcc(faucet, type="xbar")

faucet <- faucet[-25,]
faucet <- faucet[-18,]
faucet <- faucet[-3,]
xbarChart <- qcc(faucet, type="xbar")


