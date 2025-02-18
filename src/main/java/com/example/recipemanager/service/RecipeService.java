package com.example.recipemanager.service;

import com.example.recipemanager.model.Recipe;
import com.example.recipemanager.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // レシピを保存するメソッド
    public void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    // すべてのレシピを取得
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + id));
    }

    // レシピを削除するメソッド
    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }
    
}