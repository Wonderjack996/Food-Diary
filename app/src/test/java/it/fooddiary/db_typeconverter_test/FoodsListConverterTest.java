package it.fooddiary.db_typeconverter_test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.fooddiary.databases.converters.FoodsListConverter;
import it.fooddiary.models.Food;

import static org.junit.Assert.*;

public class FoodsListConverterTest {

    @Test
    public void totalTest() {
        assertNull(FoodsListConverter.listToTimestamp(null));

        List<Food> foods = new ArrayList<>();
        Food food1 = new Food("prova1", 100, 0, 0, 0);
        Food food2 = new Food("prova2", 200, 0, 0, 0);
        foods.add(food1);
        foods.add(food2);

        String timestamp = FoodsListConverter.listToTimestamp(foods);
        List<Food> newList = FoodsListConverter.fromTimestamp(timestamp);

        assertEquals(newList.size(), 2);
        assertEquals(newList.get(0), food1);
        assertEquals(newList.get(1), food2);
    }
}
