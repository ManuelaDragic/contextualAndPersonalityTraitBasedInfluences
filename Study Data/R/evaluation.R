#install.packages("readr")
#install.packages("ggplot2")
#install.packages("dplyr")
#install.packages("emoa")
#install.packages("patchwork")
#install.packages("sjPlot")

library("rstudioapi")
library(patchwork)
library(devtools)
library(lme4)
library(installr)
library(mgcv)
library(ggplot2)
library(car)
library(MASS)
library(simr)
library(emmeans)
library(broom.mixed)
library(report)
library(stargazer)
library(tidyverse)
library(lmerTest)
library(readr)
library(dplyr)
library(tidyr)
library(broom)
library(moments)  # skewness()
library(stats)    # shapiro.test()
library(sjPlot)
library(stringr)
library(gt)
library(glue)
library(webshot2)
library(ggeffects)
library(forcats)
library(MuMIn)
library(knitr)
library(kableExtra)

setwd(dirname(getActiveDocumentContext()$path))

# data
main_df <- read_csv("filtered_data.csv")

# Personality Traits
names(main_df)[names(main_df) == "Self-Control"] <- "Self_Control"

main_df$Self_Control <- as.numeric(main_df$Self_Control)
main_df$Impulsivity <- as.numeric(main_df$Impulsivity)
main_df$Anxiety <- as.numeric(main_df$Anxiety)
main_df$FoMo <- as.numeric(main_df$FoMo)

main_df$Age <- as.numeric(main_df$Age)
main_df$Gender <- as.factor(main_df$Gender)


# Contextual Factors / Independent Variables
main_df$Valence <- as.numeric(main_df$Valence)
main_df$Current_Activity <- as.numeric(main_df$Current_Activity)
main_df$Stress <- as.numeric(main_df$Stress)
main_df$Sleepiness <- as.numeric(main_df$Sleepiness)

main_df$Hour <- as.numeric(main_df$Hour)

main_df$Social_Situation <- as.factor(main_df$Social_Situation)
main_df$Multitasking <- as.factor(main_df$Multitasking)
main_df$At_Home <- ifelse(main_df$At_Home == TRUE, "Yes", "No")
main_df$At_Home <- factor(main_df$At_Home, levels = c("No", "Yes"))
main_df$Weekday <- ifelse(main_df$Weekday == TRUE, "Yes", "No")
main_df$Weekday <- factor(main_df$Weekday, levels = c("No", "Yes"))

# Dependent Variables
main_df$Responsiveness <- as.numeric(main_df$Responsiveness)
main_df$Reactance <- as.numeric(main_df$Reactance)
main_df$Agency <- as.numeric(main_df$Agency)
main_df$Satisfaction <- as.numeric(main_df$Satisfaction)
main_df$Goal_Alignment <- as.numeric(main_df$Goal_Alignment)
main_df$Usefulness <- as.numeric(main_df$Usefulness)


#
# Distribution Gender
#
main_df %>%
  dplyr::select(Prolific_ID, Gender) %>%
  distinct() %>%
  count(Gender)


#
# Average Compensation
#
df <- read.csv("validEntriesPerID.csv")
colnames(df) <- c("Prolific_ID", "Valid_Entries", "Payment")

df$Valid_Entries <- as.numeric(df$Valid_Entries)
df$Payment <- as.numeric(df$Payment)

# mean valid entries
mean(df$Valid_Entries , na.rm = TRUE)
median(df$Valid_Entries , na.rm = TRUE)
# mean payment
mean(df$Payment , na.rm = TRUE)
median(df$Payment , na.rm = TRUE)


#
# Data Pre-Processing
#
# Remove outliners
# (Data that exceeds 7 days,
# data from participants who have not participated for a full 7 days and
# data where the LimeSurvey or Questionnaire attention check was false already excluded)
#
'dependent_vars <- c("Responsiveness", "Reactance", "Agency", "Satisfaction", "Goal_Alignment", "Usefulness")

filtered_df <- main_df
outlier_log <- data.frame()

# Function: calculate Z-Score
z_score <- function(x) {
  (x - mean(x, na.rm = TRUE)) / sd(x, na.rm = TRUE)
}

# find and remove outliners
for (var in dependent_vars) {
  # calculate Z-Score
  z_col <- paste0("z_", var)
  filtered_df[[z_col]] <- z_score(filtered_df[[var]])

  # find outliners (Z > 3)
  outliers <- filtered_df %>%
    filter(abs(.data[[z_col]]) > 3) %>%
    dplyr::select(all_of(c("Prolific_ID", var, z_col))) %>%
    mutate(Variable = var) %>%
    rename(Value = !!sym(var), Z_Score = !!sym(z_col))

  outlier_log <- bind_rows(outlier_log, outliers)

  # remove outliners
  filtered_df <- filtered_df %>%
    filter(abs(.data[[z_col]]) <= 3 | is.na(.data[[z_col]]))
}
print("Deleted outliners:")
print(outlier_log)

main_df <- filtered_df'


#
# Data-Preprocessing
#
# Exclude participants who did not experience all three intervention types
#
intervention_counts <- main_df %>%
  group_by(Prolific_ID) %>%
  summarise(n_types = n_distinct(Intervention_Type))

valid_ids <- intervention_counts %>%
  filter(n_types == 3) %>%
  pull(Prolific_ID)

main_df <- main_df %>% filter(Prolific_ID %in% valid_ids)



#
# Data-Preprocessing
#
# Shapiro-Wilk-Test &
# Calculate Effectiveness
# 1. Apply log+1 transformation to responsiveness
# 2. Invert Responsiveness and Reactance & Normalize all dependent variables (min-max Normalization)
# 3. Compute Effectiveness as the mean of all normalized values
#


# STEP 1: log+1 transformation
main_df$Responsiveness_logTrans <- log1p(main_df$Responsiveness)  # log+1


# STEP 1.2: Shapiro-Wilk-Test
#
# Shapiro-Wilk-Test and skew for responsivenss
shapiro_responsiveness <- shapiro.test(main_df$Responsiveness)
skew_responsiveness <- skewness(main_df$Responsiveness, na.rm = TRUE)

cat("Original Responsiveness:\n")
cat("Shapiro-Wilk W =", round(shapiro_responsiveness$statistic, 3), ", p =", round(shapiro_responsiveness$p.value, 3), "\n")
cat("Skewness =", round(skew_responsiveness, 3), "\n\n")


# Shapiro-Wilk-Test and skew for log-transformed responsivenss
shapiro_Responsiveness_logTrans <- shapiro.test(main_df$Responsiveness_logTrans)
skew_Responsiveness_logTrans <- skewness(main_df$Responsiveness_logTrans, na.rm = TRUE)

cat("Log-Transformed Responsiveness:\n")
cat("Shapiro-Wilk W =", round(shapiro_Responsiveness_logTrans$statistic, 3), ", p =", round(shapiro_Responsiveness_logTrans$p.value, 3), "\n")
cat("Skewness =", round(skew_Responsiveness_logTrans, 3), "\n")


# Shapiro-Wilk-Test and skew for reactance
shapiro_Reactance <- shapiro.test(main_df$Reactance)
skew_Reactance <- skewness(main_df$Reactance, na.rm = TRUE)

cat("Reactance:\n")
cat("Shapiro-Wilk W =", round(shapiro_Reactance$statistic, 3), ", p =", round(shapiro_Reactance$p.value, 3), "\n")
cat("Skewness =", round(skew_Reactance, 3), "\n\n")


# Shapiro-Wilk-Test and skew for agency
shapiro_agency <- shapiro.test(main_df$Agency)
skew_agency <- skewness(main_df$Agency, na.rm = TRUE)

cat("Agency:\n")
cat("Shapiro-Wilk W =", round(shapiro_agency$statistic, 3), ", p =", round(shapiro_agency$p.value, 3), "\n")
cat("Skewness =", round(skew_agency, 3), "\n\n")


# Shapiro-Wilk-Test and skew for satisfaction
shapiro_satisfaction <- shapiro.test(main_df$Satisfaction)
skew_satisfaction <- skewness(main_df$Satisfaction, na.rm = TRUE)

cat("Satisfaction:\n")
cat("Shapiro-Wilk W =", round(shapiro_satisfaction$statistic, 3), ", p =", round(shapiro_satisfaction$p.value, 3), "\n")
cat("Skewness =", round(skew_satisfaction, 3), "\n\n")


# Shapiro-Wilk-Test and skew for Goal_Alignment
shapiro_Goal_Alignment <- shapiro.test(main_df$Goal_Alignment)
skew_Goal_Alignment <- skewness(main_df$Goal_Alignment, na.rm = TRUE)

cat("Goal_Alignment:\n")
cat("Shapiro-Wilk W =", round(shapiro_Goal_Alignment$statistic, 3), ", p =", round(shapiro_Goal_Alignment$p.value, 3), "\n")
cat("Skewness =", round(skew_Goal_Alignment, 3), "\n\n")


# Shapiro-Wilk-Test and skew for Usefulness
shapiro_Usefulness <- shapiro.test(main_df$Usefulness)
skew_Usefulness <- skewness(main_df$Usefulness, na.rm = TRUE)

cat("Usefulness:\n")
cat("Shapiro-Wilk W =", round(shapiro_Usefulness$statistic, 3), ", p =", round(shapiro_Usefulness$p.value, 3), "\n")
cat("Skewness =", round(skew_Usefulness, 3), "\n\n")


#
# Plot Responsiveness
#
# Responsiveness Original
ggplot(main_df, aes(x = Responsiveness)) +
  geom_histogram(aes(y = ..density..), bins = 30, fill = "orange", color = "black") +
  geom_density(color = "darkorange", size = 1) +
  labs(title = "Responsiveness (original)", x = "Value", y = "density") +
  theme_minimal()

# Responsiveness logTrans
ggplot(main_df, aes(x = Responsiveness_logTrans)) +
  geom_histogram(aes(y = ..density..), bins = 30, fill = "orange", color = "black") +
  geom_density(color = "darkorange", size = 1) +
  labs(title = "Responsiveness (log-transformed)", x = "Log-Value", y = "density") +
  theme_minimal()



# STEP 2: Inversion & Normalization
#

# invert responsiveness and reactance
main_df$Responsiveness_logInverted <- (max(main_df$Responsiveness_logTrans, na.rm = TRUE) + min(main_df$Responsiveness_logTrans, na.rm = TRUE)) - main_df$Responsiveness_logTrans # invert responsiveness
main_df$Reactance_inverted <- 6 - main_df$Reactance   # invert reactance

min_responsiveness <- min(main_df$Responsiveness_logInverted, na.rm = TRUE)
max_responsiveness <- max(main_df$Responsiveness_logInverted, na.rm = TRUE)

# normalization function
normalize <- function(x, min, max) {
  (x - min) / (max - min)
}

# Normalize values with min-max normalization
main_df$Responsiveness_norm <- normalize(main_df$Responsiveness_logInverted,
                                         min_responsiveness,
                                         max_responsiveness)
main_df$Reactance_norm <- normalize(main_df$Reactance_inverted, min = 1, max = 5)
main_df$Agency_norm <- normalize(main_df$Agency, min = 1, max = 7)
main_df$Satisfaction_norm <- normalize(main_df$Satisfaction, min = 1, max = 7)
main_df$Goal_Alignment_norm <- normalize(main_df$Goal_Alignment, min = 1, max = 7)
main_df$Usefulness_norm <- normalize(main_df$Usefulness, min = 1, max = 7)



# STEP 3: Calculate Effectiveness
main_df$Effectiveness <- rowMeans(main_df[, c("Responsiveness_norm", "Reactance_norm", "Agency_norm", "Satisfaction_norm", "Goal_Alignment_norm", "Usefulness_norm")], na.rm = TRUE)



#
# Descriptive Data
#
# Distribution of Intervention Types
#
ggplot(main_df, aes(x = Intervention_Type)) +
  coord_flip() +
  geom_bar(fill = "darkorange", color = "black") +
  theme_minimal() +
  labs(title = NULL,
       x = NULL,
       y = NULL)


#
# Hour of day distribution
#
p <- ggplot(main_df, aes(x = Hour)) +
  geom_histogram(
    binwidth = 1,
    boundary = -0.5,
    fill = "darkorange",
    color = "black"
  ) +
  coord_polar(start = -pi/24, direction = 1, clip = "off") +
  scale_x_continuous(
    breaks = 0:23,
    labels = paste0(0:23, "h")
  ) +
  labs(
    title = NULL,
    x = NULL,
    y = NULL
  ) +
  theme_minimal(base_size = 14) +
  theme(
    axis.text.y = element_blank(),  # Original Y-Achse ausblenden
    panel.grid.minor = element_blank(),
    legend.position = "none"
  )

max_count <- max(ggplot_build(p)$data[[1]]$count)
y_breaks <- pretty(c(0, max_count), n = 4)

ring_labels <- data.frame(
  x = 3, # centered x = 0
  y = y_breaks,
  label = y_breaks
)

p + geom_text(data = ring_labels,
              aes(x = x, y = y, label = label),
              inherit.aes = FALSE,
              color = "black",
              size = 3)


#
# Distribution for contextual factors
#
columns <- c("Valence", "Current_Activity", "Stress", "Sleepiness", "Social_Situation", "Multitasking", "At_Home", "Weekday")

plot_list <- list()

for (col in columns) {
  variable_data <- main_df[[col]]

  p <- ggplot(main_df, aes(x = !!sym(col))) +
    geom_bar(fill = "darkorange", color = "black") +
    coord_flip() +
    theme_minimal() +
    labs(title = col) +
    theme(axis.title.x = element_blank(),
          axis.title.y = element_blank())

  if (is.numeric(variable_data)) {
    p <- p + scale_x_continuous(breaks = min(variable_data, na.rm = TRUE):max(variable_data, na.rm = TRUE))
  }

  plot_list[[col]] <- p
}
wrap_plots(plot_list, ncol = 4)



#
# Distribution for Personality Factors
#
columns <- c("Impulsivity", "Anxiety", "Self_Control", "FoMo", "Age", "Gender")

# fixed scales
fixed_scales <- list(
  "Impulsivity" = c(1, 4),
  "Anxiety" = c(0, 3),
  "Self_Control" = c(1, 5),
  "FoMo" = c(1, 5)
)

plot_list <- list()

for (col in columns) {
  variable_data <- main_df[[col]]

  if (is.numeric(variable_data)) {
    p <- ggplot(main_df, aes(x = !!sym(col))) +
      geom_histogram(fill = "darkorange", color = "black", bins = 20)

    # set fixed scale
    if (col %in% names(fixed_scales)) {
      p <- p + scale_x_continuous(limits = fixed_scales[[col]])
    }

    p <- p +
      coord_flip() +
      theme_minimal() +
      labs(title = col) +
      theme(axis.title.x = element_blank(),
            axis.title.y = element_blank())

  } else {
    p <- ggplot(main_df, aes(x = !!sym(col))) +
      geom_bar(fill = "darkorange", color = "black") +
      coord_flip() +
      theme_minimal() +
      labs(title = col) +
      theme(axis.title.x = element_blank(),
            axis.title.y = element_blank())
  }

  plot_list[[col]] <- p
}

wrap_plots(plot_list, ncol = 4)



#
# average Effectiveness per Intervention Type
#

custom_colors <- c("Pop-Up" = "darkgray", "SpotOverlay" = "darkorange", "Vibration" = "blue")

ggplot(main_df, aes(x = Intervention_Type, y = Effectiveness)) +
  stat_summary(fun = mean, geom = "bar", width = 0.6, fill = "darkorange", color = "black") +
  ylim(0, 1) +
  labs(title = NULL,
       x = NULL,
       y = "Effectiveness") +
  theme_minimal()



#
# LMM: model with interactions between Intervention_Type and all predictors
# & Forest Plots
#

# Pop-Up intervention as reference
main_df$Intervention_Type <- factor(main_df$Intervention_Type, levels = c("Pop-Up", "SpotOverlay", "Vibration"))

# Fit model
model <- lmer(Effectiveness ~ Intervention_Type *
                (Valence + Current_Activity + Stress + Sleepiness + Social_Situation + Multitasking + At_Home
                 + Weekday
                 + Impulsivity + Anxiety + Self_Control + FoMo)
                 + (1|Prolific_ID), data = main_df)

# 1. print model summary
summary(model)

# 2. R²-Values (fixed + random effects)
r.squaredGLMM(model)


# Residuals
plot(model)               # Residuals vs. fitted
qqnorm(residuals(model))  # QQ-Plot
qqline(residuals(model))


# extract fixed effects
interaction_results <- tidy(model) %>%
  filter(effect == "fixed", term != "(Intercept)")

# Label direction
interaction_results <- interaction_results %>%
  mutate(effect_type = ifelse(estimate > 0, "positive", "negative"))

# stars based of significance
interaction_results <- interaction_results %>%
  mutate(
    significance = case_when(
      p.value < 0.001 ~ "***",
      p.value < 0.01  ~ "**",
      p.value < 0.05  ~ "*",
      #p.value < 0.1 ~ ".",
      TRUE ~ ""
    ),
    term_label = ifelse(significance != "", paste0(significance, " ", term), term)
  )

#
# forest plot
#
ggplot(interaction_results, aes(x = estimate, y = fct_rev(factor(term_label)), color = effect_type)) +
  geom_vline(xintercept = 0, linetype = "dashed", color = "black") +
  geom_errorbarh(aes(xmin = estimate - std.error, xmax = estimate + std.error),
                 color = "gray70", height = 0.2) +
  geom_point(size = 3) +
  scale_color_manual(values = c("positive" = "darkorange", "negative" = "blue")) +
  labs(
    x = NULL,
    y = NULL,
    color = "Effect Direction"
  ) +
  theme_minimal(base_size = 14)


#
# Plot Interaction Effects
#

# Define variables to plot
vars <- c("Impulsivity", "Anxiety")

# Loop through each variable and generate and display/save individual plots
for (v in vars) {
  pred <- ggpredict(model, terms = c(v, "Intervention_Type"), ci.lvl = 0.95)

  p <- ggplot(pred, aes(x = x, y = predicted, color = group)) +
    geom_line(size = 1.2) +
    geom_ribbon(aes(ymin = conf.low, ymax = conf.high, fill = group),
                alpha = 0.2, show.legend = FALSE) +
    ylim(c(0.2, 0.8)) +
    scale_color_manual(values = custom_colors) +
    scale_fill_manual(values = custom_colors) +
    labs(
      x = v,
      y = "Predicted Effectiveness",
      color = "Intervention Type",
      title = paste(v, " × Intervention Type")
    ) +
    theme_minimal(base_size = 14) +
    theme(legend.position = "bottom")

  # Show plot
  print(p)
}


# Social_Situation x Intervention_Type
pred_social <- ggpredict(model, terms = c("Social_Situation", "Intervention_Type"), ci.lvl = 0.95)

pred_social$x <- factor(pred_social$x, levels = c("alone", "friends"))

# plot
ggplot(pred_social, aes(x = x, y = predicted, group = group, color = group, fill = group)) +
  geom_line(size = 1.2) +
  geom_ribbon(aes(ymin = conf.low, ymax = conf.high),
              alpha = 0.2, show.legend = FALSE) +
  ylim(c(0.2,0.8)) +
  scale_color_manual(values = custom_colors) +
  scale_fill_manual(values = custom_colors) +
  labs(
    x = "Social Situation",
    y = "Predicted Effectiveness",
    color = "Intervention Type",
    fill = "Intervention Type"
  ) +
  theme_minimal(base_size = 14) +
  theme(legend.position = "bottom")



#
# Appendix
#
# Descriptive Data
#

# Personality Traits
min(main_df$Impulsivity, na.rm = TRUE)
max(main_df$Impulsivity, na.rm = TRUE)
mean(main_df$Impulsivity, na.rm = TRUE)
sd(main_df$Impulsivity, na.rm = TRUE)
median(main_df$Impulsivity, na.rm = TRUE)

min(main_df$Anxiety, na.rm = TRUE)
max(main_df$Anxiety, na.rm = TRUE)
mean(main_df$Anxiety, na.rm = TRUE)
sd(main_df$Anxiety, na.rm = TRUE)
median(main_df$Anxiety, na.rm = TRUE)

min(main_df$Self_Control, na.rm = TRUE)
max(main_df$Self_Control, na.rm = TRUE)
mean(main_df$Self_Control, na.rm = TRUE)
sd(main_df$Self_Control, na.rm = TRUE)
median(main_df$Self_Control, na.rm = TRUE)

min(main_df$FoMo, na.rm = TRUE)
max(main_df$FoMo, na.rm = TRUE)
mean(main_df$FoMo, na.rm = TRUE)
sd(main_df$FoMo, na.rm = TRUE)
median(main_df$FoMo, na.rm = TRUE)

min(main_df$Age, na.rm = TRUE)
max(main_df$Age, na.rm = TRUE)
mean(main_df$Age, na.rm = TRUE)
sd(main_df$Age, na.rm = TRUE)
median(main_df$Age, na.rm = TRUE)

occurrences <- table(main_df$Gender)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)


# Contextual Factor
min(main_df$Valence, na.rm = TRUE)
max(main_df$Valence, na.rm = TRUE)
mean(main_df$Valence, na.rm = TRUE)
sd(main_df$Valence, na.rm = TRUE)
median(main_df$Valence, na.rm = TRUE)

min(main_df$Current_Activity, na.rm = TRUE)
max(main_df$Current_Activity, na.rm = TRUE)
mean(main_df$Current_Activity, na.rm = TRUE)
sd(main_df$Current_Activity, na.rm = TRUE)
median(main_df$Current_Activity, na.rm = TRUE)

min(main_df$Stress, na.rm = TRUE)
max(main_df$Stress, na.rm = TRUE)
mean(main_df$Stress, na.rm = TRUE)
sd(main_df$Stress, na.rm = TRUE)
median(main_df$Stress, na.rm = TRUE)

min(main_df$Sleepiness, na.rm = TRUE)
max(main_df$Sleepiness, na.rm = TRUE)
mean(main_df$Sleepiness, na.rm = TRUE)
sd(main_df$Sleepiness, na.rm = TRUE)
median(main_df$Sleepiness, na.rm = TRUE)

occurrences <- table(main_df$Social_Situation)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)

occurrences <- table(main_df$Multitasking)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)

occurrences <- table(main_df$At_Home)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)

occurrences <- table(main_df$Weekday)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)

min(main_df$Age, na.rm = TRUE)
max(main_df$Age, na.rm = TRUE)
mean(main_df$Age, na.rm = TRUE)
sd(main_df$Age, na.rm = TRUE)
median(main_df$Age, na.rm = TRUE)


# Dependent Variables
min(main_df$Responsiveness, na.rm = TRUE)
max(main_df$Responsiveness, na.rm = TRUE)
mean(main_df$Responsiveness, na.rm = TRUE)
sd(main_df$Responsiveness, na.rm = TRUE)
median(main_df$Responsiveness, na.rm = TRUE)

min(main_df$Responsiveness_logTrans, na.rm = TRUE)
max(main_df$Responsiveness_logTrans, na.rm = TRUE)
mean(main_df$Responsiveness_logTrans, na.rm = TRUE)
sd(main_df$Responsiveness_logTrans, na.rm = TRUE)
median(main_df$Responsiveness_logTrans, na.rm = TRUE)


min(main_df$Reactance, na.rm = TRUE)
max(main_df$Reactance, na.rm = TRUE)
mean(main_df$Reactance, na.rm = TRUE)
sd(main_df$Reactance, na.rm = TRUE)
median(main_df$Reactance, na.rm = TRUE)


min(main_df$Agency, na.rm = TRUE)
max(main_df$Agency, na.rm = TRUE)
mean(main_df$Agency, na.rm = TRUE)
sd(main_df$Agency, na.rm = TRUE)
median(main_df$Agency, na.rm = TRUE)


min(main_df$Satisfaction, na.rm = TRUE)
max(main_df$Satisfaction, na.rm = TRUE)
mean(main_df$Satisfaction, na.rm = TRUE)
sd(main_df$Satisfaction, na.rm = TRUE)
median(main_df$Satisfaction, na.rm = TRUE)


min(main_df$Goal_Alignment, na.rm = TRUE)
max(main_df$Goal_Alignment, na.rm = TRUE)
mean(main_df$Goal_Alignment, na.rm = TRUE)
sd(main_df$Goal_Alignment, na.rm = TRUE)
median(main_df$Goal_Alignment, na.rm = TRUE)

min(main_df$Usefulness , na.rm = TRUE)
max(main_df$Usefulness , na.rm = TRUE)
mean(main_df$Usefulness , na.rm = TRUE)
sd(main_df$Usefulness , na.rm = TRUE)
median(main_df$Usefulness, na.rm = TRUE)

min(main_df$Effectiveness , na.rm = TRUE)
max(main_df$Effectiveness , na.rm = TRUE)
mean(main_df$Effectiveness , na.rm = TRUE)
sd(main_df$Effectiveness , na.rm = TRUE)
median(main_df$Effectiveness , na.rm = TRUE)


# Distributions
occurrences <- table(main_df$Intervention_Type)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)

occurrences <- table(main_df$App_Name)
percentages <- (occurrences / sum(occurrences)) * 100
print(percentages)






