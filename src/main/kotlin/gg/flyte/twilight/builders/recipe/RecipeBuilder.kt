package gg.flyte.twilight.builders.recipe

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class RecipeBuilder(result: ItemStack, plugin: JavaPlugin, key: String) {
    private val recipe: ShapedRecipe = ShapedRecipe(NamespacedKey(plugin, key), result)
    private val ingredients: MutableMap<Char, RecipeIngredient> = HashMap()

    fun setShape(vararg shape: String): RecipeBuilder {
        recipe.shape(*shape)
        return this
    }

    fun setIngredient(key: Char, material: Material, amount: Int): RecipeBuilder {
        val recipeIngredient = RecipeIngredient(ItemStack(material, amount))
        recipe.setIngredient(key, recipeIngredient.materialData)
        ingredients[key] = recipeIngredient
        return this
    }

    fun setIngredient(key: Char, itemStack: ItemStack, amount: Int): RecipeBuilder {
        val recipeIngredient = RecipeIngredient(itemStack.clone(), amount)
        recipe.setIngredient(key, recipeIngredient.materialData)
        ingredients[key] = recipeIngredient
        return this
    }

    fun build(): ShapedRecipe {
        ingredients.forEach { (key, value) ->
            recipe.setIngredient(key, value.materialData)
        }
        return recipe
    }

    private inner class RecipeIngredient(private val itemStack: ItemStack, private val amount: Int = 1) {
        val materialData: ItemStack
            get() = itemStack.clone().apply { this.amount = amount }
    }
}