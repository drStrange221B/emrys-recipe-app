package emrys.app.services;

import emrys.app.commands.IngredientCommand;

import java.util.Set;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndId(Long recipeId, Long id);

    Set<IngredientCommand> listsOfIngredientCommnad();

    public IngredientCommand saveIngerdientCommand(IngredientCommand command);


}
