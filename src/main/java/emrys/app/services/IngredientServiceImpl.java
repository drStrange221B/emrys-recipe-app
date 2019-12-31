package emrys.app.services;

import emrys.app.commands.IngredientCommand;
import emrys.app.commands.RecipeCommand;
import emrys.app.converters.IngredientCommandToIngredient;
import emrys.app.converters.IngredientToIngredientCommand;
import emrys.app.domain.Ingredient;
import emrys.app.domain.Recipe;
import emrys.app.repositories.RecipeRepository;
import emrys.app.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class IngredientServiceImpl implements IngredientService  {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    @Autowired
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public IngredientCommand findByRecipeIdAndId(Long recipeId, Long id) {

        RecipeCommand recipe = recipeService.findCommandById(recipeId);
        Optional<IngredientCommand> ingredientCommand= (recipe.getIngredients()).stream().filter(i->i.getId()==id).findFirst();

        if(!ingredientCommand.isPresent())
        {
            //throw an exception
        }
        return ingredientCommand.get();
    }

    @Override
    public Set<IngredientCommand> listsOfIngredientCommnad() {

        return ingredientService.listsOfIngredientCommnad();
    }

    @Override
    public IngredientCommand saveIngerdientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional=recipeRepository.findById(command.getId());

        if(!recipeOptional.isPresent())
        {
            return new IngredientCommand();
        }else
        {
            Recipe recipe=recipeOptional.get();

            Optional<Ingredient> ingredientOptional=(recipe.getIngredients()).stream().
                    filter((x)->x.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                .orElseThrow(()->new RuntimeException("UOM NOT FOUND")));

            }
            else
            {
                recipe.addIngredient(ingredientCommandToIngredient.convert(command));
            }

            return (recipeRepository.save(recipe)).getIngredients().stream()
                    .filter(x->x.getId().equals(command.getId())).map(x->ingredientToIngredientCommand.convert(x)).findFirst().orElse(null);
        }


    }
}
