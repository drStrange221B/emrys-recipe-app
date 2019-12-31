package emrys.app.controllers;

import emrys.app.commands.RecipeCommand;
import emrys.app.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"/recipe/{id}/show"})
    public String showById(@PathVariable Long id, Model model)
    {
        model.addAttribute("recipe",recipeService.findById(id));
        return "recipe/show";
    }

    @RequestMapping("/recipe/new")
    public String newRecipe(Model model)
    {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @PostMapping("/recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command)
    {
       RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        System.out.println("test");
        return "redirect:/recipe/" + savedCommand.getId()+"/show";
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model)
    {
        model.addAttribute("recipe",recipeService.findCommandById(id));
        return "recipe/recipeform";
    }

    @RequestMapping("recipe/{id}/delete")
    public String delete(@PathVariable Long id)
    {
        log.debug("Deleting id: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

}
