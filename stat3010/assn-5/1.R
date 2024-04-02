energy <- read.csv("hw5q1.csv", header = TRUE)

# 1a
y = energy$Energy 
x1 = energy$Plastics # % plastics by weight
x2 = energy$Paper # % paper by weight
x3 = energy$Garbage # % garbage by weight
x4 = energy$Water # prox. analysis, moisture by weight

lrm = lm(y ~ x1 + x2 + x3 + x4, data=energy)

summary(lrm)

# 1b
# Findings from LRM
c_int = 2244.923
x1_c = 28.925
x2_c = 7.644
x3_c = 4.297
x4_c = -37.354

nrg_pred = c_int + (x1_c*17.03) + (x2_c*23.46) + (x3_c*32.45) + (x4_c*53.23)

nrg_obs = y[11]

residual = nrg_obs - nrg_pred

# 1d
sm = step(lrm, direction = "both")

summary(sm)



