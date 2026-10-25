package com.example.androidbighomework

// Hard-coded preset drinks shown on the home screen.
object DrinkData {
    fun getAll(): MutableList<Drink> {
        return mutableListOf(
            Drink(-1, "Americano", 5.0, 0.0, 90.0,
                "A crisp black coffee with no added sugar.",
                "1. Prepare one shot of espresso.\n2. Add about 150 ml of hot water.\n3. Stir well and serve."),
            Drink(-2, "Caffe Latte", 130.0, 10.0, 75.0,
                "Coffee with milk, smooth and mellow.",
                "1. Prepare one shot of espresso.\n2. Heat about 200 ml of milk.\n3. Pour the milk into the coffee."),
            Drink(-3, "Cappuccino", 90.0, 7.0, 75.0,
                "A classic coffee with rich milk foam.",
                "1. Make one shot of espresso.\n2. Froth the milk.\n3. Pour in the milk and foam."),
            Drink(-4, "Lemon Tea", 55.0, 12.0, 25.0,
                "A tea with a fresh lemon aroma.",
                "1. Brew a cup of black tea.\n2. Add two lemon slices.\n3. Add a little sugar to taste and stir."),
            Drink(-5, "Bubble Milk Tea", 300.0, 35.0, 45.0,
                "A sweet milk tea with milk and tapioca pearls.",
                "1. Cook the tapioca pearls.\n2. Brew black tea and add milk.\n3. Add the pearls and sugar to taste."),
            Drink(-6, "Fresh Orange Juice", 110.0, 21.0, 0.0,
                "Juice made from fresh oranges.",
                "1. Wash and cut the oranges.\n2. Juice them with a juicer.\n3. Pour into a glass and serve.")
        )
    }

    fun getById(id: Int): Drink? {
        for (drink in getAll()) {
            if (drink.id == id) return drink
        }
        return null
    }
}
