package emrys.app.services;

import emrys.app.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService{


    public void saveImageFile(Long id, MultipartFile file);

}
