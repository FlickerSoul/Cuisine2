package snownee.cuisine.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.FoodBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineFoodInstance;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.MaterialInstance;
import snownee.cuisine.api.registry.Spice;

public class FoodBuilderImpl<C> implements FoodBuilder<C> {

    private final Cookware cookware;
    private final C context;
    private final List<MaterialInstance> materials = Lists.newArrayList();
    private final List<CuisineFoodInstance> foods = Lists.newArrayList();
    private final Set<Spice> spices = Sets.newHashSet();
    private final @Nullable Entity cook;

    public FoodBuilderImpl(Cookware cookware, C context, Entity cook) {
        this.cookware = cookware;
        this.context = context;
        this.cook = cook;
    }

    @Override
    public void add(MaterialInstance materialInstance) {
        materials.add(materialInstance);
    }

    @Override
    public void add(Material material) {
        int star = CuisineAPI.getResearchInfo(cook).getStar(material);
        add(new MaterialInstance(material, star));
    }

    @Override
    public void add(CuisineFoodInstance foodInstance) {
        foods.add(foodInstance);
    }

    @Override
    public void add(CuisineFood food) {
        int star = CuisineAPI.getResearchInfo(cook).getStar(food);
        add(new CuisineFoodInstance(food, star));
    }

    @Override
    public void add(Spice spice) {
        spices.add(spice);
    }

    @Override
    public boolean has(Object o) {
        if (o instanceof Material) {
            return materials.stream().map($ -> $.material).anyMatch(o::equals);
        }
        if (o instanceof CuisineFood) {
            return foods.stream().map($ -> $.food).anyMatch(o::equals);
        }
        if (o instanceof Spice) {
            return spices.contains(o);
        }
        if (o instanceof MaterialInstance) {
            return materials.contains(o);
        }
        if (o instanceof CuisineFoodInstance) {
            return foods.contains(o);
        }
        throw new IllegalArgumentException("Object has to be Material or Spice!");
    }

    @Override
    public int count(Object o) {
        if (o instanceof Material) {
            return (int) materials.stream().map($ -> $.material).filter(o::equals).count();
        }
        if (o instanceof CuisineFood) {
            return (int) foods.stream().map($ -> $.food).filter(o::equals).count();
        }
        if (o instanceof Spice) {
            return spices.contains(o) ? 1 : 0;
        }
        throw new IllegalArgumentException("Object has to be Material or Spice!");
    }

    @Override
    public List<MaterialInstance> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    @Override
    public List<CuisineFoodInstance> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    @Override
    public Set<Spice> getSpices() {
        return Collections.unmodifiableSet(spices);
    }

    @Override
    public Cookware getCookware() {
        return cookware;
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    @Nullable
    public Entity getCook() {
        return cook;
    }

    @Override
    public ItemStack build(CuisineRecipe recipe) {
        ItemStack stack = recipe.getResult().getItemStack();
        Map<Effect, EffectInstance> map = Maps.newHashMap();
        materials.stream().flatMap($ -> $.getEffects().stream()).forEach($ -> {
            if (map.containsKey($.getPotion())) {
                map.get($.getPotion()).combine($);
            } else {
                map.put($.getPotion(), $);
            }
        });
        return PotionUtils.appendEffects(stack, map.values());
    }

}
