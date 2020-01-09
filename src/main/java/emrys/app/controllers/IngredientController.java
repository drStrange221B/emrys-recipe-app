package emrys.app.controllers;

import emrys.app.commands.IngredientCommand;
import emrys.app.commands.RecipeCommand;
import emrys.app.commands.UnitOfMeasureCommand;
import emrys.app.domain.Ingredient;
import emrys.app.domain.Recipe;
import emrys.app.services.IngredientService;
import emrys.app.services.RecipeService;
import emrys.app.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@Slf4j
public class IngredientController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;


    @GetMapping("/recipe/{id}/ingredients")
    public String getIngredient(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable Long recipeId,
                                       @PathVariable Long id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndId(recipeId, id));


        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String createNewIngredient(@PathVariable Long recipeId, Model model)
    {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";

    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long id,
                                         Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndId(recipeId, id));

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand saveCommand = ingredientService.saveIngerdientCommand(command);

        return "redirect:/recipe/" + saveCommand.getRecipeId() + "/ingredient/" + saveCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable Long recipeId, @PathVariable Long id,
                                   Model model)
    {
       log.debug("deleting ingredient id: "  + id);

       ingredientService.deleteById(recipeId,id);

       return "redirect:/recipe/" + recipeId + "/ingredients";
    }


}
