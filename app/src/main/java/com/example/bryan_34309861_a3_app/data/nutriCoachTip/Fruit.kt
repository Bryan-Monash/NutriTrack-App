package com.example.bryan_34309861_a3_app.data.nutriCoachTip

data class Fruit(
    val id: Int,
    val name: String,
    val family: String,
    val order: String,
    val genus: String,
    val nutritions: Nutrition
)

data class Nutrition(
    val calories: Float,
    val fat: Float,
    val sugar: Float,
    val carbohydrates: Float,
    val protein: Float
)
