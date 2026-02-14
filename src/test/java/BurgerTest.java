import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;

    @Before
    public void setup() {
        burger = new Burger();
    }

    // Данные для параметризованных тестов по цене
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1.5f, 0.5f, 4.0f},  // булочка 1.5 × 2 + ингредиент 0.5 = 4.0
                {2.0f, 1.0f, 5.0f},  // булочка 2.0 × 2 + ингредиент 1.0 = 5.0
                {1.0f, 2.0f, 4.0f}   // булочка 1.0 × 2 + ингредиент 2.0 = 4.0
        });
    }

    @Parameterized.Parameter
    public float bunPrice;

    @Parameterized.Parameter(1)
    public float ingredientPrice;

    @Parameterized.Parameter(2)
    public float expectedTotalPrice;

    @Test
    public void testPriceCalculation() {
        // Удалена строка: RestAssured Mockito;

        Bun mockBun = mock(Bun.class);
        when(mockBun.getPrice()).thenReturn(bunPrice);
        when(mockBun.getName()).thenReturn("TestBun");

        Ingredient mockIngredient = mock(Ingredient.class);
        when(mockIngredient.getPrice()).thenReturn(ingredientPrice);
        when(mockIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient.getName()).thenReturn("Cheese");

        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient);

        assertEquals(expectedTotalPrice, burger.getPrice(), 0.01);
    }

    @Test
    public void testReceiptGeneration() {
        // Удалена строка: RestAssured Mockito;

        Bun mockBun = mock(Bun.class);
        when(mockBun.getPrice()).thenReturn(1.0f);
        when(mockBun.getName()).thenReturn("Bun");
        burger.setBuns(mockBun);

        Ingredient ingredient1 = mock(Ingredient.class);
        when(ingredient1.getPrice()).thenReturn(0.5f);
        when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredient1.getName()).thenReturn("Ketchup");

        Ingredient ingredient2 = mock(Ingredient.class);
        when(ingredient2.getPrice()).thenReturn(1.0f);
        when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient2.getName()).thenReturn("Cheese");

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        String receipt = burger.getReceipt();
        assertTrue(receipt.contains("Ketchup"));
        assertTrue(receipt.contains("Cheese"));
        assertTrue(receipt.contains("(==== Bun =====)"));
        assertTrue(receipt.contains("Price:"));
    }

    @Test
    public void testRemoveAndMoveIngredient() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);
        assertEquals(ingredient1, burger.ingredients.get(1));

        burger.removeIngredient(1);
        assertFalse(burger.ingredients.contains(ingredient1));
    }

    // Тест: установка булочек
    @Test
    public void testSetBuns() {
        Bun mockBun = mock(Bun.class);
        burger.setBuns(mockBun);
        assertSame(mockBun, burger.bun);
    }

    // Тест: добавление ингредиентов
    @Test
    public void testAddIngredient() {
        Ingredient mockIngredient = mock(Ingredient.class);
        burger.addIngredient(mockIngredient);
        assertEquals(1, burger.ingredients.size());
        assertTrue(burger.ingredients.contains(mockIngredient));
    }

    // Тест: удаление ингредиента с корректным индексом
    @Test
    public void testRemoveIngredientValidIndex() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);
        assertEquals(1, burger.ingredients.size());
        assertFalse(burger.ingredients.contains(ingredient1));
    }

    // Тест: удаление ингредиента с некорректным индексом (индекс < 0)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientNegativeIndex() {
        burger.removeIngredient(-1);
    }

    // Тест: удаление ингредиента с некорректным индексом (индекс >= размера списка)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientIndexOutOfBounds() {
        burger.removeIngredient(0); // попытка удалить из пустого списка
    }

    // Тест: перемещение ингредиента с корректными индексами
    @Test
    public void testMoveIngredientValidIndices() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);
        assertEquals(ingredient1, burger.ingredients.get(1));
        assertEquals(ingredient2, burger.ingredients.get(0));
    }

    // Тест: перемещение ингредиента с отрицательным индексом источника
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientNegativeSourceIndex() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(-1, 0);
    }

    // Тест: перемещение ингредиента с индексом источника вне границ
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientSourceIndexOutOfBounds() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(5, 0);
    }

    // Тест: перемещение ингредиента с отрицательным целевым индексом
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientNegativeTargetIndex() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(0, -1);
    }

    // Тест: перемещение ингредиента с целевым индексом вне границ
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientTargetIndexOutOfBounds() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(0, 5);
    }

    // Тест: расчёт цены без булочек
    @Test
    public void testGetPriceWithoutBuns() {
        Ingredient mockIngredient = mock(Ingredient.class);
        when(mockIngredient.getPrice()).thenReturn(2.0f);
        burger.addIngredient(mockIngredient);

        float price = burger.getPrice();
        assertEquals(2.0f, price, 0.01);
    }

    // Тест: расчёт цены без ингредиентов (только булочки)
    @Test
    public void testGetPriceWithoutIngredients() {
        Bun mockBun = mock(Bun.class);
        when(mockBun.getPrice()).thenReturn(1.0f);
        when(mockBun.getName()).thenReturn("Bun");
        burger.setBuns(mockBun);

        assertEquals(1.0f, burger.getPrice(), 0.01);
    }
}