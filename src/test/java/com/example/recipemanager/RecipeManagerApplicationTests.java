package com.example.recipemanager;

import com.example.recipemanager.controller.RecipeController;
import com.example.recipemanager.model.Recipe;
import com.example.recipemanager.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

class RecipeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecipeService recipeService;

    private RecipeController recipeController;

    @BeforeEach
    void setup() {
        // モックの初期化
        MockitoAnnotations.openMocks(this);
        
        // コンストラクタインジェクションを使用
        recipeController = new RecipeController(recipeService);

        // MockMvcを設定
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void testCreateRecipe() throws Exception {
        // モックの設定
        Recipe mockRecipe = new Recipe();
        mockRecipe.setTitle("Test Recipe");
        mockRecipe.setDescription("Test Description");
        mockRecipe.setSteps("Test Steps");
        mockRecipe.setIngredients("Test Ingredients");

        doNothing().when(recipeService).saveRecipe(any(Recipe.class)); // モック動作の設定

        // POSTリクエストを送信して、レスポンスを検証
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                .param("title", "Test Recipe")
                .param("description", "Test Description")
                .param("steps", "Test Steps")
                .param("ingredients", "Test Ingredients"))
                .andExpect(MockMvcResultMatchers.status().isFound()) // 302リダイレクト
                .andExpect(MockMvcResultMatchers.redirectedUrl("/recipes")); // リダイレクト先の確認
    }
}
