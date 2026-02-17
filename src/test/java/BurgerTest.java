import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;

public class BurgerTest {

    private Burger burger;

    @Before
    public void setup() {
        burger = new Burger();
    }

    private void prepareReceipt() {
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
    }


    @Test
    public void testSetBuns() {
        Bun mockBun = mock(Bun.class);
        burger.setBuns(mockBun);
        assertSame(mockBun, burger.bun);
    }


    @Test
    public void testAddIngredientIncreasesSize() {
        Ingredient mockIngredient = mock(Ingredient.class);
        burger.addIngredient(mockIngredient);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void testAddIngredientIsPresent() {
        Ingredient mockIngredient = mock(Ingredient.class);
        burger.addIngredient(mockIngredient);
        assertTrue(burger.ingredients.contains(mockIngredient));
    }

    @Test
    public void testReceiptContainsKetchup() {
        prepareReceipt();
        String receipt = burger.getReceipt();
        assertTrue(receipt.contains("Ketchup"));
    }

    @Test
    public void testReceiptContainsCheese() {
        prepareReceipt();
        String receipt = burger.getReceipt();
        assertTrue(receipt.contains("Cheese"));
    }

    @Test
    public void testReceiptContainsBunName() {
        prepareReceipt();
        String receipt = burger.getReceipt();
        assertTrue(receipt.contains("(==== Bun =====)"));
    }

    @Test
    public void testReceiptContainsPrice() {
        prepareReceipt();
        String receipt = burger.getReceipt();
        assertTrue(receipt.contains("Price:"));
    }

    @Test
    public void testMoveIngredient() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);
        assertEquals(ingredient1, burger.ingredients.get(1));
    }

    @Test
    public void testRemoveIngredientAfterMove() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);
        burger.removeIngredient(1);
        assertFalse(burger.ingredients.contains(ingredient1));
    }

    @Test
    public void testRemoveIngredientValidIndex() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredientValidIndexRemovesCorrectIngredient() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);
        assertFalse(burger.ingredients.contains(ingredient1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientNegativeIndex() {
        burger.removeIngredient(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientIndexOutOfBounds() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredientValidIndicesPosition1() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);
        assertEquals(ingredient1, burger.ingredients.get(1));
    }

    @Test
    public void testMoveIngredientValidIndicesPosition0() {
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);
        assertEquals(ingredient2, burger.ingredients.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientNegativeSourceIndex() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientSourceIndexOutOfBounds() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(5, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientNegativeTargetIndex() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientTargetIndexOutOfBounds() {
        Ingredient ingredient = mock(Ingredient.class);
        burger.addIngredient(ingredient);
        burger.moveIngredient(0, 5);
    }
}