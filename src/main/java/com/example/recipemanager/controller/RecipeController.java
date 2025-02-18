package com.example.recipemanager.controller;

import java.util.List;
import com.example.recipemanager.model.Recipe;
import com.example.recipemanager.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // コンストラクタインジェクションを使用
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // // 初期ページはレシピ一覧ページにリダイレクト
    @GetMapping("/")
    public String redirectToRecipes() {
        return "redirect:/recipes";
    }

    // 新規レシピ作成フォームを表示
    @GetMapping("/recipes/create")
    public String showCreateForm() {
        return "create"; // create.htmlを表示
    }

    // すべてのレシピを一覧表示
    @GetMapping("/recipes")
    public String listRecipes(Model model) {
        try {
            List<Recipe> recipes = recipeService.getAllRecipes();
            System.out.println("取得したレシピの数: " + recipes.size());
            model.addAttribute("recipes", recipes);
            return "recipes"; // recipes.htmlを表示
        } catch (Exception e) {
            // エラー発生時はエラーメッセージを表示
            e.printStackTrace();
            model.addAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            return "recipes"; // エラーメッセージを表示したままレシピ一覧を表示
        }
    }

    // レシピ詳細ページを表示
    @GetMapping("/recipes/{id}")
    public String getRecipeDetails(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        return "recipe-details"; // recipe-details.html
    }

    // レシピ編集フォームを表示
    @GetMapping("/recipes/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);
        System.out.println("編集対象のレシピ: " + recipe);
        model.addAttribute("recipe", recipe);
        return "edit-recipe"; // edit-recipe.html
    }

    // 新規レシピの作成
    @PostMapping("/recipes")
    public String createRecipe(@RequestParam String title, 
                                @RequestParam String description, 
                                @RequestParam String steps, 
                                @RequestParam String ingredients,
                                Model model) {
        // 必須項目のバリデーション
        if (title.isEmpty() || description.isEmpty() || steps.isEmpty() || ingredients.isEmpty()) {
            model.addAttribute("message", "すべての項目を入力してください。");
            return "create"; // create.html にエラーメッセージを表示
        }

        try {
            Recipe recipe = new Recipe();
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setSteps(steps);
            recipe.setIngredients(ingredients);

            // レシピをデータベースに保存
            recipeService.saveRecipe(recipe);

            // フォームを再表示（リダイレクト）
            return "redirect:/recipes";
        } catch (Exception e) {
            // 保存中にエラーが発生した場合
            e.printStackTrace();
            model.addAttribute("message", "レシピの作成に失敗しました: " + e.getMessage());
            return "create"; // エラーメッセージを表示したままcreateページに戻る
        }
    }

    // レシピの更新（編集）
    @PostMapping("/recipes/edit/{id}")
    public String updateRecipe(@PathVariable Long id, 
                           @RequestParam String title, 
                           @RequestParam String description, 
                           @RequestParam String ingredients, 
                           @RequestParam String steps) {
        Recipe recipe = recipeService.getRecipeById(id);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

        recipeService.saveRecipe(recipe);
        return "redirect:/recipes/" + id; // 編集後、詳細ページにリダイレクト
    }

    // レシピの削除
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipeById(id);
        return ResponseEntity.noContent().build(); // 削除が完了したことを示す
    }
}
