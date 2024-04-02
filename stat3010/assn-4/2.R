data <- read.csv("hw4q2.csv", header = TRUE)

# Part a
Humidity <- data$Humidity
Season <- data$Season

boxplot(Humidity ~ Season, data = data, xlab = "Season", ylab = "Relative Humidity")

# Part b
Winter_Humidity <- Humidity[Season == "Winter"]
Summer_Humidity <- Humidity[Season == "Summer"]

par(mfrow=c(1,2))
qqplot(Winter_Humidity, Humidity, main="Winter Humidity QQ Plot")
qqplot(Summer_Humidity, Humidity, main="Summer Humidity QQ Plot")

# Part c
var(Winter_Humidity)
IQR(Winter_Humidity)

var(Summer_Humidity)
IQR(Summer_Humidity)

