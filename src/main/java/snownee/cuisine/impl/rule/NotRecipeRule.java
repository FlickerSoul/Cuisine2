package snownee.cuisine.impl.rule;

import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.RecipeRule;
import snownee.cuisine.api.registry.Cookware;

public class NotRecipeRule implements RecipeRule {

    private RecipeRule rule;

    public NotRecipeRule(RecipeRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean apply(FoodBuilder input) {
        return !rule.apply(input);
    }

}
