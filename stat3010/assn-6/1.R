install.packages('qcc')
library(qcc)
keys <- read.csv(file = "hw6q1.csv", header = TRUE)

centerline <- sum(keys$Failed) / sum(keys$Tested)
upper_control <- centerline + 3 * sqrt((centerline*(1-centerline))/length(keys$Tested))
lower_control <- max(0,centerline - 3 * sqrt((centerline*(1-centerline))/length(keys$Tested)))

p_chart <- qcc(keys$Failed, keys$Tested, type = "p", data.name = "Keys")
